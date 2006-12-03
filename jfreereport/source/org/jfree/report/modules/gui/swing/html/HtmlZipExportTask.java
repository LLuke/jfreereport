/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.swing.html;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.io.IOUtils;
import org.jfree.layouting.modules.output.html.FlowHtmlOutputProcessor;
import org.jfree.layouting.modules.output.html.HtmlOutputProcessor;
import org.jfree.layouting.modules.output.html.HtmlPrinter;
import org.jfree.layouting.modules.output.html.PageableHtmlOutputProcessor;
import org.jfree.layouting.modules.output.html.StreamingHtmlOutputProcessor;
import org.jfree.report.ReportConfigurationException;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.streaming.StreamingReportProcessor;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.RepositoryUtilities;
import org.jfree.repository.zipwriter.ZipRepository;
import org.jfree.util.Log;

/**
 * Creation-Date: 02.12.2006, 14:15:14
 *
 * @author Thomas Morgner
 */
public class HtmlZipExportTask implements Runnable
{
  private ReportJob job;
  private String dataDirectory;
  private String exportMethod;
  private String filename;
  private File targetFile;
  private String encoding;

  public HtmlZipExportTask(final ReportJob job)
      throws ReportConfigurationException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }
    this.job = job;

    final ModifiableConfiguration config = job.getConfiguration();
    dataDirectory = config.getConfigProperty
        ("org.jfree.report.modules.gui.common.html.zip.DataDirectory");
    String targetFileName = config.getConfigProperty
        ("org.jfree.report.modules.gui.common.html.zip.TargetFileName");
    exportMethod = config.getConfigProperty
        ("org.jfree.report.modules.gui.common.html.zip.ExportMethod");
    encoding = config.getConfigProperty
        ("org.jfree.report.modules.gui.common.html.zip.Encoding", "ASCII");

    targetFile = new File(targetFileName);
    filename = IOUtils.getInstance().stripFileExtension(targetFile.getName());

    if (targetFile.exists())
    {
      // lets try to delete it ..
      if (targetFile.delete() == false)
      {
        throw new ReportConfigurationException
            ("Target-File exists, but cannot be removed.");
      }
    }
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used to
   * create a thread, starting the thread causes the object's <code>run</code>
   * method to be called in that separately executing thread.
   * <p/>
   * The general contract of the method <code>run</code> is that it may take any
   * action whatsoever.
   *
   * @see Thread#run()
   */
  public void run()
  {
    OutputStream fout = null;
    try
    {
      fout = new BufferedOutputStream(new FileOutputStream(targetFile));
      final ZipRepository zipRepository = new ZipRepository(fout);
      final ContentLocation root = zipRepository.getRoot();
      final ContentLocation data = RepositoryUtilities.createLocation
          (zipRepository, RepositoryUtilities.split(dataDirectory, "/"));
      final StreamingReportProcessor sp = new StreamingReportProcessor();

      final HtmlOutputProcessor outputProcessor = createOutputProcessor();
      final HtmlPrinter printer = outputProcessor.getPrinter();

      printer.setContentWriter(root, new DefaultNameGenerator(root, filename));
      printer.setDataWriter(data, new DefaultNameGenerator(data, "content"));
      printer.setEncoding(encoding);

      sp.setOutputProcessor(outputProcessor);
      sp.processReport(job);
    }
    catch(Exception e)
    {
      Log.error ("ZIP-Export failed. ", e);
    }
    finally
    {
      try
      {
        job.close();
        job = null;
      }
      catch(Exception e)
      {
        // ignore ..
      }

      if (fout != null)
      {
        try
        {
          fout.close();
        }
        catch(Exception e)
        {
          // ignored ..
        }
      }

    }
  }

  protected HtmlOutputProcessor createOutputProcessor()
  {
    if ("pageable".equals(exportMethod))
    {
      return new PageableHtmlOutputProcessor(job.getConfiguration());
    }
    else if ("flow".equals(exportMethod))
    {
      return new FlowHtmlOutputProcessor(job.getConfiguration());
    }
    else
    {
      return new StreamingHtmlOutputProcessor(job.getConfiguration());
    }
  }
}
