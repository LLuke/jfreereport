/**
 * Date: Jan 31, 2003
 * Time: 4:48:32 PM
 *
 * $Id: FontDefinitionObjectDescription.java,v 1.1 2003/02/01 18:30:58 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;

public class FontDefinitionObjectDescription extends AbstractObjectDescription
{
  public static final String FONT_ENCODING = "fontEncoding";
  public static final String FONT_NAME = "fontName";
  public static final String FONT_SIZE = "fontSize";
  public static final String BOLD = "bold";
  public static final String EMBEDDED_FONT = "embeddedFont";
  public static final String ITALIC = "italic";
  public static final String STRIKETHROUGH = "strikethrough";
  public static final String UNDERLINE = "underline";

  public FontDefinitionObjectDescription()
  {
    super(FontDefinition.class);
    setParameterDefinition(FONT_ENCODING, String.class);
    setParameterDefinition(FONT_NAME, String.class);
    setParameterDefinition(FONT_SIZE, Integer.class);
    setParameterDefinition(BOLD, Boolean.class);
    setParameterDefinition(EMBEDDED_FONT, Boolean.class);
    setParameterDefinition(ITALIC, Boolean.class);
    setParameterDefinition(STRIKETHROUGH, Boolean.class);
    setParameterDefinition(UNDERLINE, Boolean.class);
  }

  private boolean getBooleanParameter (String name)
  {
    Boolean bool = (Boolean) getParameter(name);
    if (bool == null)
      return false;
    return bool.booleanValue();
  }

  private int getIntegerParameter (String name)
    throws ObjectFactoryException
  {
    Integer i = (Integer) getParameter(name);
    if (i == null)
      throw new ObjectFactoryException("Parameter " + name + " is not set");

    return i.intValue();
  }

  public Object createObject()
  {
    try
    {
      String fontEncoding = (String) getParameter(FONT_ENCODING);
      String fontName = (String) getParameter(FONT_NAME);
      int fontSize = getIntegerParameter(FONT_SIZE);
      boolean bold = getBooleanParameter(BOLD);
      boolean embedded = getBooleanParameter(EMBEDDED_FONT);
      boolean italic = getBooleanParameter(ITALIC);
      boolean strike = getBooleanParameter(STRIKETHROUGH);
      boolean underline = getBooleanParameter(UNDERLINE);
      return new FontDefinition(fontName, fontSize, bold, italic, underline, strike, fontEncoding, embedded);
    }
    catch (Exception e)
    {
      Log.info ("Failed to create FontDefinition: ", e);
      return null;
    }
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if ((o instanceof FontDefinition) == false)
      throw new ObjectFactoryException("Given object is no font definition");

    FontDefinition fdef = (FontDefinition) o;
    setParameter(FONT_ENCODING, fdef.getFontEncoding(null));
    setParameter(FONT_NAME, fdef.getFontName());
    setParameter(FONT_SIZE, new Integer(fdef.getFontSize()));
    setParameter(BOLD, new Boolean(fdef.isBold()));
    setParameter(EMBEDDED_FONT, new Boolean(fdef.isEmbeddedFont()));
    setParameter(ITALIC, new Boolean(fdef.isItalic()));
    setParameter(STRIKETHROUGH, new Boolean(fdef.isStrikeThrough()));
    setParameter(UNDERLINE, new Boolean(fdef.isUnderline()));
  }
}
