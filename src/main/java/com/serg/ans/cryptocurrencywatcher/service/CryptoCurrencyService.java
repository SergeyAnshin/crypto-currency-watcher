package com.serg.ans.cryptocurrencywatcher.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serg.ans.cryptocurrencywatcher.dto.AvailableCryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.Currency;
import com.serg.ans.cryptocurrencywatcher.mapper.CryptoCurrencyMapper;
import com.serg.ans.cryptocurrencywatcher.repository.CryptoCurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CryptoCurrencyService {
    public static final String SPECIFIC_CURRENCY_INFORMATION_URL  = "https://api.coinlore.net/api/ticker/";
    public static final String FILE_NAME_FOR_AVAILABLE_CRYPTOCURRENCIES  = "available-cryptocurrencies.json";
    private final CryptoCurrencyRepository repository;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public CryptoCurrencyService(CryptoCurrencyRepository repository, ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
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
        String url = createUrlToGetCurrentStateOfCurrencies(getAvailableCurrenciesFromResources(FILE_NAME_FOR_AVAILABLE_CRYPTOCURRENCIES));
        List<CryptoCurrency> currencies = getCurrentStateForAvailableCurrenciesFromResource(url);
        if (currencies != null && !currencies.isEmpty()) {
            repository.saveAll(currencies);
        }
    }

    private String createUrlToGetCurrentStateOfCurrencies(Set<? extends Currency> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            return null;
        } else {
            String urlParameters = getParametersForCurrencies(currencies);
            return generateUrlToAnotherResource(SPECIFIC_CURRENCY_INFORMATION_URL, urlParameters);
        }
    }

    private String getParametersForCurrencies(Set<? extends Currency> currencies) {
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
        try {
            CryptoCurrency[] currencies = restTemplate.getForEntity(url, CryptoCurrency[].class).getBody();
            return currencies == null || currencies.length == 0 ? null : List.of(currencies);
        } catch (RuntimeException exception) {
            log.error("Problem with getting the current state of the cryptocurrency");
            return null;
        }
    }

    private Set<CryptoCurrency> getAvailableCurrenciesFromResources(String fileName) {
        try (InputStream inputStream = resourceLoader.getResource(String.join("", "classpath:", fileName))
                .getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>(){});
        } catch (IOException e) {
            log.error(String.join(" ", "Problems with reading JSON file", fileName));
            return null;
        }
    }

    public Set<AvailableCryptoCurrency> findAvailableCryptoCurrency() {
        return CryptoCurrencyMapper.mapToAvailableCryptoCurrencySet(getAvailableCurrenciesFromResources(FILE_NAME_FOR_AVAILABLE_CRYPTOCURRENCIES));
    }
}
