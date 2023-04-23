package ru.aosandy.brt;

import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.CallDataRecordPlus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileBuilderCDRPlus {

    private FileBuilderCDRPlus() {
        throw new IllegalStateException("Utility class");
    }

    public static void buildReport(List<CallDataRecordPlus> listCdrPlus) throws IOException {
        String folderName = "cdr_files";
        String fileName = "cdr+.txt";
        String separator = ", ";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        File file = new File(String.valueOf(Paths.get(folderName, fileName)));
        file.createNewFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (CallDataRecordPlus cdrp : listCdrPlus) {
                CallDataRecord cdr = cdrp.getCallDataRecord();
                writer.write(String.format("%02d", cdr.getCallType()));
                writer.write(separator);

                writer.write(cdr.getNumber());
                writer.write(separator);

                writer.write(cdr.getStartDateTime().format(formatter));
                writer.write(separator);

                writer.write(cdr.getEndDateTime().format(formatter));
                writer.write(separator);

                writer.write(String.format("%02d", cdrp.getTariffId()));
                writer.newLine();
            }
        }
    }
}
