/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * CSVRawExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CSVRawExportTask.java,v 1.11 2005/03/24 22:24:54 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.output.csv.CSVProcessor;
import org.jfree.report.util.Log;

/**
 * An export task implementation that writes an report into a CSV file, and uses the raw
 * target to create layouted content.
 *
 * @author Thomas Morgner
 */
public class CSVRawExportTask extends ExportTask
{
  /**
   * The name of the output file.
   */
  private final String fileName;
  /**
   * The encoding to be used for the file.
   */
  private final String encoding;
  /**
   * The report that should be exported.
   */
  private final JFreeReport report;

  /**
   * Creates a new CSV export task.
   *
   * @param fileName the filename of the target file
   * @param encoding the encoding for the generated output
   * @param report   the report that should be exported.
   */
  public CSVRawExportTask
          (final String fileName, final String encoding,
           final JFreeReport report)
  {
    if (fileName == null)
    {
      throw new NullPointerException("File name is null.");
    }
    if (report == null)
    {
      throw new NullPointerException("Report is null.");
    }
    if (encoding == null)
    {
      throw new NullPointerException("Encoding is null.");
    }
    this.fileName = fileName;
    this.report = report;
    this.encoding = encoding;
  }

  /**
   * Exports the report into a CSV file.
   */
  protected void performExport ()
  {
    Writer out = null;
    final File file = new File(fileName);
    try
    {
      final File directory = file.getAbsoluteFile().getParentFile();
      if (directory != null)
      {
        if (directory.exists() == false)
        {
          if (directory.mkdirs() == false)
          {
            Log.warn("Can't create directories. Hoping and praying now..");
          }
        }
      }

      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));

      final CSVProcessor target = new CSVProcessor(report);
      target.setWriter(out);
      target.processReport();
      out.close();
      out = null;
      setTaskDone();
    }
    catch (ReportInterruptedException re)
    {
      setTaskAborted();
      try
      {
        out.close();
        out = null;
        if (file.delete() == false)
        {
          Log.warn(new Log.SimpleMessage("Unable to delete incomplete export:", file));
        }
      }
      catch (SecurityException se)
      {
        // ignore me
      }
      catch (IOException ioe)
      {
        // ignore me...
      }
    }
    catch (Exception re)
    {
      Log.error("Exporting failed .", re);
      setTaskFailed(re);
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        setTaskFailed(e);
        Log.error("Unable to close the output stream.", e);
      }
    }
  }
}
