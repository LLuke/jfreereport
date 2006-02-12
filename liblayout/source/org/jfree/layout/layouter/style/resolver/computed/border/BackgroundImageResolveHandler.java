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
 * BackgroundImageResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed.border;

import java.net.MalformedURLException;
import java.net.URL;

import org.jfree.layouting.input.URLLayoutImageData;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSImageValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValueSupport;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.border.BackgroundSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.output.OutputProcessorFeature;
import org.jfree.util.Log;

/**
 * Creation-Date: 14.12.2005, 13:17:40
 *
 * @author Thomas Morgner
 */
public class BackgroundImageResolveHandler implements ResolveHandler
{
  public BackgroundImageResolveHandler()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve(LayoutProcess process,
                         LayoutNode currentNode,
                         LayoutStyle style,
                         StyleKey key)
  {
    // start loading all images, assume that they are found and include them
    // in the list.

    if (process.getOutputMetaData().isFeatureSupported
            (OutputProcessorFeature.BACKGROUND_IMAGE) == false)
    {
      return;
    }

    CSSValue value = style.getValue(key);
    if (value == null)
    {
      return;
    }
    if (value instanceof CSSValueList == false)
    {
      return;
    }

    CSSValueList list = (CSSValueList) value;
    final int length = list.getLength();
    if (length == 0)
    {
      return;
    }
    BackgroundSpecification backgroundSpecification =
            currentNode.getLayoutContext().getBackgroundSpecification();
    URL baseURL = process.getDocumentContext().getBaseURL();

    for (int i = 0; i < length; i++)
    {
      CSSValue item = list.getItem(i);

      if (CSSValueSupport.isURI(item))
      {
        CSSStringValue svalue = (CSSStringValue) item;
        try
        {
          URL sourceURL = new URL (baseURL, svalue.getValue());
          backgroundSpecification.setBackgroundImage
                  (i, new URLLayoutImageData(sourceURL, svalue.getValue()));
        }
        catch (MalformedURLException e)
        {
          Log.warn ("Malformed URL while processing an element background", e);
        }
      }
      else if (item instanceof CSSImageValue)
      {
        CSSImageValue img = (CSSImageValue) item;
        backgroundSpecification.setBackgroundImage
                  (i, img.getImageData());
      }
    }
  }
}
