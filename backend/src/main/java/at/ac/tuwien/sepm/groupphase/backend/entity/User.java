package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 60)
    private String passwordHash;

    @Column(name = "verified", nullable = false, columnDefinition = "boolean default false")
    private Boolean verified;

    @Column(name = "created", nullable = false)
    private LocalDate created;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Script> scripts;

    @OneToMany(mappedBy = "recordedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Line> linesRecorded;

    @ManyToMany
    @JoinTable(name = "participates_in", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "script_id"))
    private Set<Script> participatesIn;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String passwordHash, Boolean verified, LocalDate created, Set<Script> scripts, Set<Line> linesRecorded,
                Set<Script> participatesIn) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.verified = verified;
        this.created = created;
        this.scripts = scripts;
        this.linesRecorded = linesRecorded;
        this.participatesIn = participatesIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Set<Script> getScripts() {
        return scripts;
    }

    public void setScripts(Set<Script> scripts) {
        this.scripts = scripts;
    }

    public Set<Line> getLinesRecorded() {
        return linesRecorded;
    }

    public void setLinesRecorded(Set<Line> linesRecorded) {
        this.linesRecorded = linesRecorded;
    }

    public Set<Script> getParticipatesIn() {
        return participatesIn;
    }

    public void setParticipatesIn(Set<Script> participatesIn) {
        this.participatesIn = participatesIn;
    }
}
