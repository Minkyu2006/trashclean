package kr.co.broadwave.aci.devicestats;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-13
 * Time :
 * Remark : DevicestatsDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
//대시보드 배출량그래프용
public class DevicestatsDto {

    private String yyyymmdd;

    private Double fullLevel;

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public Double getFullLevel() {
        return fullLevel;
    }
}
