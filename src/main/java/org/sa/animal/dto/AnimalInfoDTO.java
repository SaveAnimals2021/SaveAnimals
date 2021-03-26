package org.sa.animal.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalInfoDTO {
    private Integer animalNumber;

    private String animalCode;
    private String serviceName;

    private String type; // 개
    private String name; // 개

    @Builder.Default
    private String species = "모름"; // 진도
    private String sex; // 암컷
    private String isVaccinated;
    private String isNeutralized; // 중성화 X
    private String age; // 2살
    private String weight; // 20.6kg
    private String special;
    private String color;

    @Builder.Default
    private List<String> imageUrlList = new ArrayList<>();
    private String imageType;

    // redirect할수 있는 원래 사이트
    private String originURL;
    // 상태
    private Boolean isAdopted; // 입양된 동물들은 Archive 게시판에 저장

    private String date;
    private Date regdate;
    private Date updatedate;

}
