/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: FontDefinitionObjectDescription.java,v 1.3 2003/08/25 14:29:33 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.objects;

import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;
import org.jfree.xml.factory.objects.AbstractObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

/**
 * An object-description for a {@link org.jfree.report.style.FontDefinition} object.
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
  private boolean getBooleanParameter(final String name)
  {
    final Boolean bool = (Boolean) getParameter(name);
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
   * @throws ObjectFactoryException if there is a problem while reading the
   * properties of the given object.
   */
  private int getIntegerParameter(final String name)
      throws ObjectFactoryException
  {
    final Integer i = (Integer) getParameter(name);
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
      final String fontEncoding = (String) getParameter(FONT_ENCODING);
      final String fontName = (String) getParameter(FONT_NAME);
      final int fontSize = getIntegerParameter(FONT_SIZE);
      final boolean bold = getBooleanParameter(BOLD);
      final boolean embedded = getBooleanParameter(EMBEDDED_FONT);
      final boolean italic = getBooleanParameter(ITALIC);
      final boolean strike = getBooleanParameter(STRIKETHROUGH);
      final boolean underline = getBooleanParameter(UNDERLINE);
      return new FontDefinition(fontName, fontSize, bold, italic, underline, strike,
          fontEncoding, embedded);
    }
    catch (Exception e)
    {
      Log.info("Failed to create FontDefinition: ", e);
      return null;
    }
  }

  /**
   * Sets the parameters of this description object to match the supplied object.
   *
   * @param o  the object (should be an instance of <code>FontDefinition</code>).
   *
   * @throws ObjectFactoryException if the object is not an instance of <code>Float</code>.
   */
  public void setParameterFromObject(final Object o) throws ObjectFactoryException
  {
    if ((o instanceof FontDefinition) == false)
    {
      throw new ObjectFactoryException(
          "The given object is no FontDefinition.");
    }

    final FontDefinition fdef = (FontDefinition) o;
    setParameter(FONT_ENCODING, fdef.getFontEncoding(null));
    setParameter(FONT_NAME, fdef.getFontName());
    setParameter(FONT_SIZE, new Integer(fdef.getFontSize()));
    setParameter(BOLD, getBoolean(fdef.isBold()));
    setParameter(EMBEDDED_FONT, getBoolean(fdef.isEmbeddedFont()));
    setParameter(ITALIC, getBoolean(fdef.isItalic()));
    setParameter(STRIKETHROUGH, getBoolean(fdef.isStrikeThrough()));
    setParameter(UNDERLINE, getBoolean(fdef.isUnderline()));
  }

  /**
   * Returns the correct Boolean object for the given primitive boolean variable.
   *
   * @param bool the primitive boolean.
   * @return the Boolean object.
   */
  private Boolean getBoolean(final boolean bool)
  {
    if (bool == true)
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
