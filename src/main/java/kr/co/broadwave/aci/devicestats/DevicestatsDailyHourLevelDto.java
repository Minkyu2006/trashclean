package kr.co.broadwave.aci.devicestats;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-16
 * Time :
 * Remark : DevicestatsDailyHourLevelDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
// 일자별장비현황용
public class DevicestatsDailyHourLevelDto {

    private String hh; //시간(그룹바이)

    private String yyyymmdd; // 검색한 해당날짜
    private Double emitCnt; // 일일장비 투입횟수 -> (합계)
    private Double actuaterCnt; // 엑추에이터 작동횟수 -> (합계)
    private Double inputdoorjammingCnt; // 투일구걸림횟수 -> (합계)
    private Double frontdoorsolopenCnt; // 솔레노이드센서 열림 횟수 -> (합계)
    private Double fullLevel; // 해당일자의 쓰레기양 -> (평균)

    public String getHh() {
        return hh;
    }

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public Double getActuaterCnt() {
        return actuaterCnt;
    }

    public Double getInputdoorjammingCnt() {
        return inputdoorjammingCnt;
    }

    public Double getFrontdoorsolopenCnt() {
        return frontdoorsolopenCnt;
    }

    public Double getEmitCnt() {
        return emitCnt;
    }

    public Double getFullLevel() {
        return fullLevel;
    }
}