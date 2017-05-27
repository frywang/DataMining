package com.msg.excel;

/**
 * Project Name: Data Author: MSG Time: 2016年8月25日 上午9:21:15
 */

public class TestExcel
{

	public static void main(String[] args)
	{
		ExcelWrite ex = new ExcelWrite();
		ex.writeAllExcel("G:\\Qieyin\\excel", "G:\\Qieyin\\食物txt");
	}

}
