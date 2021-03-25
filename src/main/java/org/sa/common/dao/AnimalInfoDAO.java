package org.sa.common.dao;

import org.sa.animal.dto.AnimalInfoDTO;

import java.util.ArrayList;
import java.util.List;

public class AnimalInfoDAO {
    private List<AnimalInfoDTO> animalInfoDTOList;

    public AnimalInfoDAO(){
        animalInfoDTOList = new ArrayList<>();
    }

    public void addAnimal(AnimalInfoDTO info){
        animalInfoDTOList.add(info);
    }

    public List<AnimalInfoDTO> getList(){
        return animalInfoDTOList;
    }
}
