/**
 * Date: Jan 12, 2003
 * Time: 5:21:13 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ext.CompoundObjectHandler;
import com.jrefinery.report.io.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

public class DataSourceHandler extends CompoundObjectHandler
{
  public static final String DATASOURCE_TAG = "datasource";

  private DataSourceHandler dataSourceHandler;

  public DataSourceHandler(Parser parser, String finishTag, String type)
    throws SAXException
  {
    super(parser, finishTag, lookupObjectDescription(parser, type));
  }

  private static ObjectDescription lookupObjectDescription (Parser parser, String type)
    throws SAXException
  {
    DataSourceCollector fc =
        (DataSourceCollector) parser.getConfigurationValue(ParserConfigHandler.DATASOURCE_FACTORY_TAG);
    ObjectDescription od = fc.getDataSourceDescription(type);
    if (od == null)
    {
      throw new SAXException("The specified DataSource type is not defined");
    }
    return od;
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(DATASOURCE_TAG) == false)
    {
      super.endElement(tagName);
      return;
    }

    if (dataSourceHandler == null)
    {
      super.endElement(tagName);
    }
    else
    {
      Log.debug ("DataSource ended " + hashCode());
      DataSource ds = (DataSource) dataSourceHandler.getValue();
      getTargetObjectDescription().setParameter("dataSource", ds);
      dataSourceHandler = null;
    }
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(DATASOURCE_TAG) == false)
    {
      super.startElement(tagName, attrs);
      return;
    }

    String typeName = attrs.getValue("type");
    if (typeName == null)
      throw new SAXException("The datasource type must be specified");

    Log.debug ("DataSource started: " + hashCode());

    dataSourceHandler = new DataSourceHandler(getParser(), tagName, typeName);
    getParser().pushFactory(dataSourceHandler);
  }
}
