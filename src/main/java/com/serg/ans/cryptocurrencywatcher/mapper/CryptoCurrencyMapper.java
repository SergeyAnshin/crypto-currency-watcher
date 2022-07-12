package com.serg.ans.cryptocurrencywatcher.mapper;

import com.serg.ans.cryptocurrencywatcher.dto.AvailableCryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;

import java.util.Set;
import java.util.stream.Collectors;

public class CryptoCurrencyMapper {

    public static Set<AvailableCryptoCurrency> mapToAvailableCryptoCurrencySet(Set<CryptoCurrency> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            return null;
        } else {
            return currencies.stream()
                    .map(CryptoCurrencyMapper::mapToAvailableCryptoCurrency)
                    .collect(Collectors.toSet());
        }
    }

    public static AvailableCryptoCurrency mapToAvailableCryptoCurrency(CryptoCurrency currency) {
        if (currency == null) {
            return null;
        } else {
            return AvailableCryptoCurrency.builder()
                    .id(currency.getId())
                    .symbol(currency.getSymbol())
                    .build();
        }
    }
}
