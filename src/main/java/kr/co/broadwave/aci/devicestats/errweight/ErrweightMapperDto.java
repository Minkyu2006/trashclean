package kr.co.broadwave.aci.devicestats.errweight;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Time :
 * Remark : 가중치등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class ErrweightMapperDto {

    private Double err01Weight; // 터치스크린
    private Double err02Weight; // 키패드
    private Double err03Weight; // 임베디드 보드
    private Double err04Weight; // 결제도어
    private Double err05Weight; // 폐기물 도어
    private Double err06Weight; // 저울 디바이스
    private Double err07Weight; // 결제 디바이스
    private Double err08Weight; // 티머니 디바이스
    private Double err09Weight; // 신용카드 디바이스
    private Double err10Weight; // LTE 디바이스

    public Double getErr01Weight() {
        return err01Weight;
    }

    public Double getErr02Weight() {
        return err02Weight;
    }

    public Double getErr03Weight() {
        return err03Weight;
    }

    public Double getErr04Weight() {
        return err04Weight;
    }

    public Double getErr05Weight() {
        return err05Weight;
    }

    public Double getErr06Weight() {
        return err06Weight;
    }

    public Double getErr07Weight() {
        return err07Weight;
    }

    public Double getErr08Weight() {
        return err08Weight;
    }

    public Double getErr09Weight() {
        return err09Weight;
    }

    public Double getErr10Weight() {
        return err10Weight;
    }

}
