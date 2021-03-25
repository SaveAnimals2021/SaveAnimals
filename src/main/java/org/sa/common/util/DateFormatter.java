package org.sa.common.util;

import lombok.extern.log4j.Log4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j
public class DateFormatter {
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    public static String parseToWords(Date date) {

        // Date resultDate = new Date(result);

        String resultStr = "";
        Date now = new Date();
        java.util.Date utilDate = new java.util.Date(date.getTime() - 9 * 1000 * 60 * 60);
        Long result = now.getTime() - utilDate.getTime();

        // 분 확인 => 방금
        if (1 > (result / 1000 / 60)) {
            resultStr = "just now";
        } else if (1 > (result/1000/60/60)) {
            resultStr = result / 1000 / 60 + " minute(s) ago";
        } else if(1 > (result/1000/60/60/24)){
            resultStr = result / 1000 / 60 /60 + " hour(s) ago";
        } else if (1 > (result / 1000 / 60 / 60 / 24 / 7)){
            resultStr = result/1000/60/60/24 + " day(s) ago";
        } else {
            resultStr = formatter.format(utilDate);
        }


        log.info("==============================");
        log.info("결과 : " + resultStr);
        log.info("시간차이 : " + result / 1000 / 60 + "분");
        log.info("현재시간 : " + now);
        log.info("등록시간 : " + utilDate);

        // 월 확인
        // 일 확인
        // 시간 확인
        // 분 확인
        // 방금

        return resultStr;
    }

    public static String fromDateToString(Date date) {
        return  formatter.format(new java.util.Date(date.getTime() - 9 * 1000 * 60 * 60));
    }


    public static Date fromStringToDate(String str) throws Exception {
        return null == str || str.length() == 0 ? null : formatter.parse(str);
    }
}
