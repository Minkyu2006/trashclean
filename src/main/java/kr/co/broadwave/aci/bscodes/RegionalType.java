package kr.co.broadwave.aci.bscodes;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Remark :
 */
public enum RegionalType {
    RT0001("RT0001", "제주 1구역"),
    RT0002("RT0002", "제주 2구역"),
    RT0003("RT0003", "제주 3구역");

    private String code;
    private String desc;

    RegionalType(String code, String desc) {
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