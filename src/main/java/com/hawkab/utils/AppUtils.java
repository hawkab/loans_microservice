package com.hawkab.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class AppUtils {

    public static boolean nonNull(Object... args) {
        return Arrays.stream(args).allMatch(Objects::nonNull);
    }

    public static boolean isNotNullOrWhitespace(String... args) {
        return Arrays.stream(args).allMatch(StringUtils::isNotBlank);
    }

    public static boolean isNullOrWhitespace(String... args) {
        return Arrays.stream(args).allMatch(StringUtils::isBlank);
    }
}
