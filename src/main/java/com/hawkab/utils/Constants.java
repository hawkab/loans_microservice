package com.hawkab.utils;

/**
 * Класс общих констант
 *
 * @author hawkab
 * @since 11.06.2020
 */
public class Constants {

    public static final String EMPTY_CLAIM_MESSAGE = "Получен пустой объект заявки";
    public static final String REQUIRED_FIELDS_CLAIM_MESSAGE = "Не заполнено одно из обязательных полей: amount, duration, " +
            "country или personnelId";
    public static final String LOAN_AMOUNT_VALIDATION_MESSAGE = "Сумма кредита должна быть больше нуля";


    public static final String SUCCESS_ADDED_TO_BLACK_LIST_MESSAGE = "Успешно добавлена запись в чёрный список";
    public static final String NOT_FOUND_IN_BLACK_LIST_MESSAGE = "В чёрном списке запись с таким personnelId не найдена";

    public static final String LOAN_NOT_FOUND_MESSAGE = "Подтверждённый кредит с такими данными не найден";
    public static final String LOAN_SUCCESSFULLY_REPAYED_MESSAGE = "Кредит успешно погашен";
    public static final String REPAY_LOAN_ERROR_MESSAGE = "Произошла ошибка при погашении кредита %s";
    public static final String REPAY_REQUIRED_FIELD_MESSAGE = "Не заполнено одно из обязательных полей: loanId или personnelId";
    public static final String READD_LOAN_NOT_FOUND_MESSAGE = "По заданным параметрам отказанный кредит не найден";
    public static final String LOAN_READD_REQUIRED_FIELD_MESSAGE = "Не заполнено одно из обязательных полей: loanId или personnelId";
    public static final String FIND_UUID_LOAN_REQUIRED_FIELD_MESSAGE = "Не заполнено обязательное поле: uuid";

    public static final String CLAIM_APPROVED = "Заявка успешно прошла проверку автоматическим средством контроля и подтверждена";
    public static final String AMOUNT_LIMIT_MESSAGE = "Превышен лимит по сумме непогашенных кредитов '%s' у " +
            "пользователя (по личному идентификатору)";
    public static final String CLAIM_COUNT_LIMIT_MESSAGE = "Превышен лимит '%s' заявок в '%s' минут от страны '%s'.";
    public static final String BLACK_LIST_CLAIM_MESSAGE = "Заявка поступает от пользователя в чёрном списке";
    public static final int COUNT_SECONDS_IN_MINUTE = 60;

    public static final String LIMIT_COUNT_CLAIMS = "limit_count_claims";
    public static final String DEFAULT_LIMIT_COUNT_CLAIMS = "5";

    public static final String LIMIT_MINUTES_CLAIMS = "limit_minutes_claims";
    public static final String DEFAULT_LIMIT_MINUTES_CLAIMS = "1";

    public static final String LIMIT_AMOUNT_CLAIMS = "limit_amount_claims";
    public static final String DEFAULT_LIMIT_AMOUNT_CLAIMS = "1000.00";

}

