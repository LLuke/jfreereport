/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------
 * PageHeaderBug.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------------------------
 * 16.07.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.bugs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ItemBand;
import org.jfree.report.JFreeReport;
import org.jfree.report.PageFooter;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportFooter;
import org.jfree.report.ReportHeader;
import org.jfree.report.demo.SampleData1;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.NumberFieldElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.BandStyleSheet;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;

public class PageHeaderBug extends JFreeReport
{
  String id = "??";
  String debh = "????";
  String fxgc = "????";
  String gcl = "???";
  String dw = "??";
  String sbf = "???";
  String azf = "???";
  String clf = "???";
  String rgf = "???";
  String jjf = "???";


  public PageHeaderBug()
  {
    PageFormat pageFormat = new PageFormat();
    Paper paper = new Paper();
    paper.setSize(844, 696);
    paper.setImageableArea(20, 38, 804, 625);

    pageFormat.setPaper(paper);

    try
    {
      this.setData(new SampleData1());
      this.setPageHeader(createPageHeader());
      this.setItemBand(createItemBand());
      this.setReportFooter(createReportFooter());
      this.setReportHeader(createReportHeader());
      this.setPageFooter(createPageFooter());
    }
    catch (Exception ex)
    {
    }
    this.getDefaultPageFormat().setOrientation(PageFormat.LANDSCAPE);
    this.setDefaultPageFormat(pageFormat);

  }

