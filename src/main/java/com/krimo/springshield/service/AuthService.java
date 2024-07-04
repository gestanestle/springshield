package com.krimo.springshield.service;

import com.krimo.springshield.dto.request.CodeDTO;
import com.krimo.springshield.dto.request.SignupDTO;
import com.krimo.springshield.exception.RequestException;
import com.krimo.springshield.model.ActivationCode;
import com.krimo.springshield.model.User;
import com.krimo.springshield.repository.ActivationCodeRepository;
import com.krimo.springshield.repository.UserRepository;
import com.krimo.springshield.security.PasswordEncoder;
import com.krimo.springshield.security.utils.JWTUtils;
import com.krimo.springshield.utils.Mailer;
import com.krimo.springshield.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.krimo.springshield.utils.Message.*;

public interface AuthService {

    void signUp(SignupDTO signupDTO);
    void activate(CodeDTO codeDTO);
    void sendActivation(String email);
    User authorize(String header); // Authorize from access token
    User validate(String header); // Validate refresh token
}

@Service
@RequiredArgsConstructor
@Slf4j
class AuthServiceImpl implements AuthService {

    private final Mailer mailer;
    private final UserRepository userRepository;
    private final ActivationCodeRepository codeRepository;

    private final PasswordEncoder passwordEncoder;
    private String encode(String pass) { return passwordEncoder.bCryptPasswordEncoder().encode(pass); }

    @Override
    public void signUp(SignupDTO signupDTO) {

        if (userRepository.findByEmail(signupDTO.email()).isPresent())
            throw new RequestException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_TAKEN.message);

        User user = User.create(signupDTO.lastName(), signupDTO.firstName(),
                signupDTO.middleName(),
                signupDTO.birthdate(), signupDTO.email(), encode(signupDTO.password()));

        userRepository.save(user);

        sendActivation(signupDTO.email());
    }

    @Override
    public void activate(CodeDTO dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow();
        ActivationCode activationCode = codeRepository.findByUserAndCode(user, dto.code()).orElseThrow();

        if (LocalDateTime.now().isAfter(activationCode.getExpiresAt()))
            throw new RequestException(HttpStatus.BAD_REQUEST, "Token has expired.");

        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);
    }

    @Override
    public void sendActivation(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String code = Utils.generateSerialCode();
        ActivationCode activationCode = ActivationCode.of(code, user);
        codeRepository.save(activationCode);

        mailer.sendMaiL(email, EMAIL_SUB.message, String.format(EMAIL_BODY.message, code));
    }

    @Override
    public User authorize(String header) {

        String token = token(header);
        String email = JWTUtils.decodeToken(token).getSubject();

        if (!JWTUtils.getGrantType(token).equals("access"))
            throw new RequestException(HttpStatus.UNAUTHORIZED, "Invalid access token");

        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public User validate(String header) {

        String token = token(header);
        String email = JWTUtils.decodeToken(token).getSubject();

        if (!JWTUtils.getGrantType(token).equals("refresh"))
            throw new RequestException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");

        return userRepository.findByEmail(email).orElseThrow();
    }

    private String token(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring("Bearer ".length());
        }
        throw new RequestException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }


}