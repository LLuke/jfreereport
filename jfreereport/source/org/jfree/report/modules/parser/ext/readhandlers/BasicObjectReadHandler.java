package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.coretypes.StringReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BasicObjectReadHandler extends AbstractPropertyXmlReadHandler
{
  private ObjectDescription objectDescription;
  private StringReadHandler stringReadHandler;
  private ClassFactory classFactory;
  private CommentHintPath commentHintPath;

  /**
   *
   * @param objectDescription may be null.
   * @param commentHintPath never null.
   */
  public BasicObjectReadHandler (final ObjectDescription objectDescription,
                                 final CommentHintPath commentHintPath)
  {
    this.stringReadHandler = new StringReadHandler();
    if (commentHintPath == null)
    {
      throw new NullPointerException("CommentHintPath must not be null.");
    }
    this.objectDescription = objectDescription;
    this.commentHintPath = commentHintPath;
  }

  /**
   * Initialises the handler.
   *
   * @param rootHandler the root handler.
   * @param tagName     the tag name.
   */
  public void init (final RootXmlReadHandler rootHandler, final String tagName)
  {
    super.init(rootHandler, tagName);
    this.classFactory = (ClassFactory)
        getRootHandler().getHelperObject(ReportDefinitionReadHandler.CLASS_FACTORY_KEY);
  }

  protected ObjectDescription getObjectDescription ()
  {
    return objectDescription;
  }

  protected void setObjectDescription (final ObjectDescription objectDescription)
  {
    this.objectDescription = objectDescription;
  }

  protected ClassFactory getClassFactory ()
  {
    return classFactory;
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
    handleStartParsing(attrs);
    getRootHandler().delegate(stringReadHandler, getTagName(), attrs);
  }

  protected void handleStartParsing (final Attributes attrs)
          throws ElementDefinitionException
  {
    final String name = attrs.getValue("name");
    if (name == null)
    {
      throw new ElementDefinitionException
              ("Required attribute 'name' is missing.", getRootHandler().getLocator());
    }


    final String attrClass = attrs.getValue("class");
    if (attrClass != null)
    {
      try
      {
        final Class clazz = Class.forName(attrClass);
        objectDescription = ObjectFactoryUtility.findDescription(classFactory, clazz);
      }
      catch (ClassNotFoundException e)
      {
        throw new ElementDefinitionException
                ("Value for given 'class' attribute is invalid",
                        getRootHandler().getLocator());
      }
    }
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    final String value = stringReadHandler.getResult();
    objectDescription.setParameter("value", value);
    super.doneParsing();
  }

  protected CommentHintPath getCommentHintPath ()
  {
    return commentHintPath;
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
    return objectDescription.createObject();
  }

  protected void storeComments ()
          throws SAXException
  {
    defaultStoreComments(commentHintPath);
  }
}
