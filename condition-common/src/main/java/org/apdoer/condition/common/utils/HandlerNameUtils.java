package org.apdoer.condition.common.utils;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/15 10:49
 */
public class HandlerNameUtils {
    private static final char UNDERLINE = '_';

    public static String buildHandleName(String topic, String postfix) {
        return underlineToCamel(topic) + postfix;
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
