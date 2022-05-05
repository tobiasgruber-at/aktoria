package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.UserNotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Service for user.
 *
 * @author luke nemeskeri
 */
public interface UserService extends UserDetailsService {

    //TODO: email ändern, abort button?

    /**
     * Creates a new User.
     *
     * @param userRegistrationDto filled with the user input (Name, email, password)
     * @return the created User
     * @throws ServiceException    is thrown when user could not be created.
     * @throws ValidationException is thrown when user data is not valid
     */
    UserRegistrationDto createUser(UserRegistrationDto userRegistrationDto) throws ServiceException, ValidationException;

    /**
     * Changes the password/username of a user.
     *
     * @param detailedUserDto filled with the new password/username
     * @return the updated user
     * @throws ServiceException is thrown when the user data could not be updated
     */
    DetailedUserDto changeUserData(DetailedUserDto detailedUserDto) throws ServiceException;

    /**
     * Deletes a user from the system.
     *
     * @param simpleUserDto filled with the user-data, which shall be deleted
     * @throws ServiceException is thrown when the user could not be deleted
     */
    void deleteUser(SimpleUserDto simpleUserDto) throws ServiceException;

    /**
     * Sends an email to the user to set a new password.
     *
     * @param email the email of the user
     * @throws UserNotFoundException is thrown if the user does not exist
     */
    void forgotPassword(String email) throws UserNotFoundException;

    /**
     * Changes the email of a user.
     *
     * @param simpleUserDto filled with the user data, including the new email
     * @return the updated user with the new email
     * @throws ServiceException is thrown if the email could not be changed
     */
    SimpleUserDto changeEmail(SimpleUserDto simpleUserDto) throws ServiceException;

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
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);
}
