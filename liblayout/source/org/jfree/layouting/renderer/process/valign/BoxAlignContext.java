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
 * BoxAlignContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process.valign;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.BoxLayoutProperties;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.renderer.text.TextUtility;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 13.10.2006, 22:22:10
 *
 * @author Thomas Morgner
 */
public class BoxAlignContext extends AlignContext
{
  private ArrayList childs;
  private long insetsTop;
  private long insetsBottom;
  private long[] baselines;

  public BoxAlignContext(RenderBox box)
  {
    super(box);
    this.childs = new ArrayList();

    ExtendedBaselineInfo baselineInfo = box.getBaselineInfo();
    if (baselineInfo == null)
    {
      baselineInfo = box.getNominalBaselineInfo();
    }

    final CSSValue dominantBaselineValue = box.getDominantBaseline();
    setDominantBaseline(TextUtility.translateDominantBaseline
        (dominantBaselineValue, baselineInfo.getDominantBaseline()));

    final BoxLayoutProperties blp = box.getBoxLayoutProperties();
    insetsTop = blp.getBorderTop() + blp.getPaddingTop();
    insetsBottom = blp.getBorderBottom() + blp.getPaddingBottom();

    baselines = baselineInfo.getBaselines();
    for (int i = 1; i < baselines.length; i++)
    {
      baselines[i] += insetsTop;
    }
    baselines[ExtendedBaselineInfo.AFTER_EDGE] =
        baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE] + insetsBottom;
  }

  public void addChild(AlignContext context)
  {
    childs.add(context);
  }

  public AlignContext[] getChilds ()
  {
    return (AlignContext[]) childs.toArray(new AlignContext[childs.size()]);
  }

  public long getInsetsTop()
  {
    return insetsTop;
  }

  public long getInsetsBottom()
  {
    return insetsBottom;
  }

  public long getBaselineDistance(int baseline)
  {
    return baselines[baseline] - baselines[getDominantBaseline()];
  }

  public void shift(final long delta)
  {
    for (int i = 0; i < baselines.length; i++)
    {
      baselines[i] += delta;
    }

    for (int i = 0; i < childs.size(); i++)
    {
      AlignContext context = (AlignContext) childs.get(i);
      context.shift(delta);
    }
  }

  public long getAfterEdge()
  {
    return this.baselines[ExtendedBaselineInfo.AFTER_EDGE];
  }

  public long getBeforeEdge()
  {
    return this.baselines[ExtendedBaselineInfo.BEFORE_EDGE];
  }

  public void setBeforeEdge(final long offset)
  {
    this.baselines[ExtendedBaselineInfo.BEFORE_EDGE] = offset;
  }

  public void setAfterEdge (final long offset)
  {
    this.baselines[ExtendedBaselineInfo.AFTER_EDGE] = offset;
  }
}
