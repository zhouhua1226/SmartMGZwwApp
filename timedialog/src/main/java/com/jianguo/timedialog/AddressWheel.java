package com.jianguo.timedialog;

import android.content.Context;
import android.view.View;

import com.jianguo.timedialog.adapters.ArrayWheelAdapter;
import com.jianguo.timedialog.config.PickerConfig;
import com.jianguo.timedialog.data.source.AddressDataSoure;
import com.jianguo.timedialog.wheel.OnWheelChangedListener;
import com.jianguo.timedialog.wheel.WheelView;

/**
 * Created by 22077 on 2017/12/6.
 */

public class AddressWheel implements OnWheelChangedListener {
    Context mContext;
    WheelView wProvice, wCity, wCounty;
    PickerConfig mPickerConfig;
    AddressDataSoure mAddressSoure;
     String mCurrentProviceName,mCurrentCityName,mCurrentDistrictName;

    public AddressWheel(View view, PickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;
        mContext = view.getContext();
        mAddressSoure = new AddressDataSoure(mContext);

        initialize(view);
    }

    public void initialize(View view) {
        initView(view);

        setUpData();
    }

    void initView(View view) {
        wProvice = (WheelView) view.findViewById(R.id.provice);
        wCity = (WheelView) view.findViewById(R.id.city);
        wCounty = (WheelView) view.findViewById(R.id.county);
    }

    private void setUpData() {
        wProvice.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mAddressSoure.mProvinceDatas));
        // 设置可见条目数量
        wProvice.setVisibleItems(7);
        wCity.setVisibleItems(7);
        wCounty.setVisibleItems(7);

        updateCities();
        updateAreas();
        // 添加change事件
        wProvice.addChangingListener(this);
        // 添加change事件
        wCity.addChangingListener(this);
        // 添加change事件
        wCounty.addChangingListener(this);

        wProvice.setCyclic(mPickerConfig.cyclic);
        wCity.setCyclic(mPickerConfig.cyclic);
        wCounty.setCyclic(mPickerConfig.cyclic);
        wProvice.setConfig(mPickerConfig);

    }
    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = wProvice.getCurrentItem();
        mCurrentProviceName = mAddressSoure.mProvinceDatas[pCurrent];
        String[] cities =mAddressSoure.mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        wCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
        wCity.setCurrentItem(0);
        wCity.setConfig(mPickerConfig);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = wCity.getCurrentItem();
        mCurrentCityName =mAddressSoure.mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas =mAddressSoure.mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        wCounty.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
        wCounty.setCurrentItem(0);
        wCounty.setConfig(mPickerConfig);
        mCurrentDistrictName = areas[0];
    }
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == wProvice) {
            updateCities();

        } else if (wheel == wCity) {
            updateAreas();
        } else if (wheel == wCounty) {
            mCurrentDistrictName = mAddressSoure.mDistrictDatasMap.get(mCurrentCityName)[newValue];
        }
    }
}
