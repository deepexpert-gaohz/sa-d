package com.ideatech.ams.dto.OutEquity;

import java.util.ArrayList;
import java.util.List;

public class Children1 {


    private String name;
    private String type;

    private Integer layer;
    private Float  percent;
    private String capital;
    private String parent;
    private List<Children1> item= new ArrayList<>();

    @Override
    public String toString() {
        return "Children1{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", layer='" + layer + '\'' +
                ", percent='" + percent + '\'' +
                ", capital='" + capital + '\'' +
                ", parent='" + parent + '\'' +
                ", item=" + item +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }


    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<Children1> getItem() {
        return item;
    }

    public void setItem(List<Children1> item) {
        this.item = item;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }
}
