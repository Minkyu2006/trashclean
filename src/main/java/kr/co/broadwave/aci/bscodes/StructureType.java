package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-07-09
 * Remark : 시설물 이반 정보의 StructtureType
 */
public enum StructureType {

    ST001("ST001", "교량"),
    ST002("ST002", "터널"),
    ST003("ST003", "옹벽")
            ;

    private String code;
    private String desc;

    StructureType(String code, String desc) {
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
