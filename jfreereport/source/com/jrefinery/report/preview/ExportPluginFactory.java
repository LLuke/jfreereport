/**
 * Date: Feb 2, 2003
 * Time: 6:52:56 PM
 *
 * $Id: ExportPluginFactory.java,v 1.1 2003/02/02 22:47:39 taqua Exp $
 */
package com.jrefinery.report.preview;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;

public class ExportPluginFactory
{
  /**
   * creates all available export plugins.
   * @param proxy
   * @return
   */
  public ArrayList createExportPlugIns (PreviewProxy proxy)
  {
    PDFSaveDialog pdfSaveDialog = null;
    ExcelExportDialog excelExportDialog = null;
    HtmlExportDialog htmlExportDialog = null;
    CSVExportDialog csvExportDialog = null;

    if (proxy instanceof Frame)
    {
      pdfSaveDialog = new PDFSaveDialog((Frame) proxy);
      excelExportDialog = new ExcelExportDialog((Frame) proxy);
      htmlExportDialog = new HtmlExportDialog((Frame) proxy);
      csvExportDialog = new CSVExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      pdfSaveDialog = new PDFSaveDialog((Dialog) proxy);
      excelExportDialog = new ExcelExportDialog((Dialog) proxy);
      htmlExportDialog = new HtmlExportDialog((Dialog) proxy);
      csvExportDialog = new CSVExportDialog((Dialog) proxy);
    }
    else
    {
      pdfSaveDialog = new PDFSaveDialog();
      excelExportDialog = new ExcelExportDialog();
      htmlExportDialog = new HtmlExportDialog();
      csvExportDialog = new CSVExportDialog();
    }
    pdfSaveDialog.pack();
    excelExportDialog.pack();
    htmlExportDialog.pack();
    csvExportDialog.pack();

    ArrayList retval = new ArrayList();
    retval.add (pdfSaveDialog);
    retval.add (excelExportDialog);
    retval.add (htmlExportDialog);
    retval.add (csvExportDialog);
    return retval;
  }
}
