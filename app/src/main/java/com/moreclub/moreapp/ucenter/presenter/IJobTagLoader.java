package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserTag;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-18-0018.
 */


@Implement(JobTagDataLoader.class)
public interface IJobTagLoader {

    void loadTag(String modal);

    interface JobTagDataBinder extends DataBinder {

        void onTagResponse(RespDto<Map<String,List<UserTag>>> res);

        void onTagFailure(String msg);

    }
}
