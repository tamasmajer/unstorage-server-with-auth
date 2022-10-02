package com.github.tamasmajer.unstorage.server.data;

import java.util.List;
import java.util.Optional;

public interface DataService {
    Data save(Data data);

    List<Data> findAll();

    Optional<Data> findByUserAndKey(String user, String key);

    List<Data> findByUser(String user);

    Integer deleteByUserAndKey(String user, String key);
}
