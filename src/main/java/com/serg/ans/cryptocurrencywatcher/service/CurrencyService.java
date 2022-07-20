package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.Currency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CurrencyService {

    public static List<Currency> getObjectOnlyCurrencyType(Iterator entities) {
        List<Currency> currencies = new ArrayList<>();
        Object entity;
        if (entities != null) {
            while (entities.hasNext()) {
                entity = entities.next();
                if (entity instanceof Currency) {
                    currencies.add((Currency) entity);
                }
            }
        }
        return currencies;
    }
}
