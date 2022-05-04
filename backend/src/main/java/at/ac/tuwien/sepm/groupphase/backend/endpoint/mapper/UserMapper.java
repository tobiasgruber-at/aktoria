package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class UserMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public UserMapper() {
    }

    public UserRegistrationDto entityToUserGenerationDto(ApplicationUser user) {
        LOGGER.trace("transform user entity into UserRegistrationDto");
        if (user == null) return null;
        return new UserRegistrationDto(user.getName(), user.getEmail(), user.getPassword());
    }
}
