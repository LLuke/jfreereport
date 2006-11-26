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
 * CSSParserFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CSSParserFactory.java,v 1.3 2006/04/23 15:18:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.selectors.CSSSelectorFactory;
import org.jfree.layouting.input.style.selectors.conditions.CSSConditionFactory;
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
        p.setConditionFactory(new FixNamespaceConditionFactory(new CSSConditionFactory()));
        p.setSelectorFactory(new FixNamespaceSelectorFactory(new CSSSelectorFactory()));
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
      p.setConditionFactory(new FixNamespaceConditionFactory(new CSSConditionFactory()));
      p.setSelectorFactory(new FixNamespaceSelectorFactory(new CSSSelectorFactory()));
      return p;
    }
    catch (Exception e)
    {
      throw new CSSParserInstantiationException();
    }
  }
}
