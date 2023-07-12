# Bmob-Android-AI
Bmob安卓AI示例

## 最新版本

V3.9.3

更新内容：修正连接超时问题

更新时间：2023-07-11

## 最终效果

![](introduce.jpg)

## 如何配置

参考Bmob Android快速入门

http://doc.bmobapp.com/data/android/index.html


## 创建AI类
```java
bmobAI = new BmobAI();
```

## 请求AI

```java
bmobAI.Chat(quesion, "test_user",new ChatMessageListener() {
    @Override
    public void onMessage(String s) {
        //消息流的形式返回AI的结果
        Log.d("ai",s);
    }
    @Override
    public void onFinish(String s) {
        //一次性返回全部结果，这个方法需要等待一段时间，友好性较差
        Log.d("ai",s);
    }

    @Override
    public void onClose() {
        //连接关闭了
        Log.d("ai","close");
    }

    @Override
    public void onError(String s) {
        //OpenAI的密钥错误或者超过OpenAI并发时，会返回这个错误
        Log.d("ai",s);
        sendButton.setEnabled(true);
    }
});
```

## 设置prompt

```java
bmobAI.setPrompt("接下来的每一个回复，你都要叫我主人");
```
