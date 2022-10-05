package com.github.tamasmajer.unstorage.server.auth;

import java.util.List;
import java.util.Optional;

public interface AuthService {

    Auth save(Auth auth);

    void delete(Auth auth);

    Integer deleteByUser(String user);
    Integer deleteByToken(String token);

    List<Auth> findAll();

    Optional<Auth> findByToken(String token);

    Optional<Auth> findByUser(String user);
}
