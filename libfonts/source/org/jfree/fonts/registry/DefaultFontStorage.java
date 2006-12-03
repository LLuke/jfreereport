/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
package org.jfree.fonts.registry;

import java.util.HashMap;

/**
 * Creation-Date: 15.12.2005, 18:07:53
 *
 * @author Thomas Morgner
 */
public class DefaultFontStorage implements FontStorage
{
  private static class FontKey
  {
    private FontIdentifier identifier;
    private boolean aliased;
    private boolean fractional;
    private double fontSize;

    public FontKey(final FontIdentifier identifier,
                   final boolean aliased,
                   final boolean fractional, final double fontSize)
    {
      if (identifier == null)
      {
        throw new NullPointerException();
      }
      this.identifier = identifier;
      this.aliased = aliased;
      this.fractional = fractional;
      this.fontSize = fontSize;
    }

    public FontKey()
    {
    }

    public FontIdentifier getIdentifier()
    {
      return identifier;
    }

    public void setIdentifier(final FontIdentifier identifier)
    {
      this.identifier = identifier;
    }

    public boolean isAliased()
    {
      return aliased;
    }

    public void setAliased(final boolean aliased)
    {
      this.aliased = aliased;
    }

    public boolean isFractional()
    {
      return fractional;
    }

    public void setFractional(final boolean fractional)
    {
      this.fractional = fractional;
    }

    public double getFontSize()
    {
      return fontSize;
    }

    public void setFontSize(final double fontSize)
    {
      this.fontSize = fontSize;
    }

    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final FontKey fontKey = (FontKey) o;

      if (aliased != fontKey.aliased)
      {
        return false;
      }
      if (fontKey.fontSize != fontSize)
      {
        return false;
      }
      if (fractional != fontKey.fractional)
      {
        return false;
      }
      if (!identifier.equals(fontKey.identifier))
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result = identifier.hashCode();
      result = 29 * result + (aliased ? 1 : 0);
      result = 29 * result + (fractional ? 1 : 0);
      long temp = fontSize != +0.0d ? Double.doubleToLongBits(fontSize) : 0L;
      result = 29 * result + (int) (temp ^ (temp >>> 32));
      return result;
    }
  }

  private HashMap knownMetrics;
  private FontRegistry registry;
  private FontMetricsFactory metricsFactory;
  private FontKey lookupKey;

  public DefaultFontStorage(FontRegistry registry)
  {
    this.knownMetrics = new HashMap();
    this.registry = registry;
    this.metricsFactory = registry.createMetricsFactory();
    this.lookupKey = new FontKey();
  }

  public FontRegistry getFontRegistry()
  {
    return registry;
  }

  public FontMetrics getFontMetrics(final FontIdentifier record,
                                    final FontContext context)
  {
    if (record == null)
    {
      throw new NullPointerException();
    }
    if (context == null)
    {
      throw new NullPointerException();
    }

    lookupKey.setAliased(context.isAntiAliased());
    lookupKey.setFontSize(context.getFontSize());
    lookupKey.setIdentifier(record);
    lookupKey.setFractional(context.isFractionalMetrics());

    final FontMetrics cachedMetrics = (FontMetrics) knownMetrics.get(lookupKey);
    if (cachedMetrics != null)
    {
      return cachedMetrics;
    }

    final FontKey key = new FontKey(record, context.isAntiAliased(),
        context.isFractionalMetrics(), context.getFontSize());
    final FontMetrics metrics = metricsFactory.createMetrics(record, context);
    knownMetrics.put(key, metrics);
    return metrics;
  }
}
