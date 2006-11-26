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
 * BackgroundSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BackgroundSpecification.java,v 1.1 2006/07/11 13:38:38 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.context;

import java.io.Serializable;

import org.jfree.layouting.input.style.keys.border.BackgroundAttachment;
import org.jfree.layouting.input.style.keys.border.BackgroundClip;
import org.jfree.layouting.input.style.keys.border.BackgroundOrigin;
import org.jfree.layouting.input.style.keys.border.BackgroundRepeat;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.resourceloader.Resource;
import org.jfree.util.ObjectList;

/**
 * Holds all information that make up an element background. (No borders!)
 *
 * @author Thomas Morgner
 */
public class BackgroundSpecification implements Serializable
{
  public static final CSSValuePair EMPTY_BACKGROUND_REPEAT =
          new CSSValuePair(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT);

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

  // todo: Use some other container for that ...
  public Resource getBackgroundImage(int i)
  {
    if (backgroundImages == null)
    {
      return null;
    }
    Resource retval = (Resource)
            backgroundImages.get(i % backgroundImages.size());
    if (retval == null)
    {
      return null;
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
                                 final Resource data)
  {
    if (backgroundImages == null)
    {
      backgroundImages = new ObjectList();
    }
    backgroundImages.set(i, data);
  }

  public CSSValuePair getBackgroundRepeat(int i)
  {
    if (backgroundRepeats == null)
    {
      return EMPTY_BACKGROUND_REPEAT;
    }
    CSSValuePair retval = (CSSValuePair)
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
                                  final CSSValuePair data)
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

  public CSSValuePair getBackgroundPosition(int i)
  {
    if (backgroundPositions == null)
    {
      return null;
    }
    CSSValuePair retval = (CSSValuePair)
            backgroundPositions.get(i % backgroundPositions.size());
    if (retval == null)
    {
      return null;
    }
    return retval;
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
                                    final CSSValuePair data)
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
      backgroundPositions.set(i, data);
    }
  }


  public CSSConstant getBackgroundOrigin(int i)
  {
    if (backgroundOrigin == null)
    {
      return BackgroundOrigin.PADDING;
    }
    CSSConstant retval = (CSSConstant)
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
                                  final CSSConstant data)
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

  public CSSConstant getBackgroundClip(int i)
  {
    if (backgroundClip == null)
    {
      return BackgroundClip.BORDER;
    }
    CSSConstant retval = (CSSConstant)
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
                                final CSSConstant data)
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

  public CSSConstant getBackgroundAttachment(int i)
  {
    if (backgroundAttachment == null)
    {
      return BackgroundAttachment.SCROLL;
    }
    CSSConstant retval = (CSSConstant)
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
                                      final CSSConstant data)
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
