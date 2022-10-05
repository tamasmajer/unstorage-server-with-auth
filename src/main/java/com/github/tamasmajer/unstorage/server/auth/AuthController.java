package com.github.tamasmajer.unstorage.server.auth;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        List<String> tokens = auths.stream().map(Auth::getUser).toList();
        return tokens;
    }

    @GetMapping(value = "/{*path}")
    public Optional<String> getItem(@PathVariable(name = "path") String path) {
        String user = path.substring(1).replace('/', ':');
        Optional<Auth> auth = authService.findByUser(user);
        Optional<String> token = auth.map(Auth::getToken);
        return token;
    }


    @RequestMapping(value = "/{*path}", method = RequestMethod.HEAD)
    public String hasItem(@PathVariable(name = "path") String path, HttpServletResponse response) {
        String user = path.substring(1).replace('/', ':');
        Optional<Auth> auth = authService.findByUser(user);
        if (auth.isEmpty()) response.setStatus(404);
        else response.setHeader("Last-Modified", auth.get().getTimestamp().toInstant().toString());
        return "";
    }

    @PutMapping("/{*path}")
    public String setItem(@PathVariable(name = "path") String path, HttpEntity<String> httpEntity) {
        String user = path.substring(1).replace('/', ':');
        String token = httpEntity.getBody();
        authService.deleteByUser(user);
        authService.save(new Auth(user, token));
        return "OK";
    }

    @DeleteMapping("/{*path}")
    public String removeItem(@PathVariable(name = "path") String path) {
        String user = path.substring(1).replace('/', ':');
        authService.deleteByUser(user);
        return "OK";
    }

}

