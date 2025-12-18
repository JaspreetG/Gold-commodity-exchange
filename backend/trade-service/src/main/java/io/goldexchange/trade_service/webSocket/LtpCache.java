package io.goldexchange.trade_service.webSocket;

import org.springframework.stereotype.Component;

/**
 * Thread-safe cache for the latest Last Traded Price (LTP).
 */
@Component
public class LtpCache {
    private volatile String latestLtp;

    /**
     * Updates the latest LTP.
     *
     * @param ltp The new LTP JSON string.
     */
    public void set(String ltp) {
        this.latestLtp = ltp;
    }

    /**
     * Retrieves the latest LTP.
     *
     * @return The latest LTP JSON string.
     */
    public String get() {
        return latestLtp;
    }
}