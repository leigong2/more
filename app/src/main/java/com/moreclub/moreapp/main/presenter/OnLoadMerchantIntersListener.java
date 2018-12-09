package com.moreclub.moreapp.main.presenter;

import com.moreclub.moreapp.main.model.MerchantsSignInters;

import java.util.List;

/**
 * Created by Administrator on 2017-06-21.
 */

public interface OnLoadMerchantIntersListener {
    void onLoadResponse(String mid,List<MerchantsSignInters> signInterses);
}
