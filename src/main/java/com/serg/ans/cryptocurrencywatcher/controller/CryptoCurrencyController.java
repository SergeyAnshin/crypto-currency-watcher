package com.serg.ans.cryptocurrencywatcher.controller;

import com.serg.ans.cryptocurrencywatcher.dto.AvailableCryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.service.CryptoCurrencyService;
import com.serg.ans.cryptocurrencywatcher.service.MonitoredPositionService;
import com.serg.ans.cryptocurrencywatcher.validator.constraint.ExistingSymbol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@RestController
@RequestMapping("/crypto-currency")
@Slf4j
@Validated
public class CryptoCurrencyController {
    private final CryptoCurrencyService cryptoCurrencyService;
    private final MonitoredPositionService monitoredPositionService;

    public CryptoCurrencyController(CryptoCurrencyService cryptoCurrencyService, MonitoredPositionService monitoredPositionService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.monitoredPositionService = monitoredPositionService;
    }

    @GetMapping("/price/findBySymbol")
    public ResponseEntity<CryptoCurrency> findCurrentCurrencyPriceBySymbol(@NotBlank @ExistingSymbol String symbol) {
        return cryptoCurrencyService.findCurrentCurrencyPriceBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Set<AvailableCryptoCurrency>> findAvailableCryptoCurrencies() {
        return ResponseEntity.ok(cryptoCurrencyService.findAvailableCryptoCurrency());
    }

    @GetMapping("/notify")
    public ResponseEntity<Object> notify(@NotBlank String username, @NotBlank @ExistingSymbol String symbol) {
        monitoredPositionService.notify(username, symbol);
        return ResponseEntity.ok().build();
    }
}
