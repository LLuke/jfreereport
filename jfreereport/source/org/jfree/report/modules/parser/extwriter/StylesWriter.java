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
 * -----------------
 * StylesWriter.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StylesWriter.java,v 1.3 2003/08/24 15:08:21 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.ext.ExtParserModuleInit;
import org.jfree.report.modules.parser.ext.ExtReportHandler;
import org.jfree.report.modules.parser.ext.StylesHandler;
import org.jfree.report.style.ElementStyleSheet;

/**
 * A styles writer.
 *
 * @author Thomas Morgner.
 */
public class StylesWriter extends AbstractXMLDefinitionWriter
{
  /** The comment hint path used to retrieve the comments from the parser hints. */
  private static final CommentHintPath STYLES_HINT_PATH =
      new CommentHintPath(new String[]
      {ExtParserModuleInit.REPORT_DEFINITION_TAG, ExtReportHandler.STYLES_TAG});


  /** Storage for the styles. */
  private final ArrayList reportStyles;

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
   * Writes the ihnerited styles to a character stream writer. This will collect
   * all inherited styles, ignoring all styles which are directly bound to an
   * element or which are global default stylesheets.
   *
   * @param writer  the character stream writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(final Writer writer) throws IOException, ReportWriterException
  {
    writeComment(writer, STYLES_HINT_PATH, CommentHandler.OPEN_TAG_COMMENT);

    final ElementStyleSheet[] styles = collectStyles();
    if (styles.length == 0)
    {
      return;
    }

    writeTag(writer, ExtReportHandler.STYLES_TAG);
    for (int i = 0; i < styles.length; i++)
    {
      final ElementStyleSheet style = styles[i];
      final CommentHintPath stylePath = STYLES_HINT_PATH.getInstance();
      stylePath.addName(style);
      writeComment(writer, stylePath, CommentHandler.OPEN_TAG_COMMENT);
      writeTag(writer, StylesHandler.STYLE_TAG, "name", style.getName(), OPEN);

      final StyleWriter stW = new StyleWriter
          (getReportWriter(), style, null, getIndentLevel(), stylePath);
      stW.write(writer);

      writeComment(writer, stylePath, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(writer, StylesHandler.STYLE_TAG);
    }
    writeComment(writer, STYLES_HINT_PATH, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, ExtReportHandler.STYLES_TAG);
    writer.write(getLineSeparator());
  }

  /**
   * Collects styles from all the bands in the report. The returned styles are
   * ordered so that parent style sheets are contained before any child stylesheets
   * in the array.
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

    final ElementStyleSheet[] styles = (ElementStyleSheet[])
        reportStyles.toArray(new ElementStyleSheet[reportStyles.size()]);
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
    if (es.isGlobalDefault())
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
  }
}
