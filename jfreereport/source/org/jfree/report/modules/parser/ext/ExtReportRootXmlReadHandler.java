package org.jfree.report.modules.parser.ext;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.ext.readhandlers.ReportDefinitionReadHandler;
import org.jfree.xml.FrontendDefaultHandler;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.jfree.xml.util.ObjectFactory;
import org.jfree.xml.util.SimpleObjectFactory;

public class ExtReportRootXmlReadHandler extends RootXmlReadHandler
{
  /** The object factory loader. */
  private ObjectFactory factoryLoader;

  /**
   * Creates a new root handler for reading {@link org.jfree.report.JFreeReport} objects from XML.
   */
  public ExtReportRootXmlReadHandler() {
    factoryLoader = new SimpleObjectFactory();
    setRootHandler(new ReportDefinitionReadHandler());
  }

  /**
   * Returns the object factory loader.
   *
   * @return the object factory loader.
   */
  public ObjectFactory getFactoryLoader() {
      return this.factoryLoader;
  }

  /**
   * Returns the chart under construction.
   *
   * @return the chart.
   */
  public JFreeReport getReport() {
      try {
          return (JFreeReport) getRootHandler().getObject();
      }
      catch (Exception e) {
          return null;
      }
  }

  /**
   * Returns a new instance of the parser.
   *
   * @return a new instance of the parser.
   */
  public FrontendDefaultHandler newInstance ()
  {
    return new ExtReportRootXmlReadHandler();
  }

}
