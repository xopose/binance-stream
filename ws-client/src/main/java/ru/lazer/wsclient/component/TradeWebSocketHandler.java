package ru.lazer.wsclient.component;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.lazer.wsclient.model.BinanceData;
import ru.lazer.wsclient.repository.BinanceDataRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TradeWebSocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final BinanceDataRepository repository;

    public TradeWebSocketHandler(BinanceDataRepository repository) {
        this.repository = repository;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        System.out.println(">>> [WebSocket] Подключился клиент: " + session.getId());
        sessions.add(session);
        System.out.println(sessions.size());
        List<BinanceData> allData = repository.findAll();
        for (BinanceData data : allData) {
            String message = String.format("{\"name\":\"%s\",\"value\":%s,\"timestamp\":%d}",
                    data.getName(), data.getValue(), data.getTimestamp());
            session.sendMessage(new TextMessage(message));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    public void broadcastMessage(String message) {
        System.out.println(">>> [WebSocket] Отправка: " + message);
        System.out.println(sessions.size());
        sessions.forEach(session -> {
            try {
                System.out.println(">>> [WebSocket] Отправка на клиент: " + session.getId());
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}