/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultLayoutContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultLayoutContext.java,v 1.2 2006/11/20 21:01:53 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.context;

import java.util.Locale;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.LayoutStyleImpl;
import org.jfree.layouting.util.AttributeMap;

/**
 * Creation-Date: 14.12.2005, 13:42:06
 *
 * @author Thomas Morgner
 */
public class DefaultLayoutContext implements LayoutContext, Cloneable
{
  private BackgroundSpecification backgroundSpecification;
  private FontSpecification fontSpecification;
  private ContentSpecification contentSpecification;
  private ListSpecification listSpecification;
  private LayoutStyleImpl style;
  private ContextId contextId;
  private String namespace;
  private String tagName;
  private AttributeMap attributeMap;
  private String pseudoElement;
  private boolean derived;

  public DefaultLayoutContext(final ContextId contextId,
                              final String namespace,
                              final String tagName,
                              final String pseudoElement,
                              final AttributeMap attributeMap)
  {
    this.pseudoElement = pseudoElement;
    if (contextId == null)
    {
      throw new NullPointerException();
    }
    if (attributeMap == null)
    {
      throw new NullPointerException();
    }

    this.namespace = namespace;
    this.tagName = tagName;
    this.attributeMap = attributeMap;

    this.contextId = contextId;
    this.style = new LayoutStyleImpl();
    this.fontSpecification = new FontSpecification(style);
    this.backgroundSpecification = new BackgroundSpecification();
    this.contentSpecification = new ContentSpecification();
    this.listSpecification = new ListSpecification();
  }

  public String getPseudoElement()
  {
    return pseudoElement;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public String getTagName()
  {
    return tagName;
  }

  public AttributeMap getAttributes()
  {
    return attributeMap;
  }

  public BackgroundSpecification getBackgroundSpecification()
  {
    return backgroundSpecification;
  }

  public FontSpecification getFontSpecification()
  {
    return fontSpecification;
  }

  public ContentSpecification getContentSpecification()
  {
    return contentSpecification;
  }

  public ListSpecification getListSpecification()
  {
    return listSpecification;
  }

  public void setValue(final StyleKey key, final CSSValue value)
  {
    if (derived)
    {
      throw new IllegalStateException();
    }
    style.setValue(key, value);
  }

  public CSSValue getValue(final StyleKey key)
  {
    return style.getValue(key);
  }

  public LayoutStyle getStyle()
  {
    return style;
  }

  public ContextId getContextId()
  {
    return contextId;
  }

  /**
   * Returns the language definition of this layout context. If not set, it
   * defaults to the parent's language. If the root's language is also not
   * defined, then use the system default.
   *
   * @return the defined language, never null.
   */
  public Locale getLanguage()
  {
    // todo:
    return Locale.getDefault();
  }

  public boolean isPseudoElement()
  {
    return pseudoElement != null;
  }

  public Object clone()
  {
    // todo: Is this sane the way it is now?
    try
    {
      final DefaultLayoutContext lc = (DefaultLayoutContext) super.clone();
      //lc.style = style.createCopy();
      lc.derived = true;
      return lc;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException
          ("Invalid implementation: Clone not supported.");
    }
  }

  public LayoutContext derive()
  {
    DefaultLayoutContext lc = (DefaultLayoutContext) clone();
    lc.tagName = tagName + "*";
    return lc;
  }

  public void dispose()
  {
    style.dispose();
  }

  public boolean copyFrom(LayoutStyle style)
  {
    if (derived)
    {
      throw new IllegalStateException();
    }

    return this.style.copyFrom(style);
  }

}
