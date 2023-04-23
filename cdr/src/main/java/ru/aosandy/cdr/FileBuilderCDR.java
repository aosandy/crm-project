package ru.aosandy.cdr;

import ru.aosandy.common.CallDataRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileBuilderCDR {

    private FileBuilderCDR() {
        throw new IllegalStateException("Utility class");
    }

    public static void buildReport(List<CallDataRecord> listCDR) throws IOException {
        String fileFolder = "cdr";
        String fileFileName = "cdr.txt";
        String separator = ", ";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        File file = new File(String.valueOf(Paths.get(fileFolder, fileFileName)));
        file.createNewFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (CallDataRecord cdr : listCDR) {
                writer.write(String.format("%02d", cdr.getCallType()));
                writer.write(separator);

                writer.write(cdr.getNumber());
                writer.write(separator);

                writer.write(cdr.getStartDateTime().format(formatter));
                writer.write(separator);

                writer.write(cdr.getEndDateTime().format(formatter));
                writer.newLine();
            }
        }
    }
}
