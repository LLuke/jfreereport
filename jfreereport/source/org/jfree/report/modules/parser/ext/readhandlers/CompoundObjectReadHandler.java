package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.ElementDefinitionException;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

public class CompoundObjectReadHandler extends BasicObjectReadHandler
{
  private HashMap basicObjects;
  private HashMap compoundObjects;

  public CompoundObjectReadHandler (final ObjectDescription objectDescription)
  {
    super(objectDescription);
    basicObjects = new HashMap();
    compoundObjects = new HashMap();
  }

  protected HashMap getBasicObjects ()
  {
    return basicObjects;
  }

  protected HashMap getCompoundObjects ()
  {
    return compoundObjects;
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
    final ObjectDescription objectDescription = getObjectDescription();
    final Iterator basicObjectsEntries = basicObjects.entrySet().iterator();
    while (basicObjectsEntries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) basicObjectsEntries.next();
      final String name = (String) entry.getKey();
      final BasicObjectReadHandler readHandler = (BasicObjectReadHandler) entry.getValue();
      objectDescription.setParameter(name, readHandler.getObject());
    }

    final Iterator compoundObjectsEntries = compoundObjects.entrySet().iterator();
    while (compoundObjectsEntries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) compoundObjectsEntries.next();
      final String name = (String) entry.getKey();
      final CompoundObjectReadHandler readHandler = (CompoundObjectReadHandler) entry.getValue();
      objectDescription.setParameter(name, readHandler.getObject());
    }
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
                                               final Attributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("basic-object"))
    {
      return handleBasicObject(atts);
    }
    else if (tagName.equals("compound-object"))
    {
      return handleCompoundObject(atts);
    }
    return null;
  }

  protected XmlReadHandler handleBasicObject (final Attributes atts)
          throws ElementDefinitionException
  {
    final String name = atts.getValue("name");
    if (name == null)
    {
      throw new ElementDefinitionException
              ("Required attribute 'name' is missing.");
    }

    final ClassFactory fact = getClassFactory();
    final ObjectDescription objectDescription =
            ObjectFactoryUtility.findDescription
            (fact, getObjectDescription().getParameterDefinition(name));

    final BasicObjectReadHandler readHandler =
            new BasicObjectReadHandler(objectDescription);
    basicObjects.put(name, readHandler);
    return readHandler;
  }

  protected XmlReadHandler handleCompoundObject (final Attributes atts)
          throws ElementDefinitionException
  {
    final String name = atts.getValue("name");
    if (name == null)
    {
      throw new ElementDefinitionException
              ("Required attribute 'name' is missing.");
    }

    final ClassFactory fact = getClassFactory();
    final ObjectDescription objectDescription =
            ObjectFactoryUtility.findDescription
            (fact, getObjectDescription().getParameterDefinition(name));

    final CompoundObjectReadHandler readHandler =
            new CompoundObjectReadHandler(objectDescription);
    compoundObjects.put(name, readHandler);
    return readHandler;
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
    handleStartParsing(attrs);
  }
}
