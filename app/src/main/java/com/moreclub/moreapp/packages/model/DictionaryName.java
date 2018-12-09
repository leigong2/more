package com.moreclub.moreapp.packages.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class DictionaryName {

    private String name;
    private Integer id;
    private String ext;
    private List<DictionaryName> dics;
    private boolean select = false;


    public DictionaryName(){}
    public DictionaryName(String name,Integer id){
        this.name = name;
        this.id = id;
    }
    public DictionaryName(String name,Integer id,String ext){
        this.name = name;
        this.id = id;
        this.ext = ext;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DictionaryName> getDics() {
        return dics;
    }

    public void setDics(List<DictionaryName> dics) {
        this.dics = dics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "DictionaryName{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", ext='" + ext + '\'' +
                ", dics=" + dics +
                '}';
    }
}
