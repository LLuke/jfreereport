/**
 * Date: Jan 10, 2003
 * Time: 8:00:51 PM
 *
 * $Id: BasicObjectHandler.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.CharacterEntityParser;

public class BasicObjectHandler implements ReportDefinitionHandler
{
  private Parser parser;
  private String finishTag;
  private StringBuffer buffer;
  private ObjectDescription objectDescription;
  private CharacterEntityParser entityParser;

  public BasicObjectHandler(Parser parser, String finishTag, ObjectDescription od)
  {
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.parser = parser;
    this.finishTag = finishTag;
    this.buffer = new StringBuffer();
    this.objectDescription = od;
  }

  public BasicObjectHandler(Parser parser, String finishTag, Class targetObject)
    throws SAXException
  {
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.parser = parser;
    this.finishTag = finishTag;
    this.buffer = new StringBuffer();

    ClassFactory fact = (ClassFactory) getParser().getConfigurationValue(
        ParserConfigHandler.OBJECT_FACTORY_TAG);
    objectDescription = fact.getDescriptionForClass(targetObject);
    if (objectDescription == null)
    {
      throw new SAXException("No object definition for class " + targetObject);
    }
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    throw new SAXException("Element '" + finishTag + "' has no child-elements.");
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    buffer.append(ch, start,  length);
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(finishTag) == false)
    {
      throw new SAXException("Expected tag '" + finishTag + "'");
    }
    ObjectDescription od = getTargetObjectDescription();
    od.setParameter("value", entityParser.decodeEntities(buffer.toString()));
    getParser().popFactory().endElement(tagName);
  }

  public Parser getParser()
  {
    return parser;
  }

  public Object getValue () throws SAXException
  {
    return getTargetObjectDescription().createObject();
  }

  protected ObjectDescription getTargetObjectDescription ()
  {
    return objectDescription;
  }

  protected String getFinishTag()
  {
    return finishTag;
  }
}
