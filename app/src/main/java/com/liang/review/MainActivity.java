package com.liang.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.liang.review.beans.NewsBusEvent;
import com.liang.review.network.NetApi;
import com.liang.review.network.RetrofitFactory;
import com.liang.review.ui.main.SecondActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        RecyclerView.Recycler
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsBusEvent newsBusEvent = new NewsBusEvent();
                newsBusEvent.setTitle("China will");
                newsBusEvent.setMsg("China will the tokoyo olmpic games chaimpins");
                EventBus.getDefault().postSticky(newsBusEvent);
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
                HashMap hashMap =new HashMap(4);


            }
        });
    }

    private static <T> ObservableTransformer<T,T> io_main(){
        return observable->observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}