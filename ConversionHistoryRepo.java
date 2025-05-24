package com.Springboot.CurrencyConverter.Repository;

import com.Springboot.CurrencyConverter.Model.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionHistoryRepo extends JpaRepository<ConversionHistory, Long> {
    List<ConversionHistory> findTop10ByOrderByTimestampDesc();
}
