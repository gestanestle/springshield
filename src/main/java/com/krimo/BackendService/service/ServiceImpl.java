package com.krimo.BackendService.service;

import com.krimo.BackendService.dto.CodeDTO;
import com.krimo.BackendService.dto.ProfileDTO;
import com.krimo.BackendService.dto.UserDTO;
import com.krimo.BackendService.dto.UserProfileDTO;
import com.krimo.BackendService.email.EmailSenderService;
import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.model.ActivationCode;
import com.krimo.BackendService.model.User;

import com.krimo.BackendService.model.UserProfile;
import com.krimo.BackendService.repository.ActivationCodeRepository;
import com.krimo.BackendService.repository.UserProfileRepository;
import com.krimo.BackendService.repository.UserRepository;
import com.krimo.BackendService.security.PasswordEncoder;
import com.krimo.BackendService.utils.Utils;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.krimo.BackendService.utils.Message.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceImpl implements UserService, AuthService, UserDetailsService {

    private final EmailSenderService emailSenderService;
    private final ActivationCodeRepository codeRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long signUp(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.email()).isPresent())
            throw new RequestException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_TAKEN.message);

        User user = User.create(userDTO.email(), encode(userDTO.password()));

        User entity = userRepository.save(user);

        String code = Utils.generateSerialCode();
        ActivationCode activationCode = ActivationCode.of(code, entity);
        codeRepository.save(activationCode);

        emailSenderService.sendMaiL(userDTO.email(), EMAIL_SUB.message, String.format(EMAIL_BODY.message, code));

        return entity.getId();
    }

    @Override
    public void activate(Long id, CodeDTO codeDTO) {
        User user = userRepository.findById(id).orElseThrow();
        ActivationCode activationCode = codeRepository.findByUserAndCode(user, codeDTO.code()).orElseThrow();

        if (LocalDateTime.now().isAfter(activationCode.getExpiresAt()))
            throw new RequestException(HttpStatus.BAD_REQUEST, "Token has expired.");

        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);
    }

    @Override
    public void sendActivation(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        ActivationCode activationCode = ActivationCode.of(Utils.generateSerialCode(), user);
        codeRepository.save(activationCode);

        emailSenderService.sendMaiL(user.getEmail(), EMAIL_SUB.message, EMAIL_BODY.message);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND.message, email)));
    }


    @Override
    public User getUser(String auth){
        return userRepository.findByEmail(auth).orElseThrow();
    }

    @Override
    public User authorize(String header) {

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());
            String email = Utils.getDecodedJWT(token).getSubject();

            return getUser(email);
        }

        throw new RequestException(HttpStatus.BAD_REQUEST, "Invalid Access Token");

    }

    @Override
    public UserProfileDTO displayUser(String auth) {
        User user = userRepository.findByEmail(auth).orElseThrow();
        UserProfile profile = profileRepository.findByUser(user).isPresent() ?
                profileRepository.findByUser(user).get() : new UserProfile();

        return new UserProfileDTO(
                new UserDTO(user.getEmail(), null),
                new ProfileDTO(
                    profile.getLastName(), profile.getFirstName(),
                    profile.getMiddleName(), profile.getBirthdate()
                )
        );
    }

    @Override
    public void updateUser(String auth, UserProfileDTO userProfileDTO) {
        User user = userRepository.findByEmail(auth).orElseThrow();
        UserProfile profile = profileRepository.findByUser(user).isPresent() ?
                profileRepository.findByUser(user).get() : new UserProfile();

        UserDTO userDTO = userProfileDTO.userDTO();
        ProfileDTO profileDTO = userProfileDTO.profileDTO();

        if (userDTO.password() != null) user.setPassword(encode(userDTO.password()));
        if (profileDTO.lastName() != null) profile.setLastName(profileDTO.lastName());
        if (profileDTO.firstName() != null) profile.setFirstName(profileDTO.firstName());
        if (profileDTO.middleName() != null) profile.setMiddleName(profileDTO.middleName());
        if (profileDTO.birthdate() != null) profile.setBirthdate(profileDTO.birthdate());

        userRepository.save(user);
        profile.setUser(user);
        profileRepository.save(profile);
    }

    @Override
    public void deleteUser(String auth) {
        User user = userRepository.findByEmail(auth).orElseThrow();
        codeRepository.deleteByUser(user);
        profileRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    private String encode(String pass) {
        return passwordEncoder.bCryptPasswordEncoder().encode(pass);
    }

}
