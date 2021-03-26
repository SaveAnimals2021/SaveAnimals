package org.sa.animal.mapper;

import org.sa.animal.domain.AnimalInfoVO;

import java.util.List;

public interface AnimalInfoMapper {

    void register(AnimalInfoVO vo);
    List<AnimalInfoVO> getAllList();
}
