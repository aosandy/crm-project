package ru.aosandy.cdr;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Generator generator = new Generator();
        List<CallDataRecord> generatedList = generator.generateCDRs(100);
        try {
            CDRFileBuilder.buildReport(generatedList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
