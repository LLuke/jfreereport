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
 * -----------------
 * StylesWriter.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StylesWriter.java,v 1.6 2003/03/18 18:28:44 taqua Exp $
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

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
import com.jrefinery.report.util.Log;

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
   */
  public StylesWriter(ReportWriter reportWriter, int indentLevel)
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
  public void write(Writer writer) throws IOException, ReportWriterException
  {
    Object[] styles = collectStyles();
    writeTag(writer, ExtReportHandler.STYLES_TAG);

    for (int i = 0; i < styles.length; i++)
    {
      ElementStyleSheet style = (ElementStyleSheet) styles[i];
      writeTag(writer, StylesHandler.STYLE_TAG, "name", style.getName(), OPEN);

      StyleWriter stW = new StyleWriter(getReportWriter(), style, null, getIndentLevel());
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
  private Object[] collectStyles ()
  {
    JFreeReport report = getReport();
    collectStylesFromBand(report.getReportHeader());
    collectStylesFromBand(report.getReportFooter());
    collectStylesFromBand(report.getPageHeader());
    collectStylesFromBand(report.getPageFooter());
    collectStylesFromBand(report.getItemBand());
    for (int i = 0; i < report.getGroupCount(); i++)
    {
      Group g = report.getGroup(i);
      collectStylesFromBand(g.getHeader());
      collectStylesFromBand(g.getFooter());
    }

    HashMap collectedStyles = new HashMap();

    for (int i = 0; i < reportStyles.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) reportStyles.get(i);
      if (collectedStyles.containsKey(es.getName()))
      {
        Log.warn ("Duplciate stylesheet-template name: " + es.getName());
      }
      else
      {
        collectedStyles.put(es.getName(), es);
      }
    }

    reportStyles.clear();

    //now sort the elements ...
    TreeSet sortedSet = new TreeSet(new StylesComparator());
    Iterator keys = collectedStyles.keySet().iterator();
    while (keys.hasNext())
    {
      String key = (String) keys.next();
      Object value = collectedStyles.get(key);
      sortedSet.add(value);
    }
    Object[] styles = sortedSet.toArray();
    return styles;
  }

  /**
   * Collects the styles from a band.
   *
   * @param band  the band.
   */
  private void collectStylesFromBand (Band band)
  {
    ElementStyleSheet bandDefaults = band.getBandDefaults();

    List parents = bandDefaults.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(es);
    }
    collectStylesFromElement(band);
  }

  /**
   * Collects the styles from an element.
   *
   * @param element  the element.
   */
  private void collectStylesFromElement (Element element)
  {
    ElementStyleSheet elementSheet = element.getStyle();

    List parents = elementSheet.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      addCollectableStyleSheet(es);
    }
  }

  /**
   * Adds a defined stylesheet to the styles collection. If the stylesheet
   * is one of the default stylesheets, then it is not collected.
   *
   * @param es  the element style sheet.
   */
  private void addCollectableStyleSheet (ElementStyleSheet es)
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
    if (reportStyles.contains(es) == false)
    {
      reportStyles.add(es);
    }
  }
}
