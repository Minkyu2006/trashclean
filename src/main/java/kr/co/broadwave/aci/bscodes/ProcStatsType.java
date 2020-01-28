package kr.co.broadwave.aci.bscodes;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Remark :
 */
public enum ProcStatsType {
    CL01("CL01", "계획"),
    CL02("CL02", "확정"),
    CL03("CL03", "수거중"),
    CL04("CL04", "완료");

    private String code;
    private String desc;

    ProcStatsType(String code, String desc) {
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
