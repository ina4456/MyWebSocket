package com.example.mywebsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity {

    //private static final String TAG = ChatSocket.class.getSimpleName();
    private static final String SERVER_URL = "https://58.180.28.220:8000";
    private TextView tvMain;
    private EditText etMsg;
    private Button btnSubmit;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMain = findViewById(R.id.tvMain);
        etMsg = findViewById(R.id.etMsg);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject preJsonObject = new JsonObject();
                preJsonObject.addProperty("comment", etMsg.getText()+"");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(preJsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("reqMsg", jsonObject);
                etMsg.setText("");
            }
        });






        try {
            socket = IO.socket("http://172.16.101.127:3100");
            socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                JsonObject preJsonObject = new JsonObject();
                preJsonObject.addProperty("roomName", "myroom");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(preJsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("joinRoom",jsonObject);
            }).on("recMsg", (Object... objects) -> {
                JsonParser jsonParsers = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0] + "");
                runOnUiThread(()->{
                    tvMain.setText(tvMain.getText().toString()+jsonObject.get("comment").getAsString());
                });
            });
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }





    }


}