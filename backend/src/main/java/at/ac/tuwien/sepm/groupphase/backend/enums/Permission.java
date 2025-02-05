package at.ac.tuwien.sepm.groupphase.backend.enums;

/**
 * Permissions, that are granted by specific roles.
 *
 * @author Tobias Gruber
 */
public interface Permission {
    String user = "ROLE_USER";
    String verified = "ROLE_VERIFIED";
    String admin = "ROLE_ADMIN";
}