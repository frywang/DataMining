package com.msg.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.msg.file.FileIO;
import com.msg.file.FilePath;

/**
 * Project Name: Data Author: MSG Time: 2016年8月26日 下午3:25:42
 */

public class ExcelWrite
{

	private Workbook workbook;

	public ExcelWrite()
	{
		this.workbook = workbookConstruct();
	}

	private Workbook workbookConstruct()
	{
		Workbook workbook = new XSSFWorkbook();
		return workbook;
	}

	/**
	 * 设置输出统计单元格的格式
	 * 
	 * @param cell
	 *            要设置格式的单元格
	 */
	private void setCellColor(Cell cell)
	{
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.DARK_YELLOW.index);
		Font font = workbook.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 14);
		font.setColor(IndexedColors.WHITE.index);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 向excel一行中连续写入三列数据
	 * 
	 * @param xssfRow
	 *            行
	 * @param cellValue0
	 *            第一列数据
	 * @param cellValue1
	 *            第二列数据
	 * @param cellValue2
	 *            第三列数据
	 * @param bool
	 *            根据bool的值决定是否设置单元格的颜色
	 */
	private void writeCell(Row row, String cellValue0, String cellValue1, String cellValue2, boolean bool)
	{

		Cell firstCell = row.createCell(0);
		firstCell.setCellValue(cellValue0);

		Cell secondCell = row.createCell(1);
		secondCell.setCellValue(cellValue1);

		Cell thirdCell = row.createCell(2);
		thirdCell.setCellValue(cellValue2);

		if (bool == true) {
			setCellColor(firstCell);
			setCellColor(secondCell);
			setCellColor(thirdCell);
		}
	}

	/**
	 * 对文件夹下的excel文件写出txt， 顺便统计下每个excel文件中的数据条数
	 * 
	 * @param path
	 *            存放excel文件夹的目录
	 * @param finished
	 *            存放txt文件将要存放到的文件夹
	 */
	public void writeAllExcel(String path, String finished)
	{

		FilePath.createPath(finished);

		List<String> list = FilePath.getFileList(path);
		Sheet sheet = workbook.createSheet("统计");
		sheet.setColumnWidth(0, 2500);
		sheet.setColumnWidth(1, 14000);
		sheet.setColumnWidth(2, 3000);
		Row topRow = sheet.createRow(0);
		writeCell(topRow, "日期", "文件名", "数据数目", true);

		int listNumber = list.size();
		String tempPath = ""; // 临时路径用来确认是否属于一个目录下
		int tempNum = 0; // 临时数目用于存储相同目录下获得的数据条数
		int numAll = 0; // 记录所有的数据条数
		int fileCount = 0; // 记录excel文件数

		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < listNumber; i++) {

			/******* 获得每一个文件名 ********/
			String filename = list.get(i);

			if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {

				fileCount++;

				String[] splitString = filename.split("\\\\"); // 注意此处的四个反斜杠
				int number = splitString.length;
				String filePath = splitString[number - 2];
				String fileName = splitString[number - 1];

				int num = 0;
				num = ExcelRead.getData(filename, finished, map);

				if (!tempPath.equals(filePath) && tempNum != 0) {
					int rowNum1 = sheet.getLastRowNum();
					Row comRow = sheet.createRow(rowNum1 + 1);

					writeCell(comRow, "统计", "", Integer.toString(tempNum), true);

					tempPath = filePath;
					numAll += tempNum;
					tempNum = num;
				} else {
					tempNum += num;
					if (fileCount == 1) {
						tempPath = filePath;
					}
				}

				if (num > 0) {
					int rowFirstNum = sheet.getLastRowNum();
					Row rowFirst = sheet.createRow(rowFirstNum + 1);
					writeCell(rowFirst, filePath, fileName, Integer.toString(num), false);
				}
				if (i == listNumber - 1) {
					int rowNumLast = sheet.getLastRowNum();
					Row rowLast = sheet.createRow(rowNumLast + 1);
					writeCell(rowLast, "统计", "", Integer.toString(tempNum), true);
					Row rowAll = sheet.createRow(rowNumLast + 2);
					writeCell(rowAll, "总数目", "", Integer.toString(numAll), true);
				}
			}
		}
		write2Excel("统计.xlsx");
	}

	/**
	 * 将workbook写入excel文件中
	 * 
	 * @param filepath
	 *            要写入的excel文件路径
	 */
	public void write2Excel(String filepath)
	{
		FileOutputStream fileOutputStream = FileIO.openFileOutputStream(filepath);
		try {
			this.workbook.write(fileOutputStream);
			this.workbook.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		FileIO.closeFileOutputStream(fileOutputStream);
	}

}
