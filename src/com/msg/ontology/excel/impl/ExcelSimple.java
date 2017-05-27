package com.msg.ontology.excel.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.msg.ontology.dict.OwlDict;
import com.msg.ontology.excel.IExcelSimple;

public class ExcelSimple implements IExcelSimple
{
	private String filePath;
    private Workbook workbook;

    public ExcelSimple(String filePath) {
        this.filePath = filePath;
        this.workbook = initWorkbook();
    }

    public ExcelSimple() {
        this.filePath = null;
        this.workbook = initWorkbook();
    }

    @Override
    public Workbook initWorkbook() {
        Workbook workbook = null;
        if (null == filePath) {
            workbook = new XSSFWorkbook();
            return workbook;
        }
        InputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

	@Override
	public void write2excel(String filename)
	{
		OutputStream os = null;
		try {
			os = new FileOutputStream(filename);
			workbook.write(os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		}
	}

	public Workbook getWorkbook()
	{
		return workbook;
	}

	@Override
	public void closeWorkbook()
	{
		if (null != workbook) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public CellStyle setStyle()
	{
		CellStyle cs = workbook.createCellStyle();
		cs.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cs.setFillForegroundColor(IndexedColors.DARK_YELLOW.index);
		Font font = workbook.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 14);
		font.setColor(IndexedColors.WHITE.index);
		cs.setFont(font);
		return cs;
	}

	/**
	 * 从excel中读取数据然后加入到一个WordDict列表中
	 * 
	 * @return 返回的内容
	 */
	public List<OwlDict> getAnnoFromExcel()
	{

		List<OwlDict> lists = new ArrayList<OwlDict>();

		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();

		Row firstRow = sheet.getRow(0);
		int colNum = firstRow.getLastCellNum();

		int synCol = 0;
		int nickCol = 0;

		for (int f = 0; f < colNum; f++) {

			Cell firstRowCell = firstRow.getCell(f);
			if (null == firstRowCell) {
				continue;
			}

			String firstRowCellValue = firstRowCell.getStringCellValue().trim();
			if ("synonym".equals(firstRowCellValue)) {
				synCol = f;
			} else if ("nickname".equals(firstRowCellValue)) {
				nickCol = f;
			}
		}

		for (int r = 1; r <= rowNum; r++) {
			OwlDict dict = new OwlDict();

			Row row = sheet.getRow(r);
			if (null == row) {
				continue;
			}

			String name = null;
			for (int c = 0; c < synCol; c++) {
				Cell cell = row.getCell(c);
				if (null != cell && !"".equals(name = cell.getStringCellValue().trim())) {
					dict.setName(name);
				}
			}

			Cell synCell = row.getCell(synCol);
			String strSyn = null;
			if (null != synCell && !"".equals(strSyn = synCell.getStringCellValue().trim())) {
				String synNames = strSyn.replaceAll("、", " ");
				dict.setSynname(synNames);
			}

			Cell nickCell = row.getCell(nickCol);
			String strNick = null;
			if (null != nickCell && !"".equals(strNick = nickCell.getStringCellValue().trim())) {
				String nickNames = strNick.replaceAll("、", " ");
				dict.setNickname(nickNames);
			}
			lists.add(dict);
		}
		return lists;
	}

	/**
	 * 从excel中获得基本Property
	 * 
	 * @return 返回由Property名和domain + range组成的Map
	 */
	public Map<String, String> getBasicPropFromExcel()
	{
		Map<String, String> props = new HashMap<String, String>();

		Sheet basicSheet = workbook.getSheet("基本");
		int basicRowNum = basicSheet.getLastRowNum();
		String parent = null;
		for (int r = 3; r <= basicRowNum; r++) {
			String name = null;
			Row row = basicSheet.getRow(r);
			String col1 = null;
			String col2 = null;
			String col3 = null;
			String col4 = null;
			String col5 = null;
			if (null != row) {
				StringBuilder sb = new StringBuilder("");

				Cell cell1 = row.getCell(0);
				if (null != cell1 && !"".equals(col1 = cell1.getStringCellValue().trim())) {
					sb.append(col1 + "#");
				}

				Cell cell2 = row.getCell(1);
				if (null != cell2 && !"".equals(col2 = cell2.getStringCellValue().trim())) {
					name = col2;
					parent = col2;
					sb.append("null#");
				}
				Cell cell3 = row.getCell(2);
				if (null != cell3 && !"".equals(col3 = cell3.getStringCellValue().trim())) {
					name = col3;
					sb.append(parent + "#");
				}
				Cell cell4 = row.getCell(3);
				if (null != cell4 && !"".equals(col4 = cell4.getStringCellValue().trim())) {
					sb.append(col4 + "#");
				}

				Cell cell5 = row.getCell(4);
				if (null != cell5 && !"".equals(col5 = cell5.getStringCellValue().trim())) {
					sb.append(col5);
				}
				System.out.println(name + ":" + sb.toString());
				props.put(name, sb.toString());
			}
		}
		return props;
	}

	public Map<String, String> getBehavePropFromExcel()
	{
		Map<String, String> map = new HashMap<String, String>();

		List<String> parent = new ArrayList<String>();

		Sheet behaveSheet = workbook.getSheet("行为");
		int behaveRowNum = behaveSheet.getLastRowNum();

		for (int r = 1; r <= behaveRowNum; r++) {
			Row row = behaveSheet.getRow(r);
			if (null == row) {
				System.out.println("第" + r + "行为空");
				continue;
			}
			String col1 = null;

			Cell cell1 = row.getCell(0);
			StringBuilder sb = new StringBuilder("");
			if (null != cell1 && !"".equals(cell1.getStringCellValue().trim())) {
				col1 = cell1.getStringCellValue().trim();
				sb.append(col1 + "#");
			}

			String colName = null;
			for (int c = 1; c <= 3; c++) {
				Cell cellName = row.getCell(c);
				if (null != cellName && !"".equals(cellName.getStringCellValue().trim())) {
					colName = cellName.getStringCellValue().trim();
					// System.out.println(parent.size());
					parent.add(c - 1, colName);

					if (1 == c) {
						sb.append("null#");
					} else {
						sb.append(parent.get(c - 2) + "#");
					}
					break;
				}
			}

			Cell cellRange = row.getCell(5);
			String colR = null;
			if (null != cellRange && !"".equals(colR = cellRange.getStringCellValue().trim())) {
				sb.append(colR);
			}
			if (null != colName) {
				System.out.println(colName + ":" + sb.toString());
				map.put(colName, sb.toString());
			}
		}
		return map;
	}
	
	public Map<String, String> getBehaveValueFromExcel()
	{
		Map<String, String> map = new HashMap<String, String>();

		Sheet behaveSheet = workbook.getSheet("Sheet2");
		int behaveRowNum = behaveSheet.getLastRowNum();

		for (int r = 1; r <= behaveRowNum; r++) {
			Row row = behaveSheet.getRow(r);
			if (null == row) {
				System.out.println("第" + r + "行为空");
				continue;
			}
			StringBuilder sb = new StringBuilder("");

			String colName = null;
			for (int c = 1; c <= 3; c++) {
				Cell cellName = row.getCell(c);
				if (null != cellName && !"".equals(cellName.getStringCellValue().trim())) {
					colName = cellName.getStringCellValue().trim();
				}
			}
			
			Cell cellRange = row.getCell(5);
			String colR = null;
			if (null != cellRange && !"".equals(colR = cellRange.getStringCellValue().trim())) {
				sb.append(colR);
			}
			Cell cellValue = row.getCell(6);
			String colV = null;
			if (null != cellValue) {
				if(cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN)
				{
					colV = Boolean.toString(cellValue.getBooleanCellValue());
					sb.append("#" + colV);
				}else{
					colV = cellValue.getStringCellValue().trim();
					if(!"".equals(colV))
					{
						sb.append("#" + colV);
					}else{
						sb.append("#null");
					}
				}
			}else{
				sb.append("#null");
			}
			if (null != colName) {
				System.out.println(colName + ":" + sb.toString());
				map.put(colName, sb.toString());
			}
		}
		return map;
	}
	
	public Map<String, String> getBesicValueFromExcel()
	{
		Map<String, String> props = new HashMap<String, String>();

		Sheet basicSheet = workbook.getSheet("Sheet1");
		int basicRowNum = basicSheet.getLastRowNum();
		String parent = null;
		for (int r = 3; r <= basicRowNum; r++) {
			String name = null;
			Row row = basicSheet.getRow(r);
			String col1 = null;
			String col2 = null;
			String col3 = null;
			String col4 = null;
			String col5 = null;
			if (null != row) {
				StringBuilder sb = new StringBuilder("");

				Cell cell1 = row.getCell(0);
				if (null != cell1 && !"".equals(col1 = cell1.getStringCellValue().trim())) {
					sb.append(col1 + "#");
				}

				Cell cell2 = row.getCell(1);
				if (null != cell2 && !"".equals(col2 = cell2.getStringCellValue().trim())) {
					name = col2;
					parent = col2;
					sb.append("null#");
				}
				Cell cell3 = row.getCell(2);
				if (null != cell3 && !"".equals(col3 = cell3.getStringCellValue().trim())) {
					name = col3;
					sb.append(parent + "#");
				}
				Cell cell4 = row.getCell(3);
				if (null != cell4 && !"".equals(col4 = cell4.getStringCellValue().trim())) {
					sb.append(col4 + "#");
				}

				Cell cell5 = row.getCell(4);
				if (null != cell5 && !"".equals(col5 = cell5.getStringCellValue().trim())) {
					sb.append(col5);
				}
				System.out.println(name + ":" + sb.toString());
				props.put(name, sb.toString());
			}
		}
		return props;
	}
	

}
