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
 * BackgroundSpecification.java
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
package org.jfree.layouting.model.border;

import java.io.Serializable;

import org.jfree.layouting.input.EmptyLayoutImageData;
import org.jfree.layouting.input.LayoutImageData;
import org.jfree.layouting.input.style.keys.border.BackgroundRepeat;
import org.jfree.layouting.input.style.keys.border.BackgroundRepeatValue;
import org.jfree.layouting.input.style.keys.border.BackgroundOrigin;
import org.jfree.layouting.input.style.keys.border.BackgroundClip;
import org.jfree.layouting.input.style.keys.border.BackgroundBreak;
import org.jfree.layouting.input.style.keys.border.BackgroundAttachment;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.layouting.util.geom.StrictPoint;
import org.jfree.util.ObjectList;

/**
 * Holds all information that make up an element background. (No borders!)
 *
 * @author Thomas Morgner
 */
public class BackgroundSpecification implements Serializable
{
  public static final EmptyLayoutImageData EMPTY_LAYOUT_IMAGE_DATA =
          new EmptyLayoutImageData();
  public static final BackgroundRepeatValue EMPTY_BACKGROUND_REPEAT =
          new BackgroundRepeatValue(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT);

  private CSSColorValue backgroundColor;
  private BackgroundBreak backgroundBreak;
  private ObjectList backgroundImages;
  private ObjectList backgroundRepeats;
  private ObjectList backgroundSizes;
  private ObjectList backgroundAttachment;
  private ObjectList backgroundPositions;
  private ObjectList backgroundOrigin;
  private ObjectList backgroundClip;

  public BackgroundSpecification()
  {
  }

  public BackgroundBreak getBackgroundBreak()
  {
    return backgroundBreak;
  }

  public void setBackgroundBreak(final BackgroundBreak backgroundBreak)
  {
    this.backgroundBreak = backgroundBreak;
  }

  public CSSColorValue getBackgroundColor()
  {
    return backgroundColor;
  }

  public void setBackgroundColor(final CSSColorValue backgroundColor)
  {
    this.backgroundColor = backgroundColor;
  }

  public LayoutImageData getBackgroundImage(int i)
  {
    if (backgroundImages == null)
    {
      return EMPTY_LAYOUT_IMAGE_DATA;
    }
    LayoutImageData retval = (LayoutImageData)
            backgroundImages.get(i % backgroundImages.size());
    if (retval == null)
    {
      return EMPTY_LAYOUT_IMAGE_DATA;
    }
    return retval;
  }

  public int getBackgroundImageCount()
  {
    if (backgroundImages == null)
    {
      return 0;
    }
    return backgroundImages.size();
  }

  public void setBackgroundImage(final int i,
                                 final LayoutImageData data)
  {
    if (backgroundImages == null)
    {
      backgroundImages = new ObjectList();
    }
    backgroundImages.set(i, data);
  }

  public BackgroundRepeatValue getBackgroundRepeat(int i)
  {
    if (backgroundRepeats == null)
    {
      return EMPTY_BACKGROUND_REPEAT;
    }
    BackgroundRepeatValue retval = (BackgroundRepeatValue)
            backgroundRepeats.get(i % backgroundRepeats.size());
    if (retval == null)
    {
      return EMPTY_BACKGROUND_REPEAT;
    }
    return retval;
  }

  public int getBackgroundRepeatCount()
  {
    if (backgroundRepeats == null)
    {
      return 0;
    }
    return backgroundRepeats.size();
  }

  public void setBackgroundRepeat(final int i,
                                  final BackgroundRepeatValue data)
  {
    if (backgroundRepeats == null)
    {
      backgroundRepeats = new ObjectList();
    }
    backgroundRepeats.set(i, data);
  }

  public StrictDimension getBackgroundSize(int i)
  {
    if (backgroundSizes == null)
    {
      return new StrictDimension();
    }
    StrictDimension retval = (StrictDimension)
            backgroundSizes.get(i % backgroundSizes.size());
    if (retval == null)
    {
      return new StrictDimension();
    }
    return (StrictDimension) retval.clone();
  }

