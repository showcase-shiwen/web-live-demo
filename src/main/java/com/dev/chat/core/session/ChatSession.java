package com.dev.chat.core.session;

import com.dev.chat.core.socket.WebSocket;

public class ChatSession implements java.io.Serializable {
    private String roomId;
    private boolean isMaster=false;
    private String userId;
    private WebSocket webSocket;

    public ChatSession() {
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public ChatSession(String userId, String roomId, WebSocket webSocket,String isMaster) {
        this.roomId = roomId;
        this.userId = userId;
        this.webSocket = webSocket;
        System.setProperty(isMaster, isMaster);
        this.isMaster=Boolean.getBoolean(isMaster);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }
}
