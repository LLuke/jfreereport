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
 * CSSParserFactory.java
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
package org.jfree.layouting.input.style.parser;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.selectors.conditions.CSSConditionFactory;
import org.jfree.layouting.input.style.selectors.CSSSelectorFactory;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.helpers.ParserFactory;

/**
 * Creates a new CSS parser by first looking for a specified parser in the
 * libLayout configuration and if that fails, by using the W3C parser factory.
 *
 * @author Thomas Morgner
 */
public class CSSParserFactory
{
  private static CSSParserFactory parserFactory;
  private CSSConditionFactory conditionFactory;
  private CSSSelectorFactory selectorFactory;

  public static synchronized CSSParserFactory getInstance()
  {
    if (parserFactory == null)
    {
      parserFactory = new CSSParserFactory();
    }
    return parserFactory;
  }

  private CSSParserFactory()
  {
    conditionFactory = new CSSConditionFactory();
    selectorFactory = new CSSSelectorFactory();
  }

  public Parser createCSSParser ()
          throws CSSParserInstantiationException
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    final String parserClass =
            config.getConfigProperty("org.jfree.layouting.css.Parser");
    if (parserClass != null)
    {
      Parser p = (Parser) ObjectUtilities.loadAndInstantiate
            (parserClass, CSSParserFactory.class);
      if (p != null)
      {
        p.setConditionFactory(conditionFactory);
        p.setSelectorFactory(selectorFactory);
        return p;
      }
    }
    try
    {
      Parser p = new ParserFactory().makeParser();
      if (p == null)
      {
        return null;
      }
      p.setConditionFactory(conditionFactory);
      p.setSelectorFactory(selectorFactory);
      return p;
    }
    catch (Exception e)
    {
      throw new CSSParserInstantiationException();
    }
  }
}
