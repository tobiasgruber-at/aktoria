package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidTokenException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.MailSender;
import at.ac.tuwien.sepm.groupphase.backend.service.SecureTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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
    private final SecurityProperties securityProperties;

    @Autowired
    public UserServiceImpl(
        UserRepository userRepository,
        UserValidation userValidation,
        PasswordEncoder passwordEncoder,
        UserMapper userMapper,
        MailSender mailSender,
        SecureTokenService secureTokenService,
        SecurityProperties securityProperties
    ) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.mailSender = mailSender;
        this.secureTokenService = secureTokenService;
        this.securityProperties = securityProperties;
    }

    @Override
    public SimpleUserDto create(UserRegistrationDto userRegistrationDto) throws ServiceException, ValidationException, ConflictException {
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
    public SimpleUserDto findById(Long id) throws NotFoundException {
        log.trace("getUser(id = {})", id);

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userMapper.userToSimpleUserDto(userOptional.get());
        } else {
            throw new NotFoundException("Could not find User with this id");
        }
    }

    @Override
    public DetailedUserDto patch(UpdateUserDto updateUserDto, Boolean passwordChange, Long id) throws ServiceException, ValidationException, ConflictException, NotFoundException {
        log.trace("patch(updateUserDto = {}, passwordChange = {}, id = {})", updateUserDto, passwordChange, id);

        try {
            userValidation.validatePatchUserInput(updateUserDto);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(Long id) throws ServiceException, NotFoundException {
        log.trace("deleteUser(id = {})", id);

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public void forgotPassword(String email) throws NotFoundException {
        log.trace("forgotPassword(email = {})", email);

        throw new UnsupportedOperationException();
    }

    @Override
    public DetailedUserDto changePassword(PasswordChangeDto passwordChangeDto, Long id) throws ServiceException, ValidationException, NotFoundException {
        log.trace("changePassword(passwordChangeDto = {}, id = {})", passwordChangeDto, id);

        try {
            userValidation.validateChangePasswordInput(passwordChangeDto);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.trace("loadUserByUsername(email = {})", email);

        try {
            User user = findByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (user.getVerified()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_VERIFIED", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        log.trace("findUserByEmail(email = {})", email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("Es konnte kein Benutzer gefunden werden.");
        }
    }

    @Override
    public void sendEmailVerificationLink(User user) throws ServiceException {
        log.trace("sendEmailVerificationLink(user = {})", user);

        SecureToken secureToken = secureTokenService.createSecureToken(TokenType.verifyEmail);
        secureToken.setAccount(user);
        secureTokenService.saveSecureToken(secureToken);

        final String link = String.join("", "http://localhost:4200/#/verifyEmail/", secureToken.getToken());
        try {
            mailSender.sendMail(user.getEmail(), "Aktoria Verifikationslink",
                """
                        <h1>Hallo %s,</h1>
                        klick auf den folgenden Link um deine Mailadresse zu bestätigen.<br>
                        <a href='%s'>Email Adresse bestätigen</a>
                        <br>
                        <br>
                        Wenn du dich nicht bei Aktoria registriert haben solltest, ignoriere bitte diese Mail.
                    """
                    .formatted(user.getFirstName(), link));
        } catch (MessagingException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void resendEmailVerificationLink() throws ServiceException, NotFoundException {
        log.trace("resendEmailVerificationLink()");

        String email = getCurrentUserEmail();

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            sendEmailVerificationLink(userOptional.get());
        } else {
            throw new NotFoundException("Es konnte kein Benutzer gefunden werden.");
        }
    }

    @Override
    public void verifyEmail(String token) throws InvalidTokenException {
        log.trace("verifyEmail(token = {})", token);

        SecureToken secureToken = secureTokenService.findByToken(token);
        secureTokenService.removeToken(token);
        if (secureToken.getType() == TokenType.verifyEmail) {
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

    /**
     * Get the email of the logged in user, returns null if user is not logged in.
     *
     * @return the email address of the current user
     */
    public String getCurrentUserEmail() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String token = request.getHeader(securityProperties.getAuthHeader());
            return (new String(Base64.decodeBase64(token.split("\\.")[1]))).split("sub\":\"")[1].split("\"")[0];
        } else {
            return null;
        }
    }
}
