package com.msg.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.msg.owl.OwlClassDict;

/**
 * 将excel中的同义词和昵称写到xml中
 * 
 * @author MSG
 *
 */
public class ExcelReadDict
{

	private String filepath;

	public ExcelReadDict(String filepath)
	{
		this.filepath = filepath;
	}

	/**
	 * 从excel中读取数据然后加入到一个WordDict列表中
	 * 
	 * @return 返回的内容
	 */
	public List<OwlClassDict> getExcelContent()
	{

		List<OwlClassDict> classList = new ArrayList<OwlClassDict>();

		Workbook workbook = ExcelRead.openWorkbook(filepath);
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getLastRowNum();

		Row firstRow = sheet.getRow(0);
		int firstRowCols = firstRow.getLastCellNum();

		int synonymCol = 0;
		int nicknameCol = 0;

		for (int f = 0; f < firstRowCols; f++) {

			Cell firstRowCell = firstRow.getCell(f);
			if (null == firstRowCell) {
				continue;
			}

			String firstRowCellValue = firstRowCell.getStringCellValue().trim();
			if ("synonym".equals(firstRowCellValue)) {
				synonymCol = f;
			} else if ("nickname".equals(firstRowCellValue)) {
				nicknameCol = f;
			}
		}

		for (int r = 1; r <= rows; r++) {
			OwlClassDict classDict = new OwlClassDict();

			Row row = sheet.getRow(r);
			if (null == row) {
				continue;
			}

			String name = null;
			for (int c = 0; c < synonymCol; c++) {
				Cell cell = row.getCell(c);
				if (null != cell && !"".equals(name = cell.getStringCellValue().trim())) {
					classDict.setName(name);
				}
			}

			Cell synonymCell = row.getCell(synonymCol);
			if (null != synonymCell) {
				String strSynName = synonymCell.getStringCellValue().trim();
				if (!"".equals(strSynName)) {
					String synNames = strSynName.replaceAll("、", " ");
					classDict.setSynname(synNames);
				}
			}

			Cell nickNameCell = row.getCell(nicknameCol);
			if (null != nickNameCell) {
				String strNickName = nickNameCell.getStringCellValue().trim();
				if (!"".equals(strNickName)) {
					String nickNames = strNickName.replaceAll("、", " ");
					classDict.setNickname(nickNames);
				}
			}
			classList.add(classDict);
		}
		return classList;
	}
}
