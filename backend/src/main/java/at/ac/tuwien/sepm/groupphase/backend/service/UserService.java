package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserNotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Service for user.
 *
 * @author Luke Nemeskeri
 */
public interface UserService extends UserDetailsService {


    /**
     * Creates a new User.
     *
     * @param userRegistrationDto filled with the user input (Name, email, password)
     * @return the created User
     * @throws ServiceException    is thrown when user could not be created.
     * @throws ValidationException is thrown when user data is not valid
     * @throws ConflictException   is thrown when there is a conflict with the data base
     */
    UserRegistrationDto createUser(UserRegistrationDto userRegistrationDto) throws ServiceException, ValidationException, ConflictException;

    /**
     * Returns a user.
     *
     * @param id the id of a user
     * @return the specified user
     * @throws ServiceException is thrown if something went wrong with getting the user
     */
    SimpleUserDto getUser(double id) throws ServiceException;

    /**
     * Changes the email/username of a user.
     *
     * @param simpleUserDto filled with the new email/username
     * @param id            the id of the user to be changed
     * @return the updated user
     * @throws ServiceException is thrown when the user data could not be updated
     */
    DetailedUserDto changeUserData(SimpleUserDto simpleUserDto, Long id) throws ServiceException;

    /**
     * Deletes a user from the system.
     *
     * @param id the id of the user to be deleted
     * @throws ServiceException is thrown when the user could not be deleted
     */
    void deleteUser(Long id) throws ServiceException;

    /**
     * Sends an email to the user to set a new password.
     *
     * @param email the email of the user
     * @throws UserNotFoundException is thrown if the user does not exist
     */
    void forgotPassword(String email) throws UserNotFoundException;

    /**
     * Changes the password of a user.
     *
     * @param passwordChangeDto filled with the old and new password
     * @param id                the id of the user
     * @return the user with the new password
     * @throws ServiceException is thrown if the password could not be changed
     */
    DetailedUserDto changePassword(PasswordChangeDto passwordChangeDto, Long id) throws ServiceException;

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return an application user
     */
    User findUserByEmail(String email);
}
