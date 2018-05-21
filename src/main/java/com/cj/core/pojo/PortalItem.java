package com.cj.core.pojo;

/**
 * TbItem的子类.
 * 主要是为了重载 TbItem 类中的image字段.
 * 因为页面上需要的字段名是images而不是image.
 * 同时由于 TbItem 类不在本包中. 所以通过扩展的方式实现.
 * 2018/5/16
 * @author cj
 */
public class PortalItem extends TbItem{

    //TbItem父类的所有字段和方法,都自动拥有了.

    //需要添加一个getter方法. 前端的"${item.images}"会执行这个方法.
    public String[] getImages() {
        //用继承自父类的方法,获取本商品的图片(在图片服务器的地址).
        //这个方法,取到的图片地址,不是一个图片的!
        //记得表中图片地址的形式么! 是所有图片的地址,全写在一起的!用逗号分隔!
        //所以这里取到的,应该是所有图片的地址.以逗号分隔的.
        String images = this.getImage();

        //先判断,. 判断这个商品对象有没有图片!
        //记得这种判断要判断两个! 不等于空,以及不equals空串!!
        if(images!=null && !images.equals("")) {
            //切出每一张图片的地址出来.
            //注意,我必须得把每个图片的地址一切出来! 把他们放进数组里,再把数组返给jsp.
            //否则,带了一堆逗号的地址,一长串,前端jsp肯定解析不出来!!
            String[] imgs = images.split(",");
            //返回这切出来的每一张独立的图片地址即可.
            return imgs;
        }
        //如果本商品没图片,直接返回空即可.
        return null;
    }
}
