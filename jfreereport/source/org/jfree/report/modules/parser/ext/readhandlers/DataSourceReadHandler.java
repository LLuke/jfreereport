package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DataTarget;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.xml.ParseException;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class DataSourceReadHandler extends CompoundObjectReadHandler
{
  private DataSourceReadHandler childHandler;

  public DataSourceReadHandler (final CommentHintPath commentPath)
  {
    super(null, commentPath);
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException, XmlReaderException
  {
    final String typeName = attrs.getValue("type");
    if (typeName == null)
    {
      throw new ParseException("The datasource type must be specified",
              getRootHandler().getLocator());
    }

    final DataSourceCollector fc = (DataSourceCollector) getRootHandler().getHelperObject(
        ReportDefinitionReadHandler.DATASOURCE_FACTORY_KEY);
    final ObjectDescription od = fc.getDataSourceDescription(typeName);
    if (od == null)
    {
      throw new ParseException("The specified DataSource type is not defined");
    }
    setObjectDescription(od);
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("datasource"))
    {
      childHandler = new DataSourceReadHandler(getCommentHintPath().getInstance());
      return childHandler;
    }
    return super.getHandlerForChild(tagName, atts);
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
    final DataSource ds = (DataSource) super.getObject();
    if (childHandler != null && ds instanceof DataTarget)
    {
      final DataTarget dt = (DataTarget) ds;
      dt.setDataSource((DataSource) childHandler.getObject());
    }
    return ds;
  }
}
