/**
 * Date: Dec 12, 2002
 * Time: 4:41:23 PM
 *
 * $Id: Layouting.java,v 1.2 2003/01/27 03:21:44 taqua Exp $
 */
package com.jrefinery.report.ext.junit.bugs;

import junit.framework.TestCase;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.Element;
import com.jrefinery.report.Band;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.DataRowBackend;
import com.jrefinery.report.DataRowConnector;
import com.jrefinery.report.function.LevelledExpressionList;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportPropertiesList;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;
import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.FloatDimension;

import javax.swing.table.DefaultTableModel;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public class Layouting extends TestCase
{
  private JFreeReport report;

  public Layouting(String s)
  {
    super(s);
  }

  protected void setUp ()
  {
    PageFooter pf = new PageFooter();
/*
    <string-field x="0" y="7" width="140" height="10" alignment="left"
                  fontstyle="bold" fontsize="10"
                  fieldname="Name"/>
*/
/*
    pf.addElement (ItemFactory.createStringElement(
        "Name",
        new Rectangle2D.Float(0,7,140, 10),
        null,
        Element.LEFT,
        null,
        null,
        "prop"));
*/
/*
    <string-field x="0" y="9" width="100%" height="9" alignment="right"
                  fontname="Monospaced" fontstyle="plain" fontsize="8"
                  fieldname="URL"/>
*/
    Element e = ItemFactory.createStringElement(
        "URL",
        new Rectangle2D.Float(0,9,-100, 9),
        null,
        Element.RIGHT,
        null,
        null,
        "URL");
    e.getStyle().setStyleProperty(StaticLayoutManager.DYNAMIC_HEIGHT, new Boolean(true));
    pf.addElement(e);

/*
    <string-field x="0" y="20" width="100%" height="0" alignment="left"
                  fontname="Serif"
                  fieldname="Description" dynamic="true"/>

    Element e = ItemFactory.createStringElement(
        "Description",
        new Rectangle2D.Float(0,20,-100, 0),
        null,
        Element.LEFT,
        null,
        null,
        "prop");
    e.getStyle().setStyleProperty(StaticLayoutManager.DYNAMIC_HEIGHT, new Boolean(true));
    pf.addElement(e);
*/
    assertTrue(e.getStyle().getBooleanStyleProperty(StaticLayoutManager.DYNAMIC_HEIGHT));

    report = new JFreeReport();
    report.setPageFooter(pf);

    report.setProperty("prop", "A long text with no linebreak, but many more lines of text\nOr not?");
    report.setPropertyMarked("prop", true);

    DataRowBackend b = new DataRowBackend();
    b.setTablemodel(new DefaultTableModel());
    b.setReportProperties(new ReportPropertiesList(report.getProperties()));
    b.setFunctions(new LevelledExpressionList (report.getExpressions(), report.getFunctions()));
    DataRowConnector c = new DataRowConnector();
    c.setDataRowBackend(b);
    DataRowConnector.connectDataSources(pf, c);

  }
/*
  public void testLayout1()
  {

    G2OutputTarget ot = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
    StaticLayoutManager staticlm = new StaticLayoutManager();
    staticlm.setOutputTarget(ot);
    Log.debug (staticlm.preferredLayoutSize(report.getPageFooter()));

  }
*/

  public void testLayout2()
  {
  // DO LAYOUT FROM SIMPLEPAGELAYOUTER ...
    G2OutputTarget ot = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());

    Band band = report.getPageFooter();

    Band myBand = new Band ();
    myBand.addElement(band);
    myBand.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension(-100, -100));
    myBand.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, new Point2D.Float(0, 0));

    // in this layouter the width of a band is always the full page width
    float width = (float) ot.getLogicalPage().getWidth();
    Log.debug ("Logical Page: Width = " + width);
    Log.debug (">>>>>>>>>>>>>>>>>>>>>>>>>>>>>PREFLAYOUTSIZE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    // the known limitations can be applied ...
    myBand.getStyle().setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, new FloatDimension(width, Short.MAX_VALUE));
    Log.debug (myBand.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE));
    BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(myBand, ot);
    Dimension2D fdim = lm.preferredLayoutSize(myBand, new FloatDimension(Short.MAX_VALUE, Short.MAX_VALUE));

    // the height is defined by the band's requirements
    float height = (float) fdim.getHeight();
    Rectangle2D bounds = new Rectangle2D.Float(0, 0, width, height);
    band.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds);
    Log.debug (">>>>>>>>>>>>>>>>>>>>>>>>>>>>>DOLAYOUT>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    lm.doLayout(band);

    Log.debug ("Bounds2: " + bounds);
    Log.debug ("URL-Bounds: " + report.getPageFooter().getElement("URL").getStyle().getStyleProperty(ElementStyleSheet.BOUNDS));

  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }


}
