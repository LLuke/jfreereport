/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------------------------
 * FontDefinitionObjectDescription.java
 * ------------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FontDefinitionObjectDescription.java,v 1.6 2003/04/09 16:18:39 mungady Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;
import com.jrefinery.xml.factory.objects.AbstractObjectDescription;
import com.jrefinery.xml.factory.objects.ObjectFactoryException;

/**
 * An object-description for a {@link FontDefinition} object.
 * 
 * @author Thomas Morgner
 */
public class FontDefinitionObjectDescription extends AbstractObjectDescription
{
  /** The font encoding parameter name. */
  public static final String FONT_ENCODING = "fontEncoding";
  
  /** The font name parameter name. */
  public static final String FONT_NAME = "fontName";
  
  /** The font size parameter name. */
  public static final String FONT_SIZE = "fontSize";
  
  /** The bold attribute text. */
  public static final String BOLD = "bold";
  
  /** The embedded font attribute text. */
  public static final String EMBEDDED_FONT = "embeddedFont";
  
  /** The italic attribute text. */
  public static final String ITALIC = "italic";
  
  /** The strikethrough attribute text. */
  public static final String STRIKETHROUGH = "strikethrough";
  
  /** The underline attribute text. */
  public static final String UNDERLINE = "underline";

  /**
   * Creates a new object description.
   */
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

  /**
   * Returns a parameter value as a boolean.
   * 
   * @param name  the parameter name.
   * 
   * @return A boolean.
   */
  private boolean getBooleanParameter (String name)
  {
    Boolean bool = (Boolean) getParameter(name);
    if (bool == null)
    {
      return false;
    }
    return bool.booleanValue();
  }

  /**
   * Returns a parameter as an int.
   * 
   * @param name  the parameter name.
   * 
   * @return The parameter value.
   * 
   * @throws com.jrefinery.xml.factory.objects.ObjectFactoryException if there is a problem while reading the
   * properties of the given object.
   */
  private int getIntegerParameter (String name)
    throws ObjectFactoryException
  {
    Integer i = (Integer) getParameter(name);
    if (i == null)
    {
      throw new ObjectFactoryException("Parameter " + name + " is not set");
    }
    return i.intValue();
  }

  /**
   * Creates an object based on this description.
   * 
   * @return The object.
   */
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
      return new FontDefinition(fontName, fontSize, bold, italic, underline, strike, 
                                fontEncoding, embedded);
    }
    catch (Exception e)
    {
      Log.info ("Failed to create FontDefinition: ", e);
      return null;
    }
  }

  /**
   * Sets the parameters of this description object to match the supplied object.
   * 
   * @param o  the object (should be an instance of <code>FontDefinition</code>).
   * 
   * @throws com.jrefinery.xml.factory.objects.ObjectFactoryException if the object is not an instance of <code>Float</code>.
   */
  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if ((o instanceof FontDefinition) == false)
    {
      throw new ObjectFactoryException(
          "The given object is no com.jrefinery.report.targets.FontDefinition.");
    }
    
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
