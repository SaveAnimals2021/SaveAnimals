package org.sa.animal.service;

import org.sa.animal.dto.MissingAnimalDTO;
import org.sa.animal.mapper.AnimalInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService{

    @Autowired
    AnimalInfoMapper mapper;

    @Override
    public void register(MissingAnimalDTO dto) {
        try {
            mapper.register(toDomain(dto));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setIsAdopted(MissingAnimalDTO dto) {
        try {
            mapper.setIsAdopted(toDomain(dto));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<MissingAnimalDTO> getAllList() {
        return toDTOList(mapper.getAllList());
    }
}
