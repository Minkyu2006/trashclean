package kr.co.broadwave.aci.collection;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
public class CollectionTaskMapperDto {

   private Integer collectionSeq; //몇번돌지확인 -> 리스트길이
   private List<String> streetRouting; //라우팅된장비리스트

   private String ctCode; //수거관리코드
   private String yyyymmdd; //처리일
   private String vehicleNumber; // 차량번호
   private String userid;

    public Integer getCollectionSeq() {
        return collectionSeq;
    }

    public List<String> getStreetRouting() {
        return streetRouting;
    }

    public String getUserid() {
        return userid;
    }

    public String getCtCode() {
        return ctCode;
    }

    public String getYyyymmdd() {
        return yyyymmdd.replaceAll("-", "");
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
}
