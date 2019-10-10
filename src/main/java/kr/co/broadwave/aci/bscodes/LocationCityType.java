package kr.co.broadwave.aci.bscodes;

/**
 * @author MinKyu
 * Date : 2019-09-06
 * Remark : 출동일지 시타입
 */
public enum LocationCityType {

    L01("L01", "서울특별시"),
    L02("L02", "인천광역시"),
    L03("L03", "대구광역시"),
    L04("L04", "부산광역시"),
    L05("L05", "대전광역시"),
    L06("L06", "울산광역시"),
    L07("L07", "광주광역시"),
    L08("L08", "경기도"),
    L09("L09", "강원도"),
    L10("L10", "충청북도"),
    L11("L11", "충청남도"),
    L12("L12", "전라북도"),
    L13("L13", "전라남도"),
    L14("L14", "경상북도"),
    L15("L15", "경상남도"),
    L16("L16", "제주특별자치도"),
    L17("L17", "세종특별자치시");

    private String code;
    private String desc;


    LocationCityType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}

