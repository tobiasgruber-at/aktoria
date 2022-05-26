package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidTokenException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnprocessableEmailException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.MailSender;
import at.ac.tuwien.sepm.groupphase.backend.service.SecureTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for User.
 *
 * @author Luke Nemeskeri
 * @author Nikolaus Peter
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserValidation userValidation;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MailSender mailSender;
    private final SecureTokenService secureTokenService;
    private final AuthorizationService authorizationService;

    @Autowired
    public UserServiceImpl(
        UserRepository userRepository,
        UserValidation userValidation,
        PasswordEncoder passwordEncoder,
        UserMapper userMapper,
        MailSender mailSender,
        SecureTokenService secureTokenService,
        AuthorizationService authorizationService
    ) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.mailSender = mailSender;
        this.secureTokenService = secureTokenService;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public SimpleUserDto create(UserRegistrationDto userRegistrationDto) {
        log.trace("createUser(userRegistrationDto = {})", userRegistrationDto);

        try {
            userValidation.validateCreateUserInput(userRegistrationDto);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto, false, passwordEncoder.encode(userRegistrationDto.getPassword()));
        User savedUser = userRepository.saveAndFlush(user);
        sendEmailVerificationLink(savedUser);

        return userMapper.userToSimpleUserDto(savedUser);
    }

    @Override
    @Transactional
    public SimpleUserDto findById(Long id) {
        log.trace("getUser(id = {})", id);

        authorizationService.checkBasicAuthorization(id);

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userMapper.userToSimpleUserDto(userOptional.get());
        } else {
            throw new NotFoundException("User existiert nicht!");
        }
    }

    @Override
    @Transactional
    public SimpleUserDto patch(UpdateUserDto updateUserDto, Long id) {
        log.trace("patch(updateUserDto = {}, id = {})", updateUserDto, id);

        authorizationService.checkBasicAuthorization(id);

        try {
            userValidation.validatePatchUserInput(updateUserDto);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        Optional<User> userOptional = userRepository.findById(id);
        User update;
        if (userOptional.isPresent()) {
            update = userOptional.get();
        } else {
            throw new NotFoundException("User existiert nicht!");
        }

        if (updateUserDto.getOldPassword() != null && updateUserDto.getNewPassword() != null) {
            try {
                DetailedUserDto updated = changePassword(new PasswordChangeDto(null, updateUserDto.getOldPassword(), updateUserDto.getNewPassword()), id);
                update.setPasswordHash(updated.getPasswordHash());
            } catch (InvalidTokenException e) {
                throw new InvalidTokenException(e.getMessage(), e);
            }
        }

        if (updateUserDto.getFirstName() != null) {
            update.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            update.setLastName(updateUserDto.getLastName());
        }
        boolean emailChanged = false;
        if (updateUserDto.getEmail() != null) {
            update.setEmail(updateUserDto.getEmail());
            emailChanged = true;
            update.setVerified(false);
        }

        User patchedUser = userRepository.saveAndFlush(update);
        if (emailChanged) {
            sendEmailVerificationLink(patchedUser);
        }
        return userMapper.userToSimpleUserDto(patchedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("deleteUser(id = {})", id);

        authorizationService.checkBasicAuthorization(id);

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        log.trace("forgotPassword(email = {})", email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Es existiert kein User mit dieser Mail-Adresse.");
        }
        User user = userOptional.get();

        SecureToken secureToken = secureTokenService.createSecureToken(TokenType.RESET_PASSWORD);
        secureToken.setAccount(user);
        secureTokenService.saveSecureToken(secureToken);

        final String link = String.join("", "http://localhost:4200/#/password/restore/", secureToken.getToken());
        try {
            mailSender.sendMail(user.getEmail(), "Aktoria Passwort zurücksetzen",
                """
                        <h1>Hallo %s,</h1>
                        klick auf den folgenden Link, um ein neues Passwort zu wählen.
                        <br>
                        <a href='%s'>Passwort zurücksetzen</a>
                    """
                    .formatted(user.getFirstName(), link));
        } catch (UnprocessableEmailException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public DetailedUserDto changePassword(PasswordChangeDto passwordChangeDto, Long id) {
        if (id == null) {
            log.trace("changePassword(passwordChangeDto = {}, id = {})", passwordChangeDto, secureTokenService.findByToken(passwordChangeDto.getToken()).getAccount().getId());
        } else {
            log.trace("changePassword(passwordChangeDto = {}, id = {})", passwordChangeDto, id);
        }

        try {
            if (id != null) {
                userValidation.validateChangePasswordInput(passwordChangeDto, id);
            } else {
                userValidation.validateChangePasswordInput(passwordChangeDto, secureTokenService.findByToken(passwordChangeDto.getToken()).getId());
            }
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        String token = passwordChangeDto.getToken();

        if (token != null) {
            SecureToken secureToken = secureTokenService.findByToken(token);
            secureTokenService.removeToken(token);
            if (secureToken.getType() == TokenType.RESET_PASSWORD) {
                if (secureToken.getExpireAt().isAfter(LocalDateTime.now())) {
                    User user = secureToken.getAccount();
                    user.setPasswordHash(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
                    return userMapper.userToDetailedUserDto(user);
                } else {
                    throw new InvalidTokenException();
                }
            } else {
                throw new InvalidTokenException();
            }
        } else {
            if (id == null) {
                throw new NotFoundException("User existiert nicht!");
            }
            Optional<User> userOptional = userRepository.findById(id);
            User update;
            if (userOptional.isPresent()) {
                update = userOptional.get();
            } else {
                throw new NotFoundException("User existiert nicht!");
            }
            update.setPasswordHash(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
            return userMapper.userToDetailedUserDto(update);
        }
    }

    //Method is not used. But is used from spring,security ???
    @Override
    @Deprecated
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        log.trace("loadUserByUsername(email = {})", email);

        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new NotFoundException("Es konnte kein Benutzer gefunden werden.");
            }
            User user = userOptional.get();
            List<GrantedAuthority> grantedAuthorities;
            if (user.getVerified()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList(Permission.user, Permission.verified);
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList(Permission.user);
            }

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public SimpleUserDto findByEmail(String email) {
        log.trace("findUserByEmail(email = {})", email);

        authorizationService.checkBasicAuthorization(email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userMapper.userToSimpleUserDto(userOptional.get());
        } else {
            throw new NotFoundException("Es konnte kein Benutzer gefunden werden.");
        }
    }

    @Override
    @Transactional
    public void sendEmailVerificationLink(User user) {
        log.trace("sendEmailVerificationLink(user = {})", user);

        SecureToken secureToken = secureTokenService.createSecureToken(TokenType.VERIFY_EMAIL);
        secureToken.setAccount(user);
        secureTokenService.saveSecureToken(secureToken);

        final String link = String.join("", "http://localhost:4200/#/verifyEmail/", secureToken.getToken());
        try {
            mailSender.sendMail(user.getEmail(), "Aktoria Verifikationslink",
                """
                        <h1>Hallo %s,</h1>
                        klick auf den folgenden Link, um deine Mailadresse zu bestätigen.
                        <br>
                        <a href='%s'>Email Adresse bestätigen</a>
                        <br>
                        <br>
                        Wenn du dich nicht bei Aktoria registriert haben solltest, ignoriere bitte diese Mail.
                    """
                    .formatted(user.getFirstName(), link));
        } catch (UnprocessableEmailException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void resendEmailVerificationLink() {
        log.trace("resendEmailVerificationLink()");

        User user = authorizationService.getLoggedInUser();
        String email = user.getEmail();

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            sendEmailVerificationLink(userOptional.get());
        } else {
            throw new NotFoundException("Es konnte kein Benutzer gefunden werden.");
        }
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        log.trace("verifyEmail(token = {})", token);

        SecureToken secureToken = secureTokenService.findByToken(token);
        secureTokenService.removeToken(token);
        if (secureToken.getType() == TokenType.VERIFY_EMAIL) {
            if (secureToken.getExpireAt().isAfter(LocalDateTime.now())) {
                User user = secureToken.getAccount();
                user.setVerified(true);
                userRepository.saveAndFlush(user);
            } else {
                throw new InvalidTokenException();
            }
        } else {
            throw new InvalidTokenException();
        }
    }
}
