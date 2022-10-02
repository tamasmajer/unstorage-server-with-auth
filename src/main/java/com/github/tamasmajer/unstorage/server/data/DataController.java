package com.github.tamasmajer.unstorage.server.data;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/data")
public class DataController {
    private final DataService dataService;

    public DataController(DataService dataService) {
        super();
        this.dataService = dataService;
    }

    // FOR DEBUG ONLY
    @GetMapping
    public List<String> getKeys(Principal principal) {
        String user = principal.getName();
        List<Data> list = dataService.findByUser(user);
        List<String> keys = list.stream().map(data -> data.getKey()).toList();
        return keys;
    }

    @GetMapping(value = "/{*path}")
    public Optional<String> getItem(@PathVariable(name = "path") String path, HttpServletRequest request, Principal principal) {
        String user = principal.getName();
        String key = path.substring(1).replace('/', ':');
        Optional<Data> data = dataService.findByUserAndKey(user, key);
        Optional<String> value = data.map(Data::getValue);
        return value;
    }

    @RequestMapping(value = "/{*path}", method = RequestMethod.HEAD)
    public String hasItem(@PathVariable(name = "path") String path, HttpServletResponse response, Principal principal) {
        String user = principal.getName();
        String key = path.substring(1).replace('/', ':');
        Optional<Data> data = dataService.findByUserAndKey(user, key);
        if (!data.isPresent()) response.setStatus(404);
        else response.setHeader("Last-Modified", data.get().getTimestamp().toInstant().toString());
        return "";
    }

    @PutMapping("/{*path}")
    public String setItem(@PathVariable(name = "path") String path, HttpEntity<String> httpEntity, Principal principal) {
        String user = principal.getName();
        String key = path.substring(1).replace('/', ':');
        String json = httpEntity.getBody();
        dataService.deleteByUserAndKey(user, key);
        dataService.save(new Data(user, key, json));
        return "OK";
    }

    @DeleteMapping("/{*path}")
    public String removeItem(@PathVariable(name = "path") String path, Principal principal) {
        String user = principal.getName();
        String key = path.substring(1).replace('/', ':');
        Integer count = dataService.deleteByUserAndKey(user, key);
        return "OK";
    }

}
