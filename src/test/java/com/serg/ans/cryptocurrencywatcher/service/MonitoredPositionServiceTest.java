package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import com.serg.ans.cryptocurrencywatcher.entity.MonitoredPosition;
import com.serg.ans.cryptocurrencywatcher.entity.User;
import com.serg.ans.cryptocurrencywatcher.repository.MonitoredPositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MonitoredPositionServiceTest {
    private static final String TEST_NOTIFY_FILE = "src/test/java/com/serg/ans/cryptocurrencywatcher/service/log/monitoredPositionServiceTest.log";
    @Mock
    private MonitoredPositionRepository monitoredPositionRepository;
    @Mock
    private CryptoCurrencyService cryptoCurrencyService;
    @Mock
    private UserService userService;
    @InjectMocks
    private MonitoredPositionService monitoredPositionService;
    private static MonitoredPosition savingMonitoredPosition;
    private static MonitoredPosition savedMonitoredPosition;

    @BeforeAll
    static void initEntity() {
        savingMonitoredPosition = MonitoredPosition.builder()
                .currency(CryptoCurrency.builder().id(1).symbol("testSymbol").price(23).build())
                .startMonitoringPrice(15)
                .user(User.builder().id(1).username("testUsername").build())
                .build();

        savedMonitoredPosition = MonitoredPosition.builder()
                .id(1)
                .currency(CryptoCurrency.builder().id(1).symbol("testSymbol").price(23).build())
                .startMonitoringPrice(15)
                .user(User.builder().id(1).username("testUsername").build())
                .build();
    }

    @Test
    void returnEmptyOptionalIfSaveNullEntity() {
        assertTrue(monitoredPositionService.save(null).isEmpty());
    }

    @Test
    void save() {
        when(monitoredPositionRepository.save(savingMonitoredPosition)).thenReturn(savedMonitoredPosition);
        assertEquals(savedMonitoredPosition, monitoredPositionService.save(savingMonitoredPosition).get());
    }

    @Test
    void findAllByCurrencyReturnEmptyListIfCurrencyNull() {
        assertTrue(monitoredPositionService.findAllByCurrency(null).isEmpty());
    }

    @Test
    void findAllByCurrencyReturnEmptyListIfCurrencyIdZero() {
        assertTrue(monitoredPositionService.findAllByCurrency(CryptoCurrency.builder().build()).isEmpty());
    }

    @Test
    void notifyAboutPriceChangeShowInConsole() {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(TEST_NOTIFY_FILE));
             BufferedReader bufferedReader = new BufferedReader(new FileReader(TEST_NOTIFY_FILE))) {
            System.setOut(printStream);
            monitoredPositionService.notifyAboutPriceChange(List.of(savedMonitoredPosition));
            String logFromConsole = bufferedReader.readLine();
            assertEquals(monitoredPositionService.getPriceChangeNotifyMessage(savedMonitoredPosition),
                    logFromConsole.substring(logFromConsole.indexOf("-") + 2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(TEST_NOTIFY_FILE);
        file.delete();
    }

    @Test
    void priceChangeNotifyMessageContains() {
        String notifyMessage = monitoredPositionService.getPriceChangeNotifyMessage(savedMonitoredPosition);
        assertAll(
                () -> assertTrue(notifyMessage.contains(savedMonitoredPosition.getCurrency().getSymbol())),
                () -> assertTrue(notifyMessage.contains(savedMonitoredPosition.getUser().getUsername())),
                () -> assertTrue(notifyMessage.contains(String.valueOf(MathOperationService.getPercentageDifferenceBetweenNumbers(savedMonitoredPosition.getCurrency().getPrice(),
                        savedMonitoredPosition.getStartMonitoringPrice()))))
        );
    }
}