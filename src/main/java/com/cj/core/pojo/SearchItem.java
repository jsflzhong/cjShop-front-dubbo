package com.cj.core.pojo;

import org.apache.commons.lang3.StringUtils;

public class SearchItem {

    //注意,字段名,要对应SQL语句中,要查的字段名.如果有别名,就按别名写.
    //注意,字段类型,要看shema.xml文件中,自己配域时,给的类型.
    private String id;
    private String title;
    private String sell_point;
    private long price;
    private String image;
    private String category_name;
    private String item_desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        if(StringUtils.isNotEmpty(image)) {
            String[] split = image.split(",");
            return split[0];
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_des) {
        this.item_desc = item_des;
    }

}
