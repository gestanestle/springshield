package com.krimo.springshield.service;

import com.krimo.springshield.dto.request.UpdateMeDTO;
import com.krimo.springshield.dto.response.UserDTO;
import com.krimo.springshield.model.User;
import com.krimo.springshield.repository.ActivationCodeRepository;
import com.krimo.springshield.repository.UserRepository;
import com.krimo.springshield.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

public interface MeService {
    UserDTO get(String email);
    void update(String email, UpdateMeDTO dto);
    void delete(String email);
}

@Service
@RequiredArgsConstructor
class MeServiceImpl implements MeService {

    private final UserRepository userRepository;
    private final ActivationCodeRepository codeRepository;

    private final PasswordEncoder passwordEncoder;
    private String encode(String pass) { return passwordEncoder.bCryptPasswordEncoder().encode(pass); }


    @Override
    public UserDTO get(String email){
        User user = userRepository.findByEmail(email).orElseThrow();

        return new UserDTO(
                user.getId(),
                user.getLastName(), user.getFirstName(), user.getMiddleName(), user.getBirthdate(),
                user.getEmail(), user.isEnabled(), user.getUserRole(), user.getCreatedAt()
        );
    }

    @Override
    public void update(String email, UpdateMeDTO dto) {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (Objects.nonNull(dto.password()))     user.setPassword(encode(dto.password()));
        if (Objects.nonNull(dto.lastName()))     user.setLastName(dto.lastName());
        if (Objects.nonNull(dto.firstName()))    user.setFirstName(dto.firstName());
        if (Objects.nonNull(dto.middleName()))   user.setMiddleName(dto.middleName());
        if (Objects.nonNull(dto.birthdate()))    user.setBirthdate(dto.birthdate());

        userRepository.save(user);
    }

    @Override
    public void delete(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        codeRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}

