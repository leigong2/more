package com.moreclub.moreapp.ucenter.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/3/13.
 */

public class AddressItem  {

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public int getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(int addressCode) {
        this.addressCode = addressCode;
    }

    private String addressName;
    private int addressCode;

    public ArrayList<AddressItem> getSubAddressArray() {
        return subAddressArray;
    }

    public void setSubAddressArray(ArrayList<AddressItem> subAddressArray) {
        this.subAddressArray = subAddressArray;
    }

    private ArrayList<AddressItem> subAddressArray;
}
