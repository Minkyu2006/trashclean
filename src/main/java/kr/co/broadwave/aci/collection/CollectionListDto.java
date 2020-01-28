package kr.co.broadwave.aci.collection;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-01-28
 * Time :
 * Remark : 수거관리 클래스 DtoList
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionListDto {
    private Long id;
    private String ctCode; // 수거관리코드
    private String yyyymmdd; // 수거 처리일
    private String devicetype;
    private String deviceid;
    private String accountName;
    private String vehicleNumber;

    public Long getId() {
        return id;
    }

    public String getCtCode() {
        return ctCode;
    }

    public StringBuffer getYyyymmdd() {
        StringBuffer yyyymmddDate = new StringBuffer(yyyymmdd); // ex)2020-11-04
        yyyymmddDate.insert(4,'-');
        yyyymmddDate.insert(7,'-');
        return yyyymmddDate;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
}
