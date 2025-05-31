package io.goldexchange.auth_service.service;

import io.goldexchange.auth_service.repository.AuthRepository;
import io.goldexchange.auth_service.model.User;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Value("${jwt.secret:secret}")
    private String jwtSecret;

    public boolean userExistsByPhone(String phoneNumber) {
        return authRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    public User getUserByPhone(String phoneNumber) {
        Optional<User> userOpt = authRepository.findByPhoneNumber(phoneNumber);
        return userOpt.orElse(null);
    }

    public boolean verifyTotp(String secretKey, String totp,User user) {
        String generatedTotp = generateTotp(secretKey);
        boolean valid = generatedTotp.equals(totp);


        if(!valid){
            if(user.getState().equals("temporary")) {
                authRepository.deleteById(user.getUserId()); 
            }
        }

        if(valid && user.getState().equals("temporary")) {
            // If the TOTP is valid and the user is in a temporary state, update to permanent
            user.setState("permanent");
            authRepository.save(user);
        }
       
        return valid;
    }

    private String generateTotp(String secretKey) {
        // Production-grade TOTP implementation (RFC 6238, 6-digit, 30s window, SHA1)
        long timeIndex = System.currentTimeMillis() / 30000;
        byte[] key = Base64.getDecoder().decode(secretKey);
        byte[] data = ByteBuffer.allocate(8).putLong(timeIndex).array();
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(data);
            int offset = hash[hash.length - 1] & 0xF;
            int binary = ((hash[offset] & 0x7F) << 24) |
                         ((hash[offset + 1] & 0xFF) << 16) |
                         ((hash[offset + 2] & 0xFF) << 8) |
                         (hash[offset + 3] & 0xFF);
            int otp = binary % 1000000;
            return String.format("%06d", otp);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }

    public String generateJwt(Long userId, String deviceFingerprint) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("deviceFingerprint", deviceFingerprint)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }

    public void deleteUser(Long userId) {
        authRepository.deleteById(userId);
    }

    public String generateSecretKey() {
        byte[] buffer = new byte[20]; // 160 bits for TOTP
        new SecureRandom().nextBytes(buffer);
        return Base64.getEncoder().encodeToString(buffer);
    }

    public User saveUser(String userName, String phoneNumber, String secretKey) {
        User user = new User();
        user.setUserName(userName);
        user.setPhoneNumber(phoneNumber);
        user.setSecretKey(secretKey);
        user.setState("temporary");
        return authRepository.save(user);
    }

    public String generateQrCode(String userName, String phoneNumber, String secretKey) {
        String issuer = "GoldExchange";
        String totpUri = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, phoneNumber, secretKey, issuer);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(totpUri, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}
