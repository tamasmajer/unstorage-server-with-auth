package com.github.tamasmajer.unstorage.server.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DataRepository extends JpaRepository<Data, Long> {

    Optional<Data> findByUserAndKey(String user, String key);

    List<Data> findByUser(String user);

    @Modifying
    @Query("delete from Data where user=:user and data_key=:data_key")
    Integer deleteByUserAndKey(@Param("user") String user, @Param("data_key") String key);

}
