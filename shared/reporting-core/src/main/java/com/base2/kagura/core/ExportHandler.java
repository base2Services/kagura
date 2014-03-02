package com.base2.kagura.core;

import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Export Handler. This is a POJO to help you convert output from report-core into various output file formats.
 * User: aubels
 * Date: 30/07/13
 * Time: 1:49 PM
 */
public class ExportHandler implements Serializable {
    /**
     * Takes the output and transforms it into a PDF file.
     * @param out Output stream.
     * @param rows Rows of data from reporting-core
     * @param columns Columns to list on report
     */
    public void generatePdf(OutputStream out, List<Map<String, Object>> rows, List<ColumnDef> columns) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            if (columns == null)
            {
                if (rows.size() > 0) return;
                columns = new ArrayList<ColumnDef>(CollectionUtils.collect(rows.get(0).keySet(), new Transformer() {
                    @Override
                    public Object transform(final Object input) {
                        return new ColumnDef()
                        {{
                                setName((String)input);
                            }};
                    }
                }));
            }
                if (columns.size() > 14)
                    document.setPageSize(PageSize.A1);
                else if (columns.size() > 10)
                    document.setPageSize(PageSize.A2);
                else if (columns.size() > 7)
                    document.setPageSize(PageSize.A3);
                else
                    document.setPageSize(PageSize.A4);
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, BaseColor.BLACK);

            int size = columns.size();
            PdfPTable table = new PdfPTable(size);
            for (ColumnDef column : columns)
            {
                PdfPCell c1 = new PdfPCell(new Phrase(column.getName(), headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
            }
            table.setHeaderRows(1);
            if (rows != null)
                for (Map<String, Object> row : rows)
                {
                    for (ColumnDef column : columns)
                    {
                        table.addCell(new Phrase(String.valueOf(row.get(column.getName())), font));
                    }
                }
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes the output and transforms it into a csv file.
     * @param out Output stream.
     * @param rows Rows of data from reporting-core
     * @param columns Columns to list on report
     */
    public void generateCsv(OutputStream out, List<Map<String, Object>> rows, List<ColumnDef> columns) {
        ICsvMapWriter csvWriter = null;
        try {
            csvWriter = new CsvMapWriter(new OutputStreamWriter(out), CsvPreference.STANDARD_PREFERENCE);
            // the header elements are used to map the bean values to each column (names must match)
            String[] header = new String[] {};
            CellProcessor[] processors = new CellProcessor[] {};
            if (columns != null)
            {
                header = (String[])CollectionUtils.collect(columns, new Transformer() {
                    @Override
                    public Object transform(Object input) {
                        ColumnDef column = (ColumnDef)input;
                        return column.getName();
                    }
                }).toArray(new String[0]) ;
                processors = (CellProcessor[]) CollectionUtils.collect(columns, new Transformer() {
                    @Override
                    public Object transform(Object input) {
                        return new Optional();
                    }
                }).toArray(new CellProcessor[0]);
            } else if (rows.size() > 0)
            {
                header = new ArrayList<String>(rows.get(0).keySet()).toArray(new String[0]);
                processors = (CellProcessor[]) CollectionUtils.collect(rows.get(0).keySet(), new Transformer() {
                    @Override
                    public Object transform(Object input) {
                        return new Optional();
                    }
                }).toArray(new CellProcessor[0]);
            }
            if (header.length > 0)
                csvWriter.writeHeader(header);
            if (rows != null)
                for (Map<String, Object> row : rows)
                {
                    csvWriter.write(row, header, processors);
                }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if( csvWriter != null ) {
                try {
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    /**
     * Takes the output and transforms it into a Excel file.
     * @param out Output stream.
     * @param rows Rows of data from reporting-core
     * @param columns Columns to list on report
     */
    public void generateXls(OutputStream out, List<Map<String, Object>> rows, List<ColumnDef> columns) {
        try {
            Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
            String safeName = WorkbookUtil.createSafeSheetName("Report"); // returns " O'Brien's sales   "
            Sheet reportSheet = wb.createSheet(safeName);

            short rowc = 0;
            Row nrow = reportSheet.createRow(rowc++);
            short cellc = 0;
            if (rows == null) return;
            if (columns == null && rows.size() > 0)
            {
                columns = new ArrayList<ColumnDef>(CollectionUtils.collect(rows.get(0).keySet(), new Transformer() {
                    @Override
                    public Object transform(final Object input) {
                        return new ColumnDef()
                        {{
                                setName((String)input);
                            }};
                    }
                }));
            }
            if (columns != null)
            {
                for (ColumnDef column : columns)
                {
                    Cell cell = nrow.createCell(cellc++);
                    cell.setCellValue(column.getName());
                }
            }
            for (Map<String, Object> row : rows)
            {
                nrow = reportSheet.createRow(rowc++);
                cellc = 0;
                for (ColumnDef column : columns)
                {
                    Cell cell = nrow.createCell(cellc++);
                    cell.setCellValue(String.valueOf(row.get(column.getName())));
                }
            }
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
