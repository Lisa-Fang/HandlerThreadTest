package com.lf.handlerthreadtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Button;

public class MainActivity extends Activity {
	public Button handlerThreadBTN;
	HandlerThread handlerThread;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		handlerThread = new HandlerThread("main");
		handlerThread.start();

		handler = new Handler(handlerThread.getLooper(), new Callback() {

			@Override
			public boolean handleMessage(Message msg) {// 这里可以做一些耗时的操作，是handlerThread的子线程
				System.out.println("receive message.whatA=" + msg.what);
				if (msg.what == 1) {
					return true;// 不再向外层传递
				} else {
					return false; // 外层的handleMessage() 继续执行
				}
			}
		}) {
			@Override
			public void handleMessage(Message msg) {// 这里是ui主线程
				super.handleMessage(msg);
				System.out.println("receive message.whatB=" + msg.what);
			}
		};

		handler.post(new Runnable() {

			@Override
			public void run() {
				System.out.println("handler_post_cur_id="
						+ Thread.currentThread().getId());
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handlerThread.quit();
	}

}
