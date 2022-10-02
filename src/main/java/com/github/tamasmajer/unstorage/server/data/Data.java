package com.github.tamasmajer.unstorage.server.data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "data", indexes = {
        @Index(name = "data_by_user_and_data_key", columnList = "user, data_key", unique = true)
})
public class Data {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "user", unique = false, nullable = false)
    private String user;

    @Column(name = "data_key", nullable = false)
    private String key;

    @Lob
    @Column(name = "data_value", nullable = false)
    private String value;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    public Data() {
        super();
    }

    public Data(String user, String key, String value) {
        super();
        this.user = user;
        this.key = key;
        this.value = value;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
