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
 * ----------------------------
 * ReportDescriptionWriter.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDescriptionWriter.java,v 1.11 2005/02/04 19:08:53 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.ReportBuilderHints;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.EmptyDataSource;
import org.jfree.report.filter.templates.Template;
import org.jfree.report.layout.BandLayoutManager;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.xml.CommentHandler;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;
import org.jfree.xml.writer.AttributeList;

/**
 * A report description writer.  The
 * {@link org.jfree.report.modules.parser.extwriter.ReportDefinitionWriter} class is
 * responsible for writing the complete XML report definition file, but it delegates
 * one large section (the report description) to this class.
 *
 * @author Thomas Morgner.
 */
public class ReportDescriptionWriter extends AbstractXMLDefinitionWriter
{
  /** The comment hint path used to store comments from the ext-parser. */
  private static final CommentHintPath REPORT_DESCRIPTION_HINT_PATH =
      new CommentHintPath(new String[]
      {REPORT_DEFINITION_TAG,
       REPORT_DESCRIPTION_TAG});


  /**
   * Creates a new report description writer.
   *
   * @param reportWriter  the report writer.
   * @param indent the current indention level.
   */
  public ReportDescriptionWriter(final ReportWriter reportWriter, final int indent)
  {
    super(reportWriter, indent);
  }

  /**
   * Writes a report description element to a character stream writer.
   *
   * @param writer  the character stream writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(final Writer writer) throws IOException, ReportWriterException
  {
    writeComment(writer, REPORT_DESCRIPTION_HINT_PATH, CommentHandler.OPEN_TAG_COMMENT);
    writeTag(writer, REPORT_DESCRIPTION_TAG);

    writeBand(writer, REPORT_HEADER_TAG,
        getReport().getReportHeader(), null, REPORT_DESCRIPTION_HINT_PATH);
    writeBand(writer, REPORT_FOOTER_TAG,
        getReport().getReportFooter(), null, REPORT_DESCRIPTION_HINT_PATH);
    writeBand(writer, PAGE_HEADER_TAG,
        getReport().getPageHeader(), null, REPORT_DESCRIPTION_HINT_PATH);
    writeBand(writer, PAGE_FOOTER_TAG,
        getReport().getPageFooter(), null, REPORT_DESCRIPTION_HINT_PATH);
    writeBand(writer, WATERMARK_TAG,
        getReport().getWatermark(), null, REPORT_DESCRIPTION_HINT_PATH);
    writeGroups(writer);
    writeBand(writer, ITEMBAND_TAG,
        getReport().getItemBand(), null, REPORT_DESCRIPTION_HINT_PATH);

    writeComment(writer, REPORT_DESCRIPTION_HINT_PATH, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, REPORT_DESCRIPTION_TAG);
  }

  /**
   * Writes an element for a report band.
   *
   * @param writer  a character stream writer.
   * @param tagName  the tag name (for the band).
   * @param band  the band.
   * @param parent  the parent band.
   * @param path the comment path used to read stored comments from the ext-parser.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeBand(final Writer writer, final String tagName,
                         final Band band, final Band parent,
                         final CommentHintPath path)
      throws IOException, ReportWriterException
  {
    if (isBandEmpty(band))
    {
      return;
    }

    final CommentHintPath newPath = path.getInstance();
    newPath.addName(band);

    writeComment(writer, newPath, CommentHandler.OPEN_TAG_COMMENT);
    if (band.getName().startsWith(Band.ANONYMOUS_BAND_PREFIX))
    {
      writeTag(writer, tagName);
    }
    else
    {
      writeTag(writer, tagName, "name", band.getName(), OPEN);
    }

    final ElementStyleSheet styleSheet = band.getStyle();
    if (isStyleSheetEmpty(styleSheet) == false)
    {
      final CommentHintPath stylePath = newPath.getInstance();
      stylePath.addName(STYLE_TAG);
      writeComment(writer, stylePath, CommentHandler.OPEN_TAG_COMMENT);
      writeTag(writer, STYLE_TAG);

      final StyleWriter styleWriter =
          new StyleWriter(getReportWriter(), band.getStyle(),
              parent.getStyle(), getIndentLevel(), stylePath);
      styleWriter.write(writer);
      writeComment(writer, stylePath, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(writer, STYLE_TAG);
    }

    writeDataSourceForElement(band, writer, newPath);

    final Element[] list = band.getElementArray();
    for (int i = 0; i < list.length; i++)
    {
      if (list[i] instanceof Band)
      {
        final Band b = (Band) list[i];
        writeBand(writer, BAND_TAG, b, band, newPath);
      }
      else
      {
        writeElement(writer, list[i], band, newPath);
      }
    }
    writeComment(writer, newPath, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, tagName);
  }

  /**
   * Checks, whether the given stylesheet is empty and does not inherit values
   * from modifiable or userdefined parents.
   *
   * @param es the element stylesheet to test
   * @return true, if the sheet is empty, false otherwise.
   */
  private boolean isStyleSheetEmpty(final ElementStyleSheet es)
  {
    if (es.getParents().length == 0 &&
        es.getDefinedPropertyNames().hasNext() == false)
    {
      return true;
    }
    return false;
  }

