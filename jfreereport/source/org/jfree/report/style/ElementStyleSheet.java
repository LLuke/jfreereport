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
 * ----------------------
 * ElementStyleSheet.java
 * ----------------------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ElementStyleSheet.java,v 1.20 2005/02/19 13:30:05 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 12-Dec-2002 : First BugFix: setFontStyle must use font.getName instead of font.getFontName
 *               or a totally different font family is used.
 * 03-Jan-2003 : Javadoc updates (DG);
 * 15-Jun-2003 : Cloning did not register the clone as change listener with the parents
 */

package org.jfree.report.style;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.jfree.report.ElementAlignment;
import org.jfree.report.util.InstanceID;
import org.jfree.report.util.SerializerHelper;
import org.jfree.report.util.geom.StrictBounds;

/**
 * An element style-sheet contains zero, one or many attributes that affect the appearance
 * of report elements.  For each attribute, there is a predefined key that can be used to
 * access that attribute in the style sheet.
 * <p/>
 * Every report element has an associated style-sheet.
 * <p/>
 * A style-sheet maintains a list of parent style-sheets.  If an attribute is not defined
 * in a style-sheet, the code refers to the parent style-sheets to see if the attribute is
 * defined there.
 * <p/>
 * All StyleSheet entries are checked against the StyleKeyDefinition for validity.
 * <p/>
 * As usual, this implementation is not synchronized, we need the performance during the
 * reporting.
 *
 * @author Thomas Morgner
 */
