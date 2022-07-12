package com.serg.ans.cryptocurrencywatcher.repository;

import com.serg.ans.cryptocurrencywatcher.entity.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {

    Optional<CryptoCurrency> findBySymbol(String symbol);
}
