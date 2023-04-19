package com.github.framework.core.lang;

import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CustomDateUtils extends DateUtils {

    public static final ZoneOffset SYSTEM_ZONE_OFFSET;
    /**
     * 默认 OFFSET  +08:00
     */
    public static final String DEFAULT_OFFSET = "+08:00";

    static {
        ZoneOffset zoneOffset = null;

        String offsetId = System.getProperty("zone.offset");
        if (CustomStringUtils.isNotBlank(offsetId)) {
            zoneOffset = ZoneOffset.of(offsetId);
        } else {
            offsetId = System.getenv("ZONE_OFFSET");
            if (CustomStringUtils.isNotBlank(offsetId)) {
                zoneOffset = ZoneOffset.of(offsetId);
            }
        }
        if (Objects.nonNull(zoneOffset)) {
            // 系统参数找到的情况
            SYSTEM_ZONE_OFFSET = zoneOffset;
        } else {
            // 否则使用默认+08:00
            SYSTEM_ZONE_OFFSET = ZoneOffset.of(DEFAULT_OFFSET);
        }
    }

    private CustomDateUtils() {}

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String formatNow() {
        return formatNow(YYYY_MM_DD);
    }

    public static String formatNow(String fmt) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(fmt));
    }

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_2 = "yyyy/MM/dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HHMMSS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DD_HHMMSS_2 = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYY_MM_DD_CN = "yyyy年MM月dd日";
    public static final String YYYY_MM_DD_HHMMSS_CN = "yyyy年MM月dd日 HH:mm:ss";
    public static final String HHMMSS = "HH:mm:ss";
    public static final String YYYY_MM_DD_T_HHMMSS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY_MM = "yyyy-MM";
}
