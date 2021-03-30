package org.sa.animal.mapper;

import org.sa.animal.domain.MissingAnimalVO;

import java.util.List;

public interface AnimalInfoMapper {

    void register(MissingAnimalVO vo);

    void setIsAdopted(MissingAnimalVO vo);

    List<MissingAnimalVO> getAllList();
}
