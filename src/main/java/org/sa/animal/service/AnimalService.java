package org.sa.animal.service;


import org.sa.animal.domain.AnimalInfoVO;
import org.sa.animal.dto.AnimalInfoDTO;
import org.sa.common.util.DateFormatter;

import java.util.Date;


public interface AnimalService {

    void register(AnimalInfoDTO dto);

    default AnimalInfoVO toDomain(AnimalInfoDTO dto) throws Exception{
        Date date = DateFormatter.fromStringToDate(dto.getDate());

        // ========== 중성화 여부
        String instr = dto.getIsNeutralized();
        Integer isNeu = 0;

        // 0=안했음, 1=했음, 2= 확인불가
        if(null == instr || instr.contains("x") || instr.contains("안했음") || instr.contains("X")){
            isNeu = 0;
        }else if(instr.contains("O") || instr.contains("했음") || instr.contains("o") || instr.contains("ㅇ")){
            isNeu = 1;
        } else{
            isNeu = 2;
        }

        // ========== 백신 여부
        
        return AnimalInfoVO.builder()
                .animalNumber(dto.getAnimalNumber()).animalCode(dto.getAnimalCode()).type(dto.getType()).serviceName(dto.getServiceName())
                .name(dto.getName()).species(dto.getSpecies()).sex(dto.getSex()).age(dto.getAge()).weight(dto.getWeight())
                .special(dto.getSpecial()).color(dto.getColor()).date(date).regdate(dto.getRegdate()).updatedate(dto.getUpdatedate())
                .isNeutralized(isNeu)
                .build();
    }
//
//    default AnimalInfoDTO toDTO(AnimalInfoVO vo) {
//        AnimalInfoDTO dto = new AnimalInfoDTO();
//        dto.setBno(vo.getBno());
//        dto.setTitle(vo.getTitle());
//        dto.setContent(vo.getContent());
//        dto.setWriter(vo.getWriter());
//        dto.setReplyCount(vo.getReplyCount());
//
//        dto.setRegdate(dto.getRegdate());
//        dto.setUpdatedate(dto.getUpdatedate());
//
//        return dto;
//    }
}
