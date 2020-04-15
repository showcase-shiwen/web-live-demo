package com.dev.chat.core;

/**
 * 报名类型
 */
public enum DataType {
    chat(1, "聊天"),
    video(2, "视频"),
    notify(3, "通知");
    private Integer value;
    private String i18n;
    DataType(int value, String i18n) {
        this.value = value;
        this.i18n = i18n;
    }

    public static DataType get(Integer type) {
        DataType result=null;
        DataType[] array=DataType.values();
        for(DataType ele : array){
            if(ele.getValue().equals(type)){
                result=ele;
                break;
            }
        }
        return result;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getI18n() {
        return i18n;
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }
}
