package com.zhuo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ant  {
	
	public static  String sendGet(String URL){
		String result = ""	;
		BufferedReader reader = null;
		InputStream in = null;
		URL url;
		try {
			url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
			 //设置超时间为3秒   
	        conn.setConnectTimeout(5*1000);  
	        //防止屏蔽程序抓取而返回403错误   
	        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"); 	
	        in = conn.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(in));
	        String line; 
	        while((line = reader.readLine()) != null){
	        	result += line;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
        	try {
        		if (reader != null){
        			reader.close();
        		}
				if (in != null){
		        	in.close();
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		return result;
	}
	
	public static List<Guoke> RegexString(String targetStr) { 
		List<Guoke> results = new ArrayList<>();
        // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容   
        Pattern pattern = Pattern.compile("stress.+?h2>.+?>(.+?)<");  
        // 定义一个matcher用来做匹配   
        Matcher matcher = pattern.matcher(targetStr);  
        
        Pattern pattern2 = Pattern.compile("stress.+?<h2><a href=\"(.+?)\"");
        Matcher matcher2 = pattern2.matcher(targetStr);
        // 如果找到了   
        boolean isFind = matcher.find() && matcher2.find();  
        while (isFind) {  
            //添加成功匹配的结果   
        	Guoke guoke = new Guoke();
        	guoke.title = matcher.group(1);
        	guoke.link = matcher2.group(1);
        	if(null != guoke.link) {
        		System.out.println("正在抓取："+guoke.link);
        		String content = sendGet(guoke.link);  
        		//System.out.println(content);
        		guoke.content = String2Md(content);
        	}
        	
            results.add(guoke);  
            // 继续查找下一个匹配对象   
            isFind = matcher.find() && matcher2.find();  
        }  
        return results;  
    }  
  
	
	/**
	 * 将果壳的网的内容转为md格式
	 * @param result
	 * @return
	 */
	public static String String2Md(String result) {
		String line ="";
		Pattern p = Pattern.compile("post-detail gbbcode-content\">(.+?)</div>");
		Matcher matcher = p.matcher(result);
		boolean isFind = matcher.find() ;
		if(isFind && matcher.group(1).trim().length() > 0) {
//			System.out.println(matcher.group(1).trim());
			String cont = matcher.group(1).trim();
			String aa[] = cont.split("</p>");
			for (String string : aa) {
//				System.out.println(string);
				Pattern p2 = Pattern.compile("data-image=\"(.+?)\"");
				Matcher matcher2 = p2.matcher(string);
				boolean flag = matcher2.find();
				if (flag) {
					line += "![]("+matcher2.group(1)+")\r\n\n";
				}else {
					Pattern p3 = Pattern.compile("<p>(.+)");
					Matcher matcher3 = p3.matcher(string);
					boolean flag2 = matcher3.find();
					if (flag2) {
						line += matcher3.group(1)+"\r\n\n";
					}
				}
			}
			//System.err.println(line);
		}
		return line;
	}

	public static void main(String[] args) {
		String url = "http://www.guokr.com/";  
        String result = sendGet(url);  
       // System.out.println(result);
        List<Guoke> imgSrc = RegexString(result); 
        int aa = 0;
        // 打印结果   
        for (Guoke object : imgSrc) {
        	try {
				FileReaderWriter.writeIntoFile(object.writeString(),  
				        "F:/ANT/guoke/"+object.title+".md", true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }
	}
}
