package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Persistence repository for session entities.
 *
 * @author Marvin Flandorfer
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Finds all sessions for a user.
     *
     * @param user User that the sessions belong to
     * @return List of sessions
     */
    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id}")
    List<Session> findAllByUser(@Param("user") User user);

    /**
     * Finds sessions for a user either deprecated or not
     *
     * @param deprecated Boolean showing if deprecated sessions should be returned or not
     * @param user User that the sessions belong to
     * @return List of sessions
     */
    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.deprecated = :#{#deprecated}")
    List<Session> findByDeprecatedAndUser(@Param("deprecated") Boolean deprecated,
                                          @Param("user") User user);

    /**
     * Finds sessions for a user by section and if they are deprecated or not.
     *
     * @param sectionId Id of the section
     * @param deprecated Boolean showing if deprecated sessions should be returned or not
     * @param user User that the sessions belong to
     * @return List of sessions
     */
    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.section.id = ?1 and s.deprecated = ?2")
    List<Session> findBySectionAndDeprecatedAndUser(@Param("section") Long sectionId,
                                                    @Param("deprecated") Boolean deprecated,
                                                    @Param("user") User user);

    /**
     * Find already finished sessions for a user by if they are deprecated.
     *
     * @param deprecated Boolean showing if deprecated sessions should be returned or not
     * @param user User that the sessions belong to
     * @return List of sessions
     */
    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.deprecated = :#{#deprecated} and s.end is not null")
    List<Session> findByDeprecatedAndUserAndPast(@Param("deprecated") Boolean deprecated,
                                                 @Param("user") User user);

    /**
     * Finds already finished sessions for a user by section and if they are deprecated or not.
     *
     * @param sectionId Id of the section
     * @param deprecated Boolean showing if deprecated sessions should be returned or not
     * @param user User that the sessions belong to
     * @return List of sessions
     */
    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.section.id = ?1 and s.deprecated = ?2 and s.end is not null ")
    List<Session> findBySectionAndDeprecatedAndUserAndPast(@Param("section") Long sectionId,
                                                           @Param("deprecated") Boolean deprecated,
                                                           @Param("user") User user);
}
