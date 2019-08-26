package com.hawkab.rest;


import java.io.Serializable;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class SettingData implements Serializable {

    private String key;

    private String value;

    private String description;

    public SettingData() {
    }
    public SettingData(String key, String value, String description) {
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

    @Override
    public String toString() {
        return "SettingData{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
