/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ----------------------------------
 * ElementStyleSheet.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.style;

import com.jrefinery.report.ElementAlignment;

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

public class ElementStyleSheet implements StyleSheet, Cloneable, Serializable
{
  public static final StyleKey MINIMUMSIZE   = StyleKey.getStyleKey("min-size", Dimension2D.class);
  public static final StyleKey MAXIMUMSIZE   = StyleKey.getStyleKey("max-size", Dimension2D.class);
  public static final StyleKey PREFERREDSIZE = StyleKey.getStyleKey("preferred-size", Dimension2D.class);

  public static final StyleKey BOUNDS        = StyleKey.getStyleKey("bounds", Rectangle2D.class);

  public static final StyleKey VISIBLE       = StyleKey.getStyleKey("visible", Boolean.class);
  public static final StyleKey PAINT         = StyleKey.getStyleKey("paint", Paint.class);     // java.awt.Paint
  public static final StyleKey STROKE        = StyleKey.getStyleKey("stroke", Stroke.class);     // java.awt.Paint

  public static final StyleKey FONT          = StyleKey.getStyleKey("font", String.class);
  public static final StyleKey FONTSIZE      = StyleKey.getStyleKey("font-size", Integer.class); // Integer
  public static final StyleKey BOLD          = StyleKey.getStyleKey("font-bold", Boolean.class); // Boolean
  public static final StyleKey ITALIC        = StyleKey.getStyleKey("font-italic", Boolean.class); // Boolean
  public static final StyleKey UNDERLINED    = StyleKey.getStyleKey("font-underline", Boolean.class); // Boolean
  public static final StyleKey STRIKETHROUGH = StyleKey.getStyleKey("font-strike", Boolean.class); // Boolean

  public static final StyleKey ALIGNMENT     = StyleKey.getStyleKey("alignment", ElementAlignment.class); // enum class
  public static final StyleKey VALIGNMENT     = StyleKey.getStyleKey("valignment", ElementAlignment.class); // enum class

  public static final StyleKey SCALE = StyleKey.getStyleKey("scale", Boolean.class);
  public static final StyleKey KEEP_ASPECT_RATIO = StyleKey.getStyleKey("keepAspectRatio", Boolean.class);

  private ArrayList parents;
  private Hashtable properties;
  private String name;

  public ElementStyleSheet (String name)
  {
    if (name == null) throw new NullPointerException();
    this.name = name;
    this.properties = new Hashtable();
    this.parents = new ArrayList();
  }

  public String getName()
  {
    return name;
  }

  public void addParent(ElementStyleSheet parent)
  {
    if (parent == null) throw new NullPointerException();

    parents.add (parent);
  }

  public void removeParent(ElementStyleSheet parent)
  {
    if (parent == null) throw new NullPointerException();

    parents.remove (parent);
  }

  public List getParents ()
  {
    return Collections.unmodifiableList(parents);
  }

  public Object getStyleProperty(StyleKey name)
  {
    return getStyleProperty(name, null);
  }

  public Object getStyleProperty(StyleKey name, Object defaultValue)
  {
    Object value = properties.get (name);
    if (value == null)
    {
      for (int i = 0; i < parents.size(); i++)
      {
        ElementStyleSheet st = (ElementStyleSheet) parents.get (i);
        value = st.getStyleProperty(name, defaultValue);
        if (value != null)
          return value;
      }
      return defaultValue;
    }
    return value;
  }

  public void setStyleProperty (StyleKey name, Object value)
  {
    if (name == null) throw new NullPointerException();
    if (value == null)
    {
      properties.remove (name);
    }
    else
    {
      if (name.getValueType().isAssignableFrom(value.getClass()) == false)
      {
        new Exception().printStackTrace();
        throw new ClassCastException ("Value is not assignable: " + value.getClass() + " is not assignable from " + name.getValueType());
      }
      properties.put (name, value);
    }
  }

  public Object clone () throws CloneNotSupportedException
  {
    ElementStyleSheet sc = (ElementStyleSheet) super.clone();
    sc.parents = (ArrayList) parents.clone();
    sc.properties = (Hashtable) properties.clone();
    return sc;
  }

  public boolean getBooleanStyleProperty (StyleKey name)
  {
    return getBooleanStyleProperty(name, false);
  }

  public boolean getBooleanStyleProperty (StyleKey name, boolean defaultValue)
  {
    Boolean b = (Boolean) getStyleProperty(name, new Boolean(defaultValue));
    return b.booleanValue();
  }

  public int getIntStyleProperty (StyleKey name, int def)
  {
    Integer i = (Integer) getStyleProperty(name, new Integer(def));
    return i.intValue();
  }

  public Font getFontStyleProperty ()
  {
    String name = (String) getStyleProperty(FONT);
    int size = getIntStyleProperty(FONTSIZE, -1);
    boolean bold = getBooleanStyleProperty(BOLD);
    boolean italic = getBooleanStyleProperty(ITALIC);
    int style = Font.PLAIN;
    if (bold) style += Font.BOLD;
    if (italic) style += Font.ITALIC;

    Font retval = new Font(name, style, size);
    return retval;
  }

  public void setFontStyleProperty (Font font)
  {
    if (font == null) throw new NullPointerException();
    setStyleProperty(FONT, font.getFontName());
    setStyleProperty(BOLD, new Boolean(font.isBold()));
    setStyleProperty(ITALIC, new Boolean(font.isItalic()));
    setStyleProperty(FONTSIZE, new Integer(font.getSize()));
  }
}
