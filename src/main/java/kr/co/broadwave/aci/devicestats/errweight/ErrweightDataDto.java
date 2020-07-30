package kr.co.broadwave.aci.devicestats.errweight;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Time :
 * Remark : 고장예측지수조회 클래스 Dto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class ErrweightDataDto {

    private String deviceid; // 장비코드

    private Integer err01Cnt; // TD001S - 터치 스크린 디바이스 오류휫수
    private Integer err02Cnt; // KD001S - 키패드 디바이스 상태
    private Integer err03Cnt; // ED001S - 임베디드 보드 디바이스 상태
    private Integer err04Cnt; // ED005S - 결제 도어 오류
    private Integer err05Cnt; // ED008S - 폐기물 도어 오류
    private Integer err06Cnt; // ED011S - 저울 디바이스 에러
    private Integer err07Cnt; // PI001S - 결제 디바이스 오류
    private Integer err08Cnt; // PI004C - 티머니 결제 오류
    private Integer err09Cnt; // PI005C - 신용카드 결제 오류
    private Integer err10Cnt; // LD001C - LTE 디바이스 오류

    public String getDeviceid() {
        return deviceid;
    }

    public Integer getErr01Cnt() {
        return err01Cnt;
    }

    public Integer getErr02Cnt() {
        return err02Cnt;
    }

    public Integer getErr03Cnt() {
        return err03Cnt;
    }

    public Integer getErr04Cnt() {
        return err04Cnt;
    }

    public Integer getErr05Cnt() {
        return err05Cnt;
    }

    public Integer getErr06Cnt() {
        return err06Cnt;
    }

    public Integer getErr07Cnt() {
        return err07Cnt;
    }

    public Integer getErr08Cnt() {
        return err08Cnt;
    }

    public Integer getErr09Cnt() {
        return err09Cnt;
    }

    public Integer getErr10Cnt() {
        return err10Cnt;
    }
}
