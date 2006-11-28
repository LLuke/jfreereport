package org.jfree.report.test;

import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.modules.gui.swing.preview.PreviewDialog;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 21.02.2006, 14:11:22
 *
 * @author Thomas Morgner
 */
public class WorldDbDemoTest
{
  private WorldDbDemoTest()
  {
  }

  public static void main(String[] args)
          throws
          ResourceKeyCreationException, ResourceCreationException,
          ResourceLoadingException
  {
    JFreeReportBoot.getInstance().start();

    WorldDbDemoTest.processReport("/world-db.xml");
  }

  private static void processReport(String file)
          throws ResourceLoadingException,
          ResourceCreationException, ResourceKeyCreationException
  {
    URL url = WorldDbDemoTest.class.getResource(file);
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    Resource res = manager.createDirectly(url, JFreeReport.class);
    final JFreeReport resource = (JFreeReport) res.getResource();

    ReportJob job = new ReportJob(resource);
//    final TableReportDataFactory dataFactory =
//            new TableReportDataFactory("default", new CountryDataTableModel());
//    job.setDataFactory(dataFactory);

    PreviewDialog dialog = new PreviewDialog();
    dialog.setModal(true);
    dialog.setReportJob(job);
    dialog.setSize(500, 300);
    dialog.setVisible(true);
//
//    dialog = new PreviewDialog();
//    dialog.setModal(true);
//    dialog.setReportJob(job);
//    dialog.setSize(500, 300);
//    dialog.setVisible(true);
    System.exit(0);
  }
}
