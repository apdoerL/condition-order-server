package org.apdoer.condition.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/13 17:50
 */
public class NumberUtils {
    private static final BigDecimal POWER18 = new BigDecimal("1000000000000000000");

    public static Long microToMil(Long data) {
        if (null != data) {
            return data / 1000;
        }
        return null;
    }

    public static String toSmall(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        } else {
            BigDecimal ds = new BigDecimal(s);
            ds = ds.divide(POWER18, 18, 4);
            return ds.stripTrailingZeros().toPlainString();
        }
    }
}
