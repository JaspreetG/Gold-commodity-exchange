package io.goldexchange.trade_service.webSocket;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import io.jsonwebtoken.*;
import jakarta.servlet.http.*;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        try {
            if (request instanceof ServletServerHttpRequest servletRequest) {
                HttpServletRequest req = servletRequest.getServletRequest();
                Cookie[] cookies = req.getCookies();

                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("jwt".equals(cookie.getName())) {
                            String jwtToken = cookie.getValue();

                            Claims claims = Jwts.parser()
                                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                                    .parseClaimsJws(jwtToken)
                                    .getBody();

                            Long userId = claims.get("userId", Long.class);
                            String deviceFingerprint = claims.get("deviceFingerprint", String.class);
                            String requestFingerprint = req.getParameter("fingerprint");

                            System.out.println("UserId: " + userId);
                            System.out.println("JWT Fingerprint: " + deviceFingerprint);
                            System.out.println("Query Param Fingerprint: " + requestFingerprint);

                            if (requestFingerprint == null || !requestFingerprint.equals(deviceFingerprint)) {
                                System.out.println("❌ Fingerprint mismatch");
                                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                                return false;
                            }

                            attributes.put("userId", userId); // optionally store user info
                            System.out.println("✅ WebSocket handshake allowed");
                            return true;
                        }
                    }
                }
            }

            // No JWT cookie
            System.out.println("❌ JWT cookie not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;

        } catch (Exception e) {
            System.err.println("❌ Exception in WebSocket handshake: " + e.getMessage());
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        // Optional logging
        if (exception != null) {
            System.err.println("❌ Handshake exception: " + exception.getMessage());
        }
    }
}
// @Component
// public class JwtHandshakeInterceptor implements HandshakeInterceptor {

// @Value("${jwt.secret}")
// private String jwtSecret;

// @Override
// public boolean beforeHandshake(ServerHttpRequest request,ServerHttpResponse
// response,WebSocketHandler wsHandler,Map<String, Object> attributes) throws
// Exception {
// if (request instanceof ServletServerHttpRequest servletRequest) {
// HttpServletRequest req = servletRequest.getServletRequest();
// Cookie[] cookies = req.getCookies();

// if (cookies != null) {
// for (Cookie cookie : cookies) {
// if ("jwt".equals(cookie.getName())) {
// String jwtToken = cookie.getValue();

// try {
// Claims claims = Jwts.parser()
// .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
// .parseClaimsJws(jwtToken)
// .getBody();

// Long userId = claims.get("userId", Long.class);
// String deviceFingerprint = claims.get("deviceFingerprint", String.class);

// System.out.println("*********IN INTERCEPTOR****************");
// System.out.println(userId);
// System.out.println("JwtWalaFingerPrint:"+deviceFingerprint);

// // String requestFingerprint = req.getHeader("X-Device-Fingerprint");
// String requestFingerprint = req.getParameter("fingerprint");

// System.out.println("queryParamWalaFingerPrint:"+requestFingerprint);
// if (requestFingerprint == null ||
// !requestFingerprint.equals(deviceFingerprint)) {
// if (response instanceof
// org.springframework.http.server.ServletServerHttpResponse servletResponse) {
// servletResponse.getServletResponse().setStatus(HttpServletResponse.SC_UNAUTHORIZED);
// servletResponse.getServletResponse().getWriter().write("Unauthorized: Device
// fingerprint mismatch");
// System.out.println("****************FINGER PRINT MISMATCH OR NULL*********");
// }
// return false;
// }
// System.out.println("*******TRUE RETURN HUA **********");
// return true;
// } catch (JwtException e) {
// response.setStatusCode(HttpStatus.UNAUTHORIZED);
// return false;
// }
// }
// }
// }
// }

// response.setStatusCode(HttpStatus.UNAUTHORIZED);
// return false;
// }

// @Override
// public void afterHandshake(ServerHttpRequest request,
// ServerHttpResponse response,
// WebSocketHandler wsHandler,
// Exception exception) {
// // No implementation needed for now
// }
// }
