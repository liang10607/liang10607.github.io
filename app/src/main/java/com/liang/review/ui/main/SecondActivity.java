package com.liang.review.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liang.review.R;
import com.liang.review.beans.NewsBusEvent;
import com.liang.review.network.NetApi;
import com.liang.review.network.RetrofitFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().register(this);
        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ImageView imageView = findViewById(R.id.img_net);
        Glide.with(this).asBitmap().load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201212%2F25%2F20121225175020_UiGcc.thumb.700_0.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631674953&t=a732e8be7181f1fba61b5047f1733590")
                .into(imageView);

        Glide.get(this).clearDiskCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true,priority = 100)
    public void  onEventArrive(NewsBusEvent event){
        System.out.println("EventBus msg arrived:"+event.getTitle()+"   MSG:"+event.getMsg());
    }
}