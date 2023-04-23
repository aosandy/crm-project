package ru.aosandy.hrs;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aosandy.common.Call;
import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.CallDataRecordPlus;
import ru.aosandy.common.Report;
import ru.aosandy.hrs.tariff.*;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
public class TariffService {

    private final TariffRepository tariffRepository;
    private final StartPeriodChoiceRepository startPeriodChoiceRepository;
    private final PeriodRepository periodRepository;

    public TariffService(
        TariffRepository tariffRepository,
        StartPeriodChoiceRepository startPeriodChoiceRepository,
        PeriodRepository periodRepository
    ) {
        this.tariffRepository = tariffRepository;
        this.startPeriodChoiceRepository = startPeriodChoiceRepository;
        this.periodRepository = periodRepository;
    }

    public List<Report> calculateBilling(List<CallDataRecordPlus> listCdrPlus) {
        List<Report> reports = parseCdrPlusToReportsList(listCdrPlus);
        try {
            for (Report report : reports) {
                calculatePriceForReport(report);
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return reports;
    }

    void calculatePriceForReport(Report report) {
        Tariff tariff = tariffRepository.findById(report.getTariffId())
            .orElseThrow(() -> new EntityNotFoundException("Tariff not found"));
        List<StartPeriodChoice> choices = startPeriodChoiceRepository.findAllByTariffId(tariff.getId());

        int maxTypeId = choices.stream().max(Comparator.comparing(StartPeriodChoice::getCallTypeId))
            .orElseThrow(() -> new EntityNotFoundException("StartPeriodChoice not found")).getCallTypeId();

        // Генерация матрицы выбора начального периода в зависимости от типа вызова и того,
        // внутресетевой звонок или нет
        int[][] periodChoiceMatrix = new int[2][maxTypeId + 1];
        for (StartPeriodChoice choice : choices) {
            periodChoiceMatrix[(choice.isIntranet() ? 1 : 0)]
                [choice.getCallTypeId()] = choice.getStartPeriodId();
        }

        Set<Integer> usedFixPrices = new HashSet<>();
        int totalCost = 0;
        int[][] totalMinutesMatrix = new int[2][maxTypeId + 1];

        for (Call call : report.getCalls()) {

            // Всегда false (0), так как в CDR нет информации о том, кому звонят
            int isSameOperator = 0;
            int callType = call.getCallType();
            int currentPeriodId = periodChoiceMatrix[isSameOperator][callType];

            // Выбор стартового периода для текущего звонка
            Period currentPeriod = periodRepository.findById(currentPeriodId)
                .orElseThrow(() -> new EntityNotFoundException("Period not found"));

            // Продолжительность текущего звонка
            long currentMinutes = ceilDurationToMinutes(call.getDuration());

            // Добавление текущих минут в текущую ячейку
            totalMinutesMatrix[isSameOperator][callType] += currentMinutes;

            // Суммарная продолжительность всех обработанных звонков
            long totalMinutes = 0;
            for (int i = 0; i < periodChoiceMatrix.length; i++) {
                for (int j = 0; j < periodChoiceMatrix[0].length; j++) {
                    if (periodChoiceMatrix[i][j] == currentPeriodId) {
                        totalMinutes += totalMinutesMatrix[i][j];
                    }
                }
            }

            int currentCallCost = 0;

            // Фиксированая плата
            while (currentPeriod.getMinuteLimit() != null) {
                if (!usedFixPrices.contains(currentPeriod.getId())) {
                    usedFixPrices.add(currentPeriod.getId());
                    totalCost += currentPeriod.getFixCost();
                }
                if (totalMinutes > currentPeriod.getMinuteLimit()) {

                    // Если текущий звонок начался до лимита, а закончился после - время до лимита обрезается и
                    // расчитывается по поминутной стоимости, так как далее произойдет переход на следующий период
                    if (totalMinutes - currentMinutes < currentPeriod.getMinuteLimit()) {
                        long newCurrentMinutes = totalMinutes - currentPeriod.getMinuteLimit();
                        currentCallCost += (currentMinutes - newCurrentMinutes) * currentPeriod.getPricePerMinute();
                        currentMinutes = newCurrentMinutes;
                    }

                    // Общее время обрезается в любом случае, так как лимит следующего периода
                    // не учитывает лимит предыдущего
                    totalMinutes = totalMinutes - currentPeriod.getMinuteLimit();

                    // Переход на следующий период
                    currentPeriod = periodRepository.findById(currentPeriod.getNextPeriodId())
                        .orElseThrow(() -> new EntityNotFoundException("Period not found"));
                } else {
                    break;
                }
            }

            // Рассчет поминутной стоимости звонка
            currentCallCost += currentMinutes * currentPeriod.getPricePerMinute();

            call.setCost(currentCallCost);
            totalCost += currentCallCost;
        }
        report.setTotalCost(totalCost);
    }

    private List<Report> parseCdrPlusToReportsList(List<CallDataRecordPlus> listCdrPlus) {
        Map<String, Report> clientDataMap = new HashMap<>();
        for (CallDataRecordPlus cdrp : listCdrPlus) {
            CallDataRecord cdr = cdrp.getCallDataRecord();
            String number = cdrp.getCallDataRecord().getNumber();
            int tariffId = cdrp.getTariffId();
            if (!clientDataMap.containsKey(number)) {
                clientDataMap.put(number, new Report(number, tariffId));
            }

            clientDataMap.get(number).appendCall(
                cdr.getCallType(),
                cdr.getStartDateTime(),
                cdr.getEndDateTime()
            );
        }
        return clientDataMap.values().stream().toList();
    }

    private long ceilDurationToMinutes(Duration duration) {
        return duration.toMinutes() + 1;
    }
}
