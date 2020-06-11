package com.hawkab.entity;


import javax.persistence.*;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Entity
@Table(name = "settings")
public class SettingEntity {

    /**
     * Кодовый идентификатор настройки
     */
    @Id
    private String key;

    /**
     * Значение настройки
     */
    private String value;

    /**
     * Описание настройки
     */
    private String description;

    public SettingEntity() {
    }

    public SettingEntity(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
