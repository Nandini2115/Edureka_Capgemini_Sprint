package utilities;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public final class ExcelReader {
    private ExcelReader() {
    }
    public static Object[][] readSheet(String resourceName, String sheetName) throws IOException {
        try (InputStream input = ExcelReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IOException("Excel resource not found: " + resourceName);
            }
            try (Workbook workbook = WorkbookFactory.create(input)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IOException("Sheet not found: " + sheetName);
                }
                if (sheet.getPhysicalNumberOfRows() <= 1) {
                    return new Object[0][0];
                }
                DataFormatter formatter = new DataFormatter();
                Row header = sheet.getRow(0);
                if (header == null) {
                    return new Object[0][0];
                }
                int columns = header.getLastCellNum();
                List<Object[]> rows = new ArrayList<>();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    
                    Object[] values = new Object[columns];
                    for (int j = 0; j < columns; j++) {
                        values[j] = formatter.formatCellValue(row.getCell(j));
                    }
                    rows.add(values);
                }
                return rows.toArray(new Object[0][]);
            }
        }
    }
}