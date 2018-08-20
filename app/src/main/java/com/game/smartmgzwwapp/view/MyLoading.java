package com.game.smartmgzwwapp.view;

import com.ipaynow.plugin.view.IpaynowLoading;

/**
 * 定制化Loading框
 *  现在支付
 * @author
 *
 */
public class MyLoading implements IpaynowLoading {
    private IpaynowLoading mLoading;
    public MyLoading(IpaynowLoading loading) {
        this.mLoading = loading;
    }

    @Override
    public void dismiss() {
        mLoading.dismiss();
    }
    @Override
    public boolean isShowing() {
        return mLoading.isShowing();
    }
    @Override
    public void setLoadingMsg(String arg0) {
        if (!"正在查询交易结果...".equals(arg0)&&!"正在退出QQ支付".equals(arg0))
            mLoading.setLoadingMsg("正在生成订单");
        else
            mLoading.setLoadingMsg(arg0);
    }
    @Override
    public Object show() {
        mLoading.show();
        return this;
    }
}
