package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.Currency;
import com.serg.ans.cryptocurrencywatcher.entity.MonitoredPosition;
import com.serg.ans.cryptocurrencywatcher.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {
    private static User firstUser;
    private static User secondUser;
    private static MonitoredPosition firstMonitoredPosition;
    private static MonitoredPosition secondMonitoredPosition;
    private static CryptoCurrency firstCurrency;
    private static CryptoCurrency secondCurrency;
    private static List<Object> updatableEntities;
    private static List<Object> expectedEntities;
    private static List<Object> unexpectedEntities;

    @BeforeAll
    @Order(1)
    static void initEntities() {
        firstUser = User.builder().id(1).build();
        secondUser = User.builder().id(2).build();
        firstMonitoredPosition = MonitoredPosition.builder().id(1).build();
        secondMonitoredPosition = MonitoredPosition.builder().id(2).build();
        firstCurrency= CryptoCurrency.builder().id(1).build();
        secondCurrency = CryptoCurrency.builder().id(2).build();
    }

    @BeforeAll
    @Order(2)
    static void initCollections() {
        updatableEntities = new ArrayList<>(List.of(firstUser, firstCurrency, firstMonitoredPosition,
                secondUser, secondCurrency, secondMonitoredPosition));
        expectedEntities = new ArrayList<>(List.of(secondCurrency, firstCurrency));
        unexpectedEntities = new ArrayList<>(List.of(firstCurrency, firstUser));
    }

    @Test
    void returnedCollectionEqualsExpected() {
        assertArrayEquals(expectedEntities.toArray(),
                CurrencyService.getObjectOnlyCurrencyType(updatableEntities.iterator()).toArray());
    }

    @Test
    void returnNotNullIfParameterIsNull() {
        assertNotNull(CurrencyService.getObjectOnlyCurrencyType(null));
    }

    @Test
    void returnCollectionWhereAllEntitiesOnlyCurrencyType() {
        assertTrue(CurrencyService.getObjectOnlyCurrencyType(updatableEntities.iterator())
                .stream()
                .allMatch(currency -> currency instanceof Currency));
    }
}