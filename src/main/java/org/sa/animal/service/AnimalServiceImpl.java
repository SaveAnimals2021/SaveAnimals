package org.sa.animal.service;

import org.sa.animal.dto.AnimalInfoDTO;
import org.sa.animal.mapper.AnimalInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AnimalServiceImpl implements AnimalService{

    @Autowired
    AnimalInfoMapper mapper;

    @Override
    public void register(AnimalInfoDTO dto) {
        try {
            mapper.register(toDomain(dto));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}