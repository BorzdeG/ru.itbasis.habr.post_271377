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
	public static final String BKT_JSON_L = "[";
	public static final String BKT_JSON_R = "]";
	public static final String BKT_JAVA_L = "{";
	public static final String BKT_JAVA_R = "}";
	public static final String COMMA      = ",";

	private static String BKT_START;
	private static String BKT_END;
	private static String BKT_I_SEPARATOR;
	private static String BKT_J_SEPARATOR;

	private static void initBrackets(final String bracketType) {
		if (bracketType != null && !bracketType.isEmpty()) {
			BKT_START = BKT_JAVA_L + BKT_JAVA_L + BKT_JAVA_L;
			BKT_END = BKT_JAVA_R + BKT_JAVA_R + BKT_JAVA_R;
			BKT_I_SEPARATOR = BKT_JAVA_R + BKT_JAVA_R + COMMA + BKT_JAVA_L + BKT_JAVA_L;
			BKT_J_SEPARATOR = BKT_JAVA_R + COMMA + BKT_JAVA_L;

		} else {
			BKT_START = BKT_JSON_L + BKT_JSON_L + BKT_JSON_L;
			BKT_END = BKT_JSON_R + BKT_JSON_R + BKT_JSON_R;
			BKT_I_SEPARATOR = BKT_JSON_R + BKT_JSON_R + COMMA + BKT_JSON_L + BKT_JSON_L;
			BKT_J_SEPARATOR = BKT_JSON_R + COMMA + BKT_JSON_L;
		}
	}

	public static void main(String[] args) throws Exception {
		final String filePath = args[0];
		final File file = new File(filePath);

		System.out.println((args.length > 1) ? toStringArray(file, args[1]) : toStringArray(file));
	}

	public static String toStringArray(final File file, final String bracketType) throws Exception {
		initBrackets(bracketType);

		final Workbook wb = WorkbookFactory.create(file);
		final Sheet sheet = wb.getSheetAt(0);

		final StringBuilder sb = new StringBuilder(BKT_START);

		final int lastRowNum = sheet.getLastRowNum();
		double i = 0, j = 0;
		for (int r = 1; r <= lastRowNum; r++) {
			final Row row = sheet.getRow(r);

			final Cell cellI = getCell(row, 0);
			if (cellI.getCellType() != Cell.CELL_TYPE_NUMERIC) { break; }
			final double tmpI = cellI.getNumericCellValue();
			if (tmpI > i) {
				if (i > 0) { sb.append(BKT_I_SEPARATOR); }
				j = 0;
			}

			final double tmpJ = getCell(row, 1).getNumericCellValue();
			if (tmpJ > j) {
				if (j > 0) {sb.append(BKT_J_SEPARATOR);}
			}

			if (tmpJ == j) {sb.append(COMMA);}

			// write value
			sb.append(String.format(Locale.ENGLISH, "%.2f", getCell(row, 3).getNumericCellValue()));

			// next step
			j = tmpJ;
			i = tmpI;
		}

		return sb.append(BKT_END).toString();
	}

	public static String toStringArray(final File file) throws Exception {
		return toStringArray(file, null);
	}
}
