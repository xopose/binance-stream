package ru.lazer.wsclient.component;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TradeWebSocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println(">>> [WebSocket] Подключился клиент: " + session.getId());
        sessions.add(session);
        System.out.println(sessions.size());
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