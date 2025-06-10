package io.goldexchange.trade_service.consumer;

import org.springframework.stereotype.Component;

@Component
public class OrderBookCache {
    private volatile String latestOrderBook;

    public void set(String data) {
        this.latestOrderBook = data;
    }

    public String get() {
        return latestOrderBook;
    }
}