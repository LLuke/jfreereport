package org.jfree.fonts.merge;

import java.util.ArrayList;
import java.util.HashSet;

import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontMetricsFactory;

/**
 * Creation-Date: 20.07.2007, 18:46:04
 *
 * @author Thomas Morgner
 */
public class CompoundFontRegistry implements FontRegistry
{
  private ArrayList registries;

  public CompoundFontRegistry()
  {
    this.registries = new ArrayList();
  }

  public void addRegistry (final FontRegistry registry)
  {
    if (registry == null)
    {
      throw new NullPointerException();
    }
    this.registries.add(registry);
  }

  public void initialize()
  {
    for (int i = 0; i < registries.size(); i++)
    {
      final FontRegistry fontRegistry = (FontRegistry) registries.get(i);
      fontRegistry.initialize();
    }
  }

  public FontFamily getFontFamily(final String name)
  {
    for (int i = 0; i < registries.size(); i++)
    {
      final FontRegistry fontRegistry = (FontRegistry) registries.get(i);
      final FontFamily fontFamily = fontRegistry.getFontFamily(name);
      if (fontFamily != null)
      {
        return new CompoundFontFamily(fontFamily, fontRegistry);
      }
    }
    return null;
  }

  public String[] getRegisteredFamilies()
  {
    final HashSet registeredFamilies = new HashSet();

    for (int i = 0; i < registries.size(); i++)
    {
      final FontRegistry fontRegistry = (FontRegistry) registries.get(i);
      final String[] fontFamilies = fontRegistry.getRegisteredFamilies();
      for (int j = 0; j < fontFamilies.length; j++)
      {
        final String fontFamily = fontFamilies[j];
        registeredFamilies.add(fontFamily);
      }
    }
    return (String[]) registeredFamilies.toArray(new String[registeredFamilies.size()]);
  }

  public String[] getAllRegisteredFamilies()
  {
    final HashSet registeredFamilies = new HashSet();

    for (int i = 0; i < registries.size(); i++)
    {
      final FontRegistry fontRegistry = (FontRegistry) registries.get(i);
      final String[] fontFamilies = fontRegistry.getAllRegisteredFamilies();
      for (int j = 0; j < fontFamilies.length; j++)
      {
        final String fontFamily = fontFamilies[j];
        registeredFamilies.add(fontFamily);
      }
    }
    return (String[]) registeredFamilies.toArray(new String[registeredFamilies.size()]);
  }

  public FontMetricsFactory createMetricsFactory()
  {
    throw new UnsupportedOperationException("The CompoundFontRegistry cannot provide font-metrics directly.");
  }
}
