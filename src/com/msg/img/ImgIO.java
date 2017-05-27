package com.msg.img;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Project Name: Data Author: MSG Time: 2016年9月2日 上午10:33:15
 */

public class ImgIO
{

	/**
	 * 下载网站上的图片
	 * 
	 * @param strUrl
	 *            图片的URL地址
	 * @param filename
	 *            保存下来的文件名
	 * @param path
	 *            保存到的地址
	 * @return 返回filename，若返回null，则证明下载失败
	 */
	public String downloadImage(String strUrl, String filename, String path)
	{

		/******** 创建URL对象 *********/
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			System.out.println("url的地址格式不正确,程序终止！");
			return null;
		}

		/********* 创建连接 **********/
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		conn.setConnectTimeout(10 * 1000);
		conn.setReadTimeout(10 * 1000);

		InputStream is = null;
		int i = 0;

		/******* 要是得不到读不到流数据，则重复连接100次 ******/
		while (i < 100) {
			try {
				/******* 获得连接流 ******/
				is = conn.getInputStream();
			} catch (IOException e) {
				i++;
			}
			if (null != is) {
				break;
			}
		}

		if (null == is) {
			System.out.println("没有获得连接流,程序退出！");
			return null;
		}

		File savepath = new File(path);
		if (!savepath.exists()) {
			savepath.mkdirs();
		}

		OutputStream os = null;

		try {
			os = new FileOutputStream(savepath.getPath() + "\\" + filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		byte[] bytes = new byte[1024];

		int len;
		try {
			while (-1 != (len = is.read(bytes))) {
				os.write(bytes, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return filename;
	}

}
