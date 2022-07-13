package com.serg.ans.cryptocurrencywatcher.repository.interceptor;

import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.Currency;
import com.serg.ans.cryptocurrencywatcher.entity.MonitoredPosition;
import com.serg.ans.cryptocurrencywatcher.service.CurrencyService;
import com.serg.ans.cryptocurrencywatcher.service.MathOperationService;
import com.serg.ans.cryptocurrencywatcher.service.MonitoredPositionService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PriceUpdateInterceptor extends EmptyInterceptor {
    private MonitoredPositionService monitoredPositionService;
    @Value("${percentChange}")
    private double percentChange;

    @Autowired
    @Lazy
    public void setMonitoredPositionService(MonitoredPositionService monitoredPositionService) {
        this.monitoredPositionService = monitoredPositionService;
    }

    @Override
    public void postFlush(Iterator entities) {
        List<Currency> updatedCurrencies = CurrencyService.getObjectOnlyCurrencyType(entities);
        List<MonitoredPosition> positions = monitoredPositionService.getMonitoredPositionForCurrenciesWherePriceChangePercentageGreaterGiven(updatedCurrencies, percentChange);
        monitoredPositionService.notifyAboutPriceChange(positions);
    }
}
