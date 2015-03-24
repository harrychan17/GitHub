package com.example.activities;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rapidnote.R;
import com.example.tools.Time_transform;

import java.io.IOException;


public class RecorderActivity extends Activity {
    TextView tv_recorder_time;
    ImageButton btn_recorder_click;
    MediaRecorder mediaRecorder;
    String record_path;
    Handler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        findViewById();
        setListener();
        init();
    }

    private void init() {
        record_path = getIntent().getExtras().getString("record_path");
        mediaRecorder = new MediaRecorder();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
              switch (msg.what){
                  case 0:
                      int old_time= Time_transform.toSecond(tv_recorder_time.getText().toString());
                      String new_time=Time_transform.toMinute_And_Second(old_time+1);
                      tv_recorder_time.setText(new_time);
                      handler.sendEmptyMessageDelayed(0, 1000);
                      break;
                  case 1:
                        handler.removeMessages(0);
                      break;
              }
            }
        };
    }

    private void findViewById() {
        btn_recorder_click = (ImageButton) findViewById(R.id.btn_recorder_click);
        btn_recorder_click.setBackgroundResource(R.drawable.btn_recorder_normal);
        tv_recorder_time = (TextView) findViewById(R.id.tv_recorder_time);
    }
    private void setListener() {
        btn_recorder_click.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == (MotionEvent.ACTION_DOWN)) {
                    startRecorder();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecorder();
                    finish();
                }
                return false;
            }
        });

    }

    private void stopRecorder() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        handler.sendEmptyMessageDelayed(1,200);
    }
    private void startRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(record_path);
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        tv_recorder_time.setVisibility(View.VISIBLE);
        new Thread(new Time_update(handler)).start();
    }

}

class Time_update implements Runnable {
    Handler handler;
    Time_update(Handler handler) {
        this.handler = handler;
    }
    public void run() {
        handler.sendEmptyMessageDelayed(0,1000);
    }
}