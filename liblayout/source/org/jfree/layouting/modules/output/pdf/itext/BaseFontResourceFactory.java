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
package org.jfree.layouting.modules.output.pdf.itext;

import com.lowagie.text.pdf.BaseFont;
import org.jfree.resourceloader.CompoundResource;
import org.jfree.resourceloader.DependencyCollector;
import org.jfree.resourceloader.FactoryParameterKey;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceFactory;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;

/**
 * Creation-Date: 16.05.2006, 17:19:38
 *
 * @author Thomas Morgner
 */
public class BaseFontResourceFactory implements ResourceFactory
{
  public static final FactoryParameterKey FONTNAME =
          new FactoryParameterKey("filename");
  public static final FactoryParameterKey ENCODING =
          new FactoryParameterKey("encoding");
  public static final FactoryParameterKey EMBEDDED =
          new FactoryParameterKey("embedded");

  public BaseFontResourceFactory()
  {
  }

  public Resource create(final ResourceManager manager,
                         final ResourceData data,
                         final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    final ResourceKey key = data.getKey();
    final boolean embedded = Boolean.TRUE.equals(key.getFactoryParameter(EMBEDDED));
    final String encoding = String.valueOf(key.getFactoryParameter(ENCODING));
    final String fontType = String.valueOf(key.getFactoryParameter(FONTNAME));

    final DependencyCollector dc = new DependencyCollector
            (key, data.getVersion(manager));

    final byte[] ttfAfm = data.getResource(manager);
    byte[] pfb = null;
    if (embedded && (fontType.endsWith(".afm") || fontType.endsWith(".pfm")))
    {
      final String pfbFileName = fontType.substring
              (0, fontType.length() - 4) + ".pfb";
      try
      {
        final ResourceKey pfbKey = manager.deriveKey(key, pfbFileName);
        ResourceData res = manager.load(pfbKey);
        pfb = res.getResource(manager);
        dc.add(pfbKey, res.getVersion(manager));
      }
      catch (ResourceException e)
      {
        // ignore ..
      }
    }

    try
    {
      Log.debug ("Created font " + fontType);
      BaseFont baseFont = BaseFont.createFont
              (fontType, encoding, embedded, false, ttfAfm, pfb);
      return new CompoundResource (key, dc, baseFont);
    }
    catch (Exception e)
    {
      throw new ResourceCreationException
              ("Failed to create the font " + fontType, e);
    }
  }

  public Class getFactoryType()
  {
    return BaseFont.class;
  }

  public void initializeDefaults()
  {
    // nothing needed ...
  }
}
