package com.msg.ontology.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface IExcelSimple
{
	public Workbook initWorkbook();

	public void write2excel(String filename);

	public void closeWorkbook();

	public CellStyle setStyle();
}
