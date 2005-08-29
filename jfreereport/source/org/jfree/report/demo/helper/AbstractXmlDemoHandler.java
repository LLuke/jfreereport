package org.jfree.report.demo.helper;

import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.ReportGenerator;

/**
 * Creation-Date: 27.08.2005, 12:44:09
 *
 * @author: Thomas Morgner
 */
public abstract class AbstractXmlDemoHandler extends AbstractDemoHandler
        implements XmlDemoHandler
{
  public AbstractXmlDemoHandler()
  {
  }

  protected JFreeReport parseReport() throws ReportDefinitionException
  {
    final URL in = getReportDefinitionSource();
    if (in == null)
    {
      throw new ReportDefinitionException("URL is invalid");
    }

    try
    {
      final ReportGenerator generator = ReportGenerator.getInstance();
      return generator.parseReport(in);
    }
    catch(Exception e)
    {
      throw new ReportDefinitionException("Parsing failed", e);
    }
  }

  public PreviewHandler getPreviewHandler()
  {
    return new DefaultPreviewHandler(this);
  }
}
