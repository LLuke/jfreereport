/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * --------------------
 * SurveyScaleDemo.java
 * --------------------
 * (C)opyright 2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SurveyScaleDemo.java,v 1.2 2005/01/25 01:13:37 taqua Exp $
 *
 * Changes
 * -------
 * 08-Jun-2004 : Version 1 (DG);
 * 
 */

package org.jfree.report.demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import javax.swing.table.TableModel;

import org.jfree.report.Band;
import org.jfree.report.ElementAlignment;
import org.jfree.report.Group;
import org.jfree.report.GroupHeader;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.report.elementfactory.DrawableFieldElementFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.NumberFieldElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.function.ItemCountFunction;
import org.jfree.report.function.PageFunction;
import org.jfree.report.function.PageTotalFunction;
import org.jfree.report.function.TextFormatExpression;
import org.jfree.report.modules.misc.survey.SurveyScale;
import org.jfree.report.modules.misc.survey.SurveyScaleExpression;
import org.jfree.report.util.Log;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.ui.FloatDimension;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.BooleanUtilities;

/**
 * A demo application that generates a report using the {@link
 * org.jfree.report.modules.misc.survey.SurveyScale} class to present responses to a
 * survey.
 *
 * @author David Gilbert
 */
public final class SurveyScaleDemo extends SimpleDemoFrame
{
  /**
   * The width of an A4 page.
   */
  private static final float A4_WIDTH = 595.0f;

  /**
   * The height of an A4 page.
   */
  private static final float A4_HEIGHT = 842.0f;

  /**
   * The top page margin (in pts).
   */
  private static final float PAGE_MARGIN_TOP = 36.0f;

  /**
   * The bottom page margin (in pts).
   */
  private static final float PAGE_MARGIN_BOTTOM = 36.0f;

  /**
   * The left page margin (in pts).
   */
  private static final float PAGE_MARGIN_LEFT = 36.0f;

  /**
   * The right page margin (in pts).
   */
  private static final float PAGE_MARGIN_RIGHT = 36.0f;

  /**
   * The width of the printing area on the page.
   */
  private static final float PRINT_WIDTH = A4_WIDTH - PAGE_MARGIN_LEFT - PAGE_MARGIN_RIGHT;

  /**
   * The height of the printing area on the page.
   */
  private static final float PRINT_HEIGHT = A4_HEIGHT - PAGE_MARGIN_TOP - PAGE_MARGIN_BOTTOM;

  /**
   * The x-coordinate for the start of the printing area.
   */
  private static final float X0 = 0.0f;

  /**
   * The gap to the first column.
   */
  private static final float LEFT_GAP = 15.0f;

  /**
   * The gap after the last column.
   */
  private static final float RIGHT_GAP = 15.0f;

  /**
   * The x-coordinate of the first column.
   */
  private static final float X1 = X0 + LEFT_GAP;

  /**
   * The width of the first column.
   */
  private static final float C1_WIDTH = 220.0f;

  /**
   * The x-coordinate of the second column.
   */
  private static final float X2 = X1 + C1_WIDTH;

  /**
   * The width of the second column.
   */
  private static final float C2_WIDTH = 176.0f;

  /**
   * The x-coordinate of the third column.
   */
  private static final float X3 = X2 + C2_WIDTH;

  /**
   * The width of the third column.
   */
  private static final float C3_WIDTH = (PRINT_WIDTH - X0 - LEFT_GAP - C1_WIDTH - C2_WIDTH - RIGHT_GAP - X0) / 2.0f;

  private static final float X4 = X3 + C3_WIDTH;

  private static final float C4_WIDTH = C3_WIDTH;

  /**
   * The height of the boxes used in the column header.
   */
  private static final float COLUMN_HEADER_BOX_HEIGHT = 20.0f;

  /**
   * The top of the boxes used in the column header.
   */
  private static final float BOX_TOP = 100.0f;

  /**
   * The core data for the report.
   */
  private TableModel data;

  /**
   * Creates a new instance of the demo app.
   */
  private SurveyScaleDemo ()
  {
    init();
  }

  /**
   * Returns a sample data model for the report.
   *
   * @return a sample data model.
   */
  protected TableModel getData ()
  {
    if (data == null)
    {
      data = new SurveyScaleDemoTableModel();
    }
    return data;
  }


