package kr.co.broadwave.aci.bscodes;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Remark :
 */
public enum EmType {
    ET0001("ET0001", "iTainer"),
    ET0002("ET0002", "iSolarbin"),
    ET0003("ET0003", "iNetbin");

    private String code;
    private String desc;

    EmType(String code, String desc) {
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
