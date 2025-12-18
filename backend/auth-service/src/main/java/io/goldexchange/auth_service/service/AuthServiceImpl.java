package io.goldexchange.auth_service.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import io.goldexchange.auth_service.dto.UserDTO;
import io.goldexchange.auth_service.model.User;
import io.goldexchange.auth_service.repository.AuthRepositoryWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;
import org.apache.commons.codec.binary.Base32;
import java.util.Map;

/**
 * Implementation of the AuthService.
 * Handles user authentication, TOTP generation/verification, JWT management, and wallet creation.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthRepositoryWrapper authRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wallet.service.url}")
    private String walletServiceUrl;

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Checks if a user exists by their phone number.
     *
     * @param phoneNumber The phone number to check.
     * @return true if the user exists, false otherwise.
     */
    @Override
    public boolean userExistsByPhone(String phoneNumber) {
        return authRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    /**
     * Retrieves a user by their phone number.
     *
     * @param phoneNumber The phone number to search for.
     * @return The User object if found, null otherwise.
     */
    @Override
    public User getUserByPhone(String phoneNumber) {
        Optional<User> userOpt = authRepository.findByPhoneNumber(phoneNumber);
        return userOpt.orElse(null);
    }

    /**
     * Generates a secret key for TOTP.
     *
     * @return A Base32 encoded secret key string.
     */
    @Override
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
            logger.error("Failed to generate secret key", e);
            throw new RuntimeException("Failed to generate secret key", e);
        }
    }

    /**
     * Saves a new user to the repository.
     *
     * @param userName    The user's name.
     * @param phoneNumber The user's phone number.
     * @param secretKey   The user's secret key for TOTP.
     * @return The saved User object.
     */
    @Override
    public User saveUser(String userName, String phoneNumber, String secretKey) {
        User user = new User();
        user.setUserName(userName);
        user.setPhoneNumber(phoneNumber);
        user.setSecretKey(secretKey);
        user.setState("temporary");
        return authRepository.save(user);
    }

    /**
     * Generates a QR code for TOTP setup.
     *
     * @param userName  The user's name.
     * @param secretKey The user's secret key.
     * @return A Base64 encoded data URI of the QR code image.
     */
    @Override
    public String generateQrCode(String userName, String secretKey) {
        String issuer = "GoldExchange";
        // Use Base32 secretKey for QR code
        String qrCodeData = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, userName, secretKey,
                issuer);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            // Still encode the image as Base64 for data URI
            return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException | java.io.IOException e) {
            logger.error("Failed to generate QR code", e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    /**
     * Verifies the provided TOTP code against the user's secret key.
     *
     * @param secretKey The user's secret key.
     * @param totp      The TOTP code to verify.
     * @param user      The user object.
     * @return true if the TOTP is valid, false otherwise.
     */
    @Override
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

            boolean valid = generatedTotp.equals(totp);
            if (!valid) {
                if (user.getState().equals("temporary")) {
                    authRepository.deleteById(user.getUserId());
                }
            }

            if (valid) {
                user.setState("permanent");
                authRepository.save(user);
            }
            return valid;

        } catch (Exception e) {
            logger.error("Failed to verify TOTP", e);
            throw new RuntimeException("Failed to verify TOTP", e);
        }
    }

    /**
     * Generates a JWT for the user.
     *
     * @param userId            The ID of the user.
     * @param deviceFingerprint The device fingerprint.
     * @return The generated JWT string.
     */
    @Override
    public String generateJwt(Long userId, String deviceFingerprint) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("deviceFingerprint", deviceFingerprint)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * Creates a wallet for the user in the Wallet Service.
     *
     * @param user              The UserDTO.
     * @param jwt               The JWT token.
     * @param deviceFingerprint The device fingerprint.
     */
    @Override
    public void createWallet(UserDTO user, String jwt, String deviceFingerprint) {
        try {
            if ("permanent".equals(user.getState())) {
                String url = walletServiceUrl;

                HttpHeaders headers = new HttpHeaders();
                headers.set("Cookie", "jwt=" + jwt); // Send JWT in cookie format
                headers.set("X-Device-Fingerprint", deviceFingerprint);
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

                ResponseEntity<Map> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        Map.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Wallet created successfully for user ID: {}", user.getUserId());
                } else {
                    logger.error("Wallet creation failed with status: {}", response.getStatusCode());
                }
            }

        } catch (Exception e) {
            logger.error("Failed to create wallet: {}", e.getMessage());
        }
    }

    /**
     * Retrieves user details by ID.
     *
     * @param userId The ID of the user.
     * @return The UserDTO, or null if not found.
     */
    @Override
    public UserDTO getUserById(Long userId) {

        Optional<User> userOpt = authRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    /**
     * Logs out the user by clearing the JWT cookie.
     *
     * @param response The HttpServletResponse.
     */
    public void logout(HttpServletResponse response) {

        // Remove the JWT cookie by setting it with maxAge=0
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

}