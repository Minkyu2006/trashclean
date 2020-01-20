package kr.co.broadwave.aci.devicestats;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-계20
 * Time :
 * Remark : DevicestatsDailyDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
// 일자별장비현황용 -> 장비클릭시 한달의 시간대별 데이터통
public class DevicestatsDailyMonthDto {

    private String hh; //날짜(그룹바이)

    private Double actuaterCnt; // 엑추에이터 작동횟수 -> (평균)
    private Double inputdoorjammingCnt; // 투일구걸림횟수 -> (평균)
    private Double frontdoorsolopenCnt; // 솔레노이드센서 열림 횟수 -> (평균)
    private Double emitCnt; // 일일장비 투입횟수 -> (평균)
    private Double fullLevel; // 해당일자의 쓰레기양 -> (평균)

    public String getHh() {
        return hh;
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
