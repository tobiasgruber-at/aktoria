package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidation userValidation, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public SimpleUserDto createUser(UserRegistrationDto userRegistrationDto) throws ServiceException, ValidationException, ConflictException {
        log.info("Post new user");
        try {
            userValidation.validateCreateUserInput(userRegistrationDto);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto, false, passwordEncoder.encode(userRegistrationDto.getPassword()));
        User savedUser = userRepository.saveAndFlush(user);
        return userMapper.userToSimpleUserDto(savedUser);
    }

    @Override
    public SimpleUserDto getUser(double id) {
        log.info("get user by id");
        Optional<User> userOptional = userRepository.findById((long) id);
        if (userOptional.isPresent()) {
            return userMapper.userToSimpleUserDto(userOptional.get());
        } else {
            throw new NotFoundException("Could not find User with this id");
        }
    }

    @Override
    public DetailedUserDto patch(UpdateUserDto updateUserDto, Boolean passwordChange, Long id) throws ServiceException, ValidationException, ConflictException {
        log.info("patch user");
        try {
            userValidation.validatePatchUser(updateUserDto);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) throws ServiceException {
        log.info("delete user with id = /{}", id);
        userRepository.deleteById(id);
    }

    @Override
    public void forgotPassword(String email) {
        log.info("forgot password, sending email");

    }

    @Override
    public DetailedUserDto changePassword(PasswordChangeDto passwordChangeDto, Long id) throws ServiceException, ValidationException {
        log.info("change password of user with id = /{}", id);
        try {
            userValidation.validateChangePassword(passwordChangeDto);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Load all user by email");
        try {
            User user = findUserByEmail(email);

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
    public User findUserByEmail(String email) {
        log.debug("Find application user by email");
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("Could not find User with this id");
        }
    }


}
