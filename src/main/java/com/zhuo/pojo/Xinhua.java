package com.zhuo.pojo;

import java.io.Serializable;

public class Xinhua  implements Serializable {
	public String title; // 文章标题
	public String img; // 图片
	public String link; // 文章链接
	public String content; // 文章内容
	
	public Xinhua() {
		title = "";
		link = "";
		content = "";
		img = "";
	}
}
