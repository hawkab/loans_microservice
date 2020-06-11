package com.hawkab.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Утилитарный класс вспомогательных методов
 *
 * @author hawkab
 * @since 26.08.2019
 */

public class AppUtils {

    /**
     * Проверить, что хотя бы один из переданных аргументов null
     *
     * @param args аргументы вариативной длины произвольного типа
     * @return флаг, определяющий, что хотя бы один из переданных аргументов null
     */
    static boolean isNullAnything(Object... args) {
        return Arrays.stream(args).anyMatch(Objects::isNull);
    }

    /**
     * Проверить, что все переданные аргументы не null, не пусты и не заполнены непечатаемыми символами
     *
     * @param args аргументы вариативной длины произвольного типа
     * @return флаг, определяющий, что все переданные аргументы не null, не пусты и не заполнены непечатаемыми символами
     */
    public static boolean isNotNullOrWhitespace(String... args) {
        return Arrays.stream(args).allMatch(StringUtils::isNotBlank);
    }

    /**
     * Проверить, что все переданные аргументы null, пусты или заполнены непечатаемыми символами
     *
     * @param args аргументы вариативной длины произвольного типа
     * @return флаг, определяющий, что все переданные аргументы null, пусты или заполнены непечатаемыми символами
     */
    public static boolean isNullOrWhitespace(String... args) {
        return Arrays.stream(args).allMatch(StringUtils::isBlank);
    }

    /**
     * Проверить, что хотя бы один из переданных аргументов null, пуст или заполнен непечатаемыми символами
     *
     * @param args аргументы вариативной длины произвольного типа
     * @return флаг, определяющий, что хотя бы один из переданных аргументов null, пуст или заполнен непечатаемыми символами
     */
    static boolean isNullOrWhitespaceAnything(String... args) {
        return Arrays.stream(args).anyMatch(StringUtils::isBlank);
    }
}
