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
 * ----------------------
 * ElementStyleSheet.java
 * ----------------------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementStyleSheet.java,v 1.28 2003/06/10 12:11:56 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 12-Dec-2002 : First BugFix: setFontStyle must use font.getName instead of font.getFontName
 *               or a totally different font family is used. 
 * 03-Jan-2002 : Javadoc updates (DG);
 * 
 */

package com.jrefinery.report.targets.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.util.SerializerHelper;
import com.jrefinery.report.targets.FontDefinition;

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
public class ElementStyleSheet implements StyleSheet, Cloneable, Serializable, StyleChangeListener
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
  public static final StyleKey PAINT         = StyleKey.getStyleKey("paint", Color.class);

  /** A key for the 'stroke' used to draw an element. */
  public static final StyleKey STROKE        = StyleKey.getStyleKey("stroke", Stroke.class);

  /** A key for the 'font name' used to draw element text. */
  public static final StyleKey FONT          = StyleKey.getStyleKey("font", String.class);

  /** A key for the 'font size' used to draw element text. */
  public static final StyleKey FONTSIZE      = StyleKey.getStyleKey("font-size", Integer.class);

  /** A key for the 'font size' used to draw element text. */
  public static final StyleKey LINEHEIGHT      = StyleKey.getStyleKey("line-height", Float.class);

  /** A key for an element's 'bold' flag. */
  public static final StyleKey BOLD          = StyleKey.getStyleKey("font-bold", Boolean.class);

  /** A key for an element's 'italic' flag. */
  public static final StyleKey ITALIC        = StyleKey.getStyleKey("font-italic", Boolean.class);

  /** A key for an element's 'underlined' flag. */
  public static final StyleKey UNDERLINED    = StyleKey.getStyleKey("font-underline",
                                                                    Boolean.class);

  /** A key for an element's 'strikethrough' flag. */
  public static final StyleKey STRIKETHROUGH = StyleKey.getStyleKey("font-strikethrough", 
                                                                    Boolean.class);

  /** A key for an element's 'embedd' flag. */
  public static final StyleKey EMBEDDED_FONT = StyleKey.getStyleKey("font-embedded", Boolean.class);

  /** A key for an element's 'embedd' flag. */
  public static final StyleKey FONTENCODING = StyleKey.getStyleKey("font-encoding", String.class);

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

  /** A key for the dynamic height flag for an element. */
  public static final StyleKey DYNAMIC_HEIGHT = StyleKey.getStyleKey("dynamic_height",
                                                                     Boolean.class);
  
  /**
   * The Layout Cacheable stylekey. Set this stylekey to false, to define that the element
   * is not cachable. This key defaults to true.
   */
  public static final StyleKey ELEMENT_LAYOUT_CACHEABLE = StyleKey.getStyleKey("layout-cacheable",
                                                                    Boolean.class);

  /** The style-sheet name. */
  private String name;

  /** The style-sheet properties. */
  private transient HashMap properties;

  /** Storage for the parent style sheets (if any). */
  private ArrayList parents;

  /** Storage for readonly style sheets. */
  private ArrayList defaultSheets;

  /** Parent style sheet cache. */
  private ElementStyleSheet[] parentsCached;
  
  /** Default style sheet cache. */
  private ElementStyleSheet[] defaultCached;
  
  /** Style cache. */
  private transient HashMap styleCache;

  /** Style change support. */
  private transient StyleChangeSupport styleChangeSupport;
  
  /** A flag that controls whether or not caching is allowed. */
  private boolean allowCaching;

  /**
   * Creates a new element style-sheet with the given name.  The style-sheet initially contains
   * no attributes, and has no parent style-sheets.
   *
   * @param name  the name (<code>null</code> not permitted).
   */
  public ElementStyleSheet (String name)
  {
    if (name == null)
    {
      throw new NullPointerException("ElementStyleSheet constructor: name is null.");
    }
    this.name = name;
    this.properties = new HashMap();
    this.parents = new ArrayList(5);
    this.defaultSheets = new ArrayList(5);
    this.styleChangeSupport = new StyleChangeSupport(this);
  }

  /**
   * Returns <code>true</code> if caching is allowed, and <code>false</code> otherwise.
   * 
   * @return A boolean.
   */
  public boolean isAllowCaching()
  {
    return allowCaching;
  }

  /**
   * Sets the flag that controls whether or not caching is allowed.
   * 
   * @param allowCaching  the flag value.
   */
  public void setAllowCaching(boolean allowCaching)
  {
    this.allowCaching = allowCaching;
  }

  /**
   * Returns the name of the style-sheet.
   *
   * @return the name (never <code>null</code>).
   */
  public String getName()
  {
    return name;
  }

  /**
   * Adds a parent style-sheet. This method adds the parent to the beginning of the
   * list, and guarantees, that this parent is queried first.
   *
   * @param parent  the parent (<code>null</code> not permitted).
   */
  public synchronized void addParent(ElementStyleSheet parent)
  {
    addParent(0, parent);
  }

  /**
   * Adds a parent style-sheet. This method adds the parent to the beginning of the
   * list, and guarantees, that this parent is queried first.
   * <p>
   * The default parents operations are reserved for the system internal stylesheet
   * operations. If you want to add own stylesheets, use the addParent methods.
   *
   * @param parent  the parent (<code>null</code> not permitted).
   */
  public synchronized void addDefaultParent(ElementStyleSheet parent)
  {
    addDefaultParent(0, parent);
  }

  /**
   * Adds a parent style-sheet. Parents on a lower position are queried before any
   * parent with an higher position in the list.
   *
   * @param position the position where to insert the parent style sheet
   * @param parent  the parent (<code>null</code> not permitted).
   *
   * @throws IndexOutOfBoundsException if the position is invalid (pos &lt; 0 or pos &gt;=
   *         numberOfParents)
   */
  public synchronized void addParent (int position, ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.addParent(...): parent is null.");
    }
    if (parent.isSubStyleSheet(this) == false)
    {
      parents.add (position, parent);
      parentsCached = null;
      parent.addListener(this);
    }
    else
    {
      throw new IllegalArgumentException("Cannot add parent as child.");
    }
  }

  /**
   * Adds a parent style-sheet. Parents on a lower position are queried before any
   * parent with an higher position in the list.
   * <p>
   * The default parents operations are reserved for the system internal stylesheet
   * operations. If you want to add own stylesheets, use the addParent methods.
   *
   * @param position the position where to insert the parent style sheet
   * @param parent  the parent (<code>null</code> not permitted).
   *
   * @throws IndexOutOfBoundsException if the position is invalid (pos &lt; 0 or pos &gt;=
   *         numberOfParents)
   */
  public synchronized void addDefaultParent (int position, ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.addParent(...): parent is null.");
    }
    if (parent.isSubStyleSheet(this) == false)
    {
      defaultSheets.add (position, parent);
      defaultCached = null;
      parent.addListener(this);
    }
    else
    {
      throw new IllegalArgumentException("Cannot add parent as child.");
    }
  }

  /**
   * Checks, whether the given element stylesheet is already added as child into
   * the stylesheet tree.
   *
   * @param parent the element that should be tested.
   * @return true, if the element is a child of this element style sheet,
   * false otherwise.
   */
  public boolean isSubStyleSheet (ElementStyleSheet parent)
  {
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      if (es == parent)
      {
        return true;
      }

      if (es.isSubStyleSheet(parent) == true)
      {
        return true;
      }
    }
    for (int i = 0; i < defaultSheets.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) defaultSheets.get(i);
      if (es == parent)
      {
        return true;
      }

      if (es.isSubStyleSheet(parent) == true)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Removes a parent style-sheet.
   *
   * @param parent  the style-sheet to remove (<code>null</code> not permitted).
   */
  public synchronized void removeParent(ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.removeParent(...): parent is null.");
    }
    parents.remove (parent);
    parent.removeListener(this);
    parentsCached = null;
  }

  /**
   * Removes a parent style-sheet.
   *
   * @param parent  the style-sheet to remove (<code>null</code> not permitted).
   */
  public synchronized void removeDefaultParent(ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.removeParent(...): parent is null.");
    }
    defaultSheets.remove (parent);
    parent.removeListener(this);
    defaultCached = null;
  }

  /**
   * Returns a list of the parent style-sheets.
   * <p>
   * The list is unmodifiable.
   *
   * @return the list.
   */
  public List getParents ()
  {
    return Collections.unmodifiableList(parents);
  }

  /**
   * Returns a list of the default style-sheets.
   * <p>
   * The list is unmodifiable.
   *
   * @return the list.
   */
  public List getDefaultParents ()
  {
    return Collections.unmodifiableList(defaultSheets);
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
   * @param defaultValue  the default value (<code>null</code> permitted).
   *
   * @return the value.
   */
  public Object getStyleProperty(StyleKey key, Object defaultValue)
  {
    Object value = properties.get (key);
    if (value != null)
    {
      return value;
    }

    if (styleCache != null)
    {
      value = styleCache.get(key);
      if (value != null)
      {
        return value;
      }
    }

    if (parentsCached == null)
    {
      parentsCached = (ElementStyleSheet[])
          parents.toArray(new ElementStyleSheet[parents.size()]);
    }

    for (int i = 0; i < parentsCached.length; i++)
    {
      ElementStyleSheet st = parentsCached[i];
      value = st.getStyleProperty(key, null);
      if (value == null)
      {
        continue;
      }
      if (isAllowCaching())
      {
        if (styleCache == null)
        {
          styleCache = new HashMap();
        }
        styleCache.put(key, value);
      }
      return value;
    }

    if (defaultCached == null)
    {
      defaultCached = (ElementStyleSheet[])
          defaultSheets.toArray(new ElementStyleSheet[defaultSheets.size()]);
    }

    for (int i = 0; i < defaultCached.length; i++)
    {
      ElementStyleSheet st = defaultCached[i];
      value = st.getStyleProperty(key, null);
      if (value == null)
      {
        continue;
      }
      if (isAllowCaching())
      {
        if (styleCache == null)
        {
          styleCache = new HashMap();
        }
        styleCache.put(key, value);
      }
      return value;
    }
    return defaultValue;
  }

  /**
   * Sets a boolean style property.
   *
   * @param key  the style key (<code>null</code> not permitted).
   * @param value  the value.
   * @throws NullPointerException if the given key is null.
   * @throws ClassCastException if the value cannot be assigned with the given key.
   */
  public void setBooleanStyleProperty (StyleKey key, boolean value)
  {
    if (value)
    {
      setStyleProperty(key, Boolean.TRUE);
    }
    else
    {
      setStyleProperty(key, Boolean.FALSE);
    }
  }

  /**
   * Sets a style property (or removes the style if the value is <code>null</code>).
   *
   * @param key  the style key (<code>null</code> not permitted).
   * @param value  the value.
   * @throws NullPointerException if the given key is null.
   * @throws ClassCastException if the value cannot be assigned with the given key.
   */
  public void setStyleProperty (StyleKey key, Object value)
  {
    if (key == null)
    {
      throw new NullPointerException("ElementStyleSheet.setStyleProperty: key is null.");
    }
    if (value == null)
    {
      if (styleCache != null)
      {
        styleCache.remove(key);
      }
      properties.remove (key);
      styleChangeSupport.fireStyleRemoved(key);
    }
    else
    {
      if (key.getValueType().isAssignableFrom(value.getClass()) == false)
      {
        throw new ClassCastException ("Value for key " + key.getName() 
                                      + " is not assignable: " + value.getClass()
                                      + " is not assignable from " + key.getValueType());
      }
      if (styleCache != null)
      {
        styleCache.put(key, value);
      }
      properties.put (key, value);
      styleChangeSupport.fireStyleChanged(key, value);
    }
  }

  /**
   * Clones the style-sheet. The assigned parent style sheets are not cloned.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    ElementStyleSheet sc = (ElementStyleSheet) super.clone();
    sc.parents = (ArrayList) parents.clone();
    sc.defaultSheets = (ArrayList) defaultSheets.clone();
    sc.properties = (HashMap) properties.clone();
    if (styleCache != null)
    {
      sc.styleCache = new HashMap (styleCache);
    }
    sc.styleChangeSupport = new StyleChangeSupport(sc);
    return sc;
  }

  /**
   * Returns a boolean style (defaults to false if the style is not found).
   *
   * @param key  the style key.
   *
   * @return <code>true</code> or <code>false</code>.
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
    Boolean b = (Boolean) getStyleProperty(key, null);
    if (b == null)
    {
      return defaultValue;
    }
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
   * @deprecated use getFontDefinition()
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
   * @deprecated use setFontDefinition()
   * @param font  the font (<code>null</code> not permitted).
   */
  public void setFontStyleProperty (Font font)
  {
    if (font == null)
    {
      throw new NullPointerException("ElementStyleSheet.setFontStyleProperty: font is null.");
    }
    setStyleProperty(FONT, font.getName());
    setBooleanStyleProperty(BOLD, font.isBold());
    setBooleanStyleProperty(ITALIC, font.isItalic());
    setStyleProperty(FONTSIZE, new Integer(font.getSize()));
  }

  /**
   * Returns the font for this style-sheet.
   *
   * @return the font.
   */
  public FontDefinition getFontDefinitionProperty ()
  {
    String name = (String) getStyleProperty(FONT);
    int size = getIntStyleProperty(FONTSIZE, -1);
    boolean bold = getBooleanStyleProperty(BOLD);
    boolean italic = getBooleanStyleProperty(ITALIC);
    boolean underlined = getBooleanStyleProperty(UNDERLINED);
    boolean strike = getBooleanStyleProperty(STRIKETHROUGH);
    boolean embed = getBooleanStyleProperty(EMBEDDED_FONT);
    String encoding = (String) getStyleProperty(FONTENCODING);

    FontDefinition retval = new FontDefinition(name, size, bold, italic, underlined, strike, 
                                               encoding, embed);
    return retval;
  }

  /**
   * Sets the font for this style-sheet.
   *
   * @param font  the font (<code>null</code> not permitted).
   */
  public void setFontDefinitionProperty (FontDefinition font)
  {
    if (font == null)
    {
      throw new NullPointerException("ElementStyleSheet.setFontStyleProperty: font is null.");
    }
    setStyleProperty(FONT, font.getFontName());
    setStyleProperty(FONTSIZE, new Integer(font.getFontSize()));
    setBooleanStyleProperty(BOLD, font.isBold());
    setBooleanStyleProperty(ITALIC, font.isItalic());
    setBooleanStyleProperty(UNDERLINED, font.isUnderline());
    setBooleanStyleProperty(STRIKETHROUGH, font.isStrikeThrough());
    setBooleanStyleProperty(EMBEDDED_FONT, font.isEmbeddedFont());
    setStyleProperty(FONTENCODING, font.getFontEncoding(null));
  }

  /**
   * Returns an enumeration of all local property keys.
   *
   * @return an enumeration of all localy defined style property keys.
   */
  public Iterator getDefinedPropertyNames ()
  {
    return properties.keySet().iterator();
  }

  /**
   * Adds a {@link StyleChangeListener}.
   * 
   * @param l  the listener.
   */
  public void addListener (StyleChangeListener l)
  {
    styleChangeSupport.addListener(l);
  }

  /**
   * Removes a {@link StyleChangeListener}.
   * 
   * @param l  the listener.
   */
  public void removeListener (StyleChangeListener l)
  {
    styleChangeSupport.removeListener(l);
  }

  /**
   * Sends a change event notification to all registered {@link StyleChangeListener} objects.
   * 
   * @param source  the source of the change.
   * @param key  the style key.
   * @param value  the new value.
   */
  public void styleChanged(ElementStyleSheet source, StyleKey key, Object value)
  {
    if (styleCache != null)
    {
      styleCache.remove(key);
    }
    styleChangeSupport.fireStyleChanged(key, value);
  }

  /**
   * Sends a change event notification to all registered {@link StyleChangeListener} objects.
   * 
   * @param source  the source of the change.
   * @param key  the style key.
   */
  public void styleRemoved(ElementStyleSheet source, StyleKey key)
  {
    if (styleCache != null)
    {
      styleCache.remove(key);
    }
    styleChangeSupport.fireStyleRemoved(key);
  }

  /**
   * Helper method for serialization.
   *
   * @param out the output stream where to write the object.
   * @throws IOException if errors occur while writing the stream.
   */
  private void writeObject(ObjectOutputStream out)
       throws IOException
  {
    out.defaultWriteObject();
    int size = properties.size();
    out.writeInt(size);
    Iterator it = properties.keySet().iterator();
    while (it.hasNext())
    {
      Object key = it.next();
      out.writeObject(key);
      Object value = properties.get(key);
      SerializerHelper.getInstance().writeObject(value, out);
    }
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object
   * could not be found.
   */
  private void readObject(ObjectInputStream in)
       throws IOException, ClassNotFoundException
  {
    styleChangeSupport = new StyleChangeSupport(this);

    in.defaultReadObject();
    int size = in.readInt();
    properties = new HashMap(size);
    for (int i = 0; i < size; i++)
    {
      Object key = in.readObject();
      Object value = SerializerHelper.getInstance().readObject(in);
      properties.put (key, value);
    }
  }

  /**
   * Tests, whether this ElementStyleSheet is equal to another stylesheet.
   * ElementStyleSheets are equal, if both stylesheets have the same name.
   * The contents or parents are not checked.
   *
   * @param o the other object to check.
   * @return true, if both objects are equal, false otherwise.
   */
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ElementStyleSheet)) return false;

    final ElementStyleSheet elementStyleSheet = (ElementStyleSheet) o;

    if (!name.equals(elementStyleSheet.name)) return false;

    return true;
  }

  /**
   * Returns a hash code value for the object. This method is
   * supported for the benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   *
   * @return the hashCode for this style sheet.
   */
  public int hashCode()
  {
    return name.hashCode();
  }
}
