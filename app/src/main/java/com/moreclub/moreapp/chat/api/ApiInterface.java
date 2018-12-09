package com.moreclub.moreapp.chat.api;


import com.moreclub.common.api.RestApi;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.model.ChatGroupUser;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HxRoomDetails;
import com.moreclub.moreapp.ucenter.model.UserFollowParam;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    String BASE_URL = Constants.BASE_URL;

    @GET("/v1/group/getUsersInGroups")
    Call<RespDto<ArrayList<ChatGroupUser>>> onLoadChatGroupUser(@Query("gid") String gid, @Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize);

    @GET("/v1/group/getGroupIdByHxId/{roomid}")
    Call<RespDto<HxRoomDetails>> onLoadRoomHxDetails(@Path("roomid") String roomid);

    @HTTP(method = "DELETE", path = "/v1/group/removeSingleUserFromChatGroup", hasBody = true)
    Call<RespDto<Boolean>> onExitChatGroup(@Query("uid") String uid,@Query("gid") String gid);

    class ApiFactory {
        public static ApiInterface createApi() {
            return RestApi.getInstance().create(ApiInterface.class);
        }

        public static ApiInterface createApi(String token) {
            return RestApi.getInstance().create(ApiInterface.class, token);
        }
    }
}
