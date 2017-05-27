package com.msg.img;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.msg.file.FileIO;

/**
 * Project Name: Data Author: MSG Time: 2016年9月2日 下午6:02:00
 */

public class Hudong extends ImgIO
{

	public void getUrl()
	{
		String topUrl = "http://www.baike.com/wiki/";

		List<String> listOfAnimals = FileIO.readLines("动物.txt");
		for (String strAnimal : listOfAnimals) {
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

			Elements photos = doc.select("div.img_r").select("a[title]");
			if (null == photos) {
				System.out.println("查找不到a");
			}
			int i = 0;
			for (Element photo : photos) {
				if (photo.attr("title").contains(strAnimal)) {
					if (photo.attr("href").contains("http")) {

						i++;
						String imgUrl = photo.attr("href");
						System.out.println(imgUrl);
						Document doc2 = null;
						try {
							doc2 = Jsoup.connect(imgUrl).userAgent("Mozilla").timeout(10000).get();
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (null == doc2) {
							continue;
						}

						Elements images = doc2.select("div.picture-show").select("img");
						if (null == images) {
							continue;
						}
						for (Element image : images) {

							// System.out.println(image.attr("src"));
							System.out.println(strAnimal);
							String urlImage = image.attr("src");
							String done = downloadImage(urlImage, strAnimal + "_" + i + ".jpg",
									"G:\\互动百科\\" + strAnimal);
							if (null == done) {
								continue;
							} else {
								System.out.println(strAnimal + "_" + i + ".jpg下载完成");
							}
						}

					}
				}
			}
		}
	}
}
