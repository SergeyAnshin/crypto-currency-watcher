package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.repository.CryptoCurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CryptoCurrencyService {
    private final CryptoCurrencyRepository repository;

    public CryptoCurrencyService(CryptoCurrencyRepository repository) {
        this.repository = repository;
    }

    public Optional<CryptoCurrency> findCurrentCurrencyPriceBySymbol(String symbol) throws IllegalArgumentException {
        if (symbol != null && exists(symbol)) {
            return repository.findBySymbol(symbol);
        } else  {
            throw new IllegalArgumentException(String.join(": ", "No currency with symbol", symbol));
        }
    }

    private boolean exists(String symbol) {
        return true;
    }
}
