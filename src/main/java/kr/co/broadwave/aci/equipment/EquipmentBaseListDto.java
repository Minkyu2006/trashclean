package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2020-02-04
 * Time :
 * Remark : 장비기본값셋팅 클래스 BaseListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentBaseListDto {

    private String emNumber; // 장비 번호
//    private MasterCode emType; // 장비타입
    private MasterCode emCountry; // 국가
    private MasterCode emLocation; // 지역
    private IModel iModel; // 모델명,종류

    private Double vInterval; // 센서 데이터 주기적 송신시간
    private Double vPresstime; // 압축 정지시간
    private Double vInputtime; // 투입구 열림 시간
    private Double vSolenoidtime; // 솔레노이드 열림 시간
    private Double vYellowstart; // 쓰레기량에 따른 노랑색등의 경계값
    private Double vRedstart; // 쓰레기량에 따른 적색등의 경계값

    public String getEmNumber() {
        return emNumber;
    }

//    public String getEmType() {
//        return emType.getName();
//    }

    public String getEmCountry() {
        return emCountry.getName();
    }

    public String getEmLocation() {
        return emLocation.getName();
    }

    public String getiModel() {
        return iModel.getMdName()+"-"+iModel.getMdType().getName();
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
