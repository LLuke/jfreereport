/**
 * Date: Jan 13, 2003
 * Time: 7:06:26 PM
 *
 * $Id: ReportDescriptionWriter.java,v 1.1 2003/01/13 21:39:19 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.Band;
import com.jrefinery.report.GroupList;
import com.jrefinery.report.Group;
import com.jrefinery.report.Element;
import com.jrefinery.report.filter.EmptyDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.templates.Template;
import com.jrefinery.report.io.ext.ReportDescriptionHandler;
import com.jrefinery.report.io.ext.GroupsHandler;
import com.jrefinery.report.io.ext.GroupHandler;
import com.jrefinery.report.io.ext.ElementHandler;
import com.jrefinery.report.io.ext.BandHandler;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

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
    writeBand(writer, ReportDescriptionHandler.REPORT_HEADER_TAG, getReport().getReportHeader());
    writeBand(writer, ReportDescriptionHandler.REPORT_FOOTER_TAG, getReport().getReportFooter());
    writeBand(writer, ReportDescriptionHandler.PAGE_HEADER_TAG, getReport().getPageHeader());
    writeBand(writer, ReportDescriptionHandler.PAGE_FOOTER_TAG, getReport().getPageFooter());
    writeGroups(writer);
    writeBand(writer, ReportDescriptionHandler.ITEMBAND_TAG, getReport().getItemBand());
  }

  private void writeBand (Writer writer, String tagName, Band band)
    throws IOException, ReportWriterException
  {
    writeTag(writer, tagName, "name", band.getName(), OPEN);

    writeTag(writer, ElementHandler.STYLE_TAG);
    StyleWriter styleWriter = new StyleWriter(getReportWriter(), band.getStyle());
    styleWriter.write(writer);
    writeCloseTag(writer, ElementHandler.STYLE_TAG);

    writeTag(writer, BandHandler.DEFAULT_STYLE_TAG);
    StyleWriter defaultStyleWriter = new StyleWriter(getReportWriter(), band.getStyle());
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
        writeBand(writer, "band", (Band) list[i]);
      }
      else
      {
        writeElement (writer, list[i]);
      }
    }
    writeCloseTag(writer, tagName);
  }

  private void writeElement (Writer writer, Element element)
    throws IOException, ReportWriterException
  {
    writeTag(writer, BandHandler.ELEMENT_TAG, "name", element.getName(), OPEN);

    writeTag(writer, ElementHandler.STYLE_TAG);
    StyleWriter styleWriter = new StyleWriter(getReportWriter(), element.getStyle());
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
    dsWriter.write(writer);
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

      writeBand(writer, GroupHandler.GROUP_HEADER_TAG, g.getHeader());
      writeBand(writer, GroupHandler.GROUP_FOOTER_TAG, g.getFooter());

      writeCloseTag(writer, GroupsHandler.GROUP_TAG);
    }
    writeCloseTag(writer, ReportDescriptionHandler.GROUPS_TAG);
  }
}
