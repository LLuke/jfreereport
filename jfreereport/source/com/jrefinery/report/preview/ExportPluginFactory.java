/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------------
 * ExportPluginFactory.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.preview;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;

import com.jrefinery.report.util.ReportConfiguration;

/**
 * An export plug-in factory.
 * 
 * @author Thomas Morgner.
 */
public class ExportPluginFactory
{
  /** The plug-in enable prefix. */
  public static final String PLUGIN_ENABLE_PREFIX = "com.jrefinery.report.preview.plugin.";

  /** The PDF plug-in key. */
  public static final String PLUGIN_PDF = "pdf";

  /** The CSV plug-in key. */
  public static final String PLUGIN_CSV = "csv";

  /** The HTML plug-in key. */
  public static final String PLUGIN_HTML = "html";

  /** The EXCEL plug-in key. */
  public static final String PLUGIN_EXCEL = "excel";

  /** The PLAIN plug-in key. */
  public static final String PLUGIN_PLAIN = "plain";

  /**
   * Creates an Excel plug-in.
   * 
   * @param proxy  the preview proxy.
   * 
   * @return The plug-in.
   */
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

  /**
   * Creates an HTML plug-in.
   * 
   * @param proxy  the preview proxy.
   * 
   * @return The plug-in.
   */
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

  /**
   * Creates a plain text plug-in.
   * 
   * @param proxy  the preview proxy.
   * 
   * @return The plug-in.
   */
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

  /**
   * Creates a CSV plug-in.
   * 
   * @param proxy  the preview proxy.
   * 
   * @return The plug-in.
   */
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

  /**
   * Creates a PDF plug-in.
   * 
   * @param proxy  the preview proxy.
   * 
   * @return The plug-in.
   */
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

  /**
   * Returns true if the plug-in is enabled for a given report configuration, and false otherwise.
   *
   * @param config  the report configuration.
   * @param plugin  the plug-in key.
   * 
   * @return A boolean.
   */
  protected boolean isPluginEnabled (ReportConfiguration config, String plugin)
  {
    return config.getConfigProperty(PLUGIN_ENABLE_PREFIX + plugin, "false").equals("true");
  }

  /**
   * Creates a list containing all available export plugins.
   * 
   * @param proxy  the preview proxy.
   * @param config  the report configuration.
   * 
   * @return  The list of export plugins.
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