  /**
   * Writes an element to a character stream writer.
   *
   * @param writer  the character stream writer.
   * @param element  the element.
   * @param parent  the band.
   * @param path the comment hint path used to read the stored comments of the ext-parser.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeElement(final Writer writer, final Element element,
                            final Band parent, final CommentHintPath path)
      throws IOException, ReportWriterException
  {
    if (parent.getElements().indexOf(element) == -1)
    {
      throw new IllegalArgumentException("The given Element is no child of the band");
    }

    final CommentHintPath newPath = path.getInstance();
    newPath.addName(element);
    writeComment(writer, newPath, CommentHandler.OPEN_TAG_COMMENT);

    final AttributeList p = new AttributeList();
    if (element.getName().startsWith(Element.ANONYMOUS_ELEMENT_PREFIX) == false)
    {
      p.setAttribute("name", element.getName());
    }
    p.setAttribute("type", element.getContentType());
    writeTag(writer, ELEMENT_TAG, p, OPEN);

    final ElementStyleSheet styleSheet = element.getStyle();
    if (isStyleSheetEmpty(styleSheet) == false)
    {
      final CommentHintPath stylePath = newPath.getInstance();
      stylePath.addName(STYLE_TAG);
      writeComment(writer, stylePath, CommentHandler.OPEN_TAG_COMMENT);
      writeTag(writer, STYLE_TAG);

      final StyleWriter styleWriter =
          new StyleWriter(getReportWriter(), element.getStyle(),
              parent.getStyle(), getIndentLevel(), stylePath);
      styleWriter.write(writer);
      writeComment(writer, stylePath, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(writer, STYLE_TAG);
    }

    writeDataSourceForElement(element, writer, newPath);

    writeComment(writer, newPath, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, ELEMENT_TAG);
  }

  /**
   * Writes the datasource- or template-tag for an given element.
   *
   * @param element the element, which should be written.
   * @param writer the writer that should receive the contents.
   * @param path the comment hint path used to read the ext-parser comments.
   * @throws ReportWriterException if there is a problem writing the report
   * @throws IOException if there is an IO error.
   */
  protected void writeDataSourceForElement
      (final Element element, final Writer writer, final CommentHintPath path)
      throws ReportWriterException, IOException
  {
    if ((element.getDataSource() instanceof EmptyDataSource))
    {
      return;
    }
    if (element.getDataSource() instanceof Template == false)
    {
      writeDataSource(writer, element.getDataSource(), path);
      return;
    }

    final TemplateCollector tc = getReportWriter().getTemplateCollector();

    final Template template = (Template) element.getDataSource();

    // the template description of the element template will get the
    // template name as its name.
    final TemplateDescription templateDescription =
        tc.getDescription(template);

    if (templateDescription == null)
    {
      throw new ReportWriterException("Unknown template type: " + templateDescription);
    }

    // create the parent description before the template description is filled.
    TemplateDescription parentTemplate = (TemplateDescription) templateDescription.getInstance();

    try
    {
      templateDescription.setParameterFromObject(template);
    }
    catch (ObjectFactoryException ofe)
    {
      throw new ReportWriterException("Error while preparing the template", ofe);
    }

    // seek the parent template. If that fails for any reason, we fall back to
    // the root template description, we safed earlier ...
    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    if (hints != null)
    {
      final String templateExtends = (String) hints.getHint
          (element, "ext.parser.template-reference", String.class);
      if (templateExtends != null)
      {
        final TemplateDescription parent =
            TemplatesWriter.getTemplateDescription(getReportWriter(), templateExtends);
        if (parent != null)
        {
          parentTemplate = parent;
        }
      }
    }

    final CommentHintPath templatePath = path.getInstance();
    templatePath.addName(TEMPLATE_TAG);

    final TemplateWriter templateWriter = new TemplateWriter
        (getReportWriter(), getIndentLevel(),
            templateDescription, parentTemplate, templatePath);
    templateWriter.write(writer);
  }

