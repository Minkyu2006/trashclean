package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-05-03
 * Time : 13:44
 * Remark :
 */
public enum TreatmentCondition {
//압축강도
    //S001 해사 사용 장기 시험체
    T10101("T10101", "노출환경", SeriesCode.S001, MeasurementItemCode.M001, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),

    //S002 내륙환경 기준 시험체
    T20101("T20101", "W/C", SeriesCode.S002, MeasurementItemCode.M001, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T20102("T20102", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M001, PeriodType.P01,"초기 염분함유량 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T20103("T20103", "W/C", SeriesCode.S002, MeasurementItemCode.M001, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M001.getDesc()),

    //S003 해안환경 기준 시험체
    T30101("T30101", "W/C", SeriesCode.S003, MeasurementItemCode.M001, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T30102("T30102", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M001, PeriodType.P01,"초기 염분함유량 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T30103("T30103", "W/C", SeriesCode.S003, MeasurementItemCode.M001, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M001.getDesc()),

    //S004 부순골재/재생모래 시험체
    T40101("T40101", "노출환경", SeriesCode.S004, MeasurementItemCode.M001, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T40102("T40102", "사용재료", SeriesCode.S004, MeasurementItemCode.M001, PeriodType.P01,"사용재료에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T40103("T40103", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M001, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T40104("T40104", "노출환경", SeriesCode.S004, MeasurementItemCode.M001, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M001.getDesc()),
    T40105("T40105", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M001, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M001.getDesc()),

    //S005 - 고로슬래그/플라이애쉬 사용 시험체
    T50101("T50101", "노출환경", SeriesCode.S005, MeasurementItemCode.M001, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T50102("T50102", "혼화재료", SeriesCode.S005, MeasurementItemCode.M001, PeriodType.P01,"혼화재료에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T50103("T50103", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M001, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T50104("T50104", "혼화재료", SeriesCode.S005, MeasurementItemCode.M001, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M001.getDesc()),
    T50105("T50105", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M001, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M001.getDesc()),

    //S006 해안환경 기준 시험체
    T60101("T60101", "노출환경", SeriesCode.S006, MeasurementItemCode.M001, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T60102("T60102", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M001, PeriodType.P01,"초기 염분함유량 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T60103("T60103", "노출환경", SeriesCode.S006, MeasurementItemCode.M001, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M001.getDesc()),

    //S007 해안환경 기준 시험체
    T70101("T70101", "노출환경", SeriesCode.S007, MeasurementItemCode.M001, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T70102("T70102", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M001, PeriodType.P01,"초기 염분함유량 따른 재령별 " + MeasurementItemCode.M001.getDesc()),
    T70103("T70103", "노출환경", SeriesCode.S007, MeasurementItemCode.M001, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M001.getDesc()),

//탄산화깊이
    //S001 해사 사용 장기 시험체
    T10201("T10201", "W/C", SeriesCode.S001, MeasurementItemCode.M002, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T10202("T10202", "방청제", SeriesCode.S001, MeasurementItemCode.M002, PeriodType.P01,"방청제에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T10203("T10203", "초기염분함유량", SeriesCode.S001, MeasurementItemCode.M002, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T10204("T10204", "방청제", SeriesCode.S001, MeasurementItemCode.M002, PeriodType.P02,"방청제에 따른 " + MeasurementItemCode.M002.getDesc()),
    T10205("T10205", "초기염분함유량", SeriesCode.S001, MeasurementItemCode.M002, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M002.getDesc()),

    //S002 내륙환경 기준 시험체
    T20201("T20201", "W/C", SeriesCode.S002, MeasurementItemCode.M002, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T20202("T20202", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M002, PeriodType.P01,"초기 염분함유량 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T20203("T20203", "W/C", SeriesCode.S002, MeasurementItemCode.M002, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M002.getDesc()),

    //S003 해안환경 기준 시험체
    T30201("T30201", "W/C", SeriesCode.S003, MeasurementItemCode.M002, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T30202("T30202", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M002, PeriodType.P01,"초기 염분함유량 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T30203("T30203", "W/C", SeriesCode.S003, MeasurementItemCode.M002, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M002.getDesc()),

    //S004 부순골재/재생모래 시험체
    T40201("T40201", "노출환경", SeriesCode.S004, MeasurementItemCode.M002, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T40202("T40202", "사용재료", SeriesCode.S004, MeasurementItemCode.M002, PeriodType.P01,"사용재료에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T40203("T40203", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M002, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T40204("T40204", "노출환경", SeriesCode.S004, MeasurementItemCode.M002, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M002.getDesc()),
    T40205("T40205", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M002, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M002.getDesc()),

    //S005 - 고로슬래그/플라이애쉬 사용 시험체
    T50201("T50201", "노출환경", SeriesCode.S005, MeasurementItemCode.M002, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T50202("T50202", "혼화재료", SeriesCode.S005, MeasurementItemCode.M002, PeriodType.P01,"혼화재료에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T50203("T50203", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M002, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T50204("T50204", "노출환경", SeriesCode.S005, MeasurementItemCode.M002, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M002.getDesc()),
    T50205("T50205", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M002, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M002.getDesc()),

    //S006 - 실리카흄 사용 시험체
    T60201("T60201", "노출환경", SeriesCode.S006, MeasurementItemCode.M002, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T60202("T60202", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M002, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T60203("T60203", "노출환경", SeriesCode.S006, MeasurementItemCode.M002, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M002.getDesc()),

    //S007 염분함유 기준 시험체
    T70201("T70201", "W/C", SeriesCode.S007, MeasurementItemCode.M002, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T70202("T70202", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M002, PeriodType.P01,"초기 염분함유량 따른 재령별 " + MeasurementItemCode.M002.getDesc()),
    T70203("T70203", "W/C", SeriesCode.S007, MeasurementItemCode.M002, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M002.getDesc()),


//길이변형율

    //S002 내륙환경 기준 시험체
    T20301("T20301", "W/C", SeriesCode.S002, MeasurementItemCode.M003, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T20302("T20302", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M003, PeriodType.P01,"초기 염분함유량에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T20303("T20303", "피복두께", SeriesCode.S002, MeasurementItemCode.M003, PeriodType.P01,"피복두께에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T20304("T20304", "W/C", SeriesCode.S002, MeasurementItemCode.M003, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M003.getDesc()),
    T20305("T20305", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M003, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M003.getDesc()),

    //S003 해안환경 기준 시험체
    T30301("T30301", "W/C", SeriesCode.S003, MeasurementItemCode.M003, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T30302("T30302", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M003, PeriodType.P01,"초기 염분함유량에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T30303("T30303", "피복두께", SeriesCode.S003, MeasurementItemCode.M003, PeriodType.P01,"피복두께에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T30304("T30304", "W/C", SeriesCode.S003, MeasurementItemCode.M003, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M003.getDesc()),
    T30305("T30305", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M003, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M003.getDesc()),

    //S004 부순골재/재생모래 시험체
    T40301("T40301", "노출환경", SeriesCode.S004, MeasurementItemCode.M003, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T40302("T40302", "사용재료", SeriesCode.S004, MeasurementItemCode.M003, PeriodType.P01,"사용재료에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T40303("T40303", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M003, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T40304("T40304", "피복두께", SeriesCode.S004, MeasurementItemCode.M003, PeriodType.P01,"피복두께에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T40305("T40305", "노출환경", SeriesCode.S004, MeasurementItemCode.M003, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M003.getDesc()),
    T40306("T40306", "사용재료", SeriesCode.S004, MeasurementItemCode.M003, PeriodType.P02,"사용재료에 따른 " + MeasurementItemCode.M003.getDesc()),
    T40307("T40307", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M003, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M003.getDesc()),


    //S005 - 고로슬래그/플라이애쉬 사용 시험체
    T50301("T50301", "노출환경", SeriesCode.S005, MeasurementItemCode.M003, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T50302("T50302", "혼화재료", SeriesCode.S005, MeasurementItemCode.M003, PeriodType.P01,"혼화재료에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T50303("T50303", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M003, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T50304("T50304", "피복두께", SeriesCode.S005, MeasurementItemCode.M003, PeriodType.P01,"피복두께에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T50305("T50305", "노출환경", SeriesCode.S005, MeasurementItemCode.M003, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M003.getDesc()),
    T50306("T50306", "혼화재료", SeriesCode.S005, MeasurementItemCode.M003, PeriodType.P02,"혼화재료에 따른 " + MeasurementItemCode.M003.getDesc()),
    T50307("T50307", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M003, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M003.getDesc()),

    //S006 - 실리카흄 사용 시험체
    T60301("T60301", "노출환경", SeriesCode.S006, MeasurementItemCode.M003, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T60302("T60302", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M003, PeriodType.P01,"초기 염분함유량에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T60303("T60303", "피복두께", SeriesCode.S006, MeasurementItemCode.M003, PeriodType.P01,"피복두께에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T60304("T60304", "노출환경", SeriesCode.S006, MeasurementItemCode.M003, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M003.getDesc()),
    T60305("T60305", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M003, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M003.getDesc()),

    //S007 염분함유 기준 시험체
    T70301("T70301", "W/C", SeriesCode.S007, MeasurementItemCode.M003, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M003.getDesc()),
    T70302("T70302", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M003, PeriodType.P01,"초기 염분함유량에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T70303("T70303", "피복두께", SeriesCode.S007, MeasurementItemCode.M003, PeriodType.P01,"피복두께에 따른 주기별 " + MeasurementItemCode.M003.getDesc()),
    T70304("T70304", "W/C", SeriesCode.S007, MeasurementItemCode.M003, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M003.getDesc()),
    T70305("T70305", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M003, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M003.getDesc()),

//초음파속도

    //S002 내륙환경 기준 시험체
    T20401("T20401", "W/C", SeriesCode.S002, MeasurementItemCode.M004, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T20402("T20402", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M004, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T20403("T20403", "피복두께", SeriesCode.S002, MeasurementItemCode.M004, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T20404("T20404", "W/C", SeriesCode.S002, MeasurementItemCode.M004, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M004.getDesc()),
    T20405("T20405", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M004, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M004.getDesc()),

    //S003 해안환경 기준 시험체
    T30401("T30401", "W/C", SeriesCode.S003, MeasurementItemCode.M004, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T30402("T30402", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M004, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T30403("T30403", "피복두께", SeriesCode.S003, MeasurementItemCode.M004, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T30404("T30404", "W/C", SeriesCode.S003, MeasurementItemCode.M004, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M004.getDesc()),
    T30405("T30405", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M004, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M004.getDesc()),

    //S004 부순골재/재생모래 시험체
    T40401("T40401", "노출환경", SeriesCode.S004, MeasurementItemCode.M004, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T40402("T40402", "사용재료", SeriesCode.S004, MeasurementItemCode.M004, PeriodType.P01,"사용재료에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T40403("T40403", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M004, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T40404("T40404", "피복두께", SeriesCode.S004, MeasurementItemCode.M004, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T40405("T40405", "노출환경", SeriesCode.S004, MeasurementItemCode.M004, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M004.getDesc()),
    T40406("T40406", "사용재료", SeriesCode.S004, MeasurementItemCode.M004, PeriodType.P02,"사용재료에 따른 " + MeasurementItemCode.M004.getDesc()),
    T40407("T40407", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M004, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M004.getDesc()),

    //S005 - 고로슬래그/플라이애쉬 사용 시험체
    T50401("T50401", "노출환경", SeriesCode.S005, MeasurementItemCode.M004, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T50402("T50402", "혼화재료", SeriesCode.S005, MeasurementItemCode.M004, PeriodType.P01,"혼화재료에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T50403("T50403", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M004, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T50404("T50404", "피복두께", SeriesCode.S005, MeasurementItemCode.M004, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T50405("T50405", "노출환경", SeriesCode.S005, MeasurementItemCode.M004, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M004.getDesc()),
    T50406("T50406", "혼화재료", SeriesCode.S005, MeasurementItemCode.M004, PeriodType.P02,"혼화재료에 따른 " + MeasurementItemCode.M004.getDesc()),
    T50407("T50407", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M004, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M004.getDesc()),

    //S006 - 실리카흄 사용 시험체
    T60401("T60401", "노출환경", SeriesCode.S006, MeasurementItemCode.M004, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T60402("T60402", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M004, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T60403("T60403", "피복두께", SeriesCode.S006, MeasurementItemCode.M004, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T60404("T60404", "노출환경", SeriesCode.S006, MeasurementItemCode.M004, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M004.getDesc()),
    T60405("T60405", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M004, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M004.getDesc()),

    //S007 염분함유 기준 시험체
    T70401("T70401", "W/C", SeriesCode.S007, MeasurementItemCode.M004, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T70402("T70402", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M004, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T70403("T70403", "피복두께", SeriesCode.S007, MeasurementItemCode.M004, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M004.getDesc()),
    T70404("T70404", "W/C", SeriesCode.S007, MeasurementItemCode.M004, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M004.getDesc()),
    T70405("T70405", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M004, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M004.getDesc()),

//철근부식량

    //S002 내륙환경 기준 시험체
    T20501("T20501", "W/C", SeriesCode.S002, MeasurementItemCode.M005, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T20502("T20502", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M005, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T20503("T20503", "피복두께", SeriesCode.S002, MeasurementItemCode.M005, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T20504("T20504", "W/C", SeriesCode.S002, MeasurementItemCode.M005, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M005.getDesc()),
    T20505("T20505", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M005, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M005.getDesc()),

    //S003 해안환경 기준 시험체
    T30501("T30501", "W/C", SeriesCode.S003, MeasurementItemCode.M005, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T30502("T30502", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M005, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T30503("T30503", "피복두께", SeriesCode.S003, MeasurementItemCode.M005, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T30504("T30504", "W/C", SeriesCode.S003, MeasurementItemCode.M005, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M005.getDesc()),
    T30505("T30505", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M005, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M005.getDesc()),

    //S004 부순골재/재생모래 시험체
    T40501("T40501", "노출환경", SeriesCode.S004, MeasurementItemCode.M005, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T40502("T40502", "사용재료", SeriesCode.S004, MeasurementItemCode.M005, PeriodType.P01,"사용재료에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T40503("T40503", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M005, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T40504("T40504", "피복두께", SeriesCode.S004, MeasurementItemCode.M005, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T40505("T40505", "노출환경", SeriesCode.S004, MeasurementItemCode.M005, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M005.getDesc()),
    T40506("T40506", "사용재료", SeriesCode.S004, MeasurementItemCode.M005, PeriodType.P02,"사용재료에 따른 " + MeasurementItemCode.M005.getDesc()),
    T40507("T40507", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M005, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M005.getDesc()),

    //S005 - 고로슬래그/플라이애쉬 사용 시험체
    T50501("T50501", "노출환경", SeriesCode.S005, MeasurementItemCode.M005, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T50502("T50502", "혼화재료", SeriesCode.S005, MeasurementItemCode.M005, PeriodType.P01,"혼화재료에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T50503("T50503", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M005, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T50504("T50504", "피복두께", SeriesCode.S005, MeasurementItemCode.M005, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T50505("T50505", "노출환경", SeriesCode.S005, MeasurementItemCode.M005, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M005.getDesc()),
    T50506("T50506", "혼화재료", SeriesCode.S005, MeasurementItemCode.M005, PeriodType.P02,"혼화재료에 따른 " + MeasurementItemCode.M005.getDesc()),
    T50507("T50507", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M005, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M005.getDesc()),

    //S006 - 실리카흄 사용 시험체
    T60501("T60501", "노출환경", SeriesCode.S006, MeasurementItemCode.M005, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T60502("T60502", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M005, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T60503("T60503", "피복두께", SeriesCode.S006, MeasurementItemCode.M005, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T60504("T60504", "노출환경", SeriesCode.S006, MeasurementItemCode.M005, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M005.getDesc()),
    T60505("T60505", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M005, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M005.getDesc()),

    //S007 - 염분함유 사용 시험체
    T70501("T70501", "W/C", SeriesCode.S007, MeasurementItemCode.M005, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T70502("T70502", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M005, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T70503("T70503", "피복두께", SeriesCode.S007, MeasurementItemCode.M005, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M005.getDesc()),
    T70504("T70504", "W/C", SeriesCode.S007, MeasurementItemCode.M005, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M005.getDesc()),
    T70505("T70505", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M005, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M005.getDesc()),

//철근부식면적량


    //S002 내륙환경 기준 시험체
    T20601("T20601", "W/C", SeriesCode.S002, MeasurementItemCode.M006, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T20602("T20602", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M006, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T20603("T20603", "피복두께", SeriesCode.S002, MeasurementItemCode.M006, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T20604("T20604", "W/C", SeriesCode.S002, MeasurementItemCode.M006, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M006.getDesc()),
    T20605("T20605", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M006, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M006.getDesc()),

    //S003 해안환경 기준 시험체
    T30601("T30601", "W/C", SeriesCode.S003, MeasurementItemCode.M006, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T30602("T30602", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M006, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T30603("T30603", "피복두께", SeriesCode.S003, MeasurementItemCode.M006, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T30604("T30604", "W/C", SeriesCode.S003, MeasurementItemCode.M006, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M006.getDesc()),
    T30605("T30605", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M006, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M006.getDesc()),

    //S004 부순골재/재생모래 시험체
    T40601("T40601", "노출환경", SeriesCode.S004, MeasurementItemCode.M006, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T40602("T40602", "사용재료", SeriesCode.S004, MeasurementItemCode.M006, PeriodType.P01,"사용재료에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T40603("T40603", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M006, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T40604("T40604", "피복두께", SeriesCode.S004, MeasurementItemCode.M006, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T40605("T40605", "노출환경", SeriesCode.S004, MeasurementItemCode.M006, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M006.getDesc()),
    T40606("T40606", "사용재료", SeriesCode.S004, MeasurementItemCode.M006, PeriodType.P02,"사용재료에 따른 " + MeasurementItemCode.M006.getDesc()),
    T40607("T40607", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M006, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M006.getDesc()),

    //S005 - 고로슬래그/플라이애쉬 사용 시험체
    T50601("T50601", "노출환경", SeriesCode.S005, MeasurementItemCode.M006, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T50602("T50602", "혼화재료", SeriesCode.S005, MeasurementItemCode.M006, PeriodType.P01,"혼화재료에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T50603("T50603", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M006, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T50604("T50604", "피복두께", SeriesCode.S005, MeasurementItemCode.M006, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T50605("T50605", "노출환경", SeriesCode.S005, MeasurementItemCode.M006, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M006.getDesc()),
    T50606("T50606", "혼화재료", SeriesCode.S005, MeasurementItemCode.M006, PeriodType.P02,"혼화재료에 따른 " + MeasurementItemCode.M006.getDesc()),
    T50607("T50607", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M006, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M006.getDesc()),

    //S006 - 실리카흄 사용 시험체
    T60601("T60601", "노출환경", SeriesCode.S006, MeasurementItemCode.M006, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T60602("T60602", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M006, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T60603("T60603", "피복두께", SeriesCode.S006, MeasurementItemCode.M006, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T60604("T60604", "노출환경", SeriesCode.S006, MeasurementItemCode.M006, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M006.getDesc()),
    T60605("T60605", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M006, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M006.getDesc()),

    //S007 - 염분함유 사용 시험체
    T70601("T70601", "W/C", SeriesCode.S007, MeasurementItemCode.M006, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T70602("T70602", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M006, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T70603("T70603", "피복두께", SeriesCode.S007, MeasurementItemCode.M006, PeriodType.P01,"피복두께에 따른 재령별 " + MeasurementItemCode.M006.getDesc()),
    T70604("T70604", "W/C", SeriesCode.S007, MeasurementItemCode.M006, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M006.getDesc()),
    T70605("T70605", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M006, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M006.getDesc()),

//염분함유량

    //S001 해사 사용 장기 시험체
    T10701("T10701", "W/C", SeriesCode.S001, MeasurementItemCode.M007, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T10702("T10702", "방청제", SeriesCode.S001, MeasurementItemCode.M007, PeriodType.P01,"방청제에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T10703("T10703", "초기염분함유량", SeriesCode.S001, MeasurementItemCode.M007, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T10704("T10704", "콘크리트깊이", SeriesCode.S001, MeasurementItemCode.M007, PeriodType.P01,"콘크리트 깊이 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T10705("T10705", "W/C", SeriesCode.S001, MeasurementItemCode.M007, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M007.getDesc()),
    T10706("T10706", "방청제", SeriesCode.S001, MeasurementItemCode.M007, PeriodType.P02,"방청제에 따른 " + MeasurementItemCode.M007.getDesc()),
    T10707("T10707", "초기염분함유량", SeriesCode.S001, MeasurementItemCode.M007, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M007.getDesc()),

    //S002 내륙환경 기준 시험체
    T20701("T20701", "W/C", SeriesCode.S002, MeasurementItemCode.M007, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T20702("T20702", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M007, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T20703("T20703", "콘크리트깊이", SeriesCode.S002, MeasurementItemCode.M007, PeriodType.P01,"콘크리트 깊이 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T20704("T20704", "W/C", SeriesCode.S002, MeasurementItemCode.M007, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M007.getDesc()),
    T20705("T20705", "초기염분함유량", SeriesCode.S002, MeasurementItemCode.M007, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M007.getDesc()),

    //S003 내륙환경 기준 시험체
    T30701("T30701", "W/C", SeriesCode.S003, MeasurementItemCode.M007, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T30702("T30702", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M007, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T30703("T30703", "콘크리트깊이", SeriesCode.S003, MeasurementItemCode.M007, PeriodType.P01,"콘크리트 깊이 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T30704("T30704", "W/C", SeriesCode.S003, MeasurementItemCode.M007, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M007.getDesc()),
    T30705("T30705", "초기염분함유량", SeriesCode.S003, MeasurementItemCode.M007, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M007.getDesc()),

    //S004 부순골재/재생모래 시험체
    T40701("T40701", "노출환경", SeriesCode.S004, MeasurementItemCode.M007, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T40702("T40702", "사용재료", SeriesCode.S004, MeasurementItemCode.M007, PeriodType.P01,"사용재료에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T40703("T40703", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M007, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T40704("T40704", "콘크리트깊이", SeriesCode.S004, MeasurementItemCode.M007, PeriodType.P01,"콘크리트 깊이에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T40705("T40705", "노출환경", SeriesCode.S004, MeasurementItemCode.M007, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M007.getDesc()),
    T40706("T40706", "사용재료", SeriesCode.S004, MeasurementItemCode.M007, PeriodType.P02,"사용재료에 따른 " + MeasurementItemCode.M007.getDesc()),
    T40707("T40707", "초기염분함유량", SeriesCode.S004, MeasurementItemCode.M007, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M007.getDesc()),


    //S005 - 고로슬래그/플라이애쉬 사용 시험체
    T50701("T50701", "노출환경", SeriesCode.S005, MeasurementItemCode.M007, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T50702("T50702", "혼화재료", SeriesCode.S005, MeasurementItemCode.M007, PeriodType.P01,"혼화재료에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T50703("T50703", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M007, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T50704("T50704", "콘크리트깊이", SeriesCode.S005, MeasurementItemCode.M007, PeriodType.P01,"콘크리트 깊이에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T50705("T50705", "노출환경", SeriesCode.S005, MeasurementItemCode.M007, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M007.getDesc()),
    T50706("T50706", "혼화재료", SeriesCode.S005, MeasurementItemCode.M007, PeriodType.P02,"혼화재료에 따른 " + MeasurementItemCode.M007.getDesc()),
    T50707("T50707", "초기염분함유량", SeriesCode.S005, MeasurementItemCode.M007, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M007.getDesc()),

    //S006 - 실리카흄 사용 시험체
    T60701("T60701", "노출환경", SeriesCode.S006, MeasurementItemCode.M007, PeriodType.P01,"노출환경에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T60702("T60702", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M007, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T60703("T60703", "콘크리트깊이", SeriesCode.S006, MeasurementItemCode.M007, PeriodType.P01,"콘크리트 깊이 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T60704("T60704", "노출환경", SeriesCode.S006, MeasurementItemCode.M007, PeriodType.P02,"노출환경에 따른 " + MeasurementItemCode.M007.getDesc()),
    T60705("T60705", "초기염분함유량", SeriesCode.S006, MeasurementItemCode.M007, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M007.getDesc()),

    //S007 - 염분함유 사용 시험체
    T70701("T70701", "W/C", SeriesCode.S007, MeasurementItemCode.M007, PeriodType.P01,"W/C에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T70702("T70702", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M007, PeriodType.P01,"초기 염분함유량에 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T70703("T70703", "콘크리트깊이", SeriesCode.S007, MeasurementItemCode.M007, PeriodType.P01,"콘크리트 깊이 따른 재령별 " + MeasurementItemCode.M007.getDesc()),
    T70704("T70704", "W/C", SeriesCode.S007, MeasurementItemCode.M007, PeriodType.P02,"W/C에 따른 " + MeasurementItemCode.M007.getDesc()),
    T70705("T70705", "초기염분함유량", SeriesCode.S007, MeasurementItemCode.M007, PeriodType.P02,"초기염분함유량에 따른 " + MeasurementItemCode.M007.getDesc()),

    ;

    private String code;
    private String desc;
    private SeriesCode seriesCode;
    private MeasurementItemCode measurementItemCode;
    private PeriodType periodType;
    private String graphTitle;

    TreatmentCondition(String code, String desc,SeriesCode seriesCode,MeasurementItemCode measurementItemCode,PeriodType periodType,String graphTitle) {
        this.code = code;
        this.desc = desc;
        this.seriesCode = seriesCode;
        this.measurementItemCode = measurementItemCode;
        this.periodType = periodType;
        this.graphTitle= graphTitle;

    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getMeasurementItemCode() {
        return measurementItemCode.getCode();
    }

    public String getPeriodType() {
        return periodType.getCode();
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public String getSeriesCode() { return seriesCode.getCode(); }
}
