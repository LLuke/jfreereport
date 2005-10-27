package org.jfree.report.ext.junit.base.functionality;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.util.ReportProperties;
import org.jfree.report.demo.helper.InternalDemoHandler;
import org.jfree.util.Log;

/**
 * Creation-Date: 27.10.2005, 15:24:14
 *
 * @author Thomas Morgner
 */
public class SerializationTest extends TestCase
{
  public SerializationTest()
  {
  }

  public SerializationTest(String string)
  {
    super(string);
  }

  protected void setUp() throws Exception
  {
    JFreeReportBoot.getInstance().start();
  }

  public void testSerializeReport() throws Exception
  {
    InternalDemoHandler[] handlers = FunctionalityTestLib.getAllDemoHandlers();
    for (int i = 0; i < handlers.length; i++)
    {
      final InternalDemoHandler handler = handlers[i];
      Log.debug("Starting to read " + handler.getDemoName());

      final ByteArrayOutputStream bo = new ByteArrayOutputStream();
      final JFreeReport report = handler.createReport();
      final TableModel model = report.getData();

      // we don't test whether our demo models are serializable :)
      report.setData(new DefaultTableModel());
      // clear all report properties, which may cause trouble ...
      final ReportProperties p = report.getProperties();
      final Iterator keys = p.keys();
      while (keys.hasNext())
      {
        String key = (String) keys.next();
        if (p.get(key) instanceof Serializable == false)
        {
          p.put(key, null);
        }
      }

      try
      {
        final ObjectOutputStream oout = new ObjectOutputStream(bo);
        oout.writeObject(report);
        oout.close();
      }
      catch (Exception e)
      {
        Log.debug("Failed to write " + handler.getDemoName(), e);
        fail();
      }

      try
      {
        final ByteArrayInputStream bin = new ByteArrayInputStream(bo.toByteArray());
        final ObjectInputStream oin = new ObjectInputStream(bin);
        final JFreeReport report2 = (JFreeReport) oin.readObject();
        report2.setData(model);

        assertTrue(FunctionalityTestLib.execGraphics2D(report2));
      }
      catch (Exception e)
      {
        Log.debug("Failed to read " + handler.getDemoName(), e);
        fail();
      }
    }
  }

}
