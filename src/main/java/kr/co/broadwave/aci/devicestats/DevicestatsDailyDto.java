package kr.co.broadwave.aci.devicestats;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-15
 * Time :
 * Remark : DevicestatsDailyDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
// 일자별장비현황용
public class DevicestatsDailyDto {

    private String yyyymmdd; //날짜(그룹바이)

    private Double actuaterCnt; // 엑추에이터 작동횟수 -> (합계)
    private Double inputdoorjammingCnt; // 투일구걸림횟수 -> (합계)
    private Double frontdoorsolopenCnt; // 솔레노이드센서 열림 횟수 -> (합계)
    private Double emitCnt; // 일일장비 배출횟수 -> (합계)
    private Double fullLevel; // 해당일자의 쓰레기양 -> (평균)

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
