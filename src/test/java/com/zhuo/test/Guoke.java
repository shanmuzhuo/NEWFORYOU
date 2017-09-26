package com.zhuo.test;


/**
 * @author zzx
 * @time 2017年9月22日
 * @desc 果壳网类
 */
public class Guoke {

	public String title; // 文章标题
	public String link; // 文章链接
	public String content; // 文章内容
	
	public Guoke() {
		title = "";
		link = "";
		content = "";
	}
	
	public String writeString() {  
        String result = "";  
        result += "###标题：" + title + "\r\n";  
        result += "####链接：" + link + "\r\n";  
        result += "内容：" + content + "\r\n";  
        return result;  
}  

	
	@Override
	public String toString() {
		  return "标题：" + title + "\n链接：" + link + "\n内容：" + content + "\n";  
	}
	
}
