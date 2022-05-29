package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id}")
    List<Session> findAllByUser(@Param("user") User user);

    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.deprecated = :#{#deprecated}")
    List<Session> findByDeprecatedAndUser(@Param("deprecated") Boolean deprecated,
                                          @Param("user") User user);

    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.section.id = ?1 and s.deprecated = ?2")
    List<Session> findBySectionAndDeprecatedAndUser(@Param("section") Long sectionId,
                                                    @Param("deprecated") Boolean deprecated,
                                                    @Param("user") User user);

    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.deprecated = :#{#deprecated} and s.end is not null")
    List<Session> findByDeprecatedAndUserAndPast(@Param("deprecated") Boolean deprecated,
                                                 @Param("user") User user);

    @Query("select s "
        + "from Session s join Section c on s.section.id = c.id join User u on c.owner.id = u.id "
        + "where u.id = :#{#user.id} and s.section.id = ?1 and s.deprecated = ?2 and s.end is not null ")
    List<Session> findBySectionAndDeprecatedAndUserAndPast(@Param("section") Long sectionId,
                                                    @Param("deprecated") Boolean deprecated,
                                                    @Param("user") User user);
}
