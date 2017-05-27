package com.msg.img;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Project Name: Data Author: MSG Time: 2016年9月2日 下午1:38:43
 */

public class Ivsky extends ImgIO
{

	/**
	 * 天堂图片网的图片下载
	 * 
	 * @return 返回保存了名字和路径的hashmap
	 */
	public Map<String, String> getUrls(String baseUrl)
	{
		Map<String, String> hmap = new HashMap<String, String>();

		String startUrl = "http://www.ivsky.com/tupian/zhiwuhuahui/";
		int i = 1;
		while (i < 165) {
			String url = startUrl + "index_" + i + ".html";
			Document doc = null;
			try {
				doc = Jsoup.connect(url).userAgent("Mozilla").timeout(10000).get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (null == doc) {
				continue;
			}
			Elements div = doc.select("ul.ali").select("p").select("a[href]");
			if (null != div) {
				for (Element e : div) {
					hmap.put(e.text(), baseUrl + e.attr("href"));
					// System.out.println(e.text() + " " +
					// "http://www.ivsky.com" + e.attr("href"));
				}
			}
			i++;
		}
		return hmap;
	}

	/**
	 * 下载所有的图片
	 * 
	 * @param baseUrl
	 *            网站的域名地址
	 */
	public void downloadAllImages(String baseUrl)
	{

		Map<String, String> hmap = getUrls(baseUrl);
		Iterator<String> key = hmap.keySet().iterator();

		while (key.hasNext()) {

			List<String> list = new ArrayList<String>();
			String path = key.next().replaceAll("/", "-");
			String url = hmap.get(path);

			Document doc = null;
			try {
				doc = Jsoup.connect(url).userAgent("Mozilla").timeout(10000).get();
			} catch (IOException e) {
				// e.printStackTrace();
			}
			if (null == doc) {
				continue;
			}

			Elements div = doc.select("ul.pli").select("li").select("p").select("a[href]");
			if (null != div) {
				for (Element e : div) {
					list.add(baseUrl + e.attr("href"));
				}
			}

			Elements nextPages = doc.select("div.pagelist").select("a.page-next");
			if (null != nextPages) {
				for (Element nextPage : nextPages) {

					Document doc2 = null;
					try {
						doc2 = Jsoup.connect(baseUrl + nextPage.attr("href")).userAgent("Mozilla").timeout(10000).get();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (null == doc2) {
						continue;
					}

					Elements div2 = doc2.select("ul.pli").select("li").select("p").select("a[href]");
					if (null != div2) {
						for (Element e : div2) {
							list.add(baseUrl + e.attr("href"));
						}
					}
				}
			}
			download(list, path);
		}

	}

	/**
	 * 获得真实的图片地址并下载
	 * 
	 * @param list
	 *            前边方法获得的包含图片所在的页面地址的列表
	 * @param filepath
	 *            将要保存到的文件夹
	 */
	public void download(List<String> list, String filepath)
	{
		for (String imgUrl : list) {
			Document doc2 = null;
			try {
				doc2 = Jsoup.connect(imgUrl).userAgent("Mozilla").timeout(10000).get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (null == doc2) {
				continue;
			}

			Elements imageUrl = doc2.select("img#imgis");
			for (Element iii : imageUrl) {
				String iUrl = iii.attr("src");
				String filename = iUrl.substring(iUrl.lastIndexOf("/") + 1);
				// System.out.println(iUrl);
				String temp = downloadImage(iUrl, filename, "G:\\植物\\" + filepath + "\\");
				if (null == temp) {
					continue;
				} else {
					System.out.println(iUrl + "下载完毕");
				}
			}
		}
	}
}
