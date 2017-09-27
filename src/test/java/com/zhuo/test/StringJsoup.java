package com.zhuo.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class StringJsoup {

	public static void main(String[] args) {
		// 解析一个完整的html
		String html = "<html><head><title>First parse</title></head>"
				  + "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html,"http://www.baidu.com");
		//System.out.println(doc);
		
		// 解析一个html片段
		String html2 = "<div><p>Lorem ipsum.</p>";
		Document doc2 = Jsoup.parseBodyFragment(html2);
		Element body = doc2.body();
		//System.out.println(body);
		
		
		// 解析一个URL
		try {
			Document doc3 = Jsoup.connect("http://www.news.cn/tech/index.htm")
					  .data("query", "Java")
					  .userAgent("Mozilla")
					  .cookie("auth", "token")
					  .timeout(3000)
					  .get();
			System.out.println(doc3.title());
			Elements h2 = doc3.select("div.swiper-wrapper > div.swiper-slide");
			for (Element element : h2) {
				System.out.println(element.getElementsByTag("img").first().attr("abs:src"));
				System.out.println(element.getElementsByTag("p").first().text());
				System.out.println(element.getElementsByTag("a").first().attr("href"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 解析一个文件
		
		/*File in = new File("/tmp/a.html");
		try {
			Document doc4 = Jsoup.parse(in, "UTF_8","http://example.com");
			//System.out.println(doc4);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	@Test
	public void testDocu() throws IOException {
		Document doc = Jsoup.connect("http://www.open-open.com").get();
		Element link = doc.select("a").get(2);
		String relHref = link.attr("href"); // == "/"
		String absHref = link.attr("abs:href"); // "http://www.open-open.com/"
		System.out.println(relHref);
		System.out.println(absHref);
	}
}
