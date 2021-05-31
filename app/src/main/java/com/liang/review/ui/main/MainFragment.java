package com.liang.review.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liang.review.R;
import com.liang.review.beans.CloneTest;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private Handler subHandler;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CloneTest cloneTest =new CloneTest();
        try {
            CloneTest ss= (CloneTest) cloneTest.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        // 创建一个子线程，并在子线程中创建一个Handler，且重写handleMessage
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                subHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        // 处理消息
                        switch (msg.what) {
                            case MSG_MAIN_TO_SUB:
                                Log.e(TAG, "接收到消息： " +  Thread.currentThread().getName() + ","+ msg.obj);
                                break;
                            default:
                                break;
                        }
                    }
                };
                Looper.loop();
            }
        }).start();

        btnSendToSubThread = (Button) findViewById(R.id.btn_sendto_subthread);
        btnSendToSubThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                msg.what = MSG_MAIN_TO_SUB;
                msg.obj = "这是一个来自主线程的消息";
                // 主线程中发送消息
                subHandler.sendMessage(msg);
            }
        });

        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}