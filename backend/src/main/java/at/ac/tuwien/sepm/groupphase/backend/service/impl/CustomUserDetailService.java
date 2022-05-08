package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserNotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;


@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserRegistrationDto createUser(UserRegistrationDto userRegistrationDto) throws ServiceException, ValidationException {
        return null;
    }

    @Override
    public SimpleUserDto getUser(double id) throws ServiceException {
        return null;
    }

    @Override
    public DetailedUserDto changeUserData(SimpleUserDto simpleUserDto, Long id) throws ServiceException {
        return null;
    }

    @Override
    public void deleteUser(Long id) throws ServiceException {
    }

    @Override
    public void forgotPassword(String email) throws UserNotFoundException {

    }

    @Override
    public DetailedUserDto changePassword(PasswordChangeDto passwordChangeDto, Long id) throws ServiceException {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }

    /*@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            User user = findUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (user.getAdmin()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }*/

    @Override
    public User findUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        return null;
        /*User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return user;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));*/
    }


}
