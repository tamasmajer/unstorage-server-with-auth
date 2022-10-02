package com.github.tamasmajer.unstorage.server.auth;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        super();
        this.authService = authService;
    }

    // FOR DEBUG ONLY
    @GetMapping
    public List<String> getKeys() {
        List<Auth> auths = authService.findAll();
        List<String> tokens = auths.stream().map(auth -> auth.getToken()).toList();
        return tokens;
    }

    @GetMapping(value = "/{*path}")
    public Optional<String> getItem(@PathVariable(name = "path") String path, HttpServletRequest request) {
        String token = path.substring(1).replace('/', ':');
        Optional<Auth> auth = authService.findByToken(token);
        Optional<String> user = auth.map(Auth::getUser);
        return user;
    }


    @RequestMapping(value = "/{*path}", method = RequestMethod.HEAD)
    public String hasItem(@PathVariable(name = "path") String path, HttpServletResponse response) {
        String token = path.substring(1).replace('/', ':');
        Optional<Auth> auth = authService.findByToken(token);
        if (!auth.isPresent()) response.setStatus(404);
        else response.setHeader("Last-Modified", auth.get().getTimestamp().toInstant().toString());
        return "";
    }

    @PutMapping("/{*path}")
    public String setItem(@PathVariable(name = "path") String path, HttpEntity<String> httpEntity) {
        String token = path.substring(1).replace('/', ':');
        String json = httpEntity.getBody();
        authService.deleteByToken(token);
        authService.save(new Auth(token, json));
        return "OK";
    }

    @DeleteMapping("/{*path}")
    public String removeItem(@PathVariable(name = "path") String path) {
        String token = path.substring(1).replace('/', ':');
        Integer count = authService.deleteByToken(token);
        return "OK";
    }

}

