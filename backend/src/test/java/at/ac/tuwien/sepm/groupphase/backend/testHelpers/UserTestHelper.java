package at.ac.tuwien.sepm.groupphase.backend.testHelpers;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserTestHelper {
    public static final String dummyUserEmail = "test1@test.com";
    public static final String dummyUserPassword = "password1";

    public SimpleUserDto dummyUserDto() {
        return SimpleUserDto.builder()
            .id(2L)
            .firstName("firstName1")
            .lastName("lastName1")
            .email(dummyUserEmail)
            .verified(false)
            .build();
    }
}
