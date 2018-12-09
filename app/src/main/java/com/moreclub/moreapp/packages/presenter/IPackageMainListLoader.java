package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.model.MerchantPackage;
import com.moreclub.moreapp.packages.model.PackageList;

import java.util.List;

/**
 * Created by Captain on 2017/3/23.
 */

@Implement(PackageMainListLoader.class)
public interface IPackageMainListLoader<T> {

    void packageListLoad(String chooseCity,String uid,String currentPage,String pageNumber);

    void loadMerchantPackage(String mid,String pn,String ps,String uid);

    interface LoaderPackageMainListDataBinder extends DataBinder{

        void onPackageListResponse(RespDto<PackageList> response);

        void onPackageListFailure(String msg);

        void onMerchantPackageResponse(RespDto<MerchantPackage> response);

        void onMerchantPackageFailure(String msg);
    }
}
