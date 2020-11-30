package com.techstar.om.dasi.controller.pojo;

import lombok.Getter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

@Getter
public class CheckResultExcelStyle {
    private String title;
    private int columnWidth;
    private HorizontalAlignment hAlign;

    public CheckResultExcelStyle(String title, int columnWidth, HorizontalAlignment hAlign) {
        this.title = title;
        this.columnWidth = columnWidth;
        this.hAlign = hAlign;
    }
}
