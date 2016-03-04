package com.haticesigirci.socketioandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

public class SocketIOActivity extends AppCompatActivity {


    SocketIO socketIO;
    private TextView userName;
    private String userNameNodeJS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_io);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {

            socketIO = new SocketIO("http://192.168.2.30:7070"); ///192.168.2.30

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        socketIO.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {
                    Log.d("Server said: ", json.toString(2));

                    System.out.println("Server said:" + json.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {

                Log.d("Server said: ", data);
                System.out.println("Server said: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {

                Log.d("an Error occured", "an Error occurred");
                System.out.println("an Error occured");
                socketIOException.printStackTrace();
            }

            @Override
            public void onDisconnect() {

                Log.d("Connection terminated", "Connection terminated");
                System.out.println("Connection terminated.");
            }

            @Override
            public void onConnect() {
                Log.d("Connection established", "Connection established");

                JSONObject obj = new JSONObject();

                try {
                    obj.put("name", "hatice".trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("SEND MESSAGE", obj.toString());

                socketIO.emit("chat", obj);
                System.out.println("Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                Log.d("Server triggered event ", event);
                System.out.println("Server triggered event '" + event + "' " + args[0]);

                JSONObject jsonObject = (JSONObject) args[0];
                try {
                    userNameNodeJS = jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Data", userNameNodeJS);

            }


        });


        socketIO.send("Hello Server");


        userName = (TextView) findViewById(R.id.username);


        Log.d("connectionAfter2", "connectionAfter2");


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        userName.setText(userNameNodeJS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_socket_io, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
