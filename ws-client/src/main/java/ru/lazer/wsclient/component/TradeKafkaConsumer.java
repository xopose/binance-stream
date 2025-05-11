package ru.lazer.wsclient.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.lazer.wsclient.model.BinanceData;
import ru.lazer.wsclient.repository.BinanceDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class TradeKafkaConsumer {

    private final TradeWebSocketHandler webSocketHandler;
    private final BinanceDataRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> buffer = new ArrayList<>();

    public TradeKafkaConsumer(TradeWebSocketHandler webSocketHandler,
                              BinanceDataRepository repository) {
        this.webSocketHandler = webSocketHandler;
        this.repository = repository;
    }

    @KafkaListener(topics = "trade_info", groupId = "ws-client-group")
    public synchronized void listen(String message) throws JsonProcessingException {
        System.out.println(">>> [Kafka] Получено сообщение: " + message);
        webSocketHandler.broadcastMessage(message);

        buffer.add(message);

        if (buffer.size() >= 4) {
            saveToDatabase(buffer);
            buffer.clear();
        }
    }

    private void saveToDatabase(List<String> messages) throws JsonProcessingException {
        for (String msg : messages) {
            JsonNode node = objectMapper.readTree(msg);
            double value = node.get("value").asDouble();
            long timestamp = node.get("timestamp").asLong();
            BinanceData data = new BinanceData("", String.valueOf(value), timestamp);
            repository.save(data);
            System.out.println("Сохраняю в БД: " + msg);
        }
    }
}
