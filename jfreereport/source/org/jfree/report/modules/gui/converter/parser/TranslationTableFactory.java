/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------------
 * TranslationTableFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------------------------
 * 26.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.converter.parser;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.jfree.report.util.Log;

public final class TranslationTableFactory
{
  public static class ContextRule
  {
    private String name;
    private String mapTo;

    public ContextRule(String name, String mapTo)
    {
      if (name == null)
      {
        throw new NullPointerException("Name is null");
      }
      this.name = name;
      this.mapTo = mapTo;
    }

    public boolean isMappingDefined ()
    {
      return mapTo != null;
    }

    public String getName()
    {
      return name;
    }

    public String getMapTo()
    {
      return mapTo;
    }

    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (!(o instanceof ContextRule)) return false;

      final ContextRule contextRule = (ContextRule) o;

      if (!name.equals(contextRule.name)) return false;

      return true;
    }

    public int hashCode()
    {
      return name.hashCode();
    }
  }

  private static TranslationTableFactory singleton;

  public static TranslationTableFactory getInstance ()
  {
    if (singleton == null)
    {
      singleton = new TranslationTableFactory();
    }
    return singleton;
  }

  private Hashtable contexts;
  private Properties translations;

  private TranslationTableFactory()
  {
    contexts = new Hashtable();
    loadContextMap();
    loadTranslationSpecs();
  }

  private void loadTranslationSpecs()
  {
    InputStream in = this.getClass().getResourceAsStream("translations.properties");
    if (in == null)
    {
      return;
    }
    translations = new Properties();
    try
    {
      translations.load(in);
    }
    catch (Exception e)
    {
      Log.debug ("Unable to load the translation set.");
    }
  }

  private void loadContextMap()
  {
    InputStream in = this.getClass().getResourceAsStream("contextmap.properties");
    if (in == null)
    {
      Log.debug ("Context map was not found.");
      return;
    }
    try
    {
      Properties contextProperties = new Properties();
      contextProperties.load(in);

      String initialContext = contextProperties.getProperty("%init");
      if (initialContext == null)
      {
        Log.debug ("Initial context is null.");
        return;
      }
      CSVTokenizer tokenizer = new CSVTokenizer(initialContext, CSVTokenizer.SEPARATOR_COMMA);
      while (tokenizer.hasMoreTokens())
      {
        String context = tokenizer.nextToken().trim();
        buildContext(context, contextProperties);
      }

      // now validate the mappings
      Enumeration enum = contexts.keys();
      while (enum.hasMoreElements())
      {
        Object key = enum.nextElement();
        ContextRule rule = (ContextRule) contexts.get(key);
        if (rule.isMappingDefined())
        {
          String mapTo = rule.getMapTo();
          if (contexts.get(mapTo) == null)
          {
            throw new IllegalStateException("No child mapping for " + key);
          }
        }
      }
    }
    catch (Exception e)
    {
      Log.error ("Failed to load the context map:", e);
      return;
    }
  }

  private void buildContext (String base, Properties contextMap)
  {
    String equals = contextMap.getProperty(base + ".equal");
    if (equals != null)
    {
      CSVTokenizer tokenizer = new CSVTokenizer(equals, CSVTokenizer.SEPARATOR_COMMA);
      while (tokenizer.hasMoreTokens())
      {
        String context = tokenizer.nextToken().trim();
        String fqContext = base + "." + context;
        ContextRule rule = new ContextRule(fqContext, base);
        contexts.put(fqContext, rule);
      }
    }

    // if a mapping is defined, there is no way to define childs for
    // an context node.
    String mapTo = contextMap.getProperty(base + ".mapto");
    if (mapTo != null)
    {
      ContextRule rule = new ContextRule(base, mapTo);
      contexts.put(base, rule);
      return;
    }

    String childs = contextMap.getProperty(base + ".child");
    if (childs == null)
    {
      throw new IllegalStateException("Property " + base + " defines no childs.");
    }
    else
    {
      CSVTokenizer tokenizer = new CSVTokenizer(childs, CSVTokenizer.SEPARATOR_COMMA);
      while (tokenizer.hasMoreTokens())
      {
        String context = tokenizer.nextToken().trim();
        String fqContext = base + "." + context;
        buildContext(fqContext, contextMap);
      }
    }

    ContextRule rule = new ContextRule(base, null);
    contexts.put(base, rule);
  }

  public ContextRule buildContext (ContextRule lastContext, String context)
  {
    if (lastContext == null)
    {
      return (ContextRule) contexts.get (context);
    }

    if (lastContext.isMappingDefined())
    {
      throw new IllegalArgumentException("Unresolved mapping found.");
    }

    ContextRule nextContext = (ContextRule)
        contexts.get(lastContext.getName() + "." + context);
    if (nextContext == null)
    {
      Log.debug ("Undefined mapping: " + lastContext.getName() + " -> " + context);
      return null;
    }

    while (nextContext.isMappingDefined())
    {
      nextContext = (ContextRule) contexts.get(nextContext.getMapTo());
    }
    return nextContext;
  }

  public TranslationTable getTranslationTable (ContextRule rule)
  {
    return new TranslationTable(translations, rule.getName());
  }

  public static void main (String [] args)
  {
    TranslationTableFactory factory = TranslationTableFactory.getInstance();
//    ArrayList cons = new ArrayList();
//    Enumeration enum = factory.contexts.keys();
//    while (enum.hasMoreElements())
//    {
//      String context = enum.nextElement().toString();
//      cons.add(context);
//    }
//    Collections.sort(cons);
//    for (int i = 0; i < cons.size(); i++)
//    {
//      System.out.println(cons.get(i));
//    }
    ContextRule rule = factory.buildContext(null, "report-definition");
    rule = factory.buildContext(rule, "report-description");
    rule = factory.buildContext(rule, "report-header");
    rule = factory.buildContext(rule, "band");
    rule = factory.buildContext(rule, "band");
    rule = factory.buildContext(rule, "element");
    rule = factory.buildContext(rule, "style");
    Log.debug (rule.getName());
  }
}
