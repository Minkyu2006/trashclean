package kr.co.broadwave.aci.collection;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Time :
 * Remark : 수거관리등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class CollectionTaskMapperDto {

   private String ctCode; //수거관리코드
   private String yyyymmdd; //처리일
   private String deviceid; //장치아이디
   private String vehicleNumber; // 차량번호

   private String userid;

    public String getUserid() {
        return userid;
    }

    public String getCtCode() {
        return ctCode;
    }

    public String getYyyymmdd() {
        return yyyymmdd.replaceAll("-", "");
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
}
