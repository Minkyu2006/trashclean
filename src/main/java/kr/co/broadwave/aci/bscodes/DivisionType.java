package kr.co.broadwave.aci.bscodes;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Remark :
 */
public enum DivisionType {
    DT0001("DT0001", "운영사"),
    DT0002("DT0002", "지자체");

    private String code;
    private String desc;

    DivisionType(String code, String desc) {
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