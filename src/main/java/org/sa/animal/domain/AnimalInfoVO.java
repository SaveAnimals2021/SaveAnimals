package org.sa.animal.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalInfoVO {
    private Integer animalNumber;

    private String animalCode;
    private String serviceName;

    private String type; // 개
    private String name; // 개

    private String species; // 진도
    private String sex; // 암컷
    private Integer isVaccinated;
    private Integer isNeutralized; // 중성화 X
    private String age; // 2살
    private String weight; // 20.6kg
    private String special;
    private String color;

    // redirect할수 있는 원래 사이트
    private String originURL;
    // 상태
    private Boolean isAdopted; // 입양된 동물들은 Archive 게시판에 저장 =>tinyInt

    private Date date;
    private Date regdate;
    private Date updatedate;

}
