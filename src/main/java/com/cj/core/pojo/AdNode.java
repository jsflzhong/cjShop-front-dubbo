package com.cj.core.pojo;

/**
 * 封装前端轮播图所需要的JSON格式的数据.
 * @author 崔健
 * @date 2016年10月7日下午10:46:45
 */
public class AdNode {
	
	//以下字段,要从前端所需要的JSON数据格式里分析.
	//从HiJSON软件里分析.挨个数据对比着写.
	//与数据库没关系.
	
	private int height; //"height": 240, 整型,没引号,int即可,图片尺寸不可能太大.
	
	private int width;//同理
	
	private String src;// "src": "http://image.taotao.com/images/2015/03/03/2015030304360302109345.jpg",
	
	private int heightB;//图片有两套.
	
	private int widthB;//图片有两套.
	
	private String srcB;//图片有两套.
	
	private String alt;//"alt": "", 有引号.
	
	private String href;// "href": "http://sale.jd.com/act/e0FMkuDhJz35CNt.html?cpdad=1DLSUE",

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public int getHeightB() {
		return heightB;
	}

	public void setHeightB(int heightB) {
		this.heightB = heightB;
	}

	public int getWidthB() {
		return widthB;
	}

	public void setWidthB(int widthB) {
		this.widthB = widthB;
	}

	public String getSrcB() {
		return srcB;
	}

	public void setSrcB(String srcB) {
		this.srcB = srcB;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	
}
