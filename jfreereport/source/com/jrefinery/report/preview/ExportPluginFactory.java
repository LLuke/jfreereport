/**
 * Date: Feb 2, 2003
 * Time: 6:52:56 PM
 *
 * $Id: ExportPluginFactory.java,v 1.4 2003/02/04 17:56:17 taqua Exp $
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.util.ReportConfiguration;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;

public class ExportPluginFactory
{
  public static final String PLUGIN_ENABLE_PREFIX = "com.jrefinery.report.preview.plugin.";
  public static final String PLUGIN_PDF = "pdf";
  public static final String PLUGIN_CSV = "csv";
  public static final String PLUGIN_HTML = "html";
  public static final String PLUGIN_EXCEL = "excel";
  public static final String PLUGIN_PLAIN = "plain";

  protected ExportPlugin createExcelPlugIn (PreviewProxy proxy)
  {
    ExcelExportDialog excelExportDialog;

    if (proxy instanceof Frame)
    {
      excelExportDialog = new ExcelExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      excelExportDialog = new ExcelExportDialog((Dialog) proxy);
    }
    else
    {
      excelExportDialog = new ExcelExportDialog();
    }
    excelExportDialog.pack();
    return excelExportDialog;
  }

  protected ExportPlugin createHTMLPlugin (PreviewProxy proxy)
  {
    HtmlExportDialog htmlExportDialog;

    if (proxy instanceof Frame)
    {
      htmlExportDialog = new HtmlExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      htmlExportDialog = new HtmlExportDialog((Dialog) proxy);
    }
    else
    {
      htmlExportDialog = new HtmlExportDialog();
    }
    htmlExportDialog.pack();
    return htmlExportDialog;
  }

  protected ExportPlugin createPlainTextPlugin (PreviewProxy proxy)
  {
    PlainTextExportDialog plainTextExportDialog;

    if (proxy instanceof Frame)
    {
      plainTextExportDialog = new PlainTextExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      plainTextExportDialog = new PlainTextExportDialog((Dialog) proxy);
    }
    else
    {
      plainTextExportDialog = new PlainTextExportDialog();
    }
    plainTextExportDialog.pack();
    return plainTextExportDialog;
  }

  protected ExportPlugin createCSVPlugin (PreviewProxy proxy)
  {
    CSVExportDialog csvExportDialog;

    if (proxy instanceof Frame)
    {
      csvExportDialog = new CSVExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      csvExportDialog = new CSVExportDialog((Dialog) proxy);
    }
    else
    {
      csvExportDialog = new CSVExportDialog();
    }
    csvExportDialog.pack();
    return csvExportDialog;
  }

  protected ExportPlugin createPDFPlugin (PreviewProxy proxy)
  {
    PDFSaveDialog pdfSaveDialog;
    if (proxy instanceof Frame)
    {
      pdfSaveDialog = new PDFSaveDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      pdfSaveDialog = new PDFSaveDialog((Dialog) proxy);
    }
    else
    {
      pdfSaveDialog = new PDFSaveDialog();
    }
    pdfSaveDialog.pack();
    return pdfSaveDialog;
  }

  protected boolean isPluginEnabled (ReportConfiguration config, String plugin)
  {
    return config.getConfigProperty(PLUGIN_ENABLE_PREFIX + plugin, "false").equals("true");
  }

  /**
   * creates all available export plugins.
   * @param proxy
   * @return
   */
  public ArrayList createExportPlugIns (PreviewProxy proxy, ReportConfiguration config)
  {

    ArrayList retval = new ArrayList();

    if (isPluginEnabled(config, PLUGIN_PLAIN))
    {
      retval.add (createPlainTextPlugin(proxy));
    }
    if (isPluginEnabled(config, PLUGIN_PDF))
    {
      retval.add (createPDFPlugin(proxy));
    }
    if (isPluginEnabled(config, PLUGIN_EXCEL))
    {
      retval.add (createExcelPlugIn(proxy));
    }
    if (isPluginEnabled(config, PLUGIN_HTML))
    {
      retval.add (createHTMLPlugin(proxy));
    }
    if (isPluginEnabled(config, PLUGIN_CSV))
    {
      retval.add (createCSVPlugin(proxy));
    }
    return retval;
  }
}
