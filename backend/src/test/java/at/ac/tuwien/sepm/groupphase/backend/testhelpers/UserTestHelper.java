package at.ac.tuwien.sepm.groupphase.backend.testhelpers;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserTestHelper {
    public static final String dummyUserEmail = "test1@test.com";
    public static final String dummyUserPassword = "password1";

    public SimpleUserDto dummyUserDto() {
        return new SimpleUserDto(1L, "testFirst1", "testLast1", dummyUserEmail, false);
    }
}
