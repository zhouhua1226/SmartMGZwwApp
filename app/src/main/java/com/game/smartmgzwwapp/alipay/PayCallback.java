package com.game.smartmgzwwapp.alipay;

import java.util.Map;

/**
 * des:支付返回结果
 * Created by cwj
 *
 */
public interface PayCallback {

    void payResult(Map<String, String> result);
}
