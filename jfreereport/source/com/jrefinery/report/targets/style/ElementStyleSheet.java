/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------------
 * ElementStyleSheet.java
 * ----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementStyleSheet.java,v 1.7 2002/12/11 23:32:26 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 12-Dec-2002 : First BugFix: setFontStyle must use font.getName instead of font.getFontName
 *               or a totaly different font family is used. 
 */

package com.jrefinery.report.targets.style;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.util.Log;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * An element style-sheet contains zero, one or many attributes that affect the appearance of
 * report elements.  For each attribute, there is a predefined key that can be used to access
 * that attribute in the style sheet.
 * <p>
 * Every report element has an associated style-sheet.
 * <p>
 * A style-sheet maintains a list of parent style-sheets.  If an attribute is not defined in a
 * style-sheet, the code refers to the parent style-sheets to see if the attribute is defined
 * there.
 * <p>
 * All StyleSheet entries are checked against the StyleKeyDefinition for validity.
 *
 * @author Thomas Morgner
 */
public class ElementStyleSheet implements StyleSheet, Cloneable, Serializable
{
  /** A key for the 'minimum size' of an element. */
  public static final StyleKey MINIMUMSIZE   = StyleKey.getStyleKey("min-size", Dimension2D.class);

  /** A key for the 'maximum size' of an element. */
  public static final StyleKey MAXIMUMSIZE   = StyleKey.getStyleKey("max-size", Dimension2D.class);

  /** A key for the 'preferred size' of an element. */
  public static final StyleKey PREFERREDSIZE = StyleKey.getStyleKey("preferred-size",
                                                                    Dimension2D.class);

  /** A key for the 'bounds' of an element. */
  public static final StyleKey BOUNDS        = StyleKey.getStyleKey("bounds", Rectangle2D.class);

  /** A key for an element's 'visible' flag. */
  public static final StyleKey VISIBLE       = StyleKey.getStyleKey("visible", Boolean.class);

  /** A key for the 'paint' used to color an element. */
  public static final StyleKey PAINT         = StyleKey.getStyleKey("paint", Paint.class);

  /** A key for the 'stroke' used to draw an element. */
  public static final StyleKey STROKE        = StyleKey.getStyleKey("stroke", Stroke.class);

  /** A key for the 'font name' used to draw element text. */
  public static final StyleKey FONT          = StyleKey.getStyleKey("font", String.class);

  /** A key for the 'font size' used to draw element text. */
  public static final StyleKey FONTSIZE      = StyleKey.getStyleKey("font-size", Integer.class);

  /** A key for an element's 'bold' flag. */
  public static final StyleKey BOLD          = StyleKey.getStyleKey("font-bold", Boolean.class);

  /** A key for an element's 'italic' flag. */
  public static final StyleKey ITALIC        = StyleKey.getStyleKey("font-italic", Boolean.class);

  /** A key for an element's 'underlined' flag. */
  public static final StyleKey UNDERLINED    = StyleKey.getStyleKey("font-underline",
                                                                    Boolean.class);

  /** A key for an element's 'strikethrough' flag. */
  public static final StyleKey STRIKETHROUGH = StyleKey.getStyleKey("font-strike", Boolean.class);

  /** A key for the horizontal alignment of an element. */
  public static final StyleKey ALIGNMENT     = StyleKey.getStyleKey("alignment",
                                                                    ElementAlignment.class);

  /** A key for the vertical alignment of an element. */
  public static final StyleKey VALIGNMENT     = StyleKey.getStyleKey("valignment",
                                                                     ElementAlignment.class);

  /** A key for an element's 'scale' flag. */
  public static final StyleKey SCALE = StyleKey.getStyleKey("scale", Boolean.class);

  /** A key for an element's 'keep aspect ratio' flag. */
  public static final StyleKey KEEP_ASPECT_RATIO = StyleKey.getStyleKey("keepAspectRatio",
                                                                        Boolean.class);

  /** The style-sheet name. */
  private String name;

  /** The style-sheet properties. */
  private Hashtable properties;

  /** Storage for the parent style sheets (if any). */
  private ArrayList parents;

  /**
   * Creates a new element style-sheet with the given name.  The style-sheet initially contains
   * no attributes, and has no parent style-sheets.
   *
   * @param name  the name (null not permitted).
   */
  public ElementStyleSheet (String name)
  {
    if (name == null)
    {
      throw new NullPointerException("ElementStyleSheet constructor: name is null.");
    }
    this.name = name;
    this.properties = new Hashtable();
    this.parents = new ArrayList();
  }

  /**
   * Returns the name of the style-sheet.
   *
   * @return the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Adds a parent style-sheet. Parents are queried in reverse order of addition,
   * so the last added parent is queried first.
   *
   * @param parent  the parent (null not permitted).
   */
  public void addParent(ElementStyleSheet parent)
  {
    addParent(0, parent);
  }

