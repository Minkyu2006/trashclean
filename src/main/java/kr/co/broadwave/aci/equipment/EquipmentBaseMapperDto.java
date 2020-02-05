package kr.co.broadwave.aci.equipment;

import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-02-04
 * Time :
 * Remark : 장비기본값셋팅 클래스 BaseMapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentBaseMapperDto {
    private String emNumbers;
    private Double vInterval; // 센서 데이터 주기적 송신시간
    private Double vPresstime; // 압축 정지시간
    private Double vInputtime; // 투입구 열림 시간
    private Double vSolenoidtime; // 솔레노이드 열림 시간
    private Double vYellowstart; // 쓰레기량에 따른 노랑색등의 경계값
    private Double vRedstart; // 쓰레기량에 따른 적색등의 경계값

    public List<String> getEmNumbers() {
        return Arrays.asList(emNumbers.split(","));
    }

    public Double getvInterval() {
        return vInterval;
    }

    public Double getvPresstime() {
        return vPresstime;
    }

    public Double getvInputtime() {
        return vInputtime;
    }

    public Double getvSolenoidtime() {
        return vSolenoidtime;
    }

    public Double getvYellowstart() {
        return vYellowstart;
    }

    public Double getvRedstart() {
        return vRedstart;
    }
}
