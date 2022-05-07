package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

//TODO: replace this class with a correct User JPARepository implementation
@Repository
public class UserRepository {

    private final User user;
    private final User admin;

    @Autowired
    public UserRepository(PasswordEncoder passwordEncoder) {
        user = new User();
        admin = new User();
    }

    public User findUserByEmail(String email) {
        if (email.equals(user.getEmail())) {
            return user;
        }
        if (email.equals(admin.getEmail())) {
            return admin;
        }
        return null; // In this case null is returned to fake Repository behavior
    }


}
