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
 * ----------------------------
 * ReportDescriptionWriter.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDescriptionWriter.java,v 1.6 2003/04/23 13:43:04 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.GroupList;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.EmptyDataSource;
import com.jrefinery.report.filter.templates.Template;
import com.jrefinery.report.io.ext.BandHandler;
import com.jrefinery.report.io.ext.DataSourceHandler;
import com.jrefinery.report.io.ext.ElementHandler;
import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.GroupHandler;
import com.jrefinery.report.io.ext.GroupsHandler;
import com.jrefinery.report.io.ext.ReportDescriptionHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import org.jfree.xml.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

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
   */
  public ReportDescriptionWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  /**
   * Writes a report description element to a character stream writer.
   * 
   * @param writer  the character stream writer.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(Writer writer) throws IOException, ReportWriterException
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
  private void writeBand (Writer writer, String tagName, Band band, Band parent)
    throws IOException, ReportWriterException
  {
    writeTag(writer, tagName, "name", band.getName(), OPEN);

    writeTag(writer, ElementHandler.STYLE_TAG);
    ElementStyleSheet parentSheet = null;
    if (parent != null)
    {
      parentSheet = parent.getBandDefaults();
    }

    StyleWriter styleWriter = new StyleWriter(getReportWriter(), band.getStyle(), parentSheet);
    styleWriter.write(writer);
    writeCloseTag(writer, ElementHandler.STYLE_TAG);

    writeTag(writer, BandHandler.DEFAULT_STYLE_TAG);
    StyleWriter defaultStyleWriter = new StyleWriter(getReportWriter(), band.getBandDefaults(), 
                                                     null);
    defaultStyleWriter.write(writer);
    writeCloseTag(writer, BandHandler.DEFAULT_STYLE_TAG);

    if ((band.getDataSource() instanceof EmptyDataSource) == false)
    {
      if (band.getDataSource() instanceof Template)
      {
        writeTemplate(writer, (Template) band.getDataSource());
      }
      else
      {
        writeDataSource (writer, band.getDataSource());
      }
    }

    Element[] list = band.getElementArray();
    for (int i = 0; i < list.length; i++)
    {
      if (list[i] instanceof Band)
      {
        Band b = (Band) list[i];
        writeBand(writer, "band", b, band);
      }
      else
      {
        writeElement (writer, list[i], band);
      }
    }
    writeCloseTag(writer, tagName);
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
  private void writeElement (Writer writer, Element element, Band parent)
    throws IOException, ReportWriterException
  {
    if (parent.getElements().indexOf(element) == -1)
    {
      throw new IllegalArgumentException("The given Element is no child of the band");
    }
    
    Properties p = new Properties();
    p.setProperty ("name", element.getName());
    p.setProperty ("type", element.getContentType());
    writeTag(writer, BandHandler.ELEMENT_TAG, p, OPEN);

    writeTag(writer, ElementHandler.STYLE_TAG);

    StyleWriter styleWriter = new StyleWriter(getReportWriter(), element.getStyle(), 
                                              parent.getBandDefaults());
    styleWriter.write(writer);
    writeCloseTag(writer, ElementHandler.STYLE_TAG);

    if ((element.getDataSource() instanceof EmptyDataSource) == false)
    {
      if (element.getDataSource() instanceof Template)
      {
        writeTemplate(writer, (Template) element.getDataSource());
      }
      else
      {
        writeDataSource (writer, element.getDataSource());
      }
    }

    writeCloseTag(writer, BandHandler.ELEMENT_TAG);
  }

  /**
   * Writes a template to a character stream writer.
   * 
   * @param writer  the character stream writer.
   * @param template  the template.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeTemplate (Writer writer, Template template)
    throws IOException, ReportWriterException
  {
    Properties p = new Properties();
    if (template.getName() != null)
    {
      p.setProperty("name", template.getName());
    }
    TemplateDescription td =
        getReportWriter().getTemplateCollector().getDescription(template);
    if (td == null)
    {
      throw new ReportWriterException("Unknown template encountered: " + template.getClass());
    }
    p.setProperty("references", td.getName());

    writeTag(writer, ElementHandler.TEMPLATE_TAG, p, OPEN);

    ObjectWriter objectWriter = new ObjectWriter(getReportWriter(), template, td.getInstance());
    objectWriter.write(writer);

    writeCloseTag(writer, ElementHandler.TEMPLATE_TAG);
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
  private void writeDataSource (Writer writer, DataSource datasource)
    throws IOException, ReportWriterException
  {
    ObjectDescription od =
        getReportWriter().getClassFactoryCollector().getDescriptionForClass(datasource.getClass());
    if (od == null)
    {
      throw new ReportWriterException("Unable to resolve DataSource: " + datasource.getClass());
    }
    DataSourceWriter dsWriter = new DataSourceWriter(getReportWriter(), datasource, od);

    DataSourceCollector dataSourceCollector = getReportWriter().getDataSourceCollector();
    String dsname = dataSourceCollector.getDataSourceName(od);
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
  private void writeGroups (Writer writer)
      throws IOException, ReportWriterException
  {
    writeTag(writer, ReportDescriptionHandler.GROUPS_TAG);

    GroupList list = getReport().getGroups();
    for (int i = 0; i < list.size(); i++)
    {
      Group g = list.get(i);
      writeTag(writer, GroupsHandler.GROUP_TAG, "name", g.getName(), OPEN);
      writeTag(writer, GroupHandler.FIELDS_TAG);
      List fields = g.getFields();
      for (int f = 0; f < fields.size(); f++)
      {
        writeTag (writer, GroupHandler.FIELD_TAG);
        writer.write(normalize(String.valueOf(fields.get(f))));
        writeCloseTag (writer, GroupHandler.FIELD_TAG);
      }
      writeCloseTag(writer, GroupHandler.FIELDS_TAG);

      writeBand(writer, GroupHandler.GROUP_HEADER_TAG, g.getHeader(), null);
      writeBand(writer, GroupHandler.GROUP_FOOTER_TAG, g.getFooter(), null);

      writeCloseTag(writer, GroupsHandler.GROUP_TAG);
    }
    writeCloseTag(writer, ReportDescriptionHandler.GROUPS_TAG);
  }
}
