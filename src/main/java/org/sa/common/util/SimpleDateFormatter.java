package org.sa.common.util;

import lombok.extern.log4j.Log4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j
public class SimpleDateFormatter {
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    // static SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd");

    public static String parseToWords(Date date) {

        // Date resultDate = new Date(result);

        String resultStr = "";
        Date now = new Date();
        java.util.Date utilDate = new java.util.Date(date.getTime() - 9 * 1000 * 60 * 60);
        Long result = now.getTime() - utilDate.getTime();

        // 분 확인 => 방금
        if (1 > (result / 1000 / 60)) {
            resultStr = "just now";
            // 시간 확인
        } else if (1 > (result/1000/60/60)) {
            resultStr = result / 1000 / 60 + " minute(s) ago";
            // 날 확인
        } else if(1 > (result/1000/60/60/24)){
            resultStr = result / 1000 / 60 /60 + " hour(s) ago";
            // 주 확인
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

    public static Boolean checkInThreeMonths(Date date){

        Boolean isInThree = false;
        // 현재 시간
        Date now = new Date();
        // 등록 시간
        java.util.Date utilDate = new java.util.Date(date.getTime() - 9 * 1000 * 60 * 60);
        // 시간 차이
        Long result = now.getTime() - utilDate.getTime();

        // 90일 차이 확인
        if (1 > (result / 1000 / 60 / 60 / 24 / 90)){
            isInThree = true;
        }

        return isInThree;
    }


    public static String makeDateFormat(String str){
        String[] arr = str.split("/");

        if(arr.length == 1){
            char[] charArr = str.toCharArray();
            String result = charArr[0] + charArr[1] + charArr[2] + charArr[3] + "/" + charArr[4] + charArr[5] + "/" + charArr[6] + charArr[7];
            return result;
        } else {
            if (arr[1].length() < 2) {
                arr[1] = "0" + arr[1];
            }
            if (arr[2].length() < 2) {
                arr[2] = "0" + arr[2];
            }

            return arr[0]+"/"+arr[1]+"/"+arr[2];
        }
    }

}
