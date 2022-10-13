package com.llthx.netunitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.llthx.netunit.NetUnitClient;
import com.llthx.netunit.NetUnitInterface;
import com.llthx.netunit.NetUnitResponseCallback;
import com.llthx.netunit.OkhttpUnit;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
private NetUnitInterface mNetUnit;
private Handler mHandler = new Handler(Looper.getMainLooper()){
    @Override
    public void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);
        Toast.makeText(getApplicationContext(),msg.obj.toString(), Toast.LENGTH_LONG).show();
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetUnit = NetUnitClient.getNetUnitClient(new OkhttpUnit(getApplicationContext()));

        if (!mNetUnit.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mNetUnit.getPermissions(),1000);
            }
        }
    }

    public void getMsg(View view) {
        new Thread(()->{
            String result = mNetUnit.GET("http://192.168.31.245:8888/getMsg");
            //String result1 = mNetUnit.GET("https://192.168.31.245:8888/getMsg");
            mNetUnit.GET("http://192.168.31.245:8888/getMsg", new NetUnitResponseCallback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFailure(String reason) {

                }

                @Override
                public void onSuccess(String json) {
                    Message message = new Message();
                    message.obj = json;
                    mHandler.sendMessageDelayed(message,3000);
                }

                @Override
                public void onEnd() {

                }
            });
            Message message = new Message();
            message.obj = result;
            mHandler.sendMessage(message);
        }).start();
    }
}