  /**
   * Adds a parent style-sheet. Parents on a lower position are queried before any
   * parent with an higher position in the list.
   *
   * @param parent  the parent (null not permitted).
   * @param position the position where to insert the parent style sheet
   *
   * @throws NullPointerException if the given parent is null
   * @throws IndexOutOfBoundsException if the position is invalid (pos &lt; 0 or pos &gt;=
   *         numberOfParents)
   */
  public void addParent (int position, ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.addParent(...): parent is null.");
    }
    parents.add (position, parent);
  }

  /**
   * Removes a parent style-sheet.
   *
   * @param parent  the style-sheet to remove (null not permitted).
   */
  public void removeParent(ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.removeParent(...): parent is null.");
    }
    parents.remove (parent);
  }

  /**
   * Returns the number of parents currently added to the stylesheet.
   *
   * @return the number of parents
   */
  public int getParentCount ()
  {
    return parents.size();
  }

  /**
   * Returns a list of the parent style-sheets.
   *
   * @return the list.
   */
  public List getParents ()
  {
    return Collections.unmodifiableList(parents);
  }

  /**
   * Returns the value of a style.  If the style is not found in this style-sheet, the code looks
   * in the parent style-sheets.  If the style is not found in any of the parent style-sheets, then
   * <code>null</code> is returned.
   *
   * @param key  the style key.
   *
   * @return the value.
   */
  public Object getStyleProperty(StyleKey key)
  {
    return getStyleProperty(key, null);
  }

  /**
   * Returns the value of a style.  If the style is not found in this style-sheet, the code looks
   * in the parent style-sheets.  If the style is not found in any of the parent style-sheets, then
   * the default value (possibly <code>null</code>) is returned.
   *
   * @param key  the style key.
   * @param defaultValue  the default value (null permitted).
   *
   * @return the value.
   */
  public Object getStyleProperty(StyleKey key, Object defaultValue)
  {
    Object value = properties.get (key);
    if (value == null)
    {
      for (int i = 0; i < parents.size(); i++)
      {
        ElementStyleSheet st = (ElementStyleSheet) parents.get (i);
        value = st.getStyleProperty(key, null);
        if (value != null)
        {
          return value;
        }
      }
      return defaultValue;
    }
    return value;
  }

  /**
   * Sets a style property (or removes the style if the value is <code>null</code>).
   *
   * @param key  the style key.
   * @param value  the value (if null, the style is removed).
   */
  public void setStyleProperty (StyleKey key, Object value)
  {
    if (key == null)
    {
      throw new NullPointerException("ElementStyleSheet.setStyleProperty: key is null.");
    }
    if (value == null)
    {
      properties.remove (key);
    }
    else
    {
      if (key.getValueType().isAssignableFrom(value.getClass()) == false)
      {
        new Exception().printStackTrace();
        throw new ClassCastException ("Value is not assignable: " + value.getClass()
                                      + " is not assignable from " + key.getValueType());
      }
      properties.put (key, value);
    }
  }

  /**
   * Clones the style-sheet.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    ElementStyleSheet sc = (ElementStyleSheet) super.clone();
    sc.parents = (ArrayList) parents.clone();
    sc.properties = (Hashtable) properties.clone();
    return sc;
  }

  /**
   * Returns a boolean style (defaults to false if the style is not found).
   *
   * @param key  the style key.
   *
   * @return true or false.
   */
  public boolean getBooleanStyleProperty (StyleKey key)
  {
    return getBooleanStyleProperty(key, false);
  }

  /**
   * Returns a boolean style.
   *
   * @param key  the style key.
   * @param defaultValue  the default value.
   *
   * @return true or false.
   */
  public boolean getBooleanStyleProperty (StyleKey key, boolean defaultValue)
  {
    Boolean b = (Boolean) getStyleProperty(key, new Boolean(defaultValue));
    return b.booleanValue();
  }

  /**
   * Returns an integer style.
   *
   * @param key  the style key.
   * @param def  the default value.
   *
   * @return the style value.
   */
  public int getIntStyleProperty (StyleKey key, int def)
  {
    Integer i = (Integer) getStyleProperty(key, new Integer(def));
    return i.intValue();
  }

  /**
   * Returns the font for this style-sheet.
   *
   * @return the font.
   */
  public Font getFontStyleProperty ()
  {
    String name = (String) getStyleProperty(FONT);
    int size = getIntStyleProperty(FONTSIZE, -1);
    boolean bold = getBooleanStyleProperty(BOLD);
    boolean italic = getBooleanStyleProperty(ITALIC);
    int style = Font.PLAIN;
    if (bold)
    {
      style += Font.BOLD;
    }
    if (italic)
    {
      style += Font.ITALIC;
    }
    Font retval = new Font(name, style, size);
    return retval;
  }

  /**
   * Sets the font for this style-sheet.
   *
   * @param font  the font (null not permitted).
   */
  public void setFontStyleProperty (Font font)
  {
    if (font == null)
    {
      throw new NullPointerException("ElementStyleSheet.setFontStyleProperty: font is null.");
    }
    setStyleProperty(FONT, font.getName());
    setStyleProperty(BOLD, new Boolean(font.isBold()));
    setStyleProperty(ITALIC, new Boolean(font.isItalic()));
    setStyleProperty(FONTSIZE, new Integer(font.getSize()));
  }
}
