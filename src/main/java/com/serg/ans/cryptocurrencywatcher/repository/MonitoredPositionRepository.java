package com.serg.ans.cryptocurrencywatcher.repository;

import com.serg.ans.cryptocurrencywatcher.entity.MonitoredPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonitoredPositionRepository extends JpaRepository<MonitoredPosition, Long> {

    Optional<MonitoredPosition> findByCurrencyId(long currencyId);

    List<MonitoredPosition> findAllByCurrencyId(long currencyId);
}
