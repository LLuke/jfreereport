package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ParseException;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ClassFactoryReadHandler extends AbstractPropertyXmlReadHandler
{
  private String className;

  public ClassFactoryReadHandler ()
  {
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
    className = attrs.getValue("class");
    if (className == null)
    {
      throw new ParseException("Attribute 'class' is missing.", getRootHandler().getLocator());
    }
    final ClassFactoryCollector fc =
        (ClassFactoryCollector) getRootHandler().getHelperObject
            (ReportDefinitionReadHandler.CLASS_FACTORY_KEY);

    final ClassFactory factory = (ClassFactory)
            ObjectUtilities.loadAndInstantiate(className, getClass());
    fc.addFactory(factory);
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath("report-definition");
    commentHintPath.addName("parser-config");
    commentHintPath.addName("object-factory");
    defaultStoreComments(commentHintPath);
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
