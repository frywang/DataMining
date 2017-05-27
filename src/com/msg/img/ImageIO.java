package com.msg.img;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.msg.file.FileIO;
import com.msg.file.FilePath;

/**
 * 这是对素材网的高清图片的压缩版本进行了爬取 Project Name: Data Author: MSG Time: 2016年8月31日
 * 下午6:22:06
 */

public class ImageIO extends ImgIO
{

	/**
	 * 获得URLs
	 * 
	 * @param strClass
	 *            下载的图片的类别名
	 * @param nums
	 *            共有的页数
	 * @return 返回一个map，存放了text和url
	 */
	private Map<String, String> getUrl(String strClass, int nums)
	{

		FileOutputStream fos = FileIO.openFileOutputStream(strClass + ".txt");
		OutputStreamWriter osw = FileIO.openOutputStreamWriter(fos);
		BufferedWriter bw = FileIO.openBufferedWriter(osw);

		int count = 0;
		Map<String, String> urls = new HashMap<String, String>();

		String domain = "http://www.16sucai.com";
		String strUrl = "http://www.16sucai.com/tupian/" + strClass + "/";

		Document doc = null;
		try {
			doc = Jsoup.connect(strUrl).userAgent("Mozilla").timeout(5000).get();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (null != doc) {
			Elements div = doc.select("div.vector_listbox").select("p").select("a[href]");

			if (null != div) {
				for (Element a : div) {
					urls.put(a.text(), domain + a.attr("href"));
					FileIO.writeLine(bw, a.text() + ":" + domain + a.attr("href"));
					count++;
				}
			}
		} else {
			System.out.println("没有获得链接为" + strUrl + "的数据");
		}

		/******** 循环遍历不同页码的数据 ********/
		for (int i = 2; i <= nums; i++) {
			String urlStr = strUrl + i + ".html";
			Document doc2 = null;
			try {
				doc2 = Jsoup.connect(urlStr).userAgent("Mozilla").timeout(5000).get();
			} catch (IOException e1) {
				System.out.println(urlStr + "连接出错！");
			}
			if (null == doc2) {
				continue;
			}
			Elements div2 = doc2.select("div.vector_listbox").select("p").select("a[href]");
			if (null != div2) {
				for (Element e : div2) {
					urls.put(e.text(), domain + e.attr("href"));
					System.out.println(e.text());
					FileIO.writeLine(bw, e.text() + ":" + domain + e.attr("href"));
					count++;
				}
			}
		}

		System.out.println("共有" + count + "个页面");

		FileIO.closeBufferedWriter(bw);
		FileIO.closeOutputStreamWriter(osw);
		FileIO.closeFileOutputStream(fos);

		return urls;
	}

	/**
	 * 下载所有的图片
	 */

	/**
	 * 下载所有的图片
	 * 
	 * @param strClass
	 *            下载的图片的类别
	 * @param nums
	 *            图片共有多少数量
	 */
	public void getPhoto(String strClass, int nums)
	{

		Map<String, String> urls = getUrl(strClass, nums);

		if (null == urls) {
			System.out.println("没有取得链接url，请重试");
			return;
		}

		Iterator<String> iter = urls.keySet().iterator();
		while (iter.hasNext()) {
			String path = iter.next();
			String imgUrl = urls.get(path);
			try {
				Document doc = Jsoup.connect(imgUrl).userAgent("Mozilla").get();
				Elements images = doc.select("div.endtext").select("img[src~=(?i)\\.(jpe?g)]");
				int num = 0;
				for (Element image : images) {
					String imageUrl = image.attr("src");

					String name = downloadImage(imageUrl, path + "_" + num + ".jpg", "G:\\" + strClass + "\\" + path);
					if (null == name) {
						continue;
					}
					System.out.println(path + "  " + imageUrl + "下载完毕");
					num++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void cleanPhoto(String filename, String movepath)
	{

		List<String> words = FileIO.readLines(filename);
		List<String> paths = FilePath.getPathList("G:\\images\\动物");
		Collections.reverse(words);
		for (String word : words) {
			// System.out.println(word);
			for (String path : paths) {
				if (path.contains(word)) {
					// System.out.println(path + ":：：：" + word);
					FilePath.movePath("G:\\images\\动物\\" + path, movepath + word);
					System.out.println("移动了<<" + path + ">>到<<" + word + ">>目录下");
				}
			}
		}

	}

}