public abstract class ElementStyleSheet implements Serializable, StyleChangeListener,
                                                   Cloneable
{
  /**
   * A marker to indicate that none of the parent stylesheets defines this value.
   */
  private static class UndefinedValue
  {
    /**
     * DefaultConstructor.
     */
    public UndefinedValue ()
    {
    }
  }

  /**
   * A singleton marker for the cache.
   */
  private static final UndefinedValue UNDEFINED_VALUE = new UndefinedValue();

  /**
   * A key for the 'minimum size' of an element.
   */
  public static final StyleKey MINIMUMSIZE =
          StyleKey.getStyleKey("min-size", Dimension2D.class, false, false);

  /**
   * A key for the 'maximum size' of an element.
   */
  public static final StyleKey MAXIMUMSIZE =
          StyleKey.getStyleKey("max-size", Dimension2D.class, false, false);

  /**
   * A key for the 'preferred size' of an element.
   */
  public static final StyleKey PREFERREDSIZE =
          StyleKey.getStyleKey("preferred-size", Dimension2D.class, false, false);

  /**
   * A key for the 'bounds' of an element.
   */
  public static final StyleKey BOUNDS =
          StyleKey.getStyleKey("bounds", StrictBounds.class, true, false);

  /**
   * A key for an element's 'visible' flag.
   */
  public static final StyleKey VISIBLE =
          StyleKey.getStyleKey("visible", Boolean.class);

  /**
   * A key for the 'paint' used to color an element. For historical reasons, this key
   * requires a color value.
   */
  public static final StyleKey PAINT = StyleKey.getStyleKey("paint", Color.class);

  /**
   * A key for the 'ext-paint' used to fill or draw an element. If the specified paint is
   * not supported by the output target, the color given with the 'paint' key is used
   * instead.
   */
  public static final StyleKey EXTPAINT = StyleKey.getStyleKey("ext-paint", Paint.class);

  /**
   * A key for the 'stroke' used to draw an element.
   */
  public static final StyleKey STROKE = StyleKey.getStyleKey("stroke", Stroke.class);

  /**
   * A key for the 'font name' used to draw element text.
   */
  public static final StyleKey FONT = StyleKey.getStyleKey("font", String.class);

  /**
   * A key for the 'font size' used to draw element text.
   */
  public static final StyleKey FONTSIZE = StyleKey.getStyleKey("font-size", Integer.class);

  /**
   * A key for the 'font size' used to draw element text.
   */
  public static final StyleKey LINEHEIGHT = StyleKey.getStyleKey("line-height", Float.class);

  /**
   * A key for an element's 'bold' flag.
   */
  public static final StyleKey BOLD = StyleKey.getStyleKey("font-bold", Boolean.class);

  /**
   * A key for an element's 'italic' flag.
   */
  public static final StyleKey ITALIC = StyleKey.getStyleKey("font-italic", Boolean.class);

  /**
   * A key for an element's 'underlined' flag.
   */
  public static final StyleKey UNDERLINED = StyleKey.getStyleKey("font-underline",
          Boolean.class);

  /**
   * A key for an element's 'strikethrough' flag.
   */
  public static final StyleKey STRIKETHROUGH = StyleKey.getStyleKey("font-strikethrough",
          Boolean.class);

  /**
   * A key for an element's 'embedd' flag.
   */
  public static final StyleKey EMBEDDED_FONT = StyleKey.getStyleKey("font-embedded", Boolean.class);

  /**
   * A key for an element's 'embedd' flag.
   */
  public static final StyleKey FONTENCODING = StyleKey.getStyleKey("font-encoding", String.class);

  /**
   * A key for the horizontal alignment of an element.
   */
  public static final StyleKey ALIGNMENT = StyleKey.getStyleKey("alignment",
          ElementAlignment.class);

  /**
   * A key for the vertical alignment of an element.
   */
  public static final StyleKey VALIGNMENT = StyleKey.getStyleKey("valignment",
          ElementAlignment.class);

  /**
   * A key for an element's 'scale' flag.
   */
  public static final StyleKey SCALE = StyleKey.getStyleKey("scale", Boolean.class);

  /**
   * A key for an element's 'keep aspect ratio' flag.
   */
  public static final StyleKey KEEP_ASPECT_RATIO = StyleKey.getStyleKey("keepAspectRatio",
          Boolean.class);

  /**
   * A key for the dynamic height flag for an element.
   */
  public static final StyleKey DYNAMIC_HEIGHT = StyleKey.getStyleKey("dynamic_height",
          Boolean.class);

  /**
   * The Layout Cacheable stylekey. Set this stylekey to false, to define that the element
   * is not cachable. This key defaults to true.
   */
  public static final StyleKey ELEMENT_LAYOUT_CACHEABLE = StyleKey.getStyleKey("layout-cacheable",
          Boolean.class);

  /**
   * The string that is used to end a text if not all text fits into the element.
   */
  public static final StyleKey RESERVED_LITERAL = StyleKey.getStyleKey
          ("reserved-literal", String.class);

  /**
   * The Layout Cacheable stylekey. Set this stylekey to false, to define that the element
   * is not cachable. This key defaults to true.
   */
  public static final StyleKey TRIM_TEXT_CONTENT = StyleKey.getStyleKey("trim-text-content",
          Boolean.class);

  public static final StyleKey HREF_TARGET = StyleKey.getStyleKey("href-target",
          String.class);

  public static final StyleKey HREF_INHERITED = StyleKey.getStyleKey("href-inherited",
          Boolean.class, true, false);

  /**
   * The StyleKey for the user defined cell data format.
   */
  public static final StyleKey EXCEL_WRAP_TEXT =
          StyleKey.getStyleKey("Excel.WrapText", Boolean.class);

  /**
   * The StyleKey for the user defined cell data format.
   */
  public static final StyleKey EXCEL_DATA_FORMAT_STRING =
          StyleKey.getStyleKey("Excel.CellDataFormat", String.class);


  /**
   * The instance id of this ElementStyleSheet. This id is shared among all clones.
   */
  private InstanceID id;

  /**
   * The style-sheet name.
   */
  private String name;

  /**
   * Storage for the parent style sheets (if any).
   */
  private ArrayList parents;

  /**
   * Storage for readonly style sheets.
   */
  private ElementDefaultStyleSheet defaultStyleSheet;
  private ElementStyleSheet cascadeStyleSheet;

  /**
   * Parent style sheet cache.
   */
  private transient StyleSheetCarrier[] parentsCached;

  private transient StyleKey[] propertyKeys;
  private transient Object[] properties;
  private transient Object[] cachedProperties;

  /**
   * Style change support.
   */
  private transient StyleChangeSupport styleChangeSupport;

  /**
   * A flag that controls whether or not caching is allowed.
   */
  private boolean allowCaching;
  /**
   * The cached font definition instance from this stylessheet.
   */
  private transient FontDefinition fontDefinition;

  /**
   * Creates a new element style-sheet with the given name.  The style-sheet initially
   * contains no attributes, and has no parent style-sheets.
   *
   * @param name the name (<code>null</code> not permitted).
   */
  protected ElementStyleSheet (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("ElementStyleSheet constructor: name is null.");
    }
    this.id = new InstanceID();
    this.name = name;
    this.parents = new ArrayList(5);
    this.styleChangeSupport = new StyleChangeSupport(this);
  }

  /**
   * Returns <code>true</code> if caching is allowed, and <code>false</code> otherwise.
   *
   * @return A boolean.
   */
  public final boolean isAllowCaching ()
  {
    return allowCaching;
  }

  /**
   * Returns true, if the given key is locally defined, false otherwise.
   *
   * @param key the key to test
   * @return true, if the key is local, false otherwise.
   */
  public boolean isLocalKey (final StyleKey key)
  {
    if (properties == null)
    {
      return false;
    }
    final int identifier = key.getIdentifier();
    if (properties.length <= identifier)
    {
      return false;
    }
    return properties[identifier] != null;
  }

  /**
   * Sets the flag that controls whether or not caching is allowed.
   *
   * @param allowCaching the flag value.
   */
  public void setAllowCaching (final boolean allowCaching)
  {
    this.allowCaching = allowCaching;
    if (this.allowCaching == false)
    {
      this.cachedProperties = null;
    }
  }

  /**
   * Returns the name of the style-sheet.
   *
   * @return the name (never <code>null</code>).
   */
  public String getName ()
  {
    return name;
  }

  /**
   * Adds a parent style-sheet. This method adds the parent to the beginning of the list,
   * and guarantees, that this parent is queried first.
   *
   * @param parent the parent (<code>null</code> not permitted).
   */
  public void addParent (final ElementStyleSheet parent)
  {
    addParent(0, parent);
  }

  /**
   * Adds a parent style-sheet. Parents on a lower position are queried before any parent
   * with an higher position in the list.
   *
   * @param position the position where to insert the parent style sheet
   * @param parent   the parent (<code>null</code> not permitted).
   * @throws IndexOutOfBoundsException if the position is invalid (pos &lt; 0 or pos &gt;=
   *                                   numberOfParents)
   */
  public void addParent (final int position, final ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.addParent(...): parent is null.");
    }
    if (parent.isSubStyleSheet(this) == false)
    {
      final StyleSheetCarrier carrier = createCarrier(parent);
      if (carrier == null)
      {
        throw new IllegalArgumentException
                ("The given StyleSheet cannot be added to this stylesheet.");
      }
      parents.add(position, carrier);
      parentsCached = null;
    }
    else
    {
      throw new IllegalArgumentException("Cannot add parent as child.");
    }
  }

  protected abstract StyleSheetCarrier createCarrier (ElementStyleSheet styleSheet);


  /**
   * Checks, whether the given element stylesheet is already added as child into the
   * stylesheet tree.
   *
   * @param parent the element that should be tested.
   * @return true, if the element is a child of this element style sheet, false
   *         otherwise.
   */
  protected boolean isSubStyleSheet (final ElementStyleSheet parent)
  {
    for (int i = 0; i < parents.size(); i++)
    {
      final StyleSheetCarrier ca = (StyleSheetCarrier) parents.get(i);
      final ElementStyleSheet es = ca.getStyleSheet();
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
   * @param parent the style-sheet to remove (<code>null</code> not permitted).
   */
  public void removeParent (final ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.removeParent(...): parent is null.");
    }
    final Iterator it = parents.iterator();
    while (it.hasNext())
    {
      final StyleSheetCarrier carrier = (StyleSheetCarrier) it.next();
      if (carrier.isSame(parent))
      {
        it.remove();
        carrier.invalidate();
      }
    }
    parentsCached = null;
  }

  /**
   * Returns a list of the parent style-sheets.
   * <p/>
   * The list is unmodifiable.
   *
   * @return the list.
   */
  public ElementStyleSheet[] getParents ()
  {
    if (parentsCached == null)
    {
      this.parentsToCache();
    }
    final ElementStyleSheet[] styleSheets =
            new ElementStyleSheet[parentsCached.length];
    for (int i = 0; i < styleSheets.length; i++)
    {
      styleSheets[i] = parentsCached[i].getStyleSheet();
    }
    return styleSheets;
  }

  /**
   * Returns the global default (if defined).
   *
   * @return the list.
   */
  public ElementDefaultStyleSheet getGlobalDefaultStyleSheet ()
  {
    return defaultStyleSheet;
  }

  public void setDefaultStyleSheet (final ElementDefaultStyleSheet defaultStyleSheet)
  {
    this.defaultStyleSheet = defaultStyleSheet;
  }

  public ElementStyleSheet getCascadeStyleSheet ()
  {
    return cascadeStyleSheet;
  }

  public void setCascadeStyleSheet (final ElementStyleSheet cascadeStyleSheet)
  {
    if (this.cascadeStyleSheet != null)
    {
      this.cascadeStyleSheet.removeListener(this);
    }
    this.cascadeStyleSheet = cascadeStyleSheet;
    if (this.cascadeStyleSheet != null)
    {
      this.cascadeStyleSheet.addListener(this);
    }
  }

  /**
   * Returns the value of a style.  If the style is not found in this style-sheet, the
   * code looks in the parent style-sheets.  If the style is not found in any of the
   * parent style-sheets, then <code>null</code> is returned.
   *
   * @param key the style key.
   * @return the value.
   */
  public Object getStyleProperty (final StyleKey key)
  {
    return getStyleProperty(key, null);
  }

  /**
   * Returns the value of a style.  If the style is not found in this style-sheet, the
   * code looks in the parent style-sheets.  If the style is not found in any of the
   * parent style-sheets, then the default value (possibly <code>null</code>) is
   * returned.
   *
   * @param key          the style key.
   * @param defaultValue the default value (<code>null</code> permitted).
   * @return the value.
   */
  public Object getStyleProperty (final StyleKey key, final Object defaultValue)
  {
    final int identifier = key.getIdentifier();
    if (properties != null)
    {
      if (properties.length > identifier)
      {
        final Object value = properties[identifier];
        if (value != null)
        {
          return value;
        }
      }
    }

    if (cachedProperties != null)
    {
      if (cachedProperties.length > identifier)
      {
        final Object value = cachedProperties[identifier];
        if (value != null)
        {
          if (value == UNDEFINED_VALUE)
          {
            return defaultValue;
          }
          return value;
        }
      }
    }

    parentsToCache();
    for (int i = 0; i < parentsCached.length; i++)
    {
      final ElementStyleSheet st = parentsCached[i].getStyleSheet();
      final Object value = st.getStyleProperty(key, null);
      if (value == null)
      {
        continue;
      }
      putInCache(key, value);
      return value;
    }

    if (cascadeStyleSheet != null && key.isInheritable())
    {
      final Object value = cascadeStyleSheet.getStyleProperty(key, null);
      if (value != null)
      {
        putInCache(key, value);
        return value;
      }
    }

    if (defaultStyleSheet != null)
    {
      final Object value = defaultStyleSheet.getStyleProperty(key, null);
      if (value != null)
      {
        putInCache(key, value);
        return value;
      }
    }

    putInCache(key, UNDEFINED_VALUE);
    return defaultValue;
  }

  /**
   * Puts an object into the cache (if caching is enabled).
   *
   * @param key   the stylekey for that object
   * @param value the object.
   */
  private void putInCache (final StyleKey key, final Object value)
  {
    if (allowCaching)
    {
      final int identifier = key.getIdentifier();
      if (cachedProperties != null)
      {
        if (cachedProperties.length <= identifier)
        {
          final Object[] newCache = new Object[StyleKey.getDefinedStyleKeyCount()];
          System.arraycopy(cachedProperties, 0, newCache, 0, cachedProperties.length);
          cachedProperties = newCache;
        }
      }
      else
      {
        cachedProperties = new Object[StyleKey.getDefinedStyleKeyCount()];
      }
      cachedProperties[identifier] = value;
    }
  }

  /**
   * Sets a boolean style property.
   *
   * @param key   the style key (<code>null</code> not permitted).
   * @param value the value.
   * @throws NullPointerException if the given key is null.
   * @throws ClassCastException   if the value cannot be assigned with the given key.
   */
  public void setBooleanStyleProperty (final StyleKey key, final boolean value)
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
   * @param key   the style key (<code>null</code> not permitted).
   * @param value the value.
   * @throws NullPointerException if the given key is null.
   * @throws ClassCastException   if the value cannot be assigned with the given key.
   */
  public void setStyleProperty (final StyleKey key, final Object value)
  {
    if (key == null)
    {
      throw new NullPointerException("ElementStyleSheet.setStyleProperty: key is null.");
    }
    if (isFontDefinitionProperty(key))
    {
      fontDefinition = null;
    }
    final int identifier = key.getIdentifier();
    if (value == null)
    {
      if (properties != null)
      {
        if (properties.length > identifier)
        {
          properties[identifier] = null;
        }
      }

      if (propertyKeys != null)
      {
        if (propertyKeys.length > identifier)
        {
          propertyKeys[identifier] = null;
        }
      }
      styleChangeSupport.fireStyleRemoved(key);
    }
    else
    {
      if (key.getValueType().isAssignableFrom(value.getClass()) == false)
      {
        throw new ClassCastException("Value for key " + key.getName()
                + " is not assignable: " + value.getClass()
                + " is not assignable from " + key.getValueType());
      }
      if (properties != null)
      {
        if (properties.length <= identifier)
        {
          final Object[] newProps = new Object[StyleKey.getDefinedStyleKeyCount()];
          System.arraycopy(properties, 0, newProps, 0, properties.length);
          properties = newProps;
        }
      }
      else
      {
        properties = new Object[StyleKey.getDefinedStyleKeyCount()];
      }

      if (propertyKeys != null)
      {
        if (propertyKeys.length <= identifier)
        {
          final StyleKey[] newProps = new StyleKey[StyleKey.getDefinedStyleKeyCount()];
          System.arraycopy(propertyKeys, 0, newProps, 0, propertyKeys.length);
          propertyKeys = newProps;
        }
      }
      else
      {
        propertyKeys = new StyleKey[StyleKey.getDefinedStyleKeyCount()];
      }

      properties[identifier] = value;
      propertyKeys[identifier] = key;
      styleChangeSupport.fireStyleChanged(key, value);
    }
  }


  /**
   * Creates and returns a copy of this object. After the cloning, the new StyleSheet is
   * no longer registered with its parents.
   *
   * @return a clone of this instance.
   *
   * @see Cloneable
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    try
    {
      final ElementStyleSheet sc = (ElementStyleSheet) super.clone();
      if (properties != null)
      {
        sc.properties = (Object[]) properties.clone();
      }
      if (propertyKeys != null)
      {
        sc.propertyKeys = (StyleKey[]) propertyKeys.clone();
      }
      sc.styleChangeSupport = new StyleChangeSupport(sc);
      if (cachedProperties != null)
      {
        sc.cachedProperties = (Object[]) cachedProperties.clone();
      }
      parentsToCache();
      sc.parents = new ArrayList(parentsCached.length);// parents.clone();
      sc.parentsCached = new StyleSheetCarrier[parentsCached.length];
      for (int i = 0; i < parentsCached.length; i++)
      {
        final StyleSheetCarrier carrier = (StyleSheetCarrier) parentsCached[i].clone();
        sc.parentsCached[i] = carrier;
        sc.parents.add(carrier);
      }
      sc.cascadeStyleSheet = null;
      sc.defaultStyleSheet = defaultStyleSheet;
      return sc;
    }
    catch (CloneNotSupportedException cne)
    {
      throw new IllegalStateException("Clone failed.");
    }
  }

  protected StyleSheetCarrier[] getParentReferences ()
  {
    parentsToCache();
    return parentsCached;
  }

  /**
   * Clones the style-sheet. The assigned parent style sheets are not cloned. The
   * stylesheets are not assigned to the contained stylesheet collection, you have to
   * reassign them manually ...
   *
   * @return the clone.
   */
  public ElementStyleSheet getCopy ()
          throws CloneNotSupportedException
  {
    return (ElementStyleSheet) clone();
  }

  /**
   * Creates the cached object array for the parent element style sheets.
   */
  private void parentsToCache ()
  {
    if (parentsCached == null)
    {
      parentsCached = (StyleSheetCarrier[])
              parents.toArray(new StyleSheetCarrier[parents.size()]);
    }
  }

  /**
   * Returns a boolean style (defaults to false if the style is not found).
   *
   * @param key the style key.
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean getBooleanStyleProperty (final StyleKey key)
  {
    return getBooleanStyleProperty(key, false);
  }

  /**
   * Returns a boolean style.
   *
   * @param key          the style key.
   * @param defaultValue the default value.
   * @return true or false.
   */
  public boolean getBooleanStyleProperty (final StyleKey key, final boolean defaultValue)
  {
    final Boolean b = (Boolean) getStyleProperty(key, null);
    if (b == null)
    {
      return defaultValue;
    }
    return b.booleanValue();
  }

  /**
   * Returns an integer style.
   *
   * @param key the style key.
   * @param def the default value.
   * @return the style value.
   */
  public int getIntStyleProperty (final StyleKey key, final int def)
  {
    final Integer i = (Integer) getStyleProperty(key, new Integer(def));
    return i.intValue();
  }

  /**
   * Checks, whether the given key is one of the keys used to define the font definition
   * from this stylesheet.
   *
   * @param key the key that should be checked.
   * @return true, if the key is a font definition key, false otherwise.
   */
  private boolean isFontDefinitionProperty (final StyleKey key)
  {
    if (key == FONT)
    {
      return true;
    }
    if (key == FONTSIZE)
    {
      return true;
    }
    if (key == BOLD)
    {
      return true;
    }
    if (key == ITALIC)
    {
      return true;
    }
    if (key == UNDERLINED)
    {
      return true;
    }
    if (key == STRIKETHROUGH)
    {
      return true;
    }
    if (key == EMBEDDED_FONT)
    {
      return true;
    }
    if (key == FONTENCODING)
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the font for this style-sheet.
   *
   * @return the font.
   */
  public FontDefinition getFontDefinitionProperty ()
  {
    if (fontDefinition == null)
    {
      final String name = (String) getStyleProperty(FONT);
      final int size = getIntStyleProperty(FONTSIZE, -1);
      final boolean bold = getBooleanStyleProperty(BOLD);
      final boolean italic = getBooleanStyleProperty(ITALIC);
      final boolean underlined = getBooleanStyleProperty(UNDERLINED);
      final boolean strike = getBooleanStyleProperty(STRIKETHROUGH);
      final boolean embed = getBooleanStyleProperty(EMBEDDED_FONT);
      final String encoding = (String) getStyleProperty(FONTENCODING);

      final FontDefinition retval = new FontDefinition(name, size, bold, italic, underlined, strike,
              encoding, embed);
      if (allowCaching)
      {
        fontDefinition = retval;
      }
      else
      {
        return retval;
      }
    }
    return fontDefinition;
  }

  /**
   * Sets the font for this style-sheet.
   *
   * @param font the font (<code>null</code> not permitted).
   */
  public void setFontDefinitionProperty (final FontDefinition font)
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
    final ArrayList al = new ArrayList();
    if (propertyKeys != null)
    {
      for (int i = 0; i < propertyKeys.length; i++)
      {
        if (propertyKeys[i] != null)
        {
          al.add(propertyKeys[i]);
        }
      }
    }
    return Collections.unmodifiableList(al).iterator();
  }

  /**
   * Adds a {@link StyleChangeListener}.
   *
   * @param l the listener.
   */
  protected void addListener (final StyleChangeListener l)
  {
    styleChangeSupport.addListener(l);
  }

  /**
   * Removes a {@link StyleChangeListener}.
   *
   * @param l the listener.
   */
  protected void removeListener (final StyleChangeListener l)
  {
    styleChangeSupport.removeListener(l);
  }

  /**
   * Forwards a change event notification to all registered {@link StyleChangeListener}
   * objects.
   *
   * @param source the source of the change.
   * @param key    the style key.
   * @param value  the new value.
   */
  public void styleChanged (final ElementStyleSheet source, final StyleKey key,
                            final Object value)
  {
    if (cachedProperties != null)
    {
      final int identifier = key.getIdentifier();
      if (cachedProperties.length > identifier)
      {
        cachedProperties[identifier] = value;
      }
      if (isFontDefinitionProperty(key))
      {
        fontDefinition = null;
      }
    }
    styleChangeSupport.fireStyleChanged(key, value);
  }

  /**
   * Forwards a change event notification to all registered {@link StyleChangeListener}
   * objects.
   *
   * @param source the source of the change.
   * @param key    the style key.
   */
  public void styleRemoved (final ElementStyleSheet source, final StyleKey key)
  {
    if (cachedProperties != null)
    {
      final int identifier = key.getIdentifier();
      if (cachedProperties.length > identifier)
      {
        cachedProperties[identifier] = null;
      }
      if (isFontDefinitionProperty(key))
      {
        fontDefinition = null;
      }
    }
    styleChangeSupport.fireStyleRemoved(key);
  }

  /**
   * Helper method for serialization.
   *
   * @param out the output stream where to write the object.
   * @throws IOException if errors occur while writing the stream.
   */
  private void writeObject (final ObjectOutputStream out)
          throws IOException
  {
    out.defaultWriteObject();

    out.writeObject(propertyKeys);
    if (properties == null)
    {
      out.writeInt(0);
    }
    else
    {
      final int size = properties.length;
      out.writeInt(size);
      for (int i = 0; i < size; i++)
      {
        final Object value = properties[i];
        SerializerHelper.getInstance().writeObject(value, out);
      }
    }
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException            when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object could
   *                                not be found.
   */
  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    styleChangeSupport = new StyleChangeSupport(this);

    in.defaultReadObject();
    final StyleKey[] keys = (StyleKey[]) in.readObject();
    final int size = in.readInt();
    final Object[] values = new Object[size];
    for (int i = 0; i < size; i++)
    {
      final Object value = SerializerHelper.getInstance().readObject(in);
      values[i] = value;
    }
    if (keys == null)
    {
      return;
    }
    properties = new Object[StyleKey.getDefinedStyleKeyCount()];
    propertyKeys = new StyleKey[StyleKey.getDefinedStyleKeyCount()];
    final int maxLen = Math.min
            (Math.min(properties.length, keys.length),
                    Math.min(propertyKeys.length, values.length));
    for (int i = 0; i < maxLen; i++)
    {
      final StyleKey key = keys[i];
      if (key != null)
      {
        final int identifier = key.getIdentifier();
        final Object value = values[i];
        properties[identifier] = value;
        propertyKeys[identifier] = key;
      }
    }
  }

  /**
   * Returns the ID of the stylesheet. The ID does identify an element stylesheet an all
   * all cloned instances of that stylesheet.
   *
   * @return the ID of this stylesheet.
   */
  public InstanceID getId ()
  {
    return id;
  }

  /**
   * Returns true, if this stylesheet is one of the global default stylesheets. Global
   * default stylesheets are unmodifiable and shared among all element stylesheets.
   *
   * @return true, if this is one of the unmodifiable global default stylesheets, false
   *         otherwise.
   */
  public boolean isGlobalDefault ()
  {
    return false;
  }
}
