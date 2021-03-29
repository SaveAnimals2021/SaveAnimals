package org.sa.animal.service;


import org.sa.animal.domain.AnimalInfoVO;
import org.sa.animal.dto.AnimalInfoDTO;
import org.sa.common.util.DateFormatter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public interface AnimalService {

    void register(AnimalInfoDTO dto);

    void setIsAdopted(AnimalInfoDTO dto);

    List<AnimalInfoDTO> getAllList();

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

        return AnimalInfoVO.builder()
                .animalNumber(dto.getAnimalNumber()).animalCode(dto.getAnimalCode()).type(dto.getType()).serviceName(dto.getServiceName())
                .name(dto.getName()).species(dto.getSpecies()).sex(dto.getSex()).age(dto.getAge()).weight(dto.getWeight())
                .special(dto.getSpecial()).color(dto.getColor()).date(date).regdate(dto.getRegdate()).updatedate(dto.getUpdatedate())
                .isNeutralized(isNeu).originURL(dto.getOriginURL()).isAdopted(dto.getIsAdopted())
                .build();
    }

    default AnimalInfoDTO toDTO(AnimalInfoVO vo) {
        AnimalInfoDTO animalDTO = new AnimalInfoDTO();

        animalDTO.setAnimalNumber(vo.getAnimalNumber());
        animalDTO.setAnimalCode(vo.getAnimalCode());
        animalDTO.setType(vo.getType());
        animalDTO.setServiceName(vo.getServiceName());
        animalDTO.setName(vo.getName());
        animalDTO.setSpecies(vo.getSpecies());
        animalDTO.setSex(vo.getSex());
        animalDTO.setAge(vo.getAge());
        animalDTO.setWeight(vo.getWeight());
        animalDTO.setSpecial(vo.getSpecial());
        animalDTO.setColor(vo.getColor());
        animalDTO.setDate(DateFormatter.fromDateToString(vo.getDate()));
        animalDTO.setRegdate(vo.getRegdate());
        animalDTO.setUpdatedate(vo.getUpdatedate());
        animalDTO.setIsNeutralized(vo.getIsNeutralized() + "");
        animalDTO.setIsVaccinated(vo.getIsVaccinated() + "");
        animalDTO.setIsAdopted(vo.getIsAdopted());
        animalDTO.setOriginURL(vo.getOriginURL());



        return animalDTO;
    }

    default List<AnimalInfoDTO> toDTOList(List<AnimalInfoVO> animalList) {
        return animalList.stream().map(a -> {
            return toDTO(a);
        }).collect(Collectors.toList());
    }
}
