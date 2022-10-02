package com.github.tamasmajer.unstorage.server.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;

    public AuthServiceImpl(AuthRepository authRepository) {
        super();
        this.authRepository = authRepository;
    }

    @Override
    public Auth save(Auth auth) {
        return authRepository.save(auth);
    }

    @Override
    public void delete(Auth auth) {
        authRepository.delete(auth);
    }

    @Transactional
    @Override
    public Integer deleteByToken(String token) {
        return authRepository.deleteByToken(token);
    }

    @Override
    public List<Auth> findAll() {
        return authRepository.findAll();
    }

    @Override
    public Optional<Auth> findByToken(String token) {
        return authRepository.findByToken(token);
    }

    @Override
    public List<Auth> findByUser(String user) {
        return authRepository.findByUser(user);
    }
}
