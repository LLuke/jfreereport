/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------
 * StylesWriter.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StylesWriter.java,v 1.14 2003/06/27 14:25:21 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.StylesHandler;
import com.jrefinery.report.targets.style.BandDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;

/**
 * A styles writer.
 *
 * @author Thomas Morgner.
 */
public class StylesWriter extends AbstractXMLDefinitionWriter
{
  /** Storage for the styles. */
  private ArrayList reportStyles;

  /**
   * Creates a new styles writer.
   *
   * @param reportWriter  the report writer.
   * @param indentLevel the current indention level.
   */
  public StylesWriter(final ReportWriter reportWriter, final int indentLevel)
  {
    super(reportWriter, indentLevel);
    reportStyles = new ArrayList();
  }

  /**
   * Writes the styles to a character stream writer.
   *
   * @param writer  the character stream writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(final Writer writer) throws IOException, ReportWriterException
  {
    final ElementStyleSheet[] styles = collectStyles();
    writeTag(writer, ExtReportHandler.STYLES_TAG);

    for (int i = 0; i < styles.length; i++)
    {
      final ElementStyleSheet style = styles[i];
      writeTag(writer, StylesHandler.STYLE_TAG, "name", style.getName(), OPEN);

      final StyleWriter stW = new StyleWriter(getReportWriter(), style, null, getIndentLevel());
      stW.write(writer);

      writeCloseTag(writer, StylesHandler.STYLE_TAG);
    }
    writeCloseTag(writer, ExtReportHandler.STYLES_TAG);
  }

  /**
   * Collects styles from all the bands in the report.
   *
   * @return The styles.
   */
  private ElementStyleSheet[] collectStyles()
  {
    final JFreeReport report = getReport();
    collectStylesFromBand(report.getReportHeader());
    collectStylesFromBand(report.getReportFooter());
    collectStylesFromBand(report.getPageHeader());
    collectStylesFromBand(report.getPageFooter());
    collectStylesFromBand(report.getItemBand());
    for (int i = 0; i < report.getGroupCount(); i++)
    {
      final Group g = report.getGroup(i);
      collectStylesFromBand(g.getHeader());
      collectStylesFromBand(g.getFooter());
    }

    //now sort the elements ...
    final ElementStyleSheet[] styles = (ElementStyleSheet[])
        reportStyles.toArray(new ElementStyleSheet[reportStyles.size()]);
/*
    for (int i = 0; i < styles.length; i++)
    {
      System.out.println(styles[i].getName());
    }
*/
    return styles;
  }

  /**
   * Collects the styles from a band.
   *
   * @param band  the band.
   */
  private void collectStylesFromBand(final Band band)
  {
    final ElementStyleSheet bandDefaults = band.getBandDefaults();

    final List parents = bandDefaults.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      final ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(es);
    }

    collectStylesFromElement(band);

    final Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      if (elements[i] instanceof Band)
      {
        collectStylesFromBand((Band) elements[i]);
      }
      else
      {
        collectStylesFromElement(elements[i]);
      }
    }

  }

  /**
   * Collects the styles from an element.
   *
   * @param element  the element.
   */
  private void collectStylesFromElement(final Element element)
  {
    final ElementStyleSheet elementSheet = element.getStyle();

    final List parents = elementSheet.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      final ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(es);
    }
  }

  /**
   * Adds a defined stylesheet to the styles collection. If the stylesheet
   * is one of the default stylesheets, then it is not collected.
   *
   * @param es  the element style sheet.
   */
  private void addCollectableStyleSheet(final ElementStyleSheet es)
  {
    if (es == BandDefaultStyleSheet.getBandDefaultStyle())
    {
      return;
    }
    if (es == ElementDefaultStyleSheet.getDefaultStyle())
    {
      return;
    }
    if (es == ShapeElement.getDefaultStyle())
    {
      return;
    }

    final List parents = es.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      final ElementStyleSheet parentsheet = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(parentsheet);
    }

    if (reportStyles.contains(es) == false)
    {
      reportStyles.add(es);
    }
/*    else
    {
      Log.debug ("Already Added: " + es.getName());
    }*/
  }
}
