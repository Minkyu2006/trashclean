package kr.co.broadwave.aci.bscodes;

/**
 * @author Minkyu
 * Date : 2020-06-01
 * Remark :
 */
public enum AccordiType {
    AT01("AT01", "배치"),
    AT02("AT02", "수거");

    private final String code;
    private final String desc;

    AccordiType(String code, String desc) {
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
