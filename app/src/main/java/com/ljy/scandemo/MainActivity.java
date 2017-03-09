package com.ljy.scandemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button startBtn;
    private ScanView scanView;
    private MyThread myThread;  //用于定时，扫描一段时间后就停止
    private boolean threadFlag = false;
    private boolean scanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        startBtn = (Button) findViewById(R.id.start_btn);
        scanView = (ScanView) findViewById(R.id.scan_view);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!scanning) {
                    myThread = new MyThread();
                    threadFlag = true;
                    myThread.start();
                    scanView.start();
                    startBtn.setText("扫描中");
                }
            }
        });
    }

    /**
     * 循环10秒则结束
     */
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            scanning = true;
            int times = 0;
            while (threadFlag) {
                try {
                    Thread.sleep(1000);
                    times ++;
                    Log.i(TAG, "run: times = "+times);
                    if (times >= 5){
                        scanning = false;
                        threadFlag = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scanView.stop();
                                startBtn.setText("开始");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
