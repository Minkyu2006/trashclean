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
//    private String id;
    private Double fullLevel; // 해당일자의 쓰레기양(%)

//    public String getId() {
//        return id;
//    }

    public Double getFullLevel() {
        return fullLevel;
    }
}
