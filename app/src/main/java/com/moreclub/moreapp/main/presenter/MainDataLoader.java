package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.ActivityData;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.HeadlineCount;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantCount;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.PackageResp;
import com.moreclub.moreapp.main.model.SignInterUser;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/2/23.
 */

public class MainDataLoader extends BasePresenter<IMainDataLoader.LoadMainDataBinder>
        implements IMainDataLoader {

    @Override
    public void onLoadBizArea(String city) {
        Callback callback = new Callback<RespDto<List<BizArea>>>() {
            @Override
            public void onResponse(Call<RespDto<List<BizArea>>> call,
                                   Response<RespDto<List<BizArea>>> response) {
                if (response.body().isSuccess())
                    if (getBinder() != null) {
                        getBinder().onBizAreaResponse(response.body());
                    }
            }

            @Override
            public void onFailure(Call<RespDto<List<BizArea>>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onBizAreaFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadBizArea(city).enqueue(callback);
    }

    @Override
    public void onLoadFeatureTag() {
        Callback callback = new Callback<RespDto<Map<Integer, TagDict>>>() {
            @Override
            public void onResponse(Call<RespDto<Map<Integer, TagDict>>> call,
                                   Response<RespDto<Map<Integer, TagDict>>> response) {
                if (response.body().isSuccess()) {
                    RespDto<Map<Integer, TagDict>> respDto = response.body();
                    RespDto<List<TagDict>> listRespDto = new RespDto<>();
                    listRespDto.setSuccess(respDto.isSuccess());
                    listRespDto.setData(new ArrayList(respDto.getData().values()));

                    if (getBinder() != null)
                        getBinder().onFeatureTagResponse(listRespDto);
                }
            }

            @Override
            public void onFailure(Call<RespDto<Map<Integer, TagDict>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onFeatureTagFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadBarFeature().enqueue(callback);
    }

    @Override
    public void onLoadBanner(int cityId, String pos, int type) {
        Callback callback = new Callback<RespDto<List<BannerItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<BannerItem>>> call,
                                   Response<RespDto<List<BannerItem>>> response) {
                if (response != null && response.body() != null && response.body().isSuccess())
                    if (getBinder() != null)
                        getBinder().onBannerResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<List<BannerItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onBannerFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadBanner(cityId, pos, type).enqueue(callback);
    }

    @Override
    public void onLoadMerchants(String city, int page, int size) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onMerchantsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantsFailure(t.getMessage());
            }
        };

        if (MoreUser.getInstance().getUid()!=null&&MoreUser.getInstance().getUid()>0){
            ApiInterface.ApiFactory.createApi().onLoadMainMerchants(city, page, size,MoreUser.getInstance().getUid()).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi().onLoadMainMerchantsNoUid(city, page, size).enqueue(callback);
        }
    }

    @Override
    public void onLoadPkgs(String city, long uid, int page, int size) {
        Callback callback = new Callback<RespDto<PackageResp>>() {
            @Override
            public void onResponse(Call<RespDto<PackageResp>> call,
                                   Response<RespDto<PackageResp>> response) {
                if (getBinder() != null) getBinder().onPkgResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<PackageResp>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPkgFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadRecentPackages(city, uid, page, size).enqueue(callback);
    }

    @Override
    public void onLoadHeadlineCount() {
        Callback<RespDto<HeadlineCount>> callback = new Callback<RespDto<HeadlineCount>>() {
            @Override
            public void onResponse(Call<RespDto<HeadlineCount>> call, Response<RespDto<HeadlineCount>> response) {
                if (getBinder() != null && response != null)
                    getBinder().onHeadlineCountResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onHeadlineCountFailure("获取失败");
            }

            @Override
            public void onFailure(Call<RespDto<HeadlineCount>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onHeadlineCountFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadHeadlineCount().enqueue(callback);
    }

    @Override
    public void onLoadMerchantCount(String cityCode, Integer cityId) {
        Callback<RespDto<MerchantCount>> callback = new Callback<RespDto<MerchantCount>>() {
            @Override
            public void onResponse(Call<RespDto<MerchantCount>> call, Response<RespDto<MerchantCount>> response) {
                if (getBinder() != null && response != null)
                    getBinder().onMerchantCountResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onMerchantCountFailure("获取失败");
            }

            @Override
            public void onFailure(Call<RespDto<MerchantCount>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantCountFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadMerchantCount(cityCode, cityId).enqueue(callback);
    }

    @Override
    public void onLoadLiveList(String cityCode, Integer pn, Integer ps, Integer type, Long uid) {
        Callback<RespDto<ActivityData>> callback = new Callback<RespDto<ActivityData>>() {
            @Override
            public void onResponse(Call<RespDto<ActivityData>> call, Response<RespDto<ActivityData>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onGetLiveListResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onGetLiveListFaiure(response.body().getErrorDesc());
                        } else {
                            getBinder().onGetLiveListFaiure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ActivityData>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetLiveListFaiure(t.getMessage());
            }
        };
        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi().onLoadActivitiesList(cityCode, pn, ps, type, uid).enqueue(callback);
    }

    @Override
    public void onLoadMainChannelList(Long uid,Integer city, Integer page, Integer pageSize) {

        Callback<RespDto<ArrayList<MainChannelItem>>> callback = new Callback<RespDto<ArrayList<MainChannelItem>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<MainChannelItem>>> call, Response<RespDto<ArrayList<MainChannelItem>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onGetChannelListResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onGetChannelListFaiure(response.body().getErrorDesc());
                        } else {
                            getBinder().onGetChannelListFaiure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<MainChannelItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetChannelListFaiure(t.getMessage());
            }
        };
        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi().
                onLoadChannelList(uid,city, page, pageSize).enqueue(callback);

    }

    @Override
    public void onLoadSignInterUser(String city) {

        Callback<RespDto<ArrayList<SignInterUser>>> callback = new Callback<RespDto<ArrayList<SignInterUser>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<SignInterUser>>> call, Response<RespDto<ArrayList<SignInterUser>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onGetSignInterResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onGetSignInterFaiure(response.body().getErrorDesc());
                        } else {
                            getBinder().onGetSignInterFaiure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<SignInterUser>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetSignInterFaiure(t.getMessage());
            }
        };


        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi().
                onSignInter(city).enqueue(callback);


    }

}


