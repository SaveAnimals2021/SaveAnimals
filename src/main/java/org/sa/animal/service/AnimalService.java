package org.sa.animal.service;


import org.sa.animal.domain.MissingAnimalVO;
import org.sa.animal.dto.MissingAnimalDTO;
import org.sa.common.util.DateFormatter;
import org.sa.common.util.SimpleDateFormatter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public interface AnimalService {

    void register(MissingAnimalDTO dto);

    void setIsAdopted(MissingAnimalDTO dto);

    List<MissingAnimalDTO> getAllList();

    default MissingAnimalVO toDomain(MissingAnimalDTO dto) throws Exception{
        Date missingDate = SimpleDateFormatter.fromStringToDate(dto.getMissingDate());
        Date rescueDate = SimpleDateFormatter.fromStringToDate(dto.getRescueDate());
        Date regDate = SimpleDateFormatter.fromStringToDate(dto.getRegDate());
        Date updateDate = SimpleDateFormatter.fromStringToDate(dto.getUpdateDate());



        return MissingAnimalVO.builder()
                .animalNumber(dto.getAnimalNumber()).animalCode(dto.getAnimalCode()).type(dto.getType()).serviceName(dto.getServiceName())
                .name(dto.getName()).species(dto.getSpecies()).sex(dto.getSex()).age(dto.getAge()).situation(dto.getSituation())
                .special(dto.getSpecial()).color(dto.getColor()).missingDate(missingDate).regDate(regDate).updateDate(updateDate)
                .originURL(dto.getOriginURL()).missingLocation(dto.getMissingLocation()).rescueLocation(dto.getRescueLocation()).rescueDate(rescueDate)
                .rescueStatus(dto.getRescueStatus()).bno(dto.getBno()).guardianName(dto.getGuardianName()).phoneNumber(dto.getPhoneNumber())
                .build();
    }

    default MissingAnimalDTO toDTO(MissingAnimalVO vo) {
        MissingAnimalDTO animalDTO = new MissingAnimalDTO();

        animalDTO.setAnimalNumber(vo.getAnimalNumber());
        animalDTO.setAnimalCode(vo.getAnimalCode());
        animalDTO.setType(vo.getType());
        animalDTO.setServiceName(vo.getServiceName());
        animalDTO.setName(vo.getName());
        animalDTO.setSpecies(vo.getSpecies());
        animalDTO.setSex(vo.getSex());
        animalDTO.setAge(vo.getAge());
        animalDTO.setSpecial(vo.getSpecial());
        animalDTO.setColor(vo.getColor());

        animalDTO.setMissingDate(DateFormatter.fromDateToString(vo.getMissingDate()));
        animalDTO.setMissingLocation(vo.getMissingLocation());
        animalDTO.setRegDate(DateFormatter.fromDateToString(vo.getRegDate()));
        animalDTO.setUpdateDate(DateFormatter.fromDateToString(vo.getUpdateDate()));
        animalDTO.setOriginURL(vo.getOriginURL());

        animalDTO.setSituation(vo.getSituation());
        animalDTO.setRescueStatus(vo.getRescueStatus());
        animalDTO.setBno(vo.getBno());
        animalDTO.setGuardianName(vo.getGuardianName());
        animalDTO.setPhoneNumber(vo.getPhoneNumber());

        animalDTO.setRescueDate(DateFormatter.fromDateToString(vo.getRescueDate()));
        animalDTO.setRescueLocation(vo.getRescueLocation());

        return animalDTO;
    }

    default List<MissingAnimalDTO> toDTOList(List<MissingAnimalVO> animalList) {
        return animalList.stream().map(a -> {
            return toDTO(a);
        }).collect(Collectors.toList());
    }
}
