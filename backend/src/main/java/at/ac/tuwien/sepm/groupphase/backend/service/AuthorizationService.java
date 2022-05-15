package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;

public interface AuthorizationService {
    boolean isLoggedInAs(Long id);

    boolean isLoggedInAs(String email);

    User getLoggedInUser();

    void checkAuthorization(String email);

    void checkAuthorization(Long id);
}
