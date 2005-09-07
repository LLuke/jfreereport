/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ActivityReportDefinition.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ActivityReportDefinition.java,v 1.1 2005/05/18 15:36:25 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.onetomany;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;

import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.HideElementByNameFunction;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

public class ActivityReportDefinition
{
  private JFreeReport report;

  public ActivityReportDefinition (final JFreeReport report)
  {
    this.report = report;
  }

  public void configure ()
  {
    configureRecordGroup();
    configureItemBand();
    configureFunctions();
  }

  private void configureRecordGroup()
  {
    final Band b = new Band();
    b.setName("activitylog");
    b.getStyle().setStyleProperty(ElementStyleSheet.BOLD, Boolean.TRUE);
    b.getStyle().setStyleProperty(ElementStyleSheet.FONT, "SansSerif");
    b.getStyle().setStyleProperty(ElementStyleSheet.FONTSIZE, new Integer(12));
    b.setLayoutCacheable(false);

    LabelElementFactory labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(0, 0));
    labelFactory.setMinimumSize(new FloatDimension(200, 15));
    labelFactory.setText("Time");
    b.addElement(labelFactory.createElement());

    labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(200, 0));
    labelFactory.setMinimumSize(new FloatDimension(-100, 15));
    labelFactory.setText("Task");
    b.addElement(labelFactory.createElement());

    b.addElement (StaticShapeElementFactory.createHorizontalLine
            (null, null, new BasicStroke(1), 15));

    final Group group = report.getGroupByName("record-group");
    group.getHeader().addElement(b);
  }

  private void configureItemBand()
  {
    final Band b = new Band();
    b.setName("activitylog");
    b.getStyle().setStyleProperty(ElementStyleSheet.BOLD, Boolean.FALSE);
    b.getStyle().setStyleProperty(ElementStyleSheet.FONT, "SansSerif");
    b.getStyle().setStyleProperty(ElementStyleSheet.FONTSIZE, new Integer(10));
    b.setLayoutCacheable(false);

    TextFieldElementFactory textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("activitylog.Time");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(0, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(200, 12));
    b.addElement(textFieldFactory.createElement());

    textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("activitylog.Task");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(200, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(-100, 12));
    textFieldFactory.setDynamicHeight(Boolean.TRUE);
    b.addElement(textFieldFactory.createElement());

    report.getItemBand().addElement(b);
  }

  private void configureFunctions ()
  {
    final HideElementByNameFunction function = new HideElementByNameFunction();
    function.setName("hideActivity");
    function.setField("recordType");
    function.setElement("activitylog");
    report.addExpression(function);
  }
}

