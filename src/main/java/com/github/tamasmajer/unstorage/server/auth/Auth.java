package com.github.tamasmajer.unstorage.server.auth;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "auth", indexes = {
        @Index(name = "auth_by_token", columnList = "token", unique = true),
        @Index(name = "auth_by_user", columnList = "user", unique = false)
})
public class Auth {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "user", unique = false, nullable = false)
    private String user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    public Auth() {
        super();
    }


    public Auth(String token, String user) {
        super();
        this.token = token;
        this.user = user;
    }

    @PrePersist
    @PreUpdate
    void onUpdate() {
        timestamp = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", user='" + user + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