  public int getBackgroundSizesCount()
  {
    if (backgroundSizes == null)
    {
      return 0;
    }
    return backgroundSizes.size();
  }

  public void setBackgroundSizes(final int i,
                                 final StrictDimension data)
  {
    if (backgroundSizes == null)
    {
      backgroundSizes = new ObjectList();
    }
    if (data == null)
    {
      backgroundSizes.set(i, null);
    }
    else
    {
      backgroundSizes.set(i, data.clone());
    }
  }

  public StrictPoint getBackgroundPosition(int i)
  {
    if (backgroundPositions == null)
    {
      return new StrictPoint();
    }
    StrictPoint retval = (StrictPoint)
            backgroundPositions.get(i % backgroundPositions.size());
    if (retval == null)
    {
      return new StrictPoint();
    }
    return (StrictPoint) retval.clone();
  }

  public int getBackgroundPositionsCount()
  {
    if (backgroundPositions == null)
    {
      return 0;
    }
    return backgroundPositions.size();
  }

  public void setBackgroundPosition(final int i,
                                    final StrictDimension data)
  {
    if (backgroundPositions == null)
    {
      backgroundPositions = new ObjectList();
    }
    if (data == null)
    {
      backgroundPositions.set(i, null);
    }
    else
    {
      backgroundPositions.set(i, data.clone());
    }
  }


  public BackgroundOrigin getBackgroundOrigin(int i)
  {
    if (backgroundOrigin == null)
    {
      return BackgroundOrigin.PADDING;
    }
    BackgroundOrigin retval = (BackgroundOrigin)
            backgroundOrigin.get(i % backgroundOrigin.size());
    if (retval == null)
    {
      return BackgroundOrigin.PADDING;
    }
    return retval;
  }

  public int getBackgroundOriginCount()
  {
    if (backgroundOrigin == null)
    {
      return 0;
    }
    return backgroundOrigin.size();
  }

  public void setBackgroundOrigin(final int i,
                                  final BackgroundOrigin data)
  {
    if (backgroundOrigin == null)
    {
      backgroundOrigin = new ObjectList();
    }
    if (data == null)
    {
      backgroundOrigin.set(i, null);
    }
    else
    {
      backgroundOrigin.set(i, data);
    }
  }

  public BackgroundClip getBackgroundClip(int i)
  {
    if (backgroundClip == null)
    {
      return BackgroundClip.BORDER;
    }
    BackgroundClip retval = (BackgroundClip)
            backgroundClip.get(i % backgroundClip.size());
    if (retval == null)
    {
      return BackgroundClip.BORDER;
    }
    return retval;
  }

  public int getBackgroundClipCount()
  {
    if (backgroundClip == null)
    {
      return 0;
    }
    return backgroundClip.size();
  }

  public void setBackgroundClip(final int i,
                                  final BackgroundClip data)
  {
    if (backgroundClip == null)
    {
      backgroundClip = new ObjectList();
    }
    if (data == null)
    {
      backgroundClip.set(i, null);
    }
    else
    {
      backgroundClip.set(i, data);
    }
  }

  public BackgroundAttachment getBackgroundAttachment(int i)
  {
    if (backgroundAttachment == null)
    {
      return BackgroundAttachment.SCROLL;
    }
    BackgroundAttachment retval = (BackgroundAttachment)
            backgroundAttachment.get(i % backgroundAttachment.size());
    if (retval == null)
    {
      return BackgroundAttachment.SCROLL;
    }
    return retval;
  }

  public int getBackgroundAttachmentCount()
  {
    if (backgroundAttachment == null)
    {
      return 0;
    }
    return backgroundAttachment.size();
  }

  public void setBackgroundAttachment(final int i,
                                  final BackgroundAttachment data)
  {
    if (backgroundAttachment == null)
    {
      backgroundAttachment = new ObjectList();
    }
    if (data == null)
    {
      backgroundAttachment.set(i, null);
    }
    else
    {
      backgroundAttachment.set(i, data);
    }
  }
}
