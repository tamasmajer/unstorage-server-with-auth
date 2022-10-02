package com.github.tamasmajer.unstorage.server.data;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    public DataServiceImpl(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
    }

    @Override
    public Data save(Data data) {
        return dataRepository.save(data);
    }

    @Override
    public List<Data> findAll() {
        return dataRepository.findAll();
    }

    @Override
    public Optional<Data> findByUserAndKey(String user, String key) {
        return dataRepository.findByUserAndKey(user, key);
    }

    @Override
    public List<Data> findByUser(String user) {
        return dataRepository.findByUser(user);
    }

    @Transactional
    @Override
    public Integer deleteByUserAndKey(String user, String key) {
        return dataRepository.deleteByUserAndKey(user, key);
    }
}
