package org.sa.common.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalInfo {


    private String siteNumber;

    private String type; // 개
    private String name; // 개
    private String species; // 진도
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

    private String boardDate;
}
