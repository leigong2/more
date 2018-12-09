package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.model.PackageDetailsInfo;
import com.moreclub.moreapp.packages.model.PackageList;

/**
 * Created by Captain on 2017/3/23.
 */
@Implement(PackageDetailsLoader.class)
public interface IPackageDetailsLoader<T> {

    void onLoadPackageDetails(String pid,String uid);

    interface loadPackageDetailsDataBinder<T> extends DataBinder{

        void onPackageDetailsResponse(RespDto<PackageDetailsInfo> response);

        void onPackageDetailsFailure(String msg);

    }
}
