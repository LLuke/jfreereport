/**
 *
 *  Date: 19.06.2002
 *  SampleReport1.java
 *  ------------------------------
 *  19.06.2002 : ...
 */
package com.jrefinery.report.demo;

import com.jrefinery.report.Element;
import com.jrefinery.report.FunctionCollection;
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
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.function.ItemSumFunction;
import com.jrefinery.report.function.ReportPropertyFunction;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * This creates a report similiar to the Report defined by report1.xml
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
   </pre
   *
   */
  private PageHeader createPageHeader ()
  {
    PageHeader header = new PageHeader ();
    header.setHeight (18);
    header.setDefaultFont (new Font ("Serif", Font.PLAIN, 10));
    // Is by default true, but it is defined in the xml template, so I add it here too.
    header.setDisplayOnFirstPage (true);
    header.addElement (
            ItemFactory.createRectangleShapeElement (
                    "anonymous",
                    Color.decode ("#AFAFAF"),
                    new BasicStroke (0),
                    new Rectangle2D.Float (0, 0, -100, -100),
                    false,
                    true
            )
    );
    header.addElement (
            ItemFactory.createDateElement(
                    "date1",
                    new Rectangle2D.Float (0, 0, -100, 14),
                    null,
                    Element.RIGHT,
                    null,
                    "<null>",
                    "d-MMM-yyyy",
                    "report_date"
            )
    );
    header.addElement (
            ItemFactory.createLineShapeElement (
                    "line1",
                    Color.decode ("#CFCFCF"),
                    new BasicStroke (2),
                    new Line2D.Float (0, 16, 0, 16)
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
   </pre
   *
   */
  private PageFooter createPageFooter ()
  {
    PageFooter footer = new PageFooter ();
    footer.setHeight (18);
    return footer;
  }

  /**
   <pre>
   <pagefooter height="18">
   <!-- insert a page number field here -->
   </pagefooter>
   </pre
   *
   */
  private ReportFooter createReportFooter ()
  {
    ReportFooter footer = new ReportFooter ();
    footer.setHeight (48);
    footer.setDefaultFont (new Font ("Serif", Font.BOLD, 16));
    footer.addElement (
            ItemFactory.createLabelElement (
                    "Label 2",
                    new Rectangle2D.Float (0, 0, -100, 24),
                    null,
                    Element.CENTER,
                    null,
                    "*** END OF REPORT ***"
            )
    );
    return footer;
  }

  /**
   <pre>
   <reportheader height="48" fontname="Serif" fontstyle="bold" fontsize="20">
   <label name="Label 1" x="0" y="0" width="100%" height="24" alignment="center"
          baseline="20">LIST OF COUNTRIES BY CONTINENT</label>
   </reportheader>
   </pre
   *
   */
  private ReportHeader createReportHeader ()
  {
    ReportHeader header = new ReportHeader ();
    header.setHeight (48);
    header.setDefaultFont (new Font ("Serif", Font.BOLD, 20));
    header.addElement (
            ItemFactory.createLabelElement (
                    "Label 1",
                    new Rectangle2D.Float (0, 0, -100, 24),
                    null,
                    Element.CENTER,
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
   </pre
   */
  private ItemBand createItemBand ()
  {
    ItemBand items = new ItemBand ();
    items.setDefaultPaint (Color.black);
    items.setDefaultFont (new Font ("Monospaced", Font.PLAIN, 8));
    items.setHeight (10);

    items.addElement (
            ItemFactory.createRectangleShapeElement (
                    "background",
                    Color.decode ("#DFDFDF"),
                    new BasicStroke (0),
                    new Rectangle2D.Float (0, 0, -100, -100), false, true
            )
    );
    items.addElement (
            ItemFactory.createLineShapeElement (
                    "top",
                    Color.decode ("#DFDFDF"),
                    new BasicStroke (0.1f),
                    new Line2D.Float (0, 0, 0, 0)
            )
    );
    items.addElement (
            ItemFactory.createLineShapeElement (
                    "bottom",
                    Color.decode ("#DFDFDF"),
                    new BasicStroke (0.1f),
                    new Line2D.Float (0, 10, 0, 10)
            )
    );
    items.addElement (
            ItemFactory.createStringElement (
                    "Code Element",
                    new Rectangle2D.Float (0, 0, 176, 8),
                    null,
                    Element.LEFT,
                    null,
                    "<null>",
                    "Country"
            )
    );
    items.addElement (
            ItemFactory.createStringElement (
                    "Code Element",
                    new Rectangle2D.Float (180, 0, 76, 8),
                    null,
                    Element.LEFT,
                    null,
                    "<null>",
                    "ISO Code"
            )
    );
    items.addElement (
            ItemFactory.createNumberElement (
                    "Population Element",
                    new Rectangle2D.Float (260, 0, 76, 8),
                    null,
                    Element.LEFT,
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
   <function name="report_date" class="com.jrefinery.report.function.ReportPropertyFunction">
   <properties>
   <property name="reportProperty">report.date</property>
   </properties>
   </function>
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
   */
  private FunctionCollection createFunctions () throws FunctionInitializeException
  {
    FunctionCollection functions = new FunctionCollection ();
    ReportPropertyFunction reportDate = new ReportPropertyFunction ();
    reportDate.setName ("report_date");
    reportDate.setProperty ("reportProperty", "report.date");
    functions.add (reportDate);

    ItemSumFunction sum = new ItemSumFunction ();
    sum.setName ("sum");
    sum.setProperty ("field", "Population");
    sum.setProperty ("group", "Continent Group");
    functions.add (sum);

    ElementVisibilitySwitchFunction backgroundTrigger = new ElementVisibilitySwitchFunction ();
    backgroundTrigger.setName ("backgroundTrigger");
    backgroundTrigger.setProperty ("element", "background");
    functions.add (backgroundTrigger);
    return functions;
  }

  /**
   <pre>
   <groups>

   ... create the groups and add them to the list ...

   </groups>
   </pre>
   */
  private GroupList createGroups ()
  {
    GroupList list = new GroupList ();
    list.add (createContinentGroup ());
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
   */
  private Group createContinentGroup ()
  {
    Group continentGroup = new Group ();
    continentGroup.setName ("Continent Group");
    continentGroup.addField ("Continent");

    GroupHeader header = new GroupHeader ();
    header.setHeight (18);
    header.setDefaultFont (new Font ("Monospaced", Font.BOLD, 9));
    header.setPageBreakBeforePrint (false);
    header.addElement (
            ItemFactory.createLabelElement (
                    "Label 5",
                    new Rectangle2D.Float (0, 1, 76, 9),
                    null,
                    Element.LEFT,
                    null,
                    "CONTINENT:"
            )
    );
    header.addElement (
            ItemFactory.createStringElement (
                    "Continent Element",
                    new Rectangle2D.Float (96, 1, 76, 9),
                    null,
                    Element.LEFT,
                    null,
                    "<null>",
                    "Continent"
            )
    );
    header.addElement (
            ItemFactory.createLineShapeElement (
                    "line1",
                    null,
                    new BasicStroke (0.5f),
                    new Line2D.Float (0, 12, 0, 12)
            )
    );
    continentGroup.setHeader (header);

    GroupFooter footer = new GroupFooter ();
    footer.setHeight (18);
    footer.setDefaultFont (new Font ("Monospaced", Font.BOLD, 9));
    footer.addElement (
            ItemFactory.createLabelElement (
                    "Label 6",
                    new Rectangle2D.Float (0, 0, 450, 12),
                    null,
                    Element.RIGHT,
                    null,
                    "Population:"
            )
    );
    footer.addElement (
            ItemFactory.createNumberElement(
                    "anonymous",
                    new Rectangle2D.Float (260, 0, 76, 12),
                    null,
                    Element.RIGHT,
                    null,
                    "<null>",
                    "#,##0",
                    "sum"
            )
    );
    continentGroup.setFooter (footer);
    return continentGroup;
  }

  public JFreeReport createReport () throws FunctionInitializeException
  {
    JFreeReport report = new JFreeReport ();
    report.setName ("Sample Report 1");
    report.setReportFooter (createReportFooter ());
    report.setReportHeader (createReportHeader ());
    report.setPageFooter (createPageFooter ());
    report.setPageHeader (createPageHeader ());
    report.setGroups (createGroups ());
    report.setItemBand (createItemBand ());
    report.setFunctions (createFunctions ());
    return report;
  }

  public SampleReport1 ()
  {
  }
}