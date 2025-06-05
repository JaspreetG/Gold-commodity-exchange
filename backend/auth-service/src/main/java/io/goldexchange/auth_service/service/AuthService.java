package io.goldexchange.auth_service.service;

import io.goldexchange.auth_service.dto.UserDTO;
import io.goldexchange.auth_service.model.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    boolean userExistsByPhone(String phoneNumber);

    User getUserByPhone(String phoneNumber);

    String generateSecretKey();

    User saveUser(String userName, String phoneNumber, String secretKey);

    String generateQrCode(String userName, String secretKey);

    boolean verifyTotp(String secretKey, String totp, User user);

    String generateJwt(Long userId, String deviceFingerprint);

    void createWallet(UserDTO user, String jwt, String deviceFingerprint);

    UserDTO getUserById(Long userId);

    void logout(HttpServletResponse response);

}