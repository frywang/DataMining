package com.msg.excel;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.msg.file.FileIO;
import com.msg.file.FilePath;
import com.msg.string.StringIO;

public class ExcelRead
{

	/**
	 * 获得excel文件
	 * 
	 * @param filePath
	 *            要打开的文件
	 * @return 返回得到的workbook对象
	 */
	public static Workbook openWorkbook(String filePath)
	{
		InputStream fis = FileIO.openFileInputStream(filePath);
		Workbook workbook = null;
		try {
			if (filePath.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			} else if (filePath.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return workbook;
	}

	/**
	 * 对生成的文件的类型进行设置，在文件名后加上"_知识"或是"_闲聊"
	 * 
	 * @param sourceStr
	 *            根据输入的字符串中是否包含知识或闲聊来处理
	 * @return 返回的是"_知识.txt"或是"_闲聊.txt"
	 */
	private static String getSuffix(String sourceStr)
	{
		if (null == sourceStr) {
			System.out.println("输入的字符串" + sourceStr + "有误");
			return null;
		}
		String strSuffix = null;
		if (sourceStr.contains("知识")) {
			strSuffix = "_知识.txt";
		} else if (sourceStr.contains("闲聊")) {
			strSuffix = "_闲聊.txt";
		} else {
			strSuffix = "_待定------------------------------待定.txt";
		}
		return strSuffix;
	}

	/**
	 * 从Excel中得到数据，写入txt文本中
	 * 
	 * @param excelPath
	 *            excel文件的绝对路径
	 * @param savePath
	 *            要写入到的文件夹
	 * @return 返回写入文本的数据条数
	 */
	public static int getData(String excelPath, String savePath, Map<String, String> map)
	{

		String[] fileNames = excelPath.split("\\\\");
		int number = fileNames.length;
		String filePath = fileNames[number - 2] + ":" + fileNames[number - 1]; // 路径+文件名

		// List<String> listfilenames = FileIO.readLines("todo.txt");

		Workbook workbook = openWorkbook(excelPath);
		int numSheet = workbook.getNumberOfSheets();
		int numData = 0;

		for (int i = 0; i < numSheet; i++) {

			Sheet sheet = workbook.getSheetAt(i);
			String sheetName = sheet.getSheetName().trim();
			if (sheetName.contains("Sheet") || sheetName.contains("统计")) {
				continue;
			}

			Row firstRow = sheet.getRow(0);
			Row secondRow = sheet.getRow(1);

			if (null == firstRow || null == secondRow) {
				// System.out.println(filePath + ":<<" + sheetName +
				// ">>第一行或第二行为空，检查！");
				continue;
			}

			if (firstRow.getPhysicalNumberOfCells() < 3) {
				if (!filePath.contains("段牡慧")) {
					// System.out.println(filePath + ":<<" + sheetName +
					// ">>格式不正确");
				}
				continue;
			}

			if (null != firstRow) {
				Cell firstRow1Cell = firstRow.getCell(0);
				if (null == firstRow1Cell || "".equals(firstRow1Cell.getStringCellValue().trim())) {
					// System.out.println(filePath + ":<<" + sheetName +
					// ">>第一行第一列为空，检查！");
					continue;
				}
			}

			if (null != secondRow) {

				Cell secondRow1Cell = secondRow.getCell(0);
				if (null != secondRow1Cell && !"".equals(secondRow1Cell.getStringCellValue().trim())) {
					// System.out.println(filePath + ":<<" + sheetName +
					// ">>第二行第一列有内容，检查！");
					continue;
				}

				Cell secondRow2Cell = secondRow.getCell(1);
				if (null == secondRow2Cell || "".equals(secondRow2Cell.getStringCellValue().trim())
						|| secondRow2Cell.getCellType() != Cell.CELL_TYPE_STRING) {
					// System.out.println(filePath + ":<<" + sheetName +
					// ">>第二行第二列为空，检查！");
					continue;
				}

				Cell secondRow3Cell = secondRow.getCell(2);
				if (null == secondRow3Cell || "".equals(secondRow3Cell.getStringCellValue().trim())
						|| secondRow3Cell.getCellType() != Cell.CELL_TYPE_STRING) {
					// System.out.println(filePath + ":<<" + sheetName +
					// ">>第二行第三列为空，检查！");
					continue;
				}
			}

			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;

			int rows = sheet.getLastRowNum();

			for (int r = 0; r <= rows; r++) {

				Row row = sheet.getRow(r);

				if (null == row) {
					continue;
				}

				Cell firstCell = row.getCell(0);

				if (null != firstCell) {
					String filename = StringIO.replaceBlank(firstCell.getStringCellValue());

					if (!"".equals(filename)) {

						/*
						 * if("艾滋病毒".equals(filename)){
						 * System.out.println(filePath + ":" + filename + ":" +
						 * sheetName); }
						 */
						/*
						 * for(String strfilenames: listfilenames) {
						 * if(strfilenames.equals(filename)){
						 * System.out.println(excelPath + "::::" + filename); }
						 * }
						 */

						FileIO.closeBufferedWriter(bw);
						FileIO.closeOutputStreamWriter(osw);
						FileIO.closeFileOutputStream(fos);

						String filepath = savePath + "\\" + filename + getSuffix(sheet.getSheetName());

						List<String> listOfFiles = FilePath.getFileList(savePath);
						int ifile = 1;

						for (String strFile : listOfFiles) {
							if (strFile.equals(filepath)) {
								// System.out.println("----<<" + filename +
								// ">>在以下两个文件中同时找到----");
								// System.out.println(map.get(filepath));
								// System.out.println(filePath + ":::" +
								// sheetName + "" + filename);
								// System.out.println("*****************************************************");
								filepath = savePath + "\\" + filename + ifile + "---------"
										+ getSuffix(sheet.getSheetName());
								ifile++;
							}
						}
						map.put(filepath, filePath + ":::" + sheetName);

						fos = FileIO.openFileOutputStream(filepath);
						osw = FileIO.openOutputStreamWriter(fos);
						bw = FileIO.openBufferedWriter(osw);

						int flag = FileIO.writeLine(bw, "##" + filename + "##");
						if (flag == 0) {
							/*
							 * System.out.println(filePath + ":<<" + sheetName +
							 * ">>第" + (r + 1) + "行写入出错！");
							 */
						}
					}
				}

				Cell secondCell = row.getCell(1);
				Cell thirdCell = row.getCell(2);

				String secondCellValue = "";
				String thirdCellValue = "";

				int flag = -1;

				if (null != secondCell && null != thirdCell) {

					secondCellValue = StringIO.replaceBlank(secondCell.getStringCellValue());
					thirdCellValue = StringIO.replaceBlank(thirdCell.getStringCellValue());

					if (!"".equals(thirdCellValue) && !"".equals(secondCellValue)) {
						flag = FileIO.writeLine(bw, secondCellValue + "$$$" + thirdCellValue);
					}
				} else if (null == secondCell && null != thirdCell) {

					thirdCellValue = StringIO.replaceBlank(thirdCell.getStringCellValue());

					if (!"".equals(thirdCellValue)) {
						flag = FileIO.writeLine(bw, "该数据问题暂时为空" + "$$$" + thirdCellValue);
						/*
						 * System.out.println(filePath + ":<<" + sheetName +
						 * ">>第" + (r + 1) + "行有答案没问题！");
						 */
					}
				} else if (null != secondCell && null == thirdCell) {

					secondCellValue = StringIO.replaceBlank(secondCell.getStringCellValue());

					if (!"".equals(secondCellValue)) {
						flag = FileIO.writeLine(bw, secondCellValue + "$$$" + "该数据答案暂时为空");
						/*
						 * System.out.println(filePath + ":<<" + sheetName +
						 * ">>第" + (r + 1) + "行有问题没答案！");
						 */
					}
				}

				if (flag == 0) {
					/*
					 * System.out.println(filePath + ":<<" + sheetName + ">>第" +
					 * (r + 1) + "行写入出错！");
					 */
				} else if (flag == 1) {
					numData++;
				}
			}

			FileIO.closeBufferedWriter(bw);
			FileIO.closeOutputStreamWriter(osw);
			FileIO.closeFileOutputStream(fos);
		}
		return numData;
	}

}
