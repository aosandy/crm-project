package ru.aosandy.cdr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.client.Client;
import ru.aosandy.common.client.ClientsRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;

@Component
public class GeneratorCDR {

    private final Random rand = new Random();
    private final Set<String> numbers = new HashSet<>();

    @Autowired
    private ClientsRepository repository;

    @Autowired
    private MessageSender messageSender;

    @Value("${call-num.max}")
    private int maxCallNum;

    @Value("${call-num.dispersion}")
    private double callNumDispersion;

    @Value("${call-minutes.max}")
    private int maxCallMinutes;

    @Value("${numbers.proportion-of-existing}")
    private double existingNumbersProportion;

    @Value("${numbers.max}")
    private int maxNumbers;

    @Value("${start.year}")
    private int currentYear;

    @Value("${start.month}")
    private int currentMonth;

    public void generateAndSend() {
        List<CallDataRecord> generatedList = generateListCdr(maxNumbers);
        try {
            FileBuilderCDR.buildReport(generatedList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageSender.sendCdr(generatedList);
    }

    private List<CallDataRecord> generateListCdr(int n) {
        List<CallDataRecord> generatedList = new LinkedList<>();
        List<String> existingNumbers = repository.findAll().stream()
            .map(Client::getNumber)
            .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(existingNumbers);

        Iterator<String> it = existingNumbers.iterator();
        int i;
        for (i = 0; i < n * existingNumbersProportion && it.hasNext(); i++) {
            String number = it.next();
            generatedList.addAll(generateCdr(number));
        }
        for (; i < n; i++) {
            String number = getNewRandomNumber();
            generatedList.addAll(generateCdr(number));
        }
        Collections.shuffle(generatedList);
        moveToNextMonth();
        return generatedList;
    }

    private List<CallDataRecord> generateCdr(String number) {
        LocalDate firstDate = LocalDate.of(currentYear, currentMonth, 1);
        LocalDateTime currentDateTime = firstDate.atStartOfDay();
        int totalMinutesInMonth = (int) Duration.between(
            currentDateTime, firstDate.with(firstDayOfNextMonth()).atStartOfDay()).toMinutes();
        int callsNum = maxCallNum - rand.nextInt((int) (maxCallNum * callNumDispersion));
        int maxMinutesBetweenCalls = (totalMinutesInMonth - maxCallMinutes * callsNum) / callsNum;
        List<CallDataRecord> generatedList = new LinkedList<>();

        for (int i = 0; i < callsNum; i++) {
            int callType = getRandomCallType();

            LocalDateTime dateTimeStart = currentDateTime.plus(
                Duration.ofSeconds(rand.nextInt(maxMinutesBetweenCalls * 60))
            );
            LocalDateTime dateTimeEnd = dateTimeStart.plus(
                Duration.ofSeconds(rand.nextInt(maxCallMinutes * 60))
            );
            currentDateTime = dateTimeEnd;
            generatedList.add(new CallDataRecord(
                callType,
                number,
                dateTimeStart,
                dateTimeEnd
            ));
        }

        return generatedList;
    }

    private void moveToNextMonth() {
        if (currentMonth < 12) {
            currentMonth++;
            return;
        }
        currentYear++;
        currentMonth = 1;
    }

    private int getRandomCallType() {
        return rand.nextInt(2) + 1;
    }

    private String getNewRandomNumber() {
        String number;
        do {
            StringBuilder builder = new StringBuilder(11);
            builder.append(7);
            for (int i = 0; i < 10; i++) {
                builder.append(rand.nextInt(9));
            }
            number = builder.toString();
        } while (numbers.contains(number));
        numbers.add(number);
        return number;
    }
}
