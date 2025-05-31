package io.goldexchange.auth_service.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import io.goldexchange.auth_service.model.User;
import io.goldexchange.auth_service.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.apache.commons.codec.binary.Base32;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public boolean userExistsByPhone(String phoneNumber) {
        return authRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    public User getUserByPhone(String phoneNumber) {
        Optional<User> userOpt = authRepository.findByPhoneNumber(phoneNumber);
        return userOpt.orElse(null);
    }

    public String generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
            keyGenerator.init(160);
            SecretKey secretKey = keyGenerator.generateKey();
            // Add time-based randomness
            long timestamp = System.currentTimeMillis();
            byte[] timeBytes = Long.toString(timestamp).getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = secretKey.getEncoded();
            byte[] combined = new byte[keyBytes.length + timeBytes.length];
            System.arraycopy(keyBytes, 0, combined, 0, keyBytes.length);
            System.arraycopy(timeBytes, 0, combined, keyBytes.length, timeBytes.length);
            // Use Base32 encoding for Google Authenticator compatibility
            Base32 base32 = new Base32();
            return base32.encodeToString(combined).replace("=", "");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secret key", e);
        }
    }

    public User saveUser(String userName, String phoneNumber, String secretKey) {
        User user = new User();
        user.setUserName(userName);
        user.setPhoneNumber(phoneNumber);
        user.setSecretKey(secretKey);
        user.setState("temporary");
        return authRepository.save(user);
    }

    public String generateQrCode(String userName, String secretKey) {
        String issuer = "GoldExchange";
        // Use Base32 secretKey for QR code
        String qrCodeData = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, userName, secretKey, issuer);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            // Still encode the image as Base64 for data URI
            return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    public boolean verifyTotp(String secretKey, String totp, User user) {
        try {
            // Google Authenticator compatible TOTP
            long timeIndex = System.currentTimeMillis() / 1000 / 30;
            // Use Base32 decoder to match the Base32-encoded secret key
            Base32 base32 = new Base32();
            byte[] keyBytes = base32.decode(secretKey);
            SecretKeySpec signKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signKey);
            byte[] data = new byte[8];
            long value = timeIndex;
            for (int i = 7; value > 0; i--) {
                data[i] = (byte) (value & 0xFF);
                value >>= 8;
            }
            byte[] hash = mac.doFinal(data);
            int offset = hash[hash.length - 1] & 0xF;
            int binary = ((hash[offset] & 0x7F) << 24) |
                         ((hash[offset + 1] & 0xFF) << 16) |
                         ((hash[offset + 2] & 0xFF) << 8) |
                         (hash[offset + 3] & 0xFF);
            int otp = binary % 1000000;
            String generatedTotp = String.format("%06d", otp);

            boolean valid=generatedTotp.equals(totp);
            if(!valid){
                if(user.getState().equals("temporary")){
                    authRepository.deleteById(user.getUserId());
                }
            }

            if (valid) {
                user.setState("permanent");
                authRepository.save(user);
            }
            return valid;

        } catch (Exception e) {
            throw new RuntimeException("Failed to verify TOTP", e);
        }
    }

    public String generateJwt(Long userId, String deviceFingerprint) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("deviceFingerprint", deviceFingerprint)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}