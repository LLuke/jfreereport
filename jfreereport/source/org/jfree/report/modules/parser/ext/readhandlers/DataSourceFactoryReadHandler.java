package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceFactory;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ParseException;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DataSourceFactoryReadHandler extends AbstractXmlReadHandler
{
  public DataSourceFactoryReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes attrs)
          throws SAXException, XmlReaderException
  {
    final String className = attrs.getValue("class");
    if (className == null)
    {
      throw new ParseException("Attribute 'class' is missing.", getRootHandler().getLocator());
    }
    final DataSourceCollector fc =
        (DataSourceCollector) getRootHandler().getHelperObject
            (ReportDefinitionReadHandler.DATASOURCE_FACTORY_KEY);

    final DataSourceFactory factory = (DataSourceFactory)
            ObjectUtilities.loadAndInstantiate(className, getClass());
    fc.addFactory(factory);
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return null;
  }
}
