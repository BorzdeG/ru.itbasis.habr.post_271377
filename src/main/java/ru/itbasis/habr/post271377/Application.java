package ru.itbasis.habr.post271377;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.util.Locale;

import static org.apache.poi.ss.util.CellUtil.getCell;

public class Application {
	public static void main(String[] args) throws Exception {
		final String filePath = args[0];
		final File file = new File(filePath);
		final String json = new Application().toJson(file);
		System.out.println(json);
	}

	public String toJson(final File file) throws Exception {
		final Workbook wb = WorkbookFactory.create(file);
		final Sheet sheet = wb.getSheetAt(0);

		final StringBuilder sb = new StringBuilder("[[[");

		final int lastRowNum = sheet.getLastRowNum();
		double i = 0, j = 0;
		for (int r = 1; r <= lastRowNum; r++) {
			final Row row = sheet.getRow(r);

			final Cell cellI = getCell(row, 0);
			if (cellI.getCellType() != Cell.CELL_TYPE_NUMERIC) { break; }
			final double tmpI = cellI.getNumericCellValue();
			if (tmpI > i) {
				if (i > 0) { sb.append("]],[["); }
				j = 0;
			}

			final double tmpJ = getCell(row, 1).getNumericCellValue();
			if (tmpJ > j) {
				if (j > 0) {sb.append("],[");}
			}

			if (tmpJ == j) {sb.append(",");}

			// write value
			sb.append(String.format(Locale.ENGLISH, "%.2f", getCell(row, 3).getNumericCellValue()));

			// close tags
			j = tmpJ;
			i = tmpI;
		}

		return sb.append("]]]").toString();
	}
}