  /**
   * Writes a data source to a character stream writer.
   *
   * @param writer  the character stream writer.
   * @param datasource  the datasource.
   * @param path the comment hint path used to read the ext-parser comments.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeDataSource(final Writer writer, final DataSource datasource,
                               final CommentHintPath path)
      throws IOException, ReportWriterException
  {
    ObjectDescription od =
        getReportWriter().getClassFactoryCollector().getDescriptionForClass(datasource.getClass());
    if (od == null)
    {
      od = getReportWriter().getClassFactoryCollector().
          getSuperClassObjectDescription(datasource.getClass(), null);
    }
    if (od == null)
    {
      throw new ReportWriterException("Unable to resolve DataSource: " + datasource.getClass());
    }

    final DataSourceCollector dataSourceCollector = getReportWriter().getDataSourceCollector();
    final String dsname = dataSourceCollector.getDataSourceName(od);
    if (dsname == null)
    {
      throw new ReportWriterException("No name for DataSource " + datasource);
    }

    final CommentHintPath dataSourcePath = path.getInstance();
    dataSourcePath.addName(DATASOURCE_TAG);
    writeComment(writer, dataSourcePath, CommentHandler.OPEN_TAG_COMMENT);
    writeTag(writer, DATASOURCE_TAG, "type", dsname, OPEN);

    final DataSourceWriter dsWriter =
        new DataSourceWriter(getReportWriter(), datasource, od, getIndentLevel(), dataSourcePath);
    dsWriter.write(writer);

    writeComment(writer, dataSourcePath, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, DATASOURCE_TAG);
  }

  /**
   * Writes groups to a character stream writer.
   *
   * @param writer  the character stream writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeGroups(final Writer writer)
      throws IOException, ReportWriterException
  {
    final CommentHintPath groupsPath = REPORT_DESCRIPTION_HINT_PATH.getInstance();
    groupsPath.addName(GROUPS_TAG);
    writeComment(writer, groupsPath, CommentHandler.OPEN_TAG_COMMENT);
    writeTag(writer, GROUPS_TAG);

    //logComment = true;
    final int groupSize = getReport().getGroupCount();
    for (int i = 0; i < groupSize; i++)
    {
      final Group g = getReport().getGroup(i);
      if (isGroupEmpty(g) == true)
      {
        continue;
      }

      final CommentHintPath groupPath = groupsPath.getInstance();
      groupPath.addName(g);
      writeComment(writer, groupPath, CommentHandler.OPEN_TAG_COMMENT);
      writeTag(writer, GROUP_TAG, "name", g.getName(), OPEN);

      final List fields = g.getFields();
      if (fields.isEmpty() == false)
      {
        final CommentHintPath fieldsPath = groupPath.getInstance();
        fieldsPath.addName(FIELDS_TAG);
        writeComment(writer, fieldsPath, CommentHandler.OPEN_TAG_COMMENT);
        writeTag(writer, FIELDS_TAG);

        for (int f = 0; f < fields.size(); f++)
        {
          final String field = (String) fields.get(f);
          final CommentHintPath fieldPath = fieldsPath.getInstance();
          fieldPath.addName(field);
          writeComment(writer, fieldPath, CommentHandler.OPEN_TAG_COMMENT);
          writeTag(writer, FIELD_TAG);
          writer.write(normalize(field));
          writeCloseTag(writer, FIELD_TAG);
        }
        writeComment(writer, fieldsPath, CommentHandler.CLOSE_TAG_COMMENT);
        writeCloseTag(writer, FIELDS_TAG);
      }

      writeBand(writer, GROUP_HEADER_TAG, g.getHeader(), null, groupPath);
      writeBand(writer, GROUP_FOOTER_TAG, g.getFooter(), null, groupPath);

      writeComment(writer, groupPath, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(writer, GROUP_TAG);
    }

    writeComment(writer, groupsPath, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, GROUPS_TAG);
    // logComment = false;
  }

  /**
   * Checks, whether a given group is empty. Returns true, if the group does not define
   * any fields and only contains the default bands.
   *
   * @param g the group that should be tested
   * @return true, if the group is empty, false otherwise.
   */
  private boolean isGroupEmpty(final Group g)
  {
    if (g.getFields().isEmpty() == false)
    {
      return false;
    }
    if (isBandEmpty(g.getFooter()) == false)
    {
      return false;
    }
    if (isBandEmpty(g.getHeader()) == false)
    {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the given band is empty.
   * <p>
   * A band is considered empty, if it does only define the a band layoutmanager
   * and no other style definition.
   *
   * @param b the band that should be tested.
   * @return true, if the band does not contain child elements and does not define a
   * specific style.
   */
  private boolean isBandEmpty(final Band b)
  {
    if (b.getElementCount() != 0)
    {
      return false;
    }
    final Iterator it = b.getStyle().getDefinedPropertyNames();
    if (it.hasNext() == false)
    {
      return false;
    }
    final StyleKey o = (StyleKey) it.next();
    if (o.equals(BandLayoutManager.LAYOUTMANAGER))
    {
      if (it.hasNext() == false)
      {
        return true;
      }
    }
    return false;
  }
}
