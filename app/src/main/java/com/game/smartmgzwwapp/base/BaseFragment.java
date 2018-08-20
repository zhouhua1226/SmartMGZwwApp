package com.game.smartmgzwwapp.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhouh on 2017/4/17.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;
    protected Unbinder unbinder;
    protected FragmentManager mfragmentManager;
    private LayoutInflater mInflater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            mInflater = inflater;
            rootView = inflater.inflate(getLayoutId(), container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mfragmentManager = getFragmentManager();
        afterCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);


}