  /**
   * Creates a report format by calling API methods directly.
   *
   * @return A report.
   */
  protected JFreeReport createReport ()
  {

    final JFreeReport report = new JFreeReport();
    report.setName("Survey Scale Demo Report");

    // use A4...
    final PageFormatFactory pff = PageFormatFactory.getInstance();
    final Paper paper = pff.createPaper(PageFormatFactory.A4);
    pff.setBorders(paper, PAGE_MARGIN_TOP, PAGE_MARGIN_LEFT, PAGE_MARGIN_BOTTOM, PAGE_MARGIN_RIGHT);
    final PageFormat format = pff.createPageFormat(paper, PageFormat.PORTRAIT);
    report.setPageDefinition(new SimplePageDefinition (format));

    setupWatermark(report);
    setupPageHeader(report);
    //// REPORT GROUP /////////////////////////////////////////////////////////////////////////
    setupGroup(report);
    //// ITEM BAND ////////////////////////////////////////////////////////////////////////////
    setupItemBand(report);
    //// PAGE FOOTER //////////////////////////////////////////////////////////////////////////
    setupPageFooter(report);

    report.setProperty("RESPONDENT_NAME", "Dave");
    report.setPropertyMarked("RESPONDENT_NAME", true);

    return report;
  }

  private void setupPageFooter (final JFreeReport report)
  {
    final Band pageFooter = report.getPageFooter();
    pageFooter.setMinimumSize(new FloatDimension(0.0f, 65.0f));

    // add a horizontal line to set off the page footer
    final StaticShapeElementFactory sef = new StaticShapeElementFactory();
    sef.setName("PageFooterRule");
    sef.setAbsolutePosition(new Point2D.Double(0.0, 0.0));
    sef.setMinimumSize(new FloatDimension(0.0f, 1.0f));
    sef.setShape(new Line2D.Double(X1, 0.0, X4 + C4_WIDTH, 0.0));
    sef.setColor(Color.black);
    sef.setStroke(new BasicStroke(1.0f));
    sef.setShouldDraw(Boolean.TRUE);
    pageFooter.addElement(sef.createElement());

    final PageFunction pf = new PageFunction("PAGE_NUMBER");
    final PageTotalFunction ptf = new PageTotalFunction();
    ptf.setName("TOTAL_PAGES");
    final TextFormatExpression pageNofM = new TextFormatExpression();
    pageNofM.setName("PAGE_N_OF_M");
    pageNofM.setPattern("Page {0} of {1}");
    pageNofM.setField(0, "PAGE_NUMBER");
    pageNofM.setField(1, "TOTAL_PAGES");
    ptf.setName("TOTAL_PAGES");

    report.addExpression(pf);
    report.addExpression(ptf);
    report.addExpression(pageNofM);

    final TextFieldElementFactory tff = new TextFieldElementFactory();
    tff.setName("PageIndicator");
    tff.setAbsolutePosition(new Point2D.Double(X4 + C4_WIDTH - 60.0, 50.0));
    tff.setMinimumSize(new FloatDimension(60.0f, 15.0f));
    tff.setFontName("Serif");
    tff.setItalic(Boolean.TRUE);
    tff.setFontSize(new Integer(8));
    tff.setHorizontalAlignment(ElementAlignment.RIGHT);
    tff.setFieldname("PAGE_N_OF_M");
    pageFooter.addElement(tff.createElement());

    final LabelElementFactory labelFactory = new LabelElementFactory();
    labelFactory.setText("Copyright \u00A9 2004 Object Refinery Limited. All Rights Reserved.");
    labelFactory.setFontName("Serif");
    labelFactory.setItalic(Boolean.TRUE);
    labelFactory.setFontSize(new Integer(8));
    labelFactory.setAbsolutePosition(new Point2D.Double(X1, 50.0));
    labelFactory.setMinimumSize(new FloatDimension(444.0f, 15.0f));
    labelFactory.setHorizontalAlignment(ElementAlignment.LEFT);
    pageFooter.addElement(labelFactory.createElement());
  }

