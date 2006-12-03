/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser;

import java.io.IOException;

import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.resourceloader.CompoundResource;
import org.jfree.resourceloader.DependencyCollector;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceFactory;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;

/**
 * Parses a single style rule.
 *
 * @author Thomas Morgner
 */
public class StyleRuleFactory implements ResourceFactory
{
  public StyleRuleFactory ()
  {
  }

  public Resource create (final ResourceManager manager,
                          final ResourceData data,
                          final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final ResourceKey key;
      final long version;
      if (context == null)
      {
        key = data.getKey();
        version = data.getVersion(manager);
      }
      else
      {
        key = context;
        version = -1;
      }
      final StyleSheetHandler handler = new StyleSheetHandler
              (manager, key, version, StyleKeyRegistry.getRegistry(), null);
      parser.setDocumentHandler(handler);

      final InputSource inputSource = new InputSource();
      inputSource.setByteStream(data.getResourceAsStream(manager));

      handler.init(inputSource);
      handler.setStyleRule(new CSSStyleRule(null, null));
      parser.parseStyleDeclaration(inputSource);

      final DependencyCollector dependencies = handler.getDependencies();
      if (context != null)
      {
        dependencies.add(data.getKey(), data.getVersion(manager));
      }

      CSSParserContext.getContext().destroy();

      final CSSDeclarationRule styleRule = handler.getStyleRule();
      if (styleRule == null)
      {
        throw new ResourceCreationException("Damn, the style rule is null");
      }
      return new CompoundResource
              (data.getKey(), dependencies, styleRule);
    }
    catch (CSSParserInstantiationException e)
    {
      throw new ResourceCreationException("Failed to parse the stylesheet.");
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Failed to load the stylesheet.");
    }
  }

  public Class getFactoryType ()
  {
    return StyleRule.class;
  }

  public void initializeDefaults ()
  {
    // nothing needed ...
  }
}
