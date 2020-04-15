package com.dev.chat.core.socket;

import com.alibaba.fastjson.JSONObject;
import com.dev.chat.core.DataType;
import com.dev.chat.core.MsgData;
import com.dev.chat.core.session.ChatSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
@Component
public class WebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<ChatSession> webSocketSet = new CopyOnWriteArraySet<ChatSession>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private Logger logger = LoggerFactory.getLogger(WebSocket.class);

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @throws EncodeException
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session) throws EncodeException, Exception {
        this.session = session;
        Map<String, List<String>> listMap = session.getRequestParameterMap();
        String userId = listMap.get("userId").get(0);
        String roomId = listMap.get("roomId").get(0);
        String isMaster = listMap.get("isMaster").get(0);

        webSocketSet.add(new ChatSession(userId, roomId, this, isMaster));

        notifyMessage(roomId, userId, isMaster);


        addOnlineCount();           //在线数加1
        logger.info("roomId:{},userId:{},有新连接加入！当前在线人数为 {}", roomId, userId, getOnlineCount());
    }

    /**
     * 创建连接 通知信息
     *
     * @param roomId   房间号
     * @param userId   用户标记
     * @param isMaster 是否房主
     * @throws Exception
     */
    private void notifyMessage(String roomId, String userId, String isMaster) throws Exception {
        String msg = null;
        for (ChatSession chatSession : webSocketSet) {
            if (chatSession.getRoomId().equals(roomId)) {
                if (chatSession.getWebSocket().equals(this)) {
                    if (chatSession.isMaster()) {
                        msg = getTime() + " 创建直播间";
                    } else {
                        msg = getTime() + " " + userId + "上线";
                    }

                } else {
                    msg = getTime() + " " + userId + "进入直播间";
                }
                chatSession.getWebSocket().sendMessage(JSONObject.toJSONString(new MsgData(DataType.notify.getValue(), roomId, msg)));
            }
        }
        // 通知主播发送__offer指令
        if ("false".equals(isMaster)) {

            for (ChatSession chatSession : webSocketSet) {
                if (chatSession.getRoomId().equals(roomId)) {
                    if (chatSession.isMaster()) {
                        if (chatSession.isMaster()) {
                            //设置接收人信息
                            JSONObject dataJson = new JSONObject();
                            dataJson.put("name", userId);
                            dataJson.put("receiver", chatSession.getUserId());
                            chatSession.getWebSocket().sendMessage(JSONObject.toJSONString(new MsgData(DataType.video.getValue(), roomId, dataJson.toJSONString())));
                        }

                    }
                }

            }
        }
    }

    private String getMasterName(String roomId) {
        String masterUser = "";
        ChatSession chatSession = null;
        Iterator<ChatSession> iterator = webSocketSet.iterator();
        while (iterator.hasNext()) {
            chatSession = iterator.next();
            if (chatSession.isMaster()) {
                masterUser = chatSession.getUserId();
                break;
            }
        }
        return masterUser;
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        ChatSession chatSession = null;
        Iterator<ChatSession> iterator = webSocketSet.iterator();
        while (iterator.hasNext()) {
            chatSession = iterator.next();
            if (chatSession.equals(this)) {
                break;
            }
        }
        webSocketSet.remove(chatSession);
        chatSession = null;

        subOnlineCount();
        logger.info(" 当前在线人数为 {}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws EncodeException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        logger.info("来自客户端的消息:{}", message);
        MsgData msgData = JSONObject.parseObject(message, MsgData.class);

        String roomId = msgData.getRoomId();

        for (ChatSession chatSession : webSocketSet) {
            if (chatSession.getRoomId().equals(roomId)) {
                DataType dataType = DataType.get(msgData.getType());
                switch (dataType) {
                    case chat:
                    case notify:
                        chatSession.getWebSocket().sendMessage(message);
                        break;
                    case video:
                        chatSession.getWebSocket().sendMessage(message);
                        break;
                    default:
                }
            }
        }
    }

    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        if (this.session.isOpen()) {
            this.session.getAsyncRemote().sendText(message);
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

}
