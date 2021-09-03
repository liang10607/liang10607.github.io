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

import com.liang.review.ClientApplication;
import com.liang.review.R;
import com.liang.review.beans.CloneTest;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private Handler subHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClientApplication.getRefWatcher(getActivity()).watch(this);
    }

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
        LinkedList linkedList = new LinkedList();
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}