package ru.aosandy.hrs;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aosandy.hrs.period.Period;
import ru.aosandy.hrs.period.PeriodRepository;
import ru.aosandy.hrs.tariff.Tariff;
import ru.aosandy.hrs.tariff.TariffRepository;
import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.CallDataRecordPlus;
import ru.aosandy.common.CallType;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
public class TariffService {

    private final TariffRepository tariffRepository;
    private final PeriodRepository periodRepository;

    public TariffService(TariffRepository tariffRepository, PeriodRepository periodRepository) {
        this.tariffRepository = tariffRepository;
        this.periodRepository = periodRepository;
    }

    public Map<String, Integer> calculateBillsMap(List<CallDataRecordPlus> listCdrPlus) {
        Map<String, Integer> bills = new HashMap<>();
        Collection<ClientData> clientDataCollection = parseCdrPlusToClientDataMap(listCdrPlus);
        try {
            for (ClientData clientData : clientDataCollection) {
                Duration total = clientData.getCalls().stream().map(Call::getDuration).reduce(Duration::plus).get();
                bills.put(clientData.getNumber(), calculateBill(clientData));
                log.info("Tariff: " + clientData.getTariffId() +
                    ", Number: " + clientData.getNumber() +
                    ", Bill: " + (bills.get(clientData.getNumber()) / 100.0) +
                    ", Total minutes: " + total.toMinutes());
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return bills;
    }

    private Collection<ClientData> parseCdrPlusToClientDataMap(List<CallDataRecordPlus> listCdrPlus) {
        Map<String, ClientData> clientDataMap = new HashMap<>();
        for (CallDataRecordPlus cdrp : listCdrPlus) {
            CallDataRecord cdr = cdrp.getCallDataRecord();
            String number = cdrp.getCallDataRecord().getNumber();
            int tariffId = cdrp.getTariffId();
            if (!clientDataMap.containsKey(number)) {
                clientDataMap.put(number, new ClientData(number, tariffId));
            }

            clientDataMap.get(number).appendCall(
                cdr.getCallType(),
                cdr.getStartDateTime(),
                cdr.getEndDateTime()
            );
        }
        return clientDataMap.values();
    }

    private int calculateBill(ClientData clientData) {
        Tariff tariff = tariffRepository.findById(clientData.getTariffId()).orElseThrow(
            () -> new EntityNotFoundException("Tariff not found"));
        int bill = 0;

        // Инициализация общего времени разговора, раздельно для входящих/исходящих и для
        // звонков абоненту своего/чужого опертора
        Duration totalDurationIncoming = Duration.ofSeconds(0);
        Duration totalDurationOutcoming = Duration.ofSeconds(0);
        Duration totalDurationIncomingSameOperator = Duration.ofSeconds(0);
        Duration totalDurationOutcomingSameOperator = Duration.ofSeconds(0);

        Set<Integer> usedFixPrices = new HashSet<>();

        for (Call call : clientData.getCalls()) {
            CallType callType = call.getCallType();

            // Всегда false, так как в CDR нет информации о том, кому звонят
            boolean isSameOperator = false;

            int startPeriodOutcomingId = isSameOperator ?
                tariff.getStartPeriodOutcomingSameOp() : tariff.getStartPeriodOutcoming();
            int startPeriodIncomingId = isSameOperator ?
                tariff.getStartPeriodIncomingSameOp() : tariff.getStartPeriodIncoming();

            // Выбор стартового периода для текущего звонка
            Period currentPeriod = periodRepository.findById(
                switch (callType) {
                    case OUTGOING -> startPeriodOutcomingId;
                    case INCOMING -> startPeriodIncomingId;
                }
            ).orElseThrow(() -> new EntityNotFoundException("Period not found"));

            // Выбор текущего общего времени разговора в зависимости от оператора собеседника
            Duration currentTotalDurationIncoming = isSameOperator ?
                totalDurationIncomingSameOperator : totalDurationIncoming;
            Duration currentTotaldurationOutcoming = isSameOperator ?
                totalDurationOutcomingSameOperator : totalDurationOutcoming;

            // Продолжительность текущего звонка
            Duration currentDuration = call.getDuration();

            switch (callType) {
                case OUTGOING -> currentTotaldurationOutcoming =
                    currentTotaldurationOutcoming.plus(currentDuration);
                case INCOMING -> currentTotalDurationIncoming =
                    currentTotalDurationIncoming.plus(currentDuration);
            }

            // Суммарная продолжительность всех обработанных звонков
            Duration totalDuration;
            if (startPeriodOutcomingId == startPeriodIncomingId) {
                totalDuration = currentTotaldurationOutcoming
                    .plus(currentTotalDurationIncoming);
            } else {
                totalDuration = switch (callType) {
                    case OUTGOING -> currentTotaldurationOutcoming;
                    case INCOMING -> currentTotalDurationIncoming;
                };
            }

            // Фиксированая плата
            while (currentPeriod.getMinuteLimit() != null) {
                if (!usedFixPrices.contains(currentPeriod.getId())) {
                    usedFixPrices.add(currentPeriod.getId());
                    bill += currentPeriod.getFixPrice();
                }
                if (totalDuration.toMinutes() > currentPeriod.getMinuteLimit()) {

                    // Если текущий звонок начался до лимита, а закончился после - время до лимита обрезается и
                    // расчитывается по поминутной стоимости, так как далее произойдет переход на следующий период
                    if (totalDuration.minus(currentDuration).toMinutes() < currentPeriod.getMinuteLimit()) {
                        Duration newCurrentDuration = totalDuration.minusMinutes(currentPeriod.getMinuteLimit());
                        bill += (ceilDurationToMinutes(currentDuration) - ceilDurationToMinutes(newCurrentDuration))
                            * currentPeriod.getPricePerMinute();
                        currentDuration = newCurrentDuration;
                    }

                    // Общее время обрезается в любом случае, так как лимит следующего периода
                    // не учитывает лимит предыдущего
                    totalDuration = totalDuration.minusMinutes(currentPeriod.getMinuteLimit());

                    // Переход на следующий период
                    currentPeriod = periodRepository.findById(currentPeriod.getNextPeriod())
                        .orElseThrow(() -> new EntityNotFoundException("Period not found"));
                } else {
                    break;
                }
            }

            // Рассчет поминутной стоимости звонка
            bill += ceilDurationToMinutes(currentDuration) * currentPeriod.getPricePerMinute();

            // Обновление общих данных о потраченном времени
            if (isSameOperator) {
                totalDurationIncomingSameOperator = currentTotalDurationIncoming;
                totalDurationOutcomingSameOperator = currentTotaldurationOutcoming;
            } else {
                totalDurationIncoming = currentTotalDurationIncoming;
                totalDurationOutcoming = currentTotaldurationOutcoming;
            }
        }

        return bill;
    }

    private long ceilDurationToMinutes(Duration duration) {
        return duration.toMinutes() + 1;
    }
}
