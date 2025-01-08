package com.paytr.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelHelper {

    public static Map<String, String> readExcelData(String filePath, String sheetName) throws IOException {
        Map<String, String> data = new HashMap<>();

        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheet(sheetName);

        for (Row row : sheet) {
            Cell keyCell = row.getCell(0); // İlk sütun anahtar
            Cell valueCell = row.getCell(1); // İkinci sütun değer

            if (keyCell != null && valueCell != null) {
                data.put(keyCell.getStringCellValue(), valueCell.getStringCellValue());
            }
        }

        workbook.close();
        fileInputStream.close();
        return data;
    }
}