  private void setupItemBand (final JFreeReport report)
  {
    final Band band = report.getItemBand();
    band.setMinimumSize(new FloatDimension(0.0f, 20.0f));

    final ItemCountFunction icf = new ItemCountFunction();
    icf.setName("ITEM_COUNT");

    final TextFormatExpression tfe2 = new TextFormatExpression();
    tfe2.setName("ItemNumber");
    tfe2.setPattern("{0}.");
    tfe2.setField(0, "ITEM_COUNT");

    report.addExpression(icf);
    report.addExpression(tfe2);

    final TextFieldElementFactory factory2 = new TextFieldElementFactory();
    factory2.setFontName("Serif");
    factory2.setFontSize(new Integer(11));
    factory2.setBold(Boolean.FALSE);

    factory2.setName("ItemNumberTextField");
    factory2.setAbsolutePosition(new Point2D.Double(X1, 7.0));
    factory2.setMinimumSize(new FloatDimension(25.0f, 16.0f));
    factory2.setVerticalAlignment(ElementAlignment.TOP);
    factory2.setFieldname("ItemNumber");
    band.addElement(factory2.createElement());

    factory2.setName("ItemField");
    factory2.setAbsolutePosition(new Point2D.Double(X1 + 25.0, 7.0));
    factory2.setMinimumSize(new FloatDimension(C1_WIDTH - 25.0f, 16.0f));
    factory2.setDynamicHeight(Boolean.TRUE);
    factory2.setTrimTextContent(Boolean.TRUE);
    factory2.setFieldname("Item");
    band.addElement(factory2.createElement());

    final SurveyScaleExpression iaf1 = new SurveyScaleExpression(1, 5);
    iaf1.setName("Survey Response");
    iaf1.setField(0, "Your Response");
    iaf1.setField(1, "Average Response");

    report.addExpression(iaf1);

    final DrawableFieldElementFactory f = new DrawableFieldElementFactory();
    f.setFieldname("Survey Response");
    f.setMinimumSize(new FloatDimension(C2_WIDTH, 15.0f));
    f.setAbsolutePosition(new Point2D.Double(X2, 6.0));
    band.addElement(f.createElement());

    final NumberFieldElementFactory nfef = new NumberFieldElementFactory();
    nfef.setFontName("Serif");
    nfef.setFontSize(new Integer(11));
    nfef.setName("F1");
    nfef.setAbsolutePosition(new Point2D.Double(X3, 7.0));
    nfef.setMinimumSize(new FloatDimension(C3_WIDTH, 16.0f));
    nfef.setFieldname("Your Response");
    nfef.setFormatString("0.00");
    nfef.setHorizontalAlignment(ElementAlignment.CENTER);
    band.addElement(nfef.createElement());

    nfef.setName("F2");
    nfef.setAbsolutePosition(new Point2D.Double(X4, 7.0));
    nfef.setFieldname("Average Response");
    band.addElement(nfef.createElement());
  }

  private void setupGroup (final JFreeReport report)
  {
    final Group group = new Group();
    group.setName("Category Group");
    group.addField("Category");

    final GroupHeader gh = group.getHeader();
    gh.setRepeat(true);
    gh.setMinimumSize(new FloatDimension(0.0f, 26.0f));

    final TextFieldElementFactory factory1 = new TextFieldElementFactory();
    factory1.setName("CategoryTextField");
    factory1.setAbsolutePosition(new Point2D.Double(X1, 10.0));
    factory1.setMinimumSize(new FloatDimension(C1_WIDTH + C2_WIDTH + C3_WIDTH, 16.0f));
    factory1.setVerticalAlignment(ElementAlignment.TOP);
    factory1.setFieldname("Category");
    factory1.setFontName("SansSerif");
    factory1.setFontSize(new Integer(12));
    factory1.setBold(Boolean.TRUE);
    factory1.setDynamicHeight(Boolean.TRUE);
    factory1.setTrimTextContent(Boolean.TRUE);
    gh.addElement(factory1.createElement());

    factory1.setFieldname("Category Description");
    factory1.setBold(BooleanUtilities.valueOf(false));
    factory1.setAbsolutePosition(new Point2D.Double(X1, 26));
    factory1.setFontName("Serif");
    factory1.setFontSize(new Integer(11));
    gh.addElement(factory1.createElement());
    report.addGroup(group);
  }

