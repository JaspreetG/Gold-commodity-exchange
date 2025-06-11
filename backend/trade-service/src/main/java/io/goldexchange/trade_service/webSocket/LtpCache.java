package io.goldexchange.trade_service.webSocket;

import org.springframework.stereotype.Component;

@Component
public class LtpCache {
    private volatile String latestLtp;

    public void set(String ltp) {
        this.latestLtp = ltp;
    }

    public String get() {
        return latestLtp;
    }
}