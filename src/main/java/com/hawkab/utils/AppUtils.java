package com.hawkab.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class AppUtils {

    public static boolean isNullAnything(Object... args) {
        return Arrays.stream(args).anyMatch(Objects::isNull);
    }

    public static boolean isNotNullOrWhitespace(String... args) {
        return Arrays.stream(args).allMatch(StringUtils::isNotBlank);
    }

    public static boolean isNullOrWhitespace(String... args) {
        return Arrays.stream(args).allMatch(StringUtils::isBlank);
    }

    public static boolean isNullOrWhitespaceAnything(String... args) {
        return Arrays.stream(args).anyMatch(StringUtils::isBlank);
    }
}
