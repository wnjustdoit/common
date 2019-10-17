package com.caiya.common.excel.core;

import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作簿
 */
@Data
public class ExtendedWorkbook {

    /**
     * 导出文件名
     */
    private String fileName;

    /**
     * 表格集合
     */
    private List<ExtendedSheet> extendedSheets = new ArrayList<>();

    /**
     * 导出文件后缀
     */
    private final Suffix suffix;

    /**
     * 原生工作簿，一对一
     */
    private Workbook workbook;

    private ExtendedWorkbook() throws IOException {
        // 默认文件名称，日期格式：yyyyMMddHHmmssSSS
        this.fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        this.suffix = Suffix.XLS;
        this.setWorkbook(WorkbookFactory.create(Suffix.XLSX.equals(this.suffix)));
    }

    public ExtendedWorkbook(String fileName) throws IOException {
        this.fileName = fileName;
        this.suffix = Suffix.XLS;
        this.setWorkbook(WorkbookFactory.create(Suffix.XLSX.equals(this.suffix)));
    }

    public ExtendedWorkbook(String fileName, Suffix suffix) throws IOException {
        this.fileName = fileName;
        this.suffix = suffix;
        this.setWorkbook(WorkbookFactory.create(Suffix.XLSX.equals(this.suffix)));
    }

    /**
     * 读取excel使用的构造函数
     *
     * @param inputStream 输入流
     * @param suffix      后缀，可为空
     * @param fileName    文件名，可为空
     * @throws IOException IOException
     */
    public ExtendedWorkbook(InputStream inputStream, String fileName, Suffix suffix) throws IOException {
        this.fileName = fileName;
        this.suffix = suffix;
        this.setWorkbook(WorkbookFactory.create(inputStream));
    }

    public void addExtendedSheet(ExtendedSheet extendedSheet) {
        this.getExtendedSheets().add(extendedSheet);
    }

    public CellStyle createCellStyle() {
        return this.getWorkbook().createCellStyle();
    }

    public Font createFont() {
        return this.getWorkbook().createFont();
    }

    public enum Suffix {
        XLS(".xls"),
        XLSX(".xlsx");

        private final String value;

        Suffix(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

}
