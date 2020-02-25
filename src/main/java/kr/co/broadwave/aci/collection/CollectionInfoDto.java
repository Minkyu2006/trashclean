package kr.co.broadwave.aci.collection;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-28
 * Time :
 * Remark : 수거업무관리 클래스 InfoDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionInfoDto {
    private String ctCode; // 수거관리코드
    private Integer ctSeq; // 수거시퀀스
    private String yyyymmdd; // 수거 처리일
    private String deviceid; // 장비아이디
    private String accountname; // 유저이름
    private String accountuserid; // 유저아이디(고유)
    private String vehiclenumber; // 차량번호
    private String vehiclename; // 차량명

    public String getDeviceid() {
        return deviceid;
    }

    public Integer getCtSeq() {
        return ctSeq;
    }

    public String getCtCode() {
        return ctCode;
    }

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public String getAccountname() {
        return accountname;
    }

    public String getAccountuserid() {
        return accountuserid;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public String getVehiclename() {
        return vehiclename;
    }
}
