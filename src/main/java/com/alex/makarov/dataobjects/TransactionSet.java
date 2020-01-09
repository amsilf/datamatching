package com.alex.makarov.dataobjects;

import java.util.*;

public class TransactionSet {

    // will use Google Guava ListMultiMap in real life
    private Map<String, List<Transaction>> transactionMap;

    public TransactionSet() {
        transactionMap = new HashMap<>();
    }

    public void addTransaction(Transaction transaction) {
        String accountId = transaction.getAccountId();
        if (transactionMap.containsKey(accountId)) {
            transactionMap.get(accountId).add(transaction);
            return;
        }

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionMap.put(accountId, transactionList);
    }

    public Map<String, List<Transaction>> getTransactionMap() {
        return transactionMap;
    }

}
