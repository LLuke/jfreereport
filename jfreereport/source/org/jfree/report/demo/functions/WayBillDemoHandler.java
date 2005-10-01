package org.jfree.report.demo.functions;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.util.ObjectUtilities;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 01.10.2005, 11:47:14
 *
 * @author Thomas Morgner
 */
public class WayBillDemoHandler extends AbstractXmlDemoHandler
{
  private WayBillTableModel tableModel;

  public WayBillDemoHandler()
  {
    tableModel = new WayBillTableModel();
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container A", "Glass Pearls", "Fragile", 5000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container A", "Chinese Silk", "Keep Dry", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container A", "Incense", "", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Palladium", "", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Tungsten", "", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Grain", "Keep Dry", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Scottish Whiskey", "Stay Dry!", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Notes", "Note", "This freight is dutyable.", 0));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Notes", "Note", "Customs paid on 2005-08-12 12:00.", 0));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Notes", "Note", "Customs bill id: NY-A32ZY48473", 0));
  }

  public String getDemoName()
  {
    return "Way-Bill Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(tableModel);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("waybill.html", WayBillDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(tableModel);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("waybill.xml", WayBillDemoHandler.class);
  }


  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final WayBillDemoHandler demoHandler = new WayBillDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
