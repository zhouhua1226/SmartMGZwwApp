package com.jianguo.timedialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jianguo.timedialog.config.PickerConfig;
import com.jianguo.timedialog.data.Type;
import com.jianguo.timedialog.listener.OnAddressSetListener;
/**
 * Created by 22077 on 2017/12/6.
 */
public class AddressDialog extends DialogFragment implements View.OnClickListener {
    PickerConfig mPickerConfig;
    private AddressWheel mAddressWheel;

    private static AddressDialog newIntance(PickerConfig pickerConfig) {
        AddressDialog addressDialog = new AddressDialog();
        addressDialog.initialize(pickerConfig);
        return addressDialog;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    private void initialize(PickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;
    }
    @Override
    public void onResume() {
        super.onResume();
        int height = getResources().getDimensionPixelSize(R.dimen.picker_height);

        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);//Here!
        window.setGravity(Gravity.BOTTOM);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_NoTitle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(initView());
        return dialog;
    }

    View initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.address_dialog_lauout, null);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        sure.setOnClickListener(this);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        View toolbar = view.findViewById(R.id.toolbar);

        title.setText(mPickerConfig.mTitleString);
        cancel.setText(mPickerConfig.mCancelString);
        sure.setText(mPickerConfig.mSureString);
        toolbar.setBackgroundColor(mPickerConfig.mThemeColor);

        mAddressWheel = new AddressWheel(view, mPickerConfig);
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss();
        } else if (i == R.id.tv_sure) {
            sureClicked();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    /*
        * @desc This method is called when onClick method is invoked by sure button. A Calendar instance is created and
        *       initialized.
        * @param none
        * @return none
        */
    void sureClicked() {

        if (mPickerConfig.mAddressBack != null) {
            mPickerConfig.mAddressBack.onAddressDateSet(this, mAddressWheel.mCurrentProviceName,
                    mAddressWheel.mCurrentCityName,mAddressWheel.mCurrentDistrictName);
        }

        dismiss();
    }

    public static class Builder {
        PickerConfig mPickerConfig;

        public Builder() {
            mPickerConfig = new PickerConfig();
        }

        public AddressDialog.Builder setType(Type type) {
            mPickerConfig.mType = type;
            return this;
        }

        public AddressDialog.Builder setThemeColor(int color) {
            mPickerConfig.mThemeColor = color;
            return this;
        }

        public AddressDialog.Builder setCancelStringId(String left) {
            mPickerConfig.mCancelString = left;
            return this;
        }

        public AddressDialog.Builder setSureStringId(String right) {
            mPickerConfig.mSureString = right;
            return this;
        }

        public AddressDialog.Builder setTitleStringId(String title) {
            mPickerConfig.mTitleString = title;
            return this;
        }

        public AddressDialog.Builder setToolBarTextColor(int color) {
            mPickerConfig.mToolBarTVColor = color;
            return this;
        }

        public AddressDialog.Builder setWheelItemTextNormalColor(int color) {
            mPickerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public AddressDialog.Builder setWheelItemTextSelectorColor(int color) {
            mPickerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public AddressDialog.Builder setWheelItemTextSize(int size) {
            mPickerConfig.mWheelTVSize = size;
            return this;
        }

        public AddressDialog.Builder setCyclic(boolean cyclic) {
            mPickerConfig.cyclic = cyclic;
            return this;
        }


        public AddressDialog.Builder setProviceText(String provice) {
            mPickerConfig.mProvice = provice;
            return this;
        }

        public AddressDialog.Builder setCityText(String city) {
            mPickerConfig.mCity = city;
            return this;
        }

        public AddressDialog.Builder setCountyText(String county) {
            mPickerConfig.mCity = county;
            return this;
        }


        public AddressDialog.Builder setAddressBack(OnAddressSetListener listener) {
            mPickerConfig.mAddressBack = listener;
            return this;
        }

        public AddressDialog build() {
            return newIntance(mPickerConfig);
        }
      }
    }

