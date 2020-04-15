package com.dev.chat.core;

public class MsgData implements java.io.Serializable {
    private Integer type;// 1 聊天  2 视频 3 通知
    private String roomId;
    private String data;

    public MsgData(Integer type, String roomId, String data) {
        this.type = type;
        this.roomId = roomId;
        this.data = data;
    }

    private DataType typeEnum;

    public DataType getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(DataType typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
        if(type!=null){
            this.typeEnum=DataType.get(type);
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
