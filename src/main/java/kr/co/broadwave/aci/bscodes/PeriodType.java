package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-05-03
 * Time : 13:25
 * Remark :
 */
public enum PeriodType {
    P01("P01", "전체주기","Line그래프를 사용한다."),
    P02("P02", "해당주기","Bar그래프를 사용한다."),
    ;

    private String code;
    private String desc;
    private String remark;

    PeriodType(String code, String desc,String remark) {
        this.code = code;
        this.desc = desc;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getRemark() {
        return remark;
    }
}
