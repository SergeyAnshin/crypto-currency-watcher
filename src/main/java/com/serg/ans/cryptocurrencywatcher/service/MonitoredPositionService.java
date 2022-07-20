package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.Currency;
import com.serg.ans.cryptocurrencywatcher.entity.MonitoredPosition;
import com.serg.ans.cryptocurrencywatcher.entity.User;
import com.serg.ans.cryptocurrencywatcher.repository.MonitoredPositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MonitoredPositionService {
    private final MonitoredPositionRepository monitoredPositionRepository;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final UserService userService;

    public MonitoredPositionService(MonitoredPositionRepository monitoredPositionRepository, CryptoCurrencyService cryptoCurrencyService, UserService userService) {
        this.monitoredPositionRepository = monitoredPositionRepository;
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.userService = userService;
    }

    public Optional<MonitoredPosition> save(MonitoredPosition monitoredPosition) {
        if (monitoredPosition == null) {
            return Optional.empty();
        } else {
            return Optional.of(monitoredPositionRepository.save(monitoredPosition));
        }
    }

    public void notify(String username, String symbol) throws IllegalArgumentException {
        if (username == null || symbol == null || username.isBlank() || symbol.isBlank()) {
            throw new IllegalArgumentException();
        } else {
            Optional<? extends Currency> currency = cryptoCurrencyService.findBySymbol(symbol);
            Optional<User> user = userService.findByUsername(username);
            if (currency.isPresent() && user.isPresent()) {
                MonitoredPosition monitoredPosition = MonitoredPosition.builder()
                        .currency(currency.get())
                        .startMonitoringPrice(currency.get().getPrice())
                        .user(user.get())
                        .build();
                save(monitoredPosition);
            }
        }
    }

    public List<MonitoredPosition> findAllByCurrency(Currency currency) {
        if (currency == null || currency.getId() == 0) {
            return Collections.emptyList();
        } else {
            return monitoredPositionRepository.findAllByCurrencyId(currency.getId());
        }
    }

    public List<MonitoredPosition> getMonitoredPositionForCurrenciesWherePriceChangePercentageGreaterGiven(List<Currency> currencies, double percentChange) {
        return currencies.stream()
                .map(currency -> getMonitoredPositionWithCurrencyWherePriceChangePercentageGreaterGiven(currency, percentChange))
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }
    private List<MonitoredPosition> getMonitoredPositionWithCurrencyWherePriceChangePercentageGreaterGiven(Currency currency, double percentChange) {
        if (currency == null) {
            return Collections.emptyList();
        } else {
            return findAllByCurrency(currency).stream()
                    .filter(monitoredPosition -> MathOperationService.isDifferenceBetweenNumbersGreaterPercent(currency.getPrice(), monitoredPosition.getStartMonitoringPrice(), percentChange))
                    .collect(Collectors.toList());
        }
    }

    public void notifyAboutPriceChange(List<MonitoredPosition> monitoredPositions) {
        monitoredPositions.forEach(monitoredPosition -> log.warn(getPriceChangeNotifyMessage(monitoredPosition)));
    }

    public String getPriceChangeNotifyMessage(MonitoredPosition monitoredPosition) {
        return String.join(", ",
                monitoredPosition.getCurrency().getSymbol(),
                monitoredPosition.getUser().getUsername(),
                String.valueOf(MathOperationService.getPercentageDifferenceBetweenNumbers(monitoredPosition.getCurrency().getPrice(),
                        monitoredPosition.getStartMonitoringPrice())));
    }
}
