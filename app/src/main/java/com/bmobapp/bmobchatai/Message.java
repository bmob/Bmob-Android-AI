package com.bmobapp.bmobchatai;

/**
 * 聊天内容类
 */
public class Message {
    public static String SEND_BY_ME="me";
    public static String SEND_BY_BOT="bot";

    String message;

    /**
     * 获取聊天内容
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置聊天内容
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取发送者
     * @return
     */
    public String getSendBy() {
        return sendBy;
    }

    /**
     * 设置发送者
     * @param sendBy
     */
    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    /**
     * 发送者
     */
    String sendBy;

    /**
     * 聊天内容的构造函数
     * @param message 聊天内容
     * @param sendBy 发送者
     */
    public Message(String message, String sendBy) {
        this.message = message;
        this.sendBy = sendBy;
    }
}
