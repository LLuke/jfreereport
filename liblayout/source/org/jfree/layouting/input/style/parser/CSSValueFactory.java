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
 * CSSValueFactory.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CSSValueFactory.java,v 1.1 2006/02/12 21:57:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 25.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 25.11.2005, 17:43:38
 *
 * @author Thomas Morgner
 */
public class CSSValueFactory
{
  public static final String SIMPLE_PREFIX = "org.jfree.layouting.parser.handlers.";
  public static final String COMPOUND_PREFIX = "org.jfree.layouting.parser.compoundhandlers.";

  private HashMap handlers;
  private HashMap compoundHandlers;
  private StyleKeyRegistry registry;

  public CSSValueFactory (StyleKeyRegistry registry)
  {
    if (registry == null)
    {
      throw new NullPointerException();
    }
    this.registry = registry;
    this.handlers = new HashMap();
    this.compoundHandlers = new HashMap();
    this.registerDefaults();
  }


  public void registerDefaults ()
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    Iterator sit = config.findPropertyKeys(SIMPLE_PREFIX);
    while (sit.hasNext())
    {
      final String key = (String) sit.next();
      final String name = key.substring(SIMPLE_PREFIX.length()).toLowerCase();
      final String c = config.getConfigProperty(key);
      CSSValueReadHandler module = (CSSValueReadHandler)
              ObjectUtilities.loadAndInstantiate(c, CSSValueFactory.class);
      if (module != null)
      {
        handlers.put(name, module);
      }
      else
      {
        Log.warn("Invalid module implementation: " + c);
      }
    }

    Iterator cit = config.findPropertyKeys(COMPOUND_PREFIX);
    while (cit.hasNext())
    {
      final String key = (String) cit.next();
      final String name = key.substring(COMPOUND_PREFIX.length()).toLowerCase();
      final String c = config.getConfigProperty(key);
      CSSCompoundValueReadHandler module = (CSSCompoundValueReadHandler)
              ObjectUtilities.loadAndInstantiate(c, CSSValueFactory.class);
      if (module != null)
      {
        compoundHandlers.put(name, module);
      }
    }
  }


  private CSSValue createValue (StyleKey key, LexicalUnit value)
  {
    final CSSValueReadHandler module =
            (CSSValueReadHandler) handlers.get(key.getName());
    if (module == null)
    {
      return null;
    }

    return module.createValue(key, value);
  }

  public static CSSAttrFunction parseAttrFunction (LexicalUnit unit)
  {
    if (unit.getLexicalUnitType() != LexicalUnit.SAC_ATTR &&
            unit.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION)
    {
      return null;
    }

    LexicalUnit parameter = unit.getParameters();
    if (parameter.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      final String attrName = parameter.getStringValue();
      final LexicalUnit comma = parameter.getNextLexicalUnit();
      if (comma == null)
      {
        return null;
      }
      final String attrType = parseAttributeType(comma.getNextLexicalUnit());
      if (attrType == null || attrName == null)
      {
        return null;
      }
      return new CSSAttrFunction(attrName, attrType);
    }
    return null;
  }

  private static String parseAttributeType (LexicalUnit unit)
  {
    if (unit == null)
    {
      return null;
    }
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      return unit.getStringValue();
    }
    return null;
  }

  public void parseValue (CSSDeclarationRule rule,
                          String name,
                          LexicalUnit value,
                          boolean important)
          throws CSSParserFactoryException
  {
    String normalizedName = name.toLowerCase();
    final StyleKey key = registry.findKeyByName(normalizedName);
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INHERIT)
    {
      if (key == null)
      {
        Log.debug("Got no key for " + normalizedName);
        return;
      }
      rule.setPropertyValue(key, CSSInheritValue.getInstance());
      rule.setImportant(key, important);
      return;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_ATTR)
    {
      // ATTR function.
      if (key == null)
      {
        Log.debug("Got no key for " + normalizedName);
        return;
      }
      final CSSAttrFunction attrFn = parseAttrFunction(value);
      if (attrFn != null)
      {
        rule.setPropertyValue(key, attrFn);
        rule.setImportant(key, important);
      }
      return;

    }

    if (key != null)
    {
      CSSValue cssValue = createValue(key, value);
      if (cssValue != null)
      {
        rule.setPropertyValue(key, cssValue);
        rule.setImportant(key, important);
        //Log.debug ("Got value " + key.getName() + " = " + cssValue + "(" + cssValue.getClass() + ") - (important = " + important + ")");
        return;
      }
    }

    final CSSCompoundValueReadHandler module =
            (CSSCompoundValueReadHandler) compoundHandlers.get(normalizedName);
    if (module == null)
    {
      if (key == null)
      {
        Log.debug("Got no key for " + normalizedName);
        return;
      }
      else
      {
        Log.debug("Got no value for " + normalizedName);
      }
      return; // ignore this rule ..
    }
    Map map = module.createValues(value);
    if (map == null)
    {
      return;
    }
    Iterator iterator = map.entrySet().iterator();
    while (iterator.hasNext())
    {
      Map.Entry entry = (Map.Entry) iterator.next();
      StyleKey entryKey = (StyleKey) entry.getKey();
      CSSValue mapCssValue = (CSSValue) entry.getValue();

      rule.setPropertyValue(entryKey, mapCssValue);
      rule.setImportant(entryKey, important);
      //Log.debug ("Got value " + entryKey.getName() + " = " + mapCssValue + "(" + mapCssValue.getClass() + ") - (important = " + important + ")");
    }
  }

  public static CSSStringValue createUriValue (LexicalUnit value)
  {
    if (value.getLexicalUnitType() != LexicalUnit.SAC_URI)
    {
      return null;
    }

    final String uri = value.getStringValue();
    final URL url = CSSParserContext.getContext().getURL();
    try
    {
      final URL target = new URL(url, uri);
      return new CSSStringValue(CSSStringType.URI, target.toExternalForm());
    }
    catch (MalformedURLException e)
    {
      // dont know what to do here ...
      return null;
    }
  }

  public static boolean isNumericValue (LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL)
    {
      return true;
    }
    return false;
  }


  public static CSSNumericValue createNumericValue (LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      return new CSSNumericValue(CSSNumericType.NUMBER,
              value.getIntegerValue());
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL)
    {
      return new CSSNumericValue(CSSNumericType.NUMBER,
              value.getFloatValue());
    }
    return null;
  }

  public static boolean isLengthValue (LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_EM)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_EX)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PIXEL)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_INCH)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_CENTIMETER)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_MILLIMETER)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PICA)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_POINT)
    {
      return true;
    }
    return false;
  }


  public static CSSNumericValue createLengthValue (LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      if (value.getFloatValue() != 0)
      {
        return null;
      }
      return new CSSNumericValue(CSSNumericType.PT, 0);
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_EM)
    {
      return new CSSNumericValue(CSSNumericType.EM,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_EX)
    {
      return new CSSNumericValue(CSSNumericType.EX,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PIXEL)
    {
      return new CSSNumericValue(CSSNumericType.PX,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_INCH)
    {
      return new CSSNumericValue(CSSNumericType.INCH,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_CENTIMETER)
    {
      return new CSSNumericValue(CSSNumericType.CM,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_MILLIMETER)
    {
      return new CSSNumericValue(CSSNumericType.MM,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PICA)
    {
      return new CSSNumericValue(CSSNumericType.PC,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_POINT)
    {
      return new CSSNumericValue(CSSNumericType.PT,
              value.getFloatValue());
    }
    return null;
  }

  public static LexicalUnit parseComma (final LexicalUnit value)
  {
    if (value == null)
    {
      return null;
    }

    LexicalUnit maybeComma = value.getNextLexicalUnit();
    if (maybeComma == null)
    {
      return null;
    }
    if (maybeComma.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
    {
      return maybeComma.getNextLexicalUnit();
    }
    return null;
  }

}