  private void setupPageHeader (final JFreeReport report)
  {
    // define the page header...
    final Band pageHeader = report.getPageHeader();
    pageHeader.setMinimumSize(new FloatDimension(0.0f, 10.0f));

    // the main heading is just a fixed label centered on the page...
    final LabelElementFactory labelFactory = new LabelElementFactory();
    labelFactory.setText("Free / Open Source Software Survey");
    labelFactory.setFontName("Arial");
    labelFactory.setFontSize(new Integer(18));
    labelFactory.setBold(Boolean.TRUE);
    labelFactory.setAbsolutePosition(new Point2D.Double(X1, 10.0));
    labelFactory.setMinimumSize(new FloatDimension(C1_WIDTH + C2_WIDTH + C3_WIDTH + C4_WIDTH, 28.0f));
    labelFactory.setHorizontalAlignment(ElementAlignment.CENTER);
    pageHeader.addElement(labelFactory.createElement());

    // the following expression is used to format the manager name into a message string
    // that says 'Respondent:  <name>'.  The whole string can be centered on the page that way.
    // This expression expects to find a marked report property called 'MANAGER_NAME', look
    // in the attemptPreview() method to see how this is set up.
    final TextFormatExpression tfe = new TextFormatExpression();
    tfe.setName("RESPONDENT_NAME_LABEL");
    tfe.setPattern("Respondent: {0}");
    tfe.setField(0, "RESPONDENT_NAME");

    report.addExpression(tfe);

    // here is the element that displays the string calculated in the expression above.
    final TextFieldElementFactory tfef = new TextFieldElementFactory();
    tfef.setFieldname("RESPONDENT_NAME_LABEL");
    tfef.setFontName("Arial");
    tfef.setFontSize(new Integer(12));
    tfef.setBold(Boolean.TRUE);
    tfef.setAbsolutePosition(new Point2D.Double(0.0, 38.0));
    tfef.setMinimumSize(new FloatDimension(PRINT_WIDTH, 14.0f));
    tfef.setHorizontalAlignment(ElementAlignment.CENTER);
    pageHeader.addElement(tfef.createElement());

    labelFactory.setAbsolutePosition(new Point2D.Double(X1, 58.0));
    labelFactory.setText("Please note that the questions AND responses presented below were INVENTED for the the purpose of this demo report.  They are NOT real.");
    labelFactory.setFontName("Serif");
    labelFactory.setFontSize(new Integer(11));
    labelFactory.setBold(Boolean.FALSE);
    labelFactory.setItalic(Boolean.TRUE);
    pageHeader.addElement(labelFactory.createElement());

    // labels
    labelFactory.setFontName("SansSerif");
    labelFactory.setFontSize(new Integer(7));
    labelFactory.setItalic(Boolean.FALSE);
    labelFactory.setBold(Boolean.FALSE);
    labelFactory.setVerticalAlignment(ElementAlignment.BOTTOM);

    final float delta = C2_WIDTH / 5.0f;
    labelFactory.setText("Not Important");
    labelFactory.setAbsolutePosition(new Point2D.Double(X2, 70.0));
    labelFactory.setMinimumSize(new FloatDimension(delta, 30.0f));
    pageHeader.addElement(labelFactory.createElement());

    labelFactory.setText("Very Important");
    labelFactory.setAbsolutePosition(new Point2D.Double(X2 + 4 * delta, 70.0));
    labelFactory.setMinimumSize(new FloatDimension(delta, 30.0f));
    pageHeader.addElement(labelFactory.createElement());

    addBoxedLabelToBand(pageHeader, null, X1, BOX_TOP, C1_WIDTH, COLUMN_HEADER_BOX_HEIGHT,
            "SansSerif", 10, true, Color.black, new Color(220, 255, 220));
    addBoxedLabelToBand(pageHeader, null, X2, BOX_TOP, C2_WIDTH, COLUMN_HEADER_BOX_HEIGHT,
            "SansSerif", 10, true, Color.black, new Color(220, 255, 220));

    // BOX 1 - background
    final StaticShapeElementFactory sef = new StaticShapeElementFactory();
    sef.setMinimumSize(new FloatDimension(C1_WIDTH, COLUMN_HEADER_BOX_HEIGHT));
    sef.setAbsolutePosition(new Point2D.Double(X1, BOX_TOP));
    sef.setShape(new Rectangle2D.Double(0.0, 0.0, C1_WIDTH, COLUMN_HEADER_BOX_HEIGHT));
    sef.setColor(Color.lightGray);
    sef.setStroke(new BasicStroke(1.0f));
    sef.setShouldFill(Boolean.TRUE);

    final SurveyScale scaleHeader = new SurveyScale(1, 5, null);
    scaleHeader.setDrawScaleValues(true);
    scaleHeader.setDrawTickMarks(false);
    scaleHeader.setScaleValueFont(new Font("SansSerif", Font.PLAIN, 9));
    report.setProperty("SCALE_HEADER", scaleHeader);
    report.setPropertyMarked("SCALE_HEADER", true);
    final DrawableFieldElementFactory dfef = new DrawableFieldElementFactory();
    dfef.setName("ScaleHeaderElement");
    dfef.setAbsolutePosition(new Point2D.Double(X2, BOX_TOP));
    dfef.setFieldname("SCALE_HEADER");
    dfef.setMinimumSize(new FloatDimension(C2_WIDTH, COLUMN_HEADER_BOX_HEIGHT));
    pageHeader.addElement(dfef.createElement());

    addBoxedLabelToBand(pageHeader, "Your Response", X3, BOX_TOP, C3_WIDTH, COLUMN_HEADER_BOX_HEIGHT,
            "SansSerif", 8, false, Color.black, new Color(220, 255, 220));

    addBoxedLabelToBand(pageHeader, "Average Response", X3 + C3_WIDTH, BOX_TOP, C3_WIDTH, COLUMN_HEADER_BOX_HEIGHT,
            "SansSerif", 8, false, Color.black, new Color(220, 255, 220));
  }

