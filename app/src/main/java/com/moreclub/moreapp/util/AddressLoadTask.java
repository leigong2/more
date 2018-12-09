package com.moreclub.moreapp.util;

import android.content.Context;
import android.os.AsyncTask;

import com.moreclub.moreapp.ucenter.model.AddressItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Captain on 2017/3/14.
 */

public class AddressLoadTask extends AsyncTask<Void,Void,ArrayList<AddressItem>> {

    Context mContext;
    ParseAddressListener parseAddressListener;
    public AddressLoadTask(Context c,ParseAddressListener listener){
        mContext = c;
        parseAddressListener = listener;
    }

    @Override
    protected ArrayList<AddressItem> doInBackground(Void... params) {

        try {
            InputStreamReader isr = new InputStreamReader(mContext.getAssets().open("areas.json"),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
            return parseJson(testjson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<AddressItem> listObject) {
        super.onPostExecute(listObject);
        if (parseAddressListener!=null){
            parseAddressListener.parseAddressSuccess(listObject);
        }
    }


    public interface ParseAddressListener{
        void parseAddressSuccess(ArrayList<AddressItem> list);
        void parseAddressFails();
    }

    private ArrayList<AddressItem> parseJson(JSONObject json){
        //直接传入JSONObject来构造一个实例
        try{
            Iterator<String> iterator = json.keys();         //从JSONObject中取出数组对象
            ArrayList<AddressItem> array = new ArrayList<AddressItem>();
            while (iterator.hasNext())
            {
                String key = (String) iterator.next();
                AddressItem item= new AddressItem();
                ArrayList<AddressItem> subArray = new ArrayList<AddressItem>();
                item.setAddressName(key);
                item.setSubAddressArray(subArray);
                array.add(item);

                JSONObject subjson= json.getJSONObject(key);
                Iterator<String> subiterator = subjson.keys();
                while (subiterator.hasNext()){
                    String subkey = (String) subiterator.next();
                    JSONArray threejson= subjson.getJSONArray(subkey);
                    AddressItem subitem= new AddressItem();
                    subitem.setAddressName(subkey);
                    ArrayList<AddressItem> threeArray = new ArrayList<AddressItem>();
                    subitem.setSubAddressArray(threeArray);

                    for(int i=0;i<threejson.length();i++){
                        AddressItem threeitem= new AddressItem();
                        threeitem.setAddressName(threejson.getString(i));
                        threeArray.add(threeitem);
                    }

                    subArray.add(subitem);


                    System.out.println(subkey);
                }

            }
            return array;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
