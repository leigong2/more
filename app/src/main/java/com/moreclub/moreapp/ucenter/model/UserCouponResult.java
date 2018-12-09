package com.moreclub.moreapp.ucenter.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/6/23.
 */

public class UserCouponResult {

    public ArrayList<UserCoupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<UserCoupon> coupons) {
        this.coupons = coupons;
    }

    public int getUsable() {
        return usable;
    }

    public void setUsable(int usable) {
        this.usable = usable;
    }

    private ArrayList<UserCoupon> coupons;
    private int usable;

//    {
//        "data":{
//        "coupons":[{
//            "cid":31, "couponModal":21, "originate":1, "name":"more福利！回馈广大用户！", "modalName":
//            "节日礼遇券", "promotionType":1, "type":5, "parValue":50.00, "mustConsumption":
//            0.00, "superposition":true, "status":4, "avlStartDateTime":1498185900, "avlEndDateTime":
//            1498272300, "rid":4901, "balance":1, "locked":false
//        },{
//            "cid":22, "couponModal":21, "originate":1, "name":"more福利！回馈广大用户！", "modalName":
//            "节日礼遇券", "promotionType":1, "type":5, "parValue":50.00, "mustConsumption":
//            0.00, "superposition":true, "status":4, "avlStartDateTime":1498113480, "avlEndDateTime":
//            1498286340, "rid":4874, "balance":1, "locked":false
//        },{
//            "cid":33, "couponModal":24, "originate":1, "name":"直减券，不叠加黑橙卡", "modalName":
//            "more券", "promotionType":1, "type":1, "parValue":49.00, "mustConsumption":
//            0.00, "superposition":false, "status":4, "avlStartDateTime":
//            1498186020, "avlEndDateTime":1498272420, "rid":4913, "balance":1, "locked":false
//        },{
//            "cid":24, "couponModal":23, "originate":1, "name":"消费200减80", "modalName":
//            "more券", "promotionType":2, "type":1, "parValue":80.00, "mustConsumption":
//            200.00, "superposition":true, "status":5, "avlStartDateTime":
//            1498115400, "avlEndDateTime":1498201800, "rid":4884, "balance":1, "locked":false
//        },{
//            "cid":35, "couponModal":26, "originate":1, "name":"优惠券一波接一波，不叠加特权！", "modalName":
//            "more券", "promotionType":1, "type":1, "parValue":59.00, "mustConsumption":
//            0.00, "superposition":false, "status":5, "avlStartDateTime":
//            1498200300, "avlEndDateTime":1498201800, "rid":4925, "balance":1, "locked":false
//        },{
//            "cid":39, "couponModal":25, "originate":1, "name":"more直减券又一波，叠加特权卡！", "modalName":
//            "more券", "promotionType":1, "type":1, "parValue":51.00, "mustConsumption":
//            0.00, "superposition":true, "status":5, "avlStartDateTime":1498203463, "avlEndDateTime":
//            1498204785, "rid":4934, "balance":1, "locked":false
//        },{
//            "cid":34, "couponModal":25, "originate":1, "name":"more直减券又一波，叠加特权卡！", "modalName":
//            "more券", "promotionType":1, "type":1, "parValue":51.00, "mustConsumption":
//            0.00, "superposition":true, "status":5, "avlStartDateTime":1498200120, "avlEndDateTime":
//            1498201200, "rid":4919, "balance":1, "locked":false
//        },{
//            "cid":25, "couponModal":21, "originate":1, "name":"more福利！回馈广大用户！", "modalName":
//            "节日礼遇券", "promotionType":1, "type":5, "parValue":50.00, "mustConsumption":
//            0.00, "superposition":true, "status":5, "avlStartDateTime":1498127040, "avlEndDateTime":
//            1498213440, "rid":4889, "balance":1, "locked":false
//        },{
//            "cid":23, "couponModal":22, "originate":1, "name":"消费满100减39！", "modalName":
//            "more券", "promotionType":2, "type":1, "parValue":39.00, "mustConsumption":
//            100.00, "superposition":false, "status":5, "avlStartDateTime":
//            1498115280, "avlEndDateTime":1498201680, "rid":4879, "balance":1, "locked":false
//        },{
//            "cid":21, "couponModal":20, "originate":1, "name":"新用户专享直减券20元，下单立减！", "modalName":
//            "新用户专享券", "promotionType":1, "type":2, "parValue":20.00, "mustConsumption":
//            0.00, "superposition":false, "status":5, "avlStartDateTime":
//            1498101600, "avlEndDateTime":1498187940, "rid":4869, "balance":1, "locked":false
//        }],"usable":3
//    },"errorCode":null, "errorDesc":null, "elapsedMilliseconds":7, "success":true
//    }





}
