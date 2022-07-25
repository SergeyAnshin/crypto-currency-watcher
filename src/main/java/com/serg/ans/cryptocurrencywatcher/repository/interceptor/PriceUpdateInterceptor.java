package com.serg.ans.cryptocurrencywatcher.repository.interceptor;

import com.serg.ans.cryptocurrencywatcher.entity.Currency;
import com.serg.ans.cryptocurrencywatcher.entity.MonitoredPosition;
import com.serg.ans.cryptocurrencywatcher.service.CurrencyService;
import com.serg.ans.cryptocurrencywatcher.service.MonitoredPositionService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class PriceUpdateInterceptor extends EmptyInterceptor {
    private MonitoredPositionService monitoredPositionService;
    @Value("${percentChange}")
    private double percentChange;

    @Lazy
    public PriceUpdateInterceptor(MonitoredPositionService monitoredPositionService) {
        this.monitoredPositionService = monitoredPositionService;
    }

    @Override
    public void postFlush(Iterator entities) {
        List<Currency> updatedCurrencies = CurrencyService.getObjectOnlyCurrencyType(entities);
        List<MonitoredPosition> positions = monitoredPositionService.getMonitoredPositionForCurrenciesWherePriceChangePercentageGreaterGiven(updatedCurrencies, percentChange);
        monitoredPositionService.notifyAboutPriceChange(positions);
    }
}
