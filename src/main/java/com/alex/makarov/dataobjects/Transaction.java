package com.alex.makarov.dataobjects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {

    public static final String VALUE_SEPARATOR = ";";

    public static final Transaction EMPTY_TRANSACTION = new Transaction("","", LocalDate.now(), 0f);

    public static final String DATE_PATTER = "dd-MMM-yyyy";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTER);

    private String transactionId;
    private String accountId;
    private LocalDate postingDate;
    private Float amount;

    public Transaction(String transactionId, String accountId, LocalDate postingDate, Float amount) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.postingDate = postingDate;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public Float getAmount() {
        return amount;
    }

    public static Transaction valueOf(String string) {
        // in real life will use StringUtils.isEmpty
        if (string == null || string.length() == 0) {
            return EMPTY_TRANSACTION;
        }

        String[] values = string.split(VALUE_SEPARATOR);

        String transactionId = values[0].trim();
        String accountId = values[1].trim();
        LocalDate postingDate;
        postingDate = LocalDate.parse(values[2].trim(), DATE_FORMATTER);
        Float amount = Float.valueOf(values[3].trim());

        return new Transaction(transactionId, accountId, postingDate, amount);
    }

}
