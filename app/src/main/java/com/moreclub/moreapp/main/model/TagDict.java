package com.moreclub.moreapp.main.model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */

public class TagDict {
    private String name;
    private Integer id;
    private String ext;
    private List<TagDict> dics;


    public TagDict(){}
    public TagDict(String name,Integer id){
        this.name = name;
        this.id = id;
    }
    public TagDict(String name,Integer id,String ext){
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

    public List<TagDict> getDics() {
        return dics;
    }

    public void setDics(List<TagDict> dics) {
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

    @Override
    public String toString() {
        return "TagDict{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", ext='" + ext + '\'' +
                ", dics=" + dics +
                '}';
    }
}
