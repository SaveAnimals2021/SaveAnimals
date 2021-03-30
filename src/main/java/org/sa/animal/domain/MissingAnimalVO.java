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
public class MissingAnimalVO {
    private Integer animalNumber;

    private String animalCode;
    private String serviceName;

    private String type;
    private String name;

    @Builder.Default
    private String species = "모름"; // 진도
    private String sex; // 암컷
    private String age; // 2살
    private String special;
    private String color;

    private String imageType;

    // redirect할수 있는 원래 사이트
    private String originURL;

    private Date missingDate;
    private String missingLocation;

    private Date rescueDate;
    private String rescueLocation;

    private String phoneNumber;
    private String guardianName;
    private Integer bno;

    private Date regDate;
    private Date updateDate;
    // 상태
    private Integer rescueStatus;
    private String situation;
}
