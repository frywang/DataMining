package com.msg.img;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.msg.file.FileIO;

/**
 * Project Name: Data Author: MSG Time: 2016年9月2日 下午7:20:44
 */

public class Baidu extends ImgIO
{

	public void getUrl()
	{

		String topUrl = "http://baike.baidu.com/item/";
		List<String> listOfAnimals = FileIO.readLines("动物.txt");
		for (String strAnimal : listOfAnimals) {
			int i = 0;
			String url = topUrl + strAnimal;
			Document doc = null;
			try {
				doc = Jsoup.connect(url).userAgent("Mozilla").timeout(10000).get();
			} catch (IOException e) {
				// e.printStackTrace();
			}

			if (null == doc) {
				continue;
			}

			Elements photos = doc.select("div.summary-pic").select("a");
			if (null == photos) {
				System.out.println("查找不到a");
			}

			for (Element photo : photos) {
				// System.out.println(photo.attr("href"));

				String imgUrl = "http://baike.baidu.com" + photo.attr("href");

				Document doc2 = null;
				try {
					doc2 = Jsoup.connect(imgUrl).userAgent("Mozilla").timeout(10000).get();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (null == doc2) {
					continue;
				}

				Elements images = doc2.select("div.pic-list").select("a.pic-item");
				for (Element image : images) {
					i++;
					String strUrl = "http://baike.baidu.com" + image.attr("href");
					// System.out.println(strUrl);
					// System.out.println(strAnimal);
					Document doc3 = null;
					try {
						doc3 = Jsoup.connect(strUrl).userAgent("Mozilla").timeout(10000).get();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (null == doc3) {
						System.out.println("..........................");
						continue;
					}
					// System.out.println(doc3);
					Element img = doc3.getElementById("imgPicture");

					if (null == img) {
						continue;
					}

					String done = downloadImage(img.attr("src"), strAnimal + "_" + i + ".jpg",
							"G:\\百度百科\\" + strAnimal);

					if (null == done) {
						continue;
					} else {
						System.out.println(img.attr("src") + "   " + strAnimal + "_" + i + ".jpg 下载完成");
					}
				}
				// System.out.println(i);
			}

		}

	}
}
