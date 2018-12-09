package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.CheckPackageParam;
import com.moreclub.moreapp.ucenter.model.PackageCheck;
import com.moreclub.moreapp.ucenter.model.PackageCheckItem;

/**
 * Created by Captain on 2017/7/18.
 */
@Implement(PackageCheckListLoader.class)
public interface IPackageCheckListLoader {


    void packageCheckList(String mid,String status,String pageNum,String pageSize);
    void onCheckPackage(CheckPackageParam param);


    interface LoaderPackageCheckListDataBinder extends DataBinder{

        void onPackageCheckResponse(RespDto<PackageCheck> response);
        void onPackageCheckFailure(String msg);

        void onCheckResponse(RespDto<PackageCheckItem> response);
        void onCheckFailure(String msg);

    }
}
