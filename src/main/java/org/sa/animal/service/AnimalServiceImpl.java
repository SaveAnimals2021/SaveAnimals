package org.sa.animal.service;

import org.sa.animal.dto.AnimalInfoDTO;
import org.sa.animal.mapper.AnimalInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Override
    public void setIsAdopted(AnimalInfoDTO dto) {
        try {
            mapper.setIsAdopted(toDomain(dto));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<AnimalInfoDTO> getAllList() {
        return toDTOList(mapper.getAllList());
    }
}
