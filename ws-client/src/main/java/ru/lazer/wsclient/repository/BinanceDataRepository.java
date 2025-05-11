package ru.lazer.wsclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lazer.wsclient.model.BinanceData;


public interface BinanceDataRepository extends JpaRepository<BinanceData, Long> {
}

