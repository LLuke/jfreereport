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
 * ----------------------------
 * ReportDescriptionWriter.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDescriptionWriter.java,v 1.1 2003/07/07 22:44:08 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.GroupList;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.EmptyDataSource;
import org.jfree.report.filter.templates.Template;
import org.jfree.report.modules.parser.ext.BandHandler;
import org.jfree.report.modules.parser.ext.DataSourceHandler;
import org.jfree.report.modules.parser.ext.ElementHandler;
import org.jfree.report.modules.parser.ext.ExtReportHandler;
import org.jfree.report.modules.parser.ext.GroupHandler;
import org.jfree.report.modules.parser.ext.GroupsHandler;
import org.jfree.report.modules.parser.ext.ReportDescriptionHandler;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

/**
 * A report description writer.  The {@link ReportDefinitionWriter} class is responsible for
 * writing the complete XML report definition file, but it delegates one large section (the
 * report description) to this class.
 *
 * @author Thomas Morgner.
 */
public class ReportDescriptionWriter extends AbstractXMLDefinitionWriter
{
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
    writeTag(writer, ExtReportHandler.REPORT_DESCRIPTION_TAG);
    writeBand(writer, ReportDescriptionHandler.REPORT_HEADER_TAG,
        getReport().getReportHeader(), null);
    writeBand(writer, ReportDescriptionHandler.REPORT_FOOTER_TAG,
        getReport().getReportFooter(), null);
    writeBand(writer, ReportDescriptionHandler.PAGE_HEADER_TAG, getReport().getPageHeader(), null);
    writeBand(writer, ReportDescriptionHandler.PAGE_FOOTER_TAG, getReport().getPageFooter(), null);
    writeGroups(writer);
    writeBand(writer, ReportDescriptionHandler.ITEMBAND_TAG, getReport().getItemBand(), null);
    writeCloseTag(writer, ExtReportHandler.REPORT_DESCRIPTION_TAG);
  }

  /**
   * Writes an element for a report band.
   *
   * @param writer  a character stream writer.
   * @param tagName  the tag name (for the band).
   * @param band  the band.
   * @param parent  the parent band.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeBand(final Writer writer, final String tagName, final Band band, final Band parent)
      throws IOException, ReportWriterException
  {
    if (band.getName().startsWith(Band.ANONYMOUS_BAND_PREFIX))
    {
      writeTag(writer, tagName);
    }
    else
    {
      writeTag(writer, tagName, "name", band.getName(), OPEN);
    }

    ElementStyleSheet styleSheet = band.getStyle();
    if (isStyleSheetEmpty(styleSheet) == false)
    {
      writeTag(writer, ElementHandler.STYLE_TAG);
      ElementStyleSheet parentSheet = null;
      if (parent != null)
      {
        parentSheet = parent.getBandDefaults();
      }

      final StyleWriter styleWriter =
          new StyleWriter(getReportWriter(), band.getStyle(), parentSheet, getIndentLevel());
      styleWriter.write(writer);
      writeCloseTag(writer, ElementHandler.STYLE_TAG);
    }

    ElementStyleSheet bandDefaults = band.getBandDefaults();
    if (isStyleSheetEmpty(bandDefaults) == false)
    {
      writeTag(writer, BandHandler.DEFAULT_STYLE_TAG);
      final StyleWriter defaultStyleWriter =
          new StyleWriter(getReportWriter(), band.getBandDefaults(), null, getIndentLevel());
      defaultStyleWriter.write(writer);
      writeCloseTag(writer, BandHandler.DEFAULT_STYLE_TAG);
    }

    writeDataSourceForElement(band, writer);

    final Element[] list = band.getElementArray();
    for (int i = 0; i < list.length; i++)
    {
      if (list[i] instanceof Band)
      {
        final Band b = (Band) list[i];
        writeBand(writer, BandHandler.BAND_TAG, b, band);
      }
      else
      {
        writeElement(writer, list[i], band);
      }
    }
    writeCloseTag(writer, tagName);
  }

  private boolean isStyleSheetEmpty (ElementStyleSheet es)
  {
    if (es.getParents().isEmpty() && es.getDefinedPropertyNames().hasNext() == false)
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
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeElement(final Writer writer, final Element element, final Band parent)
      throws IOException, ReportWriterException
  {
    if (parent.getElements().indexOf(element) == -1)
    {
      throw new IllegalArgumentException("The given Element is no child of the band");
    }

    final Properties p = new Properties();
    if (element.getName().startsWith(Element.ANONYMOUS_ELEMENT_PREFIX) == false)
    {
      p.setProperty("name", element.getName());
    }
    p.setProperty("type", element.getContentType());
    writeTag(writer, BandHandler.ELEMENT_TAG, p, OPEN);

    ElementStyleSheet styleSheet = element.getStyle();
    if (isStyleSheetEmpty(styleSheet) == false)
    {
      writeTag(writer, ElementHandler.STYLE_TAG);

      final StyleWriter styleWriter =
          new StyleWriter(getReportWriter(), element.getStyle(),
              parent.getBandDefaults(), getIndentLevel());
      styleWriter.write(writer);
      writeCloseTag(writer, ElementHandler.STYLE_TAG);
    }

    writeDataSourceForElement(element, writer);

    writeCloseTag(writer, BandHandler.ELEMENT_TAG);
  }

  protected void writeDataSourceForElement (Element element, Writer writer)
    throws ReportWriterException, IOException
  {
    if ((element.getDataSource() instanceof EmptyDataSource))
    {
      return;
    }
    if (element.getDataSource() instanceof Template == false)
    {
      writeDataSource(writer, element.getDataSource());
      return;
    }

    TemplateCollector tc = getReportWriter().getTemplateCollector();

    Template template = (Template) element.getDataSource();

    // the template description of the element template will get the
    // template name as its name.
    TemplateDescription templateDescription =
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
    String templateExtends = (String) getReport().getReportBuilderHints().getHint
        (element, "ext.parser.template-reference", String.class);
    if (templateExtends != null)
    {
      TemplateDescription parent = TemplatesWriter.getTemplateDescription(getReportWriter(), templateExtends);
      if (parent != null)
      {
        parentTemplate = parent;
      }
    }
    TemplateWriter templateWriter = new TemplateWriter
        (getReportWriter(), getIndentLevel(),
            templateDescription, parentTemplate);
    templateWriter.write(writer);
  }

  /**
   * Writes a data source to a character stream writer.
   *
   * @param writer  the character stream writer.
   * @param datasource  the datasource.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeDataSource(final Writer writer, final DataSource datasource)
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
    final DataSourceWriter dsWriter =
        new DataSourceWriter(getReportWriter(), datasource, od, getIndentLevel());

    final DataSourceCollector dataSourceCollector = getReportWriter().getDataSourceCollector();
    final String dsname = dataSourceCollector.getDataSourceName(od);
    if (dsname == null)
    {
      throw new ReportWriterException("No name for DataSource " + datasource);
    }

    writeTag(writer, DataSourceHandler.DATASOURCE_TAG, "type", dsname, OPEN);
    dsWriter.write(writer);
    writeCloseTag(writer, DataSourceHandler.DATASOURCE_TAG);
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
    writeTag(writer, ReportDescriptionHandler.GROUPS_TAG);

    final GroupList list = getReport().getGroups();
    for (int i = 0; i < list.size(); i++)
    {
      final Group g = list.get(i);
      writeTag(writer, GroupsHandler.GROUP_TAG, "name", g.getName(), OPEN);
      writeTag(writer, GroupHandler.FIELDS_TAG);
      final List fields = g.getFields();
      for (int f = 0; f < fields.size(); f++)
      {
        writeTag(writer, GroupHandler.FIELD_TAG);
        writer.write(normalize(String.valueOf(fields.get(f))));
        writeCloseTag(writer, GroupHandler.FIELD_TAG);
      }
      writeCloseTag(writer, GroupHandler.FIELDS_TAG);

      writeBand(writer, GroupHandler.GROUP_HEADER_TAG, g.getHeader(), null);
      writeBand(writer, GroupHandler.GROUP_FOOTER_TAG, g.getFooter(), null);

      writeCloseTag(writer, GroupsHandler.GROUP_TAG);
    }
    writeCloseTag(writer, ReportDescriptionHandler.GROUPS_TAG);
  }
}
