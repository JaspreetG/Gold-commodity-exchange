package io.goldexchange.trade_service.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Broadcaster for Last Traded Price (LTP).
 * Periodically sends the latest LTP from the cache to WebSocket clients.
 */
@Component
public class LtpBroadcaster {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private LtpCache ltpCache;

    /**
     * Broadcasts the latest LTP to "/topic/ltp" every 3 seconds.
     */
    @Scheduled(fixedRate = 3000)
    public void broadcastLtp() {
        String ltp = ltpCache.get();
        if (ltp != null) {
            messagingTemplate.convertAndSend("/topic/ltp", ltp);
        }
    }
}