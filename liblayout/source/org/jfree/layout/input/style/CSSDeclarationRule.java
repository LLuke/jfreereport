/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * CSSDeclarationRule.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.11.2005 : Initial version
 */
package org.jfree.layouting.input.style;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jfree.layouting.input.style.values.CSSValue;

/**
 * This class is a merger between the CSSStyleDeclaration and the other
 * stylerule classes holding property name pairs. Actually, this is what once
 * was called a stylesheet in JFreeReport.
 * <p/>
 * StyleProperties are key as Strings and have CSSValues as mapped values..
 *
 * @author Thomas Morgner
 */
public abstract class CSSDeclarationRule extends StyleRule
{
  private HashMap declarations;
  private HashSet importantDeclarations;

  protected CSSDeclarationRule(final StyleSheet parentStyle,
                               final StyleRule parentRule)
  {
    super(parentStyle, parentRule);
    this.declarations = new HashMap();
    this.importantDeclarations = new HashSet();
  }

  public boolean isImportant(StyleKey propertyName)
  {
    return importantDeclarations.contains(propertyName);
  }

  public void setImportant(StyleKey propertyName, final boolean important)
  {
    if (important)
    {
      importantDeclarations.add(propertyName);
    }
    else
    {
      importantDeclarations.remove(propertyName);
    }
  }

//  public String getPropertyValue(StyleKey propertyName)
//  {
//    CSSValue value = getPropertyCSSValue(propertyName);
//    if (value == null)
//    {
//      return null;
//    }
//    return value.getCSSText();
//  }
//
  public CSSValue getPropertyCSSValue(StyleKey propertyName)
  {
    return (CSSValue) declarations.get(propertyName);
  }

//  public void setPropertyCSSValue(StyleKey propertyName, String value)
//  {
//    // for performance: Keep the parser code central.
//    // where do we get the base URL from?
//    // We cant use the parent-style, as this one might not be given
//    // (which is true for inline styles)
//  }
//
  public void setPropertyValue(StyleKey propertyName, CSSValue value)
  {
    if (value == null)
    {
      declarations.remove(propertyName);
    }
    else
    {
      declarations.put(propertyName, value);
    }
  }

  public void removeProperty(StyleKey name)
  {
    declarations.remove(name);
  }

  public Iterator getPropertyKeys()
  {
    return declarations.keySet().iterator();
  }

  public Iterator getImportantKeys()
  {
    return importantDeclarations.iterator();
  }

  public int getSize()
  {
    return declarations.size();
  }

  public Object clone() throws CloneNotSupportedException
  {
    CSSDeclarationRule rule = (CSSDeclarationRule) super.clone();
    rule.declarations = (HashMap) declarations.clone();
    rule.importantDeclarations = (HashSet) importantDeclarations.clone();
    return rule;
  }

}
