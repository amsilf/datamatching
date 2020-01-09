package com.alex.makarov.controller.matching;

import com.alex.makarov.dataobjects.MatchingResult;
import com.alex.makarov.dataobjects.Transaction;
import com.alex.makarov.dataobjects.TransactionSet;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.alex.makarov.controller.matching.ProcessingStatus.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class FileMatchingEngine implements MatchingEngine {

    private static final Float DELTA = 0.01f;
    private static final Long DAYS_BETWEEN_THRESHOLD = 1l;

    @Autowired
    private DataReader<TransactionSet> transactionReader;

    public MatchingResult match(String aPath, String bPath) {

        TransactionSet aSet = transactionReader.readData(aPath);
        TransactionSet bSet = transactionReader.readData(bPath);

        Map<String, List<Transaction>> aMap = aSet.getTransactionMap();
        Map<String, List<Transaction>> bMap = bSet.getTransactionMap();

        Set<String> aSideBreak = new HashSet<>();
        Set<String> bSideBreak = new HashSet<>();

        Set<String> exactMatch = new HashSet<>();
        Set<String> weakMatch = new HashSet<>();

        // For real project I will use guava utilities to perform operations on sets
        for (String aId : aMap.keySet()) {

            List<Transaction> aTransactions = aMap.get(aId);

            if (!bMap.containsKey(aId)) {
                aTransactions.forEach(t -> aSideBreak.add(t.getTransactionId()));
                aMap.remove(aId);
            }

            for (Transaction aCurrentTransaction : aTransactions) {

                List<Transaction> bTransactions = bMap.get(aId);
                if (bTransactions == null) {
                    aSideBreak.add(aCurrentTransaction.getTransactionId());
                    break;
                }

                Transaction bCurrentTransaction = bTransactions.get(0);

                if (isExactMatch(aCurrentTransaction, bCurrentTransaction)) {
                    exactMatch.add(aCurrentTransaction.getTransactionId() + bCurrentTransaction.getTransactionId());
                } else if(isWeakMatch(aCurrentTransaction, bCurrentTransaction)) {
                    weakMatch.add(aCurrentTransaction.getTransactionId() + bCurrentTransaction.getTransactionId());
                } else {
                    aSideBreak.add(aCurrentTransaction.getTransactionId());
                    bSideBreak.add(bCurrentTransaction.getTransactionId());
                }

                bTransactions.remove(bCurrentTransaction);

                if (aTransactions.isEmpty()) {
                    aMap.remove(aId);
                }

                if (bTransactions.isEmpty()) {
                    bMap.remove(aId);
                }
            }

        }

        for (String bId : bMap.keySet()) {
            List<Transaction> bTransactions = bMap.get(bId);
            bTransactions.forEach(t -> bSideBreak.add(t.getTransactionId()));
        }

        return new MatchingResult(exactMatch, weakMatch, aSideBreak, bSideBreak, COMPLETED);
    }

    private boolean isExactMatch(Transaction aTransaction, Transaction bTransaction) {

        LocalDate aDate = aTransaction.getPostingDate();
        LocalDate bDate = bTransaction.getPostingDate();

        Float aAmount = aTransaction.getAmount();
        Float bAmount = bTransaction.getAmount();

        return aDate.equals(bDate) && aAmount.equals(bAmount);
    }

    private boolean isWeakMatch(Transaction aTransaction, Transaction bTransaction) {

        boolean areAmountsEqual = areAmountsMatched(aTransaction.getAmount(), bTransaction.getAmount());
        boolean areDatesEqual = areDatesMatched(aTransaction.getPostingDate(), bTransaction.getPostingDate());

        return areDatesEqual && areAmountsEqual;
    }

    private boolean areAmountsMatched(Float aAmount, Float bAmount) {
        Float absDifference = Math.abs(round(aAmount - bAmount, 2));
        return Float.compare(absDifference, DELTA) <= 0;
    }

    private boolean areDatesMatched(LocalDate aDate, LocalDate bDate) {
        long daysBetween = DAYS.between(aDate, bDate);

        if (DAYS_BETWEEN_THRESHOLD >= daysBetween) {
            return true;
        }

        if (areDaysSeparatedByWeekends(aDate, bDate, daysBetween)) {
            return true;
        }

        return false;
    }

    private boolean areDaysSeparatedByWeekends(LocalDate aDate, LocalDate bDate, long daysBetween) {
        DayOfWeek aDayOfWeek = aDate.getDayOfWeek();
        DayOfWeek bDayOfWeek = bDate.getDayOfWeek();


        return daysBetween == 3l &&
                ( (DayOfWeek.FRIDAY == aDayOfWeek && DayOfWeek.MONDAY == bDayOfWeek) ||
                        (DayOfWeek.MONDAY == aDayOfWeek && DayOfWeek.FRIDAY == bDayOfWeek) );
    }

    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}
