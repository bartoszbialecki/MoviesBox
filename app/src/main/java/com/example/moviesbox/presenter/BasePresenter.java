package com.example.moviesbox.presenter;

import com.example.moviesbox.view.BaseView;

public interface BasePresenter<V extends BaseView> {
    void start();
    void stop();
}
