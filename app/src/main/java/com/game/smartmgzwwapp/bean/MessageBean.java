package com.game.smartmgzwwapp.bean;

import java.io.Serializable;

/**
 * Created by mi on 2018/3/28.
 */

public class MessageBean implements Serializable{
    private String roomId;
     private String userName;
     private String content;

    public MessageBean(String roomId, String userName, String content) {
        this.roomId = roomId;
        this.userName = userName;
        this.content = content;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
