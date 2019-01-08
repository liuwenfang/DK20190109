package com.example.administrator.dk;

/**
 * Created by urnotxx on 2018/10/24.
 */

public class BannerBean {

    /**
     * ID : 2
     * Url : www.baidu.com
     * Image : 7290ed43-1606-4a3c-b1e1-d54c657d8f10.png
     * IsShow : 1
     * Sort : 1
     */

    private int ID;
    private String Url;
    private String Image;
    private int IsShow;
    private int Sort;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public int getIsShow() {
        return IsShow;
    }

    public void setIsShow(int IsShow) {
        this.IsShow = IsShow;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }
}
