package org.jfree.report.ext.junit.bugs;

import java.awt.geom.Point2D;

import javax.swing.table.DefaultTableModel;

import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ext.junit.base.functionality.FunctionalityTestLib;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

/**
 * Creation-Date: 27.10.2005, 16:55:00
 *
 Column: 0 Name = crew; DataType = class java.lang.Object
 Column: 1 Name = trial; DataType = class java.lang.Object
 Column: 2 Name = run; DataType = class java.lang.Object
 Column: 3 Name = FloatVar; DataType = class java.lang.Object
 Column: 4 Name = IntVar; DataType = class java.lang.Object
 Column: 5 Name = run; DataType = class java.lang.Object
 Column: 6 Name = clock; DataType = class java.lang.Object

 * @author Thomas Morgner
 */
public class InvalidColumnBug
{
  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();
    String[] COLNAMES = {
            "crew", "trial", "run", "FloatVar", "IntVar", "run", "clock"
    };

    final DefaultTableModel tableModel = new DefaultTableModel(COLNAMES,8000);

    final JFreeReport report = new JFreeReport();
    report.setData(tableModel);

    final Group group = new Group();
    group.setName("Run");
    group.addField("run");

    LabelElementFactory labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(0, 0));
    labelFactory.setMinimumSize(new FloatDimension(160, 12));
    labelFactory.setText("Crew:");
    group.getHeader().addElement(labelFactory.createElement());

    TextFieldElementFactory textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("crew");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(50, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(-100, 12));
    group.getHeader().addElement(textFieldFactory.createElement());

    labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(100, 0));
    labelFactory.setMinimumSize(new FloatDimension(160, 12));
    labelFactory.setText("Trial:");
    group.getHeader().addElement(labelFactory.createElement());

    textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("trial");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(150, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(-100, 12));
    group.getHeader().addElement(textFieldFactory.createElement());

    labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(200, 0));
    labelFactory.setMinimumSize(new FloatDimension(160, 12));
    labelFactory.setText("Run:");
    group.getHeader().addElement(labelFactory.createElement());

    textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("run");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(250, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(-100, 12));
    group.getHeader().addElement(textFieldFactory.createElement());

    group.getFooter().setMinimumSize(new FloatDimension(0, 15));

    report.addGroup(group);

    final Band b = new Band();
    b.setName("variables");
    b.getStyle().setStyleProperty(ElementStyleSheet.BOLD, Boolean.FALSE);
    b.getStyle().setStyleProperty(ElementStyleSheet.FONT, "SansSerif");
    b.getStyle().setStyleProperty(ElementStyleSheet.FONTSIZE, new Integer(10));
//    b.setLayoutCacheable(false);

    for (int i = 3, max = tableModel.getColumnCount(); i < max; ++i)
    {
      TextFieldElementFactory tFF = new TextFieldElementFactory();
      tFF.setFieldname(tableModel.getColumnName(i));
      tFF.setAbsolutePosition(new Point2D.Float(200 * (i - 3), 0));
      tFF.setMinimumSize(new FloatDimension(200, 12));
      b.addElement(tFF.createElement());
    }
    report.getItemBand().addElement(b);

    if (FunctionalityTestLib.execGraphics2D(report) == false)
      throw new NullPointerException();
  }
}
