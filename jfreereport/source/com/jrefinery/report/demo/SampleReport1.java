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
 * ------------------
 * SampleReport1.java
 * ------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SampleReport1.java,v 1.20 2003/05/02 12:39:38 taqua Exp $
 *
 * Changes:
 * --------
 * 19-Jun-2002 : Initial version
 * 28-Nov-2002 : Added vertical alignment parameter (DG);
 * 10-Dec-2002 : Minor Javadoc changes (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.Group;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.GroupList;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.function.ElementVisibilitySwitchFunction;
import com.jrefinery.report.function.ExpressionCollection;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.function.ItemSumFunction;
import com.jrefinery.report.targets.style.ElementStyleSheet;

/**
 * This creates a report similar to the one defined by report1.xml.
 *
 * @author Thomas Morgner
 */
public class SampleReport1
{
  /**
   <pre>
   <pageheader height="18" fontname="Serif" fontstyle="plain" fontsize="10" onfirstpage="true">
   <rectangle x="0" y="0" width="100%" height="100%" color="#AFAFAF" weight="0"/>
   <date-function name="date1" x="0" y="0" width="100%" height="14" alignment="right"
   baseline="12" format="d-MMM-yyyy" function="report_date"/>
   <line name="line1" x1="0" y1="16" x2="0" y2="16" color="#CFCFCF" weight="2.0"/>
   </pageheader>
   </pre>
   *
   * @return the page header.
   */
  private PageHeader createPageHeader()
  {
    PageHeader header = (PageHeader)
        ItemFactory.createPageHeader(18, new Font("Serif", Font.PLAIN, 10), null, true, false);
    // is by default true, but it is defined in the xml template, so I add it here too.
    header.addElement(
        ItemFactory.createRectangleShapeElement(
            "anonymous",
            Color.decode("#AFAFAF"),
            new BasicStroke(0),
            new Rectangle2D.Float(0, 0, -100, -100),
            false,
            true
        )
    );
    header.addElement(
        ItemFactory.createDateElement(
            "date1",
            new Rectangle2D.Float(0, 0, -100, 14),
            null,
            ElementAlignment.RIGHT.getOldAlignment(), 
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "<null>",
            "d-MMM-yyyy",
            "report.date"
        )
    );
    header.addElement(
        ItemFactory.createLineShapeElement(
            "line1",
            Color.decode("#CFCFCF"),
            new BasicStroke(2),
            new Line2D.Float(0, 16, 0, 16)
        )
    );
    return header;
  }

  /**
   <pre>
   <reportfooter height="48" fontname="Serif" fontstyle="bold" fontsize="16">
   <label name="Label 2" x="0" y="0" width="100%" height="24"
   alignment="center">*** END OF REPORT ***</label>
   </reportfooter>
   </pre>
   *
   * @return the page footer.
   */
  /*
  private PageFooter createPageFooter ()
  {
    PageFooter footer = (PageFooter) ItemFactory.createPageFooter (18, null, null, false, false);
    return footer;
  }
*/
  /** A constant for 100% width. */
  private static final float WIDTH = -100;

  /**
   * Creates a page footer.
   * 
   * @return The page footer. 
   */
  private PageFooter createPageFooter()
  {
    PageFooter pageFooter = (PageFooter) ItemFactory.createPageFooter(
        30, new Font("Dialog", Font.PLAIN, 10), null, true, true);
    pageFooter.addElement(ItemFactory.createRectangleShapeElement(
        "", Color.black, null, new Rectangle2D.Float(0, 0, -100, -100), true, false));
    Element field3 = ItemFactory.createLabelElement(
        "Label 2",
        new Rectangle2D.Float(10, 0, WIDTH, 0),
        null,
        ElementAlignment.LEFT.getOldAlignment(),
        ElementAlignment.TOP.getOldAlignment(),
        null,
        " 111111111 2222222222 333333333 4444444444444 5555 66666666 777 88888888888888 99999999 "
        + "10101011 "
    );
    field3.getStyle().setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT,
                                       new Boolean(true));
    pageFooter.addElement(field3);
    return pageFooter;
  }

  /**
   * Creates the report footer.
   *
   * @return the report footer.
   */
  private ReportFooter createReportFooter()
  {
    ReportFooter footer = (ReportFooter)
        ItemFactory.createReportFooter(48, new Font("Serif", Font.BOLD, 16), null, false);
    footer.addElement(
        ItemFactory.createLabelElement(
            "Label 2",
            new Rectangle2D.Float(0, 0, -100, 24),
            null,
            ElementAlignment.CENTER.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "*** END OF REPORT ***"
        )
    );
    return footer;
  }

  /**
   * Creates the report header.
   *
   * @return the report header.
   */
  private ReportHeader createReportHeader()
  {
    ReportHeader header = (ReportHeader)
        ItemFactory.createReportHeader(48, new Font("Serif", Font.BOLD, 20), null, false);
    header.addElement(
        ItemFactory.createLabelElement(
            "Label 1",
            new Rectangle2D.Float(0, 0, -100, 24),
            null,
            ElementAlignment.CENTER.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "LIST OF CONTINENTS BY COUNTRY"
        )
    );
    return header;
  }


  /**
   * The itemBand as in the xml definition:
   *
   <pre>
   <items height="10" fontname="Monospaced" fontstyle="plain" fontsize="8">
   <rectangle name="background" x="0" y="0" width="100%" height="100%" color="#DFDFDF" weight="0"/>
   <line name="top" x1="0" y1="0" x2="0" y2="0" color="#DFDFDF" weight="0.1"/>
   <line name="bottom" x1="0" y1="10" x2="0" y2="10" color="#DFDFDF" weight="0.1"/>
   <string-field name="Country Element" x="0" y="0" width="176" height="8" alignment="left"
   fieldname="Country" />
   <string-field name="Code Element" x="180" y="0" width="76" height="8" alignment="left"
   fieldname="ISO Code"/>
   <number-field name="Population Element" x="260" y="0" width="76" height="8" alignment="right"
   format="#,##0" fieldname="Population"/>
   </items>
   </pre>
   *
   * @return the item band.
   */
  private ItemBand createItemBand()
  {
    ItemBand items = (ItemBand)
        ItemFactory.createItemBand(10, new Font("Monospaced", Font.PLAIN, 8), Color.black);
    items.addElement(
        ItemFactory.createRectangleShapeElement(
            "background",
            Color.decode("#DFDFDF"),
            new BasicStroke(0),
            new Rectangle2D.Float(0, 0, -100, -100), false, true
        )
    );
    items.addElement(
        ItemFactory.createLineShapeElement(
            "top",
            Color.decode("#DFDFDF"),
            new BasicStroke(0.1f),
            new Line2D.Float(0, 0, 0, 0)
        )
    );
    items.addElement(
        ItemFactory.createLineShapeElement(
            "bottom",
            Color.decode("#DFDFDF"),
            new BasicStroke(0.1f),
            new Line2D.Float(0, 10, 0, 10)
        )
    );
    items.addElement(
        ItemFactory.createStringElement(
            "Code Element",
            new Rectangle2D.Float(0, 0, 176, 8),
            null,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "<null>",
            "Country"
        )
    );
    items.addElement(
        ItemFactory.createStringElement(
            "Code Element",
            new Rectangle2D.Float(180, 0, 76, 8),
            null,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "<null>",
            "ISO Code"
        )
    );
    items.addElement(
        ItemFactory.createNumberElement(
            "Population Element",
            new Rectangle2D.Float(260, 0, 76, 8),
            null,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "<null>",
            "#,##0",
            "Population"
        )
    );
    return items;
  }

  /**
   * Creates the function collection. The xml definition for this construct:
   *
   <pre>
   <functions>
   <function name="sum" class="com.jrefinery.report.function.ItemSumFunction">
   <properties>
   <property name="field">Population</property>
   <property name="group">Continent Group</property>
   </properties>
   </function>
   <function name="backgroundTrigger"
   class="com.jrefinery.report.function.ElementVisibilitySwitchFunction">
   <properties>
   <property name="element">background</property>
   </properties>
   </function>
   </functions>
   </pre>
   *
   * @return the functions.
   *
   * @throws FunctionInitializeException if there is a problem initialising the functions.
   */
  private ExpressionCollection createFunctions() throws FunctionInitializeException
  {
    ExpressionCollection functions = new ExpressionCollection();

    ItemSumFunction sum = new ItemSumFunction();
    sum.setName("sum");
    sum.setProperty("field", "Population");
    sum.setProperty("group", "Continent Group");
    functions.add(sum);

    ElementVisibilitySwitchFunction backgroundTrigger = new ElementVisibilitySwitchFunction();
    backgroundTrigger.setName("backgroundTrigger");
    backgroundTrigger.setProperty("element", "background");
    functions.add(backgroundTrigger);
    return functions;
  }

  /**
   <pre>
   <groups>

   ... create the groups and add them to the list ...

   </groups>
   </pre>
   *
   * @return the groups.
   */
  private GroupList createGroups()
  {
    GroupList list = new GroupList();
    list.add(createContinentGroup());
    return list;
  }

  /**
   <pre>
   <group name="Continent Group">
   <groupheader height="18" fontname="Monospaced" fontstyle="bold" fontsize="9" pagebreak="false">
   <label name="Label 5" x="0" y="1" width="76" height="9" alignment="left">CONTINENT:</label>
   <string-field name="Continent Element" x="96" y="1" width="76" height="9" alignment="left"
   fieldname="Continent"/>
   <line name="line1" x1="0" y1="12" x2="0" y2="12" weight="0.5"/>
   </groupheader>
   <groupfooter height="18" fontname="Monospaced" fontstyle="bold" fontsize="9">
   <label name="Label 6" x="0" y="0" width="450" height="12" alignment="left"
   baseline="10">Population:</label>
   <number-function x="260" y="0" width="76" height="12" alignment="right" baseline="10"
   format="#,##0" function="sum"/>
   </groupfooter>
   <fields>
   <field>Continent</field>
   </fields>
   </group>
   </pre>
   *
   * @return the continent group.
   */
  private Group createContinentGroup()
  {
    Group continentGroup = new Group();
    continentGroup.setName("Continent Group");
    continentGroup.addField("Continent");

    GroupHeader header = (GroupHeader)
        ItemFactory.createGroupHeader(18, new Font("Monospaced", Font.BOLD, 9), null, false);
    header.addElement(
        ItemFactory.createLabelElement(
            "Label 5",
            new Rectangle2D.Float(0, 1, 76, 9),
            null,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "CONTINENT:"
        )
    );
    header.addElement(
        ItemFactory.createStringElement(
            "Continent Element",
            new Rectangle2D.Float(96, 1, 76, 9),
            null,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "<null>",
            "Continent"
        )
    );
    header.addElement(
        ItemFactory.createLineShapeElement(
            "line1",
            null,
            new BasicStroke(0.5f),
            new Line2D.Float(0, 12, 0, 12)
        )
    );
    continentGroup.setHeader(header);

    GroupFooter footer = (GroupFooter)
        ItemFactory.createGroupFooter(18, new Font("Monospaced", Font.BOLD, 9), null);
    footer.addElement(
        ItemFactory.createLabelElement(
            "Label 6",
            new Rectangle2D.Float(0, 0, -100, 12),
            null,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "Population:"
        )
    );
    footer.addElement(
        ItemFactory.createNumberElement(
            "anonymous",
            new Rectangle2D.Float(260, 0, 76, 12),
            null,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null,
            "<null>",
            "#,##0",
            "sum"
        )
    );
    continentGroup.setFooter(footer);
    return continentGroup;
  }

  /**
   * Creates the report.
   *
   * @return the constructed report.
   *
   * @throws FunctionInitializeException if there was a problem initialising any of the functions.
   */
  public JFreeReport createReport() throws FunctionInitializeException
  {
    JFreeReport report = new JFreeReport();
    report.setName("Sample Report 1");
    report.setReportFooter(createReportFooter());
    report.setReportHeader(createReportHeader());
    report.setPageFooter(createPageFooter());
    report.setPageHeader(createPageHeader());
    report.setGroups(createGroups());
    report.setItemBand(createItemBand());
    report.setFunctions(createFunctions());
    report.setPropertyMarked("report.date", true);
    return report;
  }

  /**
   * Default constructor.
   */
  public SampleReport1()
  {
  }

}