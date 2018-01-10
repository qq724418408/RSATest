package com.forms.wjl.rsa.bean;

import java.util.List;

/**
 * Created by bubbly on 2018/1/10.
 */

public class Level1Bean {

    private String name;
    private List<Level2Bean> level2List;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Level2Bean> getLevel2List() {
        return level2List;
    }

    public void setLevel2List(List<Level2Bean> level2List) {
        this.level2List = level2List;
    }
}
