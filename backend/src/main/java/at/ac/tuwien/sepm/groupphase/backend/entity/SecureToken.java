package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Entity class for verification tokens.
 *
 * @author Nikolaus Peter
 */
@Entity
@Table(name = "secureTokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecureToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private TokenType type;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;

    @Column(updatable = false, nullable = false)
    private LocalDateTime expireAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account")
    private User account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "script")
    private Script script;
}
