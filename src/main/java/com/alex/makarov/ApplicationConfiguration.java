package com.alex.makarov;

import com.alex.makarov.controller.matching.DataReader;
import com.alex.makarov.controller.matching.FileMatchingEngine;
import com.alex.makarov.controller.matching.MatchingEngine;
import com.alex.makarov.controller.matching.TransactionFileReader;
import com.alex.makarov.controller.requesthanders.MatchingController;
import com.alex.makarov.dataobjects.TransactionSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public DataReader<TransactionSet> transactionFileReader() {
        return new TransactionFileReader();
    }

    @Bean
    public MatchingEngine matchingEngine() {
        return new FileMatchingEngine();
    }

    @Bean
    public MatchingController matchingController() {
        return new MatchingController();
    }

}
