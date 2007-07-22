package org.jfree.fonts.merge;

import java.util.HashMap;

import org.jfree.fonts.registry.FontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontMetricsFactory;

/**
 * Creation-Date: 20.07.2007, 19:35:31
 *
 * @author Thomas Morgner
 */
public class CompoundFontStorage implements FontStorage
{
  private CompoundFontRegistry fontRegistry;
  private HashMap metricsFactories;

  public CompoundFontStorage()
  {
    this.fontRegistry = new CompoundFontRegistry();
    this.metricsFactories = new HashMap();
  }

  public void addRegistry(final FontRegistry registry)
  {
    fontRegistry.addRegistry(registry);
  }

  public FontRegistry getFontRegistry()
  {
    return fontRegistry;
  }

  public FontMetrics getFontMetrics(final FontIdentifier record, final FontContext context)
  {
    final CompoundFontIdentifier cid = (CompoundFontIdentifier) record;
    final FontRegistry registry = cid.getRegistry();
    FontMetricsFactory metricsFactory = (FontMetricsFactory) metricsFactories.get(registry);
    if (metricsFactory == null)
    {
      metricsFactory = registry.createMetricsFactory();
      metricsFactories.put (registry, metricsFactory);
    }

    return metricsFactory.createMetrics(cid.getIdentifier(), context);
  }
}
