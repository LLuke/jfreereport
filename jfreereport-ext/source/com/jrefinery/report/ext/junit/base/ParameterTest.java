/**
 *
 *  Date: 04.06.2002
 *  ParameterTest.java
 *  ------------------------------
 *  04.06.2002 : ...
 */
package com.jrefinery.report.ext.junit.base;

import junit.framework.TestCase;
import junit.framework.Assert;
import com.jrefinery.report.Band;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.FunctionElement;
import com.jrefinery.report.StringFunctionElement;
import com.jrefinery.report.Element;
import com.jrefinery.report.DateElement;
import com.jrefinery.report.DataElement;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.GeneralElement;
import com.jrefinery.report.StringElement;
import com.jrefinery.report.MultilineTextElement;
import com.jrefinery.report.NumberElement;
import com.jrefinery.report.LabelElement;
import com.jrefinery.report.DateFunctionElement;
import com.jrefinery.report.NumberFunctionElement;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.filter.NumberFormatFilter;

import java.awt.Font;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * Test all properties of elements and bands. Check that all exceptions are thrown
 * (NullPointer) and check that the values are set properly.
 */
public class ParameterTest extends TestCase
{
  public ParameterTest (String name)
  {
    super (name);
  }

  public void performTestBand (Band band)
  {
    try {
      band.addElement(null);
      Assert.fail("No nullpointer thrown, this is evil");
    } catch (NullPointerException e) {}
    try {
      band.addElements(null);
      Assert.fail("No nullpointer thrown, this is evil");
    } catch (NullPointerException e) {}

    Assert.assertNotNull(band.toString());
    Assert.assertNotNull(band.getElements());
    Assert.assertNotNull(band.getDefaultFont());
    Assert.assertNotNull(band.getDefaultPaint());

    Font font = new Font ("Serif", Font.PLAIN, 12);
    band.setDefaultFont(font);
    Assert.assertEquals(font, band.getDefaultFont());

    Color color = Color.green;
    band.setDefaultPaint(color);
    Assert.assertEquals(color, band.getDefaultPaint());

    FunctionElement functionE1 = new StringFunctionElement();

  }

  public void testGetLastDatasource ()
  {
    DataSource ds = new StaticDataSource ("12");
    NumberFormatFilter nfilter  =new NumberFormatFilter();
    nfilter.setDataSource(ds);
    StringFilter filter = new StringFilter();
    filter.setDataSource(nfilter);

    Assert.assertEquals(Band.getLastDatasource(filter), ds);
  }

  public void testItemBand ()
  {
    performTestBand(new ItemBand ());
  }

  public void testGroupFooter ()
  {
    performTestBand(new GroupFooter ());
  }

  public void testGroupHeader ()
  {
    performTestBand(new GroupHeader ());
  }

  public void testReportFooter ()
  {
    performTestBand(new ReportFooter ());
  }

  public void testReportHeader ()
  {
    performTestBand(new ReportHeader ());
  }

  public void testPageFooter ()
  {
    performTestBand(new PageFooter ());
  }

  public void testPageHeader ()
  {
    performTestBand(new PageHeader ());
  }

  public void performTestElement (Element e)
  {
    Band testBand = new ItemBand();
    Assert.assertNotNull(e);
    Assert.assertNotNull(e.getBounds());
    Assert.assertNotNull(e.getBounds(null));
    Assert.assertNotNull(e.getBounds(new Rectangle2D.Float ()));
    Assert.assertNotNull(e.getDataSource());
    Assert.assertNotNull(e.getName());
    Assert.assertNotNull(e.getPaint(testBand));

    try { e.setName(null); Assert.fail();} catch (NullPointerException npe) {}
    try { e.setBounds(null); Assert.fail();} catch (NullPointerException npe) {}
    try { e.setDataSource(null); Assert.fail();} catch (NullPointerException npe) {}
  }

  public void performTestTextElement (TextElement e)
  {
    Band testBand = new ItemBand();
    Assert.assertNotNull(e.getNullString());
    Assert.assertNotNull(e.getFormattedText());
    Assert.assertNotNull(e.getValue());
    Assert.assertNotNull(e.getFont(testBand));
    Assert.assertEquals(e, e);
    Assert.assertTrue(e.equals(new Object()) == false);
    Assert.assertTrue(e.equals(null) == false);
    // This field not initialized causes the pdf-target to ignore the printing of
    // text, so this one is really important.
    Assert.assertTrue(
            e.getAlignment() == TextElement.LEFT ||
            e.getAlignment() == TextElement.RIGHT ||
            e.getAlignment() == TextElement.CENTER);

    e.setNullString(null); Assert.assertNotNull(e.getNullString());
  }

  public void performTestDataElement (DataElement e)
  {
    performTestTextElement(e);
    Assert.assertNotNull(e.getField());
    try { e.setField(null); Assert.fail();} catch (NullPointerException npe) {}
  }

  public void performTestFunctionElement (FunctionElement e)
  {
    performTestTextElement(e);
    Assert.assertNotNull(e.getFunctionName());
    try { e.setFunctionName(null); Assert.fail();} catch (NullPointerException npe) {}
  }

  public void testDateElement ()
  {
    DateElement e = new DateElement ();
    performTestDataElement(e);
    Assert.assertNotNull(e.getFormatter());
    e.setFormatString(null);
    try { e.setFormatter(null); Assert.fail();} catch (NullPointerException npe) {}
  }

  public void testGeneralElement ()
  {
    performTestDataElement(new GeneralElement ());
  }

  public void testStringElement ()
  {
    performTestDataElement(new StringElement ());
  }

  public void testMultilineTextElement ()
  {
    performTestDataElement(new MultilineTextElement ());
  }

  public void testNumberElement ()
  {
    NumberElement e = new NumberElement ();
    performTestDataElement(e);
    Assert.assertNotNull(e.getFormatter());
    e.setDecimalFormatString(null);
    try { e.setFormatter(null); Assert.fail();} catch (NullPointerException npe) {}
  }

  public void testLabelElement ()
  {
    performTestTextElement(new LabelElement ());
  }

  public void testDateFunction ()
  {
    DateFunctionElement e = new DateFunctionElement ();
    performTestFunctionElement(e);
    Assert.assertNotNull(e.getFormatter());
    e.setFormatString(null);
    try { e.setFormatter(null); Assert.fail();} catch (NullPointerException npe) {}
  }

  public void testStringFunction ()
  {
    performTestFunctionElement(new StringFunctionElement ());
  }

  public void testNumberFunction ()
  {
    NumberFunctionElement e = new NumberFunctionElement();
    performTestFunctionElement(e);
    Assert.assertNotNull(e.getFormatter());
    e.setDecimalFormatString(null);
    try { e.setFormatter(null); Assert.fail();} catch (NullPointerException npe) {}
  }
}