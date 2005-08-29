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
 * PeopleReportDefinition.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PeopleReportDefinition.java,v 1.1 2005/05/18 15:36:25 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.onetomany;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.BasicStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.Group;
import org.jfree.report.ElementAlignment;
import org.jfree.report.function.PageOfPagesFunction;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.DateFieldElementFactory;
import org.jfree.ui.FloatDimension;

public class PeopleReportDefinition
{
  private JFreeReport report;

  public PeopleReportDefinition ()
  {
    report = new JFreeReport();
    report.setName("People Report (API)");
    configurePeopleGroup();
    configureRecordGroup();

    // now the groups ... configured by an external class (as if it would be included)
    final ActivityReportDefinition activityDef = new ActivityReportDefinition(report);
    activityDef.configure();

    final LunchReportDefinition lunchDef = new LunchReportDefinition(report);
    lunchDef.configure();

    final OfficeReportDefinition officeDef = new OfficeReportDefinition(report);
    officeDef.configure();

    configurePageHeader();
    configurePageFooter();
    configureFunctions();
  }

  private void configureFunctions ()
  {
    report.setPropertyMarked("report.date", true);

    final PageOfPagesFunction pageFunction = new PageOfPagesFunction();
    pageFunction.setName("pageXofY");
    pageFunction.setFormat("Page {0} of {1}");
    report.addExpression(pageFunction);

  }

  private void configurePeopleGroup()
  {
    final Group group = new Group();
    group.setName("person-group");
    group.addField("person.name");

    LabelElementFactory labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(0, 0));
    labelFactory.setMinimumSize(new FloatDimension(160, 12));
    labelFactory.setText("Name:");
    group.getHeader().addElement(labelFactory.createElement());

    TextFieldElementFactory textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("person.name");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(170, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(-100, 12));
    group.getHeader().addElement(textFieldFactory.createElement());

    labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(0, 15));
    labelFactory.setMinimumSize(new FloatDimension(160, 12));
    labelFactory.setText("Address:");
    group.getHeader().addElement(labelFactory.createElement());

    textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("person.address");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(170, 15));
    textFieldFactory.setMinimumSize(new FloatDimension(-100, 12));
    group.getHeader().addElement(textFieldFactory.createElement());

    group.getFooter().setMinimumSize(new FloatDimension(0, 15));

    report.addGroup(group);
  }

  private void configureRecordGroup()
  {
    final Group group = new Group();
    group.setName("record-group");
    group.addField("person.name");
    group.addField("recordType");

    report.addGroup(group);
  }

  private void configurePageHeader ()
  {
    final ElementStyleSheet style = report.getPageHeader().getStyle();
    report.getPageHeader().setDisplayOnFirstPage(true);
    style.setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, 24));
    style.setStyleProperty(ElementStyleSheet.FONT, "Serif");
    style.setStyleProperty(ElementStyleSheet.FONTSIZE, new Integer(10));

    report.getPageHeader().addElement
            (StaticShapeElementFactory.createRectangleShapeElement
            (null, new Color(0xAFAFAF), null,
                    new Rectangle2D.Float(0, 0, -100, -100), false, true));

    report.getPageHeader().addElement
            (StaticShapeElementFactory.createHorizontalLine
            (null, null, new BasicStroke(1), 18));

    final DateFieldElementFactory elementFactory = new DateFieldElementFactory();
    elementFactory.setAbsolutePosition(new Point2D.Float(-50, 0));
    elementFactory.setMinimumSize(new FloatDimension(-50, -100));
    elementFactory.setVerticalAlignment(ElementAlignment.MIDDLE);
    elementFactory.setHorizontalAlignment(ElementAlignment.RIGHT);
    elementFactory.setFormatString("d-MMM-yyyy");
    elementFactory.setFieldname("report.date");
    report.getPageHeader().addElement(elementFactory.createElement());
  }

  private void configurePageFooter ()
  {
    final ElementStyleSheet style = report.getPageFooter().getStyle();
    report.getPageHeader().setDisplayOnFirstPage(true);
    style.setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, 24));

    report.getPageFooter().addElement
            (StaticShapeElementFactory.createRectangleShapeElement
            (null, new Color(0xAFAFAF), null,
                    new Rectangle2D.Float(0, 0, -100, -100), false, true));

    report.getPageFooter().addElement
            (StaticShapeElementFactory.createHorizontalLine
            (null, null,  new BasicStroke(1), 0));

    final TextFieldElementFactory elementFactory = new TextFieldElementFactory();
    elementFactory.setAbsolutePosition(new Point2D.Float(0, 4));
    elementFactory.setMinimumSize(new FloatDimension(-100, -100));
    elementFactory.setVerticalAlignment(ElementAlignment.MIDDLE);
    elementFactory.setHorizontalAlignment(ElementAlignment.RIGHT);
    elementFactory.setFieldname("pageXofY");
    report.getPageFooter().addElement(elementFactory.createElement());
  }


  public JFreeReport getReport ()
  {
    return report;
  }
}
