package kr.co.broadwave.aci.bscodes;

/**
 * @author Minkyu
 * Date : 2020-06-01
 * Remark :
 */
public enum CiStatusType {
    CST01("CST01", "지시"),
    CST02("CST02", "완료");

    private final String code;
    private final String desc;

    CiStatusType(String code, String desc) {
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
