/**
 * Date: Jan 31, 2003
 * Time: 4:48:32 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;

public class FontDefinitionObjectDescription extends AbstractObjectDescription
{
  public FontDefinitionObjectDescription()
  {
    super(FontDefinition.class);
    setParameterDefinition("FontEncoding", String.class);
    setParameterDefinition("FontName", String.class);
    setParameterDefinition("FontSize", Integer.class);
    setParameterDefinition("Bold", Boolean.class);
    setParameterDefinition("EmbeddedFont", Boolean.class);
    setParameterDefinition("Italic", Boolean.class);
    setParameterDefinition("StrikeThrough", Boolean.class);
    setParameterDefinition("Underline", Boolean.class);
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
      String fontEncoding = (String) getParameter("FontEncoding");
      String fontName = (String) getParameter("FontName");
      int fontSize = getIntegerParameter("FontSize");
      boolean bold = getBooleanParameter("Bold");
      boolean embedded = getBooleanParameter("EmbeddedFont");
      boolean italic = getBooleanParameter("Italic");
      boolean strike = getBooleanParameter("StrikeThrough");
      boolean underline = getBooleanParameter("Underline");
      return new FontDefinition(fontName, fontSize, bold, italic, underline, strike, fontEncoding, embedded);
    }
    catch (Exception e)
    {
      Log.debug ("Failed to create FontDefinition: ", e);
      return null;
    }
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if ((o instanceof FontDefinition) == false)
      throw new ObjectFactoryException("Given object is no font definition");

    FontDefinition fdef = (FontDefinition) o;
    setParameter("FontEncoding", fdef.getFontEncoding(null));
    setParameter("FontName", fdef.getFontName());
    setParameter("FontSize", new Integer(fdef.getFontSize()));
    setParameter("Bold", new Boolean(fdef.isBold()));
    setParameter("EmbeddedFont", new Boolean(fdef.isEmbeddedFont()));
    setParameter("Italic", new Boolean(fdef.isItalic()));
    setParameter("StrikeThrough", new Boolean(fdef.isStrikeThrough()));
    setParameter("Underline", new Boolean(fdef.isUnderline()));
  }
}
