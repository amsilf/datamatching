package com.alex.makarov.controller.matching;

import com.alex.makarov.dataobjects.Transaction;
import com.alex.makarov.dataobjects.TransactionSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TransactionFileReader implements DataReader<TransactionSet> {

    @Override
    public TransactionSet readData(String url) {
        TransactionSet transactionSet = new TransactionSet();

        try (Stream<String> stream = Files.lines(Paths.get(url))) {
            stream.forEach(row -> {
                Transaction transaction = Transaction.valueOf(row);
                transactionSet.addTransaction(transaction);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return transactionSet;
    }

}