  private void setupWatermark (final JFreeReport report)
  {
    // use a watermark to draw a frame around the page...
    final Band watermarkBand = report.getWatermark();
    final StaticShapeElementFactory sef = new StaticShapeElementFactory();
    sef.setShape(new Rectangle2D.Double(0.0, 0.0, PRINT_WIDTH, PRINT_HEIGHT));
    sef.setMinimumSize(new FloatDimension(0.0f, PRINT_HEIGHT));
    sef.setColor(Color.black);
    sef.setStroke(new BasicStroke(1.0f));
    sef.setShouldDraw(Boolean.TRUE);
    watermarkBand.addElement(sef.createElement());
  }

  /**
   * A utility method that creates a boxed label and adds it to a band.
   *
   * @param band            the band.
   * @param label           the field name.
   * @param x               the x-coordinate within the band.
   * @param y               the y-coordinate within the band.
   * @param w               the width.
   * @param h               the height.
   * @param fontName        the font name.
   * @param fontSize        the font size.
   * @param bold            bold?
   * @param outlineColor    the outline color.
   * @param backgroundColor the background color.
   */
  private static void addBoxedLabelToBand (final Band band, final String label,
                                           final float x, final float y, final float w, final float h,
                                           final String fontName, final int fontSize, final boolean bold,
                                           final Color outlineColor,
                                           final Color backgroundColor)
  {

    // background
    final StaticShapeElementFactory f1 = new StaticShapeElementFactory();
    f1.setMinimumSize(new FloatDimension(w, h));
    f1.setAbsolutePosition(new Point2D.Double(x, y));
    f1.setShape(new Rectangle2D.Double(0.0, 0.0, w, h));
    f1.setColor(backgroundColor);
    f1.setShouldFill(Boolean.TRUE);
    f1.setShouldDraw(Boolean.FALSE);
    band.addElement(f1.createElement());

    // border
    f1.setColor(outlineColor);
    f1.setShouldFill(Boolean.FALSE);
    f1.setShouldDraw(Boolean.TRUE);
    band.addElement(f1.createElement());

    // field text
    if (label != null)
    {
      final LabelElementFactory f2 = new LabelElementFactory();
      f2.setAbsolutePosition(new Point2D.Double(x, y));
      f2.setMinimumSize(new FloatDimension(w, h));
      f2.setText(label);
      f2.setFontName(fontName);
      f2.setFontSize(new Integer(fontSize));
      f2.setBold(new Boolean(bold));
      f2.setHorizontalAlignment(ElementAlignment.CENTER);
      f2.setVerticalAlignment(ElementAlignment.MIDDLE);
      band.addElement(f2.createElement());
    }

  }

  protected String getResourcePrefix ()
  {
    return "demo.surveyscale";
  }

  /**
   * The starting point for the demo application.
   *
   * @param args the command-line arguments (ignored).
   */
  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    final SurveyScaleDemo demo = new SurveyScaleDemo();
    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);
  }

}