  private PageHeader createPageHeader()
  {

    PageHeader pageHeader = new PageHeader();
    pageHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, 60));
    pageHeader.getBandDefaults().setFontDefinitionProperty(new FontDefinition("??", 12));
    pageHeader.setDisplayOnFirstPage(false);
    pageHeader.setDisplayOnLastPage(true);
    Element ttile = LabelElementFactory.createLabelElement
        ("title", new Rectangle2D.Float(0, 0, 804, 30), null,
        ElementAlignment.CENTER, new FontDefinition("??", 1), "? ? ? ? ? ? ? ? ? ? ? ?");
    pageHeader.addElement(ttile);
    Element eid = LabelElementFactory.createLabelElement("mid", new Rectangle2D.Float(0, 42, 30, 20)
        , null, ElementAlignment.CENTER, null, id);
    Element l1 = LabelElementFactory.createLabelElement("l1", new Rectangle2D.Float(400, 32, 80, 20)
        , null, ElementAlignment.CENTER, null, "? ?");
    Element l2 = LabelElementFactory.createLabelElement("l2", new Rectangle2D.Float(600, 32, 80, 20)
        , null, ElementAlignment.CENTER, null, "? ?");
    Element edebh = LabelElementFactory.createLabelElement("mdebh", new Rectangle2D.Float(30, 42, 50, 20)
        , null, ElementAlignment.CENTER, null, debh);
    Element efxgc = LabelElementFactory.createLabelElement("mfxgc", new Rectangle2D.Float(80, 42, 110, 20)
        , null, ElementAlignment.CENTER, null, fxgc);
    Element egcl = LabelElementFactory.createLabelElement("mgcl", new Rectangle2D.Float(190, 42, 55, 20)
        , null, ElementAlignment.CENTER, null, gcl);
    Element edw = LabelElementFactory.createLabelElement("mdw", new Rectangle2D.Float(245, 42, 60, 20)
        , null, ElementAlignment.CENTER, null, dw);
    Element esbf = LabelElementFactory.createLabelElement("msbf", new Rectangle2D.Float(305, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, sbf);
    Element eazf = LabelElementFactory.createLabelElement("mazf", new Rectangle2D.Float(355, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, azf);
    Element ergf = LabelElementFactory.createLabelElement("mrgf", new Rectangle2D.Float(405, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, rgf);
    Element eclf = LabelElementFactory.createLabelElement("mclf", new Rectangle2D.Float(455, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, clf);
    Element ejjf = LabelElementFactory.createLabelElement("mjjf", new Rectangle2D.Float(505, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, jjf);
    Element eesbf = LabelElementFactory.createLabelElement("mssbf", new Rectangle2D.Float(555, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, sbf);
    Element eeazf = LabelElementFactory.createLabelElement("msazf", new Rectangle2D.Float(605, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, azf);
    Element eergf = LabelElementFactory.createLabelElement("msrgf", new Rectangle2D.Float(655, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, rgf);
    Element eeclf = LabelElementFactory.createLabelElement("msclf", new Rectangle2D.Float(705, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, clf);
    Element eejjf = LabelElementFactory.createLabelElement("msjjf", new Rectangle2D.Float(755, 48, 50, 20)
        , null, ElementAlignment.CENTER, null, jjf);

    pageHeader.addElement(l1);
    pageHeader.addElement(l2);
    pageHeader.addElement(eid);
    pageHeader.addElement(edebh);
    pageHeader.addElement(efxgc);
    pageHeader.addElement(egcl);
    pageHeader.addElement(edw);
    pageHeader.addElement(esbf);
    pageHeader.addElement(eazf);
    pageHeader.addElement(ergf);
    pageHeader.addElement(eclf);
    pageHeader.addElement(ejjf);
    pageHeader.addElement(eesbf);
    pageHeader.addElement(eeazf);
    pageHeader.addElement(eergf);
    pageHeader.addElement(eeclf);
    pageHeader.addElement(eejjf);


    pageHeader.addElement(
        StaticShapeElementFactory.createLineShapeElement(
            "line1",
            Color.decode("#CFCFCF"),
            new BasicStroke(2),
            new Line2D.Float(0, 30, 0, 30)
        )
    );

    pageHeader.addElement(
        StaticShapeElementFactory.createLineShapeElement(
            "line1",
            Color.decode("#CFCFCF"),
            new BasicStroke(2),
            new Line2D.Float(30, 60, 30, 60)
        )
    );

    pageHeader.addElement(
        StaticShapeElementFactory.createLineShapeElement(
            "line1",
            Color.decode("#CFCFCF"),
            new BasicStroke(0.5f),
            new Line2D.Float(305, 46, 804, 46)
        )
    );

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("li", Color.decode("#CFCFCF"), new BasicStroke(1),
        new Line2D.Float(0, 30, 0, 6)));
    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("as", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(30, 30, 30, 6)));
    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(804, 30, 804, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(755, 46, 755, 6)));


    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(705, 46, 705, 6)));


    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(655, 46, 655, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(605, 46, 605, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(555, 30, 555, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(505, 46, 505, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(455, 46, 455, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(405, 46, 405, 6)));
    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(355, 46, 355, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(305, 30, 305, 6)));

    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(245, 30, 245, 6)));


    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(190, 30, 190, 6)));


    pageHeader.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(80, 30, 80, 6)));


    return pageHeader;
  }

  private ItemBand createItemBand ()
  {
    ItemBand items = new ItemBand();
    items.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, 18));
    items.getBandDefaults().setFontDefinitionProperty(new FontDefinition("??", 12));
    items.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, Color.black);

    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(804, 0, 804, 1)));


    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(755, 0, 755, 1)));

    items.addElement(StaticShapeElementFactory.createLineShapeElement("dad", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(705, 0, 705, 1)));


    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(655, 0, 655, 1)));


    items.addElement(
        StaticShapeElementFactory.createLineShapeElement(
            "bottom",
            Color.decode("#CFCFCF"),
            new BasicStroke(0.1f),
            new Line2D.Float(0, 17, 0, 17)
        )
    );

    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(605, 0, 605, 1)));


    items.addElement(StaticShapeElementFactory.createLineShapeElement("dk", Color.decode("#CFCFCF"),
        new BasicStroke(1), new Line2D.Float(0, 0, 0, 1)));
    items.addElement(StaticShapeElementFactory.createLineShapeElement("dk", Color.decode("#CFCFCF"),
        new BasicStroke(0.2f), new Line2D.Float(30, 0, 30, 1)));


    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(555, 0, 555, 1)));

    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(505, 0, 505, 1)));

    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(455, 0, 455, 1)));

    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(405, 0, 405, 1)));
    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(355, 0, 355, 1)));

    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(305, 0, 305, 1)));

    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(245, 0, 245, 1)));


    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(190, 0, 190, 1)));


    items.addElement(StaticShapeElementFactory.createLineShapeElement("da", Color.decode("#CFCFCF"), new BasicStroke(0.2f),
        new Line2D.Float(80, 0, 80, 1)));


    items.addElement(
        TextFieldElementFactory.createStringElement(
            "myid",
            new Rectangle2D.Float(0, 0, 30, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "id"
        )
    );

    items.addElement(
        TextFieldElementFactory.createStringElement(
            "mydebh",
            new Rectangle2D.Float(30, 0, 50, 17),
            null,
            ElementAlignment.LEFT, ElementAlignment.MIDDLE,
            null,
            "<null>",
            "debh"
        )
    );
    items.addElement(
        TextFieldElementFactory.createStringElement(
            "myfxgc",
            new Rectangle2D.Float(80, 0, 110, 17),
            null,
            ElementAlignment.LEFT, ElementAlignment.MIDDLE,
            null,
            "<null>",
            "fxgc"
        )
    );
    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "mygcl",
            new Rectangle2D.Float(190, 0, 35, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "gcl"
        )
    );

    items.addElement(
        TextFieldElementFactory.createStringElement(
            "mydw",
            new Rectangle2D.Float(245, 0, 55, 17),
            null,
            ElementAlignment.LEFT, ElementAlignment.MIDDLE,
            null,
            "0",
            "dw"
        )
    );

    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "mysbf",
            new Rectangle2D.Float(305, 0, 40, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "sbf"
        )
    );
    Element azf = NumberFieldElementFactory.createNumberElement("azf",
        new Rectangle2D.Float(355, 0, 50, 17),
        null,
        ElementAlignment.CENTER, ElementAlignment.MIDDLE,
        null,
        "0",
        "0.00",
        "azf"
    );
    items.addElement(
        azf);

    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(405, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "rgf"
        )
    );


    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(455, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "clf"
        )
    );

    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(505, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "jjf"
        )
    );
    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(555, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "rsbf"
        )
    );

    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(605, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "razf"
        )
    );

    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(655, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "rrgf"
        )
    );

    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(705, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "rclf"
        )
    );
    items.addElement(
        NumberFieldElementFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(755, 0, 50, 17),
            null,
            ElementAlignment.CENTER, ElementAlignment.MIDDLE,
            null,
            "0",
            "0.00",
            "rjjf"
        )
    );


    return items;
  }

  private PageFooter createPageFooter()
  {
    PageFooter pageFooter = new PageFooter();
    pageFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, 18));
    pageFooter.getBandDefaults().setFontDefinitionProperty(new FontDefinition("??", 12));
    pageFooter.setDisplayOnFirstPage(true);
    pageFooter.setDisplayOnLastPage(true);
    return pageFooter;
  }

  private ReportFooter createReportFooter ()
  {
    ReportFooter reportFooter = new ReportFooter();
    reportFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, 18));
    return reportFooter;
  }

  private ReportHeader createReportHeader()
  {
    ReportHeader reportHeader = new ReportHeader();
    reportHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, 600));
    reportHeader.getBandDefaults().setFontDefinitionProperty(new FontDefinition("??", 12));
    reportHeader.getStyle().setBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER, true);
    Element edebh = LabelElementFactory.createLabelElement("mdebh", new Rectangle2D.Float(30, 100, 50, 20)
        , null, ElementAlignment.CENTER, null, debh);
    reportHeader.addElement(edebh);
    return reportHeader;
  }

  public static void main
      (String[] args)
  {
    try
    {
      PageHeaderBug rp = new PageHeaderBug();
      PreviewFrame pf = new PreviewFrame(rp);
      pf.setVisible(true);
      pf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    }
    catch (Exception ex)
    {
    }
  }

}