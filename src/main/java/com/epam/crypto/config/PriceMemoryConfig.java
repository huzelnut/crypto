package com.epam.crypto.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Configuration
public class PriceMemoryConfig {

    private final static String DATA_FOLDER_PATH = "C:\\Users\\Andrey Work\\IdeaProjects\\crypto\\src\\main\\resources\\prices";
    private final static String CSV_DELIMITER = ",";

    @Bean(name = "priceStorage")
    public Map<String, NavigableMap<Long, BigDecimal>> initPriceStorage() throws IOException {
        var storage = new HashMap<String, NavigableMap<Long, BigDecimal>>();
        File folder = new File(DATA_FOLDER_PATH);
        for (File file : folder.listFiles()) {
            readCsvFile(file).forEach(line -> processCsvLine(line, storage));
        }
        return storage;
    }

    private List<String> readCsvFile(File csvFile) throws IOException {
        List<String> result = new LinkedList<>();
        boolean firstLineSkipped = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (firstLineSkipped) {
                    result.add(line);
                } else {
                    firstLineSkipped = true;
                }
            }
        }
        return result;
    }

    private void processCsvLine(String line, Map<String, NavigableMap<Long, BigDecimal>> storage) {
        String[] cells = line.split(CSV_DELIMITER);
        String currencySymbol = cells[1];
        String timestamp = cells[0];
        String price = cells[2];
        Map<Long, BigDecimal> subStorage = storage.computeIfAbsent(currencySymbol, key -> new TreeMap<>());
        subStorage.put(Long.valueOf(timestamp), new BigDecimal(price));
    }
}
