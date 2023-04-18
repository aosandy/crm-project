package ru.aosandy.cdr;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

public class Generator {
    private static final int MAX_CALL_NUM = 20;
    private static final double DISPERSION_OF_CALL_NUM = 0.1;
    private static final int MAX_CALL_DURATION_MINUTES = 15;
    private static final int TOTAL_MINUTES_IN_YEAR = 525960;

    private final Random rand = new Random();
    private final Set<String> numbers = new HashSet<>();

    public List<CallDataRecord> generateCDRs(int n) {
        List<CallDataRecord> generatedList = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            String number = getNewRandomNumber();
            generatedList.addAll(generateCDR(number));
        }
        Collections.shuffle(generatedList);
        return generatedList;
    }

    private List<CallDataRecord> generateCDR(String number) {
        LocalDateTime currentDateTime = LocalDate.now().with(firstDayOfYear()).atStartOfDay();
        int callsNum = MAX_CALL_NUM - rand.nextInt((int) (MAX_CALL_NUM * DISPERSION_OF_CALL_NUM));
        int maxMinutesBetweenCalls = (TOTAL_MINUTES_IN_YEAR - MAX_CALL_DURATION_MINUTES * callsNum) / callsNum;
        List<CallDataRecord> generatedList = new LinkedList<>();

        for (int i = 0; i < callsNum; i++) {
            CallType callType = getRandomCallType();

            LocalDateTime dateTimeStart = currentDateTime.plus(
                Duration.ofSeconds(rand.nextInt(maxMinutesBetweenCalls * 60))
            );
            LocalDateTime dateTimeEnd = dateTimeStart.plus(
                Duration.ofSeconds(rand.nextInt(MAX_CALL_DURATION_MINUTES * 60))
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

    private CallType getRandomCallType() {
        return CallType.valueOfIndex(rand.nextInt(2) + 1);
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
