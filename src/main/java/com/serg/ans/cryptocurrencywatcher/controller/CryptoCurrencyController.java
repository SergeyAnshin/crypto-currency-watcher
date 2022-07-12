package com.serg.ans.cryptocurrencywatcher.controller;

import com.serg.ans.cryptocurrencywatcher.dto.AvailableCryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.service.CryptoCurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/crypto-currency")
public class CryptoCurrencyController {
    private final CryptoCurrencyService cryptoCurrencyService;

    public CryptoCurrencyController(CryptoCurrencyService cryptoCurrencyService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
    }

    @GetMapping("/price/findBySymbol")
    public ResponseEntity<CryptoCurrency> findCurrentCurrencyPriceBySymbol(String symbol) {
        return cryptoCurrencyService.findCurrentCurrencyPriceBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Set<AvailableCryptoCurrency>> findAvailableCryptoCurrencies() {
        return ResponseEntity.ok(cryptoCurrencyService.findAvailableCryptoCurrency());
    }
}
