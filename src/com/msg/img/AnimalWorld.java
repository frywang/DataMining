package com.msg.img;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Project Name: Data Author: MSG Time: 2016年9月2日 下午3:27:35
 */

public class AnimalWorld extends ImageIO
{

	public void getUrl()
	{
		String topUrl = "http://www.iltaw.com/animal/photo/";
		int i = 0;
		while (i <= 1200) {
			i++;
			String url = topUrl + i;
			// System.out.println(url);
			Document doc = null;
			try {
				doc = Jsoup.connect(url).userAgent("Mozilla").timeout(10000).get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (null == doc) {
				System.out.println(url + "打开出错啦");
				continue;
			}

			Element title = doc.select("div.title").first();
			if (null == title) {
				System.out.println(url + "错误了");
				continue;
			}
			// System.out.println(title.text());
			String animalname = title.text();
			/*
			 * for(Element li: lis){ System.out.println(li.text());
			 * System.out.println(li.attr("href")); }
			 */
			Elements photos = doc.select("div.item-t").select("div.img").select("a");
			for (Element photo : photos) {
				// System.out.println(photo.attr("href"));
				// System.out.println(photo.attr("title"));
				String strUrl = photo.attr("href");
				String filename = photo.attr("title");

				String temp = downloadImage(strUrl, filename + ".jpg", "G:\\动物世界\\" + animalname);
				if (null == temp) {
					System.out.println(filename + ".jpg  下载失败");
					continue;
				} else {
					System.out.println(filename + "下载成功");
				}
			}
			/*
			 * try { Thread.sleep(50); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */

		}
	}
}
