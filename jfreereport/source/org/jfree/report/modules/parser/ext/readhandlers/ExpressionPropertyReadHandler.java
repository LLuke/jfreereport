package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.beans.BeanException;
import org.jfree.report.util.beans.BeanUtility;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.ParseException;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.coretypes.StringReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ExpressionPropertyReadHandler extends StringReadHandler
{
  public static final String NAME_ATT = "name";
  public static final String CLASS_ATT = "class";

  private BeanUtility beanUtility;
  private CharacterEntityParser entityParser;
  private String propertyName;

  public ExpressionPropertyReadHandler (final BeanUtility expression,
                                        final CharacterEntityParser entityParser)
  {
    this.beanUtility = expression;
    this.entityParser = entityParser;
  }


  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes attrs)
          throws SAXException
  {
    super.startParsing(attrs);
    propertyName = attrs.getValue(NAME_ATT);
    if (propertyName == null)
    {
      throw new ElementDefinitionException("Required attribute 'name' is null.");
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
    super.doneParsing();
    final String result = getResult();
    if (beanUtility == null)
    {
      throw new ParseException("No current beanUtility");
    }
    try
    {
      beanUtility.setPropertyAsString
          (propertyName, entityParser.decodeEntities(result));
    }
    catch (BeanException e)
    {
      throw new ParseException("Unable to assign property '" + propertyName
              + "' to expression.", e);
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return null;
  }
}