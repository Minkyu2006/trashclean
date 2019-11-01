package kr.co.broadwave.aci.bscodes;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Remark :
 */
public enum NowStateType {
    ST0001("ST0001", "운영"),
    ST0002("ST0002", "대기"),
    ST0003("ST0003", "고장"),
    ST0004("ST0004", "폐기");

    private String code;
    private String desc;

    NowStateType(String code, String desc) {
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
