package com.bmobapp.bmobchatai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.ai.BmobAI;
import cn.bmob.v3.ai.ChatMessageListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.BmobNative;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;

    List<Message> messageList;

    MessageAdapter messageAdapter;

    BmobAI bmobAI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建Bmob AI实例
        bmobAI = new BmobAI();
        //初始化AI内容问答存储
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.msg_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_bt);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        //点击发送提问到AI服务器的按钮
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取问题
                String quesion = messageEditText.getText().toString().trim();
                if(quesion.isEmpty() || quesion.trim()=="")
                    return;
                //连接AI服务器（这个代码为了防止AI连接中断）
                bmobAI.Connect();

                //显示问题
                addToChat(quesion,Message.SEND_BY_ME);
                messageEditText.setText("");

                //发送内容到AI中
                bmobAI.Chat(quesion, "test_user",new ChatMessageListener() {
                    @Override
                    public void onMessage(String s) {
                        //消息流的形式返回AI的结果
                        addToLastMessage(s);
                        Log.d("ai",s);
                    }
                    @Override
                    public void onFinish(String s) {
                        //一次性返回全部结果，这个方法需要等待一段时间，友好性较差
                        //addToChat(s,Message.SEND_BY_BOT);
                    }

                    @Override
                    public void onClose() {
                        //连接关闭了
                        Log.d("ai","close");
                    }
                });
            }
        });

    }

    /**
     * 支持流的形式呈现内容到界面
     * @param s
     */
    public void addToLastMessage(String s)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(messageList.size()<=0) return;
                Message message =  messageList.get(messageList.size()-1);

                if(message.sendBy==Message.SEND_BY_ME){
                    Message newmessage = new Message(s,Message.SEND_BY_BOT);
                    messageList.add(newmessage);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }else{
                    message.setMessage(message.getMessage() + s);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }
            }
        });
    }

    /**
     * 一次性将全部内容呈现到界面
     * @param message
     * @param sendBy
     */
    void addToChat(String message,String sendBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sendBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
}