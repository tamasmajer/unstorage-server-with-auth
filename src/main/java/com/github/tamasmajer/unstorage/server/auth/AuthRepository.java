package com.github.tamasmajer.unstorage.server.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByToken(String token);

    List<Auth> findByUser(String user);

    @Modifying
    @Query("delete from Auth where token=:token")
    Integer deleteByToken(@Param("token") String token);

}
