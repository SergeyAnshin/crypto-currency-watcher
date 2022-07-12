package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.Currency;
import com.serg.ans.cryptocurrencywatcher.repository.CryptoCurrencyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CryptoCurrencyService {
    public static final String SPECIFIC_CURRENCY_INFORMATION_URL  = "https://api.coinlore.net/api/ticker/";
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

    @Scheduled(fixedRate = 60000)
    private void updateCurrentCurrencyPrices() {
        System.out.println("ВЫПОЛНЯЕТСЯ");
        Set<Currency> availableCurrencies = new HashSet<>() {{
            add(CryptoCurrency.builder().id(80).symbol("A").build());
            add(CryptoCurrency.builder().id(90).symbol("B").build());
        }};
        String url = createUrlToGetCurrentStateOfCurrencies(availableCurrencies);
        List<CryptoCurrency> currencies = getCurrentStateForAvailableCurrenciesFromResource(url);
        if (currencies != null && !currencies.isEmpty()) {
            repository.saveAll(currencies);
        }
    }

    private String createUrlToGetCurrentStateOfCurrencies(Set<Currency> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            return null;
        } else {
            String urlParameters = getParametersForCurrencies(currencies);
            return generateUrlToAnotherResource(SPECIFIC_CURRENCY_INFORMATION_URL, urlParameters);
        }
    }

    private String getParametersForCurrencies(Set<Currency> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            return null;
        } else {
            return currencies.stream()
                    .map(currency -> String.valueOf(currency.getId()))
                    .collect(Collectors.joining(","));
        }
    }

    private String generateUrlToAnotherResource(String resourceUrl, String urlParameters) {
        if (resourceUrl == null || urlParameters == null) {
            return null;
        } else {
            return String.join("", resourceUrl, "?id=",  urlParameters);
        }
    }

    private List<CryptoCurrency> getCurrentStateForAvailableCurrenciesFromResource(String url) {
        RestTemplate restTemplate = new RestTemplate();
        CryptoCurrency[] currencies = restTemplate.getForEntity(url, CryptoCurrency[].class).getBody();
        return currencies == null || currencies.length == 0 ? null : List.of(currencies);
    }
}
