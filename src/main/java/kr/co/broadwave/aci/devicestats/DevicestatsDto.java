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
