/**
 * Date: Jan 13, 2003
 * Time: 7:06:26 PM
 *
 * $Id: ReportDescriptionWriter.java,v 1.2 2003/01/22 19:38:28 taqua Exp $
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
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

public class ReportDescriptionWriter extends AbstractXMLDefinitionWriter
{
  public ReportDescriptionWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  public void write(Writer writer) throws IOException, ReportWriterException
  {
    writeTag(writer, ExtReportHandler.REPORT_DESCRIPTION_TAG);
    writeBand(writer, ReportDescriptionHandler.REPORT_HEADER_TAG, getReport().getReportHeader(), null);
    writeBand(writer, ReportDescriptionHandler.REPORT_FOOTER_TAG, getReport().getReportFooter(), null);
    writeBand(writer, ReportDescriptionHandler.PAGE_HEADER_TAG, getReport().getPageHeader(), null);
    writeBand(writer, ReportDescriptionHandler.PAGE_FOOTER_TAG, getReport().getPageFooter(), null);
    writeGroups(writer);
    writeBand(writer, ReportDescriptionHandler.ITEMBAND_TAG, getReport().getItemBand(), null);
    writeCloseTag(writer, ExtReportHandler.REPORT_DESCRIPTION_TAG);
  }

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
    StyleWriter defaultStyleWriter = new StyleWriter(getReportWriter(), band.getStyle(), null);
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

  private void writeElement (Writer writer, Element element, Band parent)
    throws IOException, ReportWriterException
  {
    if (parent.getElements().indexOf(element) == -1)
      throw new IllegalArgumentException("The given Element is no child of the band");

    Properties p = new Properties();
    p.setProperty ("name", element.getName());
    p.setProperty ("type", element.getContentType());
    writeTag(writer, BandHandler.ELEMENT_TAG, p, OPEN);

    writeTag(writer, ElementHandler.STYLE_TAG);

    StyleWriter styleWriter = new StyleWriter(getReportWriter(), element.getStyle(), parent.getBandDefaults());
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
      throw new ReportWriterException("No name for DataSource " + datasource);


    writeTag(writer, DataSourceHandler.DATASOURCE_TAG, "type", dsname, OPEN);
    dsWriter.write(writer);
    writeCloseTag(writer, DataSourceHandler.DATASOURCE_TAG);
  }

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
