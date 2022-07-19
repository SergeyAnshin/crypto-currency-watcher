package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.Currency;
import com.serg.ans.cryptocurrencywatcher.repository.CryptoCurrencyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CryptoCurrencyServiceTest {
    @InjectMocks
    private CryptoCurrencyService currencyService;
    @Mock
    private CryptoCurrencyRepository currencyRepository;
    private static CryptoCurrency existCurrency;

    @BeforeAll
    static void initEntities() {
        existCurrency = CryptoCurrency.builder().id(90).symbol("BTC").price(15).build();
    }

    @Test
    void findCurrentCurrencyPriceBySymbolThrowExceptionIfSymbolNull() {
        assertThrows(IllegalArgumentException.class, () -> currencyService.findCurrentCurrencyPriceBySymbol(null));
    }

    @Test
    void findCurrentCurrencyPriceBySymbolThrowExceptionIfSymbolWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> currencyService.findCurrentCurrencyPriceBySymbol(" "));
    }

    @Test
    void findCurrentCurrencyPriceBySymbolThrowExceptionIfSymbolNotExists() {
        assertThrows(IllegalArgumentException.class, () -> currencyService.findCurrentCurrencyPriceBySymbol("GGG"));
    }

    @Test
    void findBySymbol() {
        when(currencyRepository.findBySymbol("BTC")).thenReturn(Optional.of(existCurrency));
        assertEquals(existCurrency, currencyService.findBySymbol("BTC").get());
    }
}