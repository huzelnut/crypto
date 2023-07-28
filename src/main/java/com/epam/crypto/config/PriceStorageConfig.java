package com.epam.crypto.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

@Configuration
public class PriceStorageConfig {

    private final static String DATA_FOLDER_PATH = "prices";
    private final static String CSV_DELIMITER = ",";

    @Bean(name = "priceStorage")
    public Map<String, NavigableMap<Long, BigDecimal>> initPriceStorage() throws IOException {
        var storage = new HashMap<String, NavigableMap<Long, BigDecimal>>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:" + DATA_FOLDER_PATH + "/*.csv");
        for (Resource resource : resources) {
            readCsvFile(resource).forEach(line -> processCsvLine(line, storage));
        }
        return storage;
    }

    private List<String> readCsvFile(Resource csvFile) throws IOException {
        List<String> result = new LinkedList<>();
        boolean firstLineSkipped = false;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
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
