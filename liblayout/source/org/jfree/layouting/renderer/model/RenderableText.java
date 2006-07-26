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
 * RenderedText.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RenderableText.java,v 1.5 2006/07/26 11:52:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.text.Glyph;
import org.jfree.layouting.renderer.text.Spacing;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.renderer.text.breaks.BreakOpportunityProducer;
import org.jfree.util.Log;

/**
 * The renderable text is a text chunk, enriched with layouting information,
 * such as break opportunities, character sizes, kerning information and spacing
 * information.
 * <p/>
 * Text is given as codepoints. Break opportunities are given as integer values,
 * where zero forbids breaking, and higher values denote better breaks. Spacing
 * and glyph sizes and kerning is given in micro-points; Spacing is the 'added'
 * space between codepoints if text-justification is enabled.
 * <p/>
 * The text is computed as grapheme clusters; this means that several unicode
 * codepoints may result in a single /virtual/ glyph/codepoint/character.
 * (Example: 'A' + accent symbols). If the font supports Lithurges, these
 * lithurges may also be represented as a single grapheme cluster (and thus
 * behave unbreakable).
 * <p/>
 * Grapheme clusters with more than one unicode char have the size of that char
 * added to the first codepoint, all subsequence codepoints of the same cluster
 * have a size/kerning/etc of zero and are unbreakable.
 * <p/>
 * This text chunk is perfectly suitable for horizontal text, going either from
 * left-to-right or right-to-left. (Breaking mixed text is up to the
 * textfactory).
 *
 * @author Thomas Morgner
 */
public class RenderableText extends RenderNode
{
  private Glyph[] glyphs;
  private int offset;
  private int length;
  private LayoutContext layoutContext;
  private boolean ltr;
  private int script;

  private long minimumChunkWidth;
  private long minimumWidth;
  private long preferredWidth;
  private long maximumWidth;

  private long height;
  private boolean forceLinebreak;
  private ExtendedBaselineInfo baselineInfo;

  public RenderableText(final LayoutContext layoutContext,
                        final ExtendedBaselineInfo baselineInfo,
                        final Glyph[] glyphs,
                        final int offset,
                        final int length,
                        final int script,
                        final boolean forceLinebreak)
  {
    if (glyphs == null)
    {
      throw new NullPointerException();
    }
    if (layoutContext == null)
    {
      throw new NullPointerException();
    }
    if (glyphs.length < offset + length)
    {
      throw new IllegalArgumentException();
    }

    this.baselineInfo = baselineInfo;
    this.ltr = true; // this depends on the script value
    this.script = script;

    this.layoutContext = layoutContext;
    this.glyphs = glyphs;
    this.offset = offset;
    this.length = length;
    this.forceLinebreak = forceLinebreak;

    // Major axis: All child boxes are placed from left-to-right
    setMajorAxis(HORIZONTAL_AXIS);
    // Minor: The childs might be aligned on their position (shifted up or down)
    setMinorAxis(VERTICAL_AXIS);

    long wordMinChunkWidth = 0;
    long wordMinWidth = 0;
    long wordPrefWidth = 0;
    long wordMaxWidth = 0;

    long heightAbove = 0;
    long heightBelow = 0;

    final int lastPos = Math.min(glyphs.length, offset + length);
    for (int i = offset; i < lastPos; i++)
    {
      Glyph glyph = glyphs[i];
      Spacing spacing = glyph.getSpacing();
      heightAbove = Math.max(glyph.getBaseLine(), heightAbove);
      heightBelow = Math.max(glyph.getHeight() - glyph.getBaseLine(), heightBelow);
      final int kerning = glyph.getKerning();
      final int width = glyph.getWidth();
      // Log.debug ("Glyph: " + width + " - " + kerning);
      if (glyph.getBreakWeight() <= BreakOpportunityProducer.BREAK_CHAR)
      {
        // for the layouting, we avoid inner-word breaks
        // Later, when we have to force breaks, we may take inner-breaks into
        // account.
        wordMinChunkWidth += width + spacing.getMinimum() - kerning;
        wordMinWidth += width + spacing.getMinimum() - kerning;
        wordPrefWidth += width + spacing.getOptimum() - kerning;
        wordMaxWidth += width + spacing.getMaximum() - kerning;
      }
      else
      {
        wordMinChunkWidth += width + spacing.getMinimum() - kerning;
        wordMinWidth += width + spacing.getMinimum() - kerning;
        wordPrefWidth += width + spacing.getOptimum() - kerning;
        wordMaxWidth += width + spacing.getMaximum() - kerning;

        minimumChunkWidth = Math.max(minimumChunkWidth, wordMinChunkWidth);
        wordMinWidth = 0;

        // Paranoid sanity checks: The word- and linebreaks should have been
        // replaced by other definitions in the text factory.
        if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_LINE)
        {
          throw new IllegalStateException("A renderable text cannot and must " +
                  "not contain linebreaks.");
        }
      }
    }

    height = heightAbove + heightBelow;
    minimumChunkWidth = Math.max(minimumChunkWidth, wordMinChunkWidth);
    minimumWidth = Math.max(minimumWidth, wordMinWidth);
    preferredWidth = Math.max(preferredWidth, wordPrefWidth);
    maximumWidth = Math.max(maximumWidth, wordMaxWidth);

    setHeight(height);
    // Log.debug ("Text: " + minimumWidth + ", " + preferredWidth + ", " + maximumWidth);

  }

  public boolean isForceLinebreak()
  {
    return forceLinebreak;
  }

  public boolean isLtr()
  {
    return ltr;
  }

  public LayoutContext getLayoutContext()
  {
    return layoutContext;
  }

  public Glyph[] getGlyphs()
  {
    return glyphs;
  }

  public int getOffset()
  {
    return offset;
  }

  public int getLength()
  {
    return length;
  }


  /**
   * Splits the render node at the given position. This method returns an array
   * with the length of two; if the node is not splittable, the first element
   * should be empty (in the element's behavioural context) and the second
   * element should contain an independent copy of the original node.
   * <p/>
   * If the break position is ambugious, the break should appear *in front of*
   * the position - where in front-of depends on the reading direction.
   * <p/>
   * Todo: This does not yet support syllable breaks; we would have to insert
   * that special '-' glyph for that, which is too much work for now.
   *
   * @param axis     the axis on which to break
   * @param position the break position within that axis.
   * @param target   the target array that should receive the broken node. If
   *                 the target array is not null, it must have at least two
   *                 slots.
   * @return the broken nodes contained in the target array.
   */
  public RenderNode[] split(int axis, long position, RenderNode[] target)
  {
    if (target == null || target.length < 2)
    {
      target = new RenderNode[2];
    }

    if (position == 0)
    {
      target[0] = new SpacerRenderNode();
      target[1] = derive(true);
      return target;
    }

    Log.debug("Attempt to split text: " + axis + " - " + position);

    if (axis == getMinorAxis())
    {
      // not splitable. By using the invisible render box, we allow the content
      // to move into the next line ..
      Log.debug("Renderable Text is not spittable on this axis");
      target[0] = new SpacerRenderNode();
      target[1] = derive(true);
      return target;
    }

    long width = 0;
    int optimumBreakPos = 0;
    int forcedBreakPos = 0;
    boolean seekForcedBreak = true;

    final int lastPos = Math.min(glyphs.length, offset + length);
    for (int i = offset; i < lastPos; i++)
    {
      Glyph glyph = glyphs[i];
      Spacing spacing = glyph.getSpacing();

      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_NEVER)
      {
        continue;
      }
      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_LINE)
      {
        // ok, we've found a linebreak. Stop poking around and break
        return breakOnPosition(i, target);
      }
      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_WORD)
      {
        optimumBreakPos = i;
        if (seekForcedBreak)
        {
          forcedBreakPos = i;
        }
      }
      else // must be - break-char
      {
        if (seekForcedBreak)
        {
          forcedBreakPos = i;
        }
      }

      width += glyph.getWidth() + spacing.getMinimum() - glyph.getKerning();
      if (width <= position)
      {
        continue;
      }
      else
      {
        seekForcedBreak = false;
        if (optimumBreakPos == 0)
        {
          continue;
        }
      }
      break;
    }

    if (optimumBreakPos > 0)
    {
      return breakOnPosition(optimumBreakPos, target);
    }
    else
    {
      return breakOnPosition(forcedBreakPos, target);
    }
  }

  private RenderNode[] breakOnPosition(int pos, RenderNode[] target)
  {
    if (pos == glyphs.length)
    {
      // no need to break
      Log.warn("PERFORMANCE: Incredible stupid operation detected: " + pos);
      target[0] = derive(true);
      target[1] = null;
      return target;
    }

    target[0] = null;
    target[1] = null;

    // From the break position, move backward unless you come accross the first
    // non-space char. This is our end-point for the first chunk.
    for (int i = pos - 1; i >= 0; i--)
    {
      Glyph g = glyphs[i];
      if (g.getClassification() != Glyph.SPACE_CHAR)
      {
        target[0] = new RenderableText(getLayoutContext(), baselineInfo,
                glyphs, offset, i - offset + 1, script, false);
//        Log.debug("Text[0]: " + getRawText() + " " + pos + " -> " + offset + ":" + (i - offset));
        break;
      }
    }

    // From the break position, move forward unless you come accross the first
    // non-space char. This is our start-point for the second chunk.
    final int length = offset + this.length;
    for (int i = pos; i < length; i++)
    {
      Glyph g = glyphs[i];
      if (g.getClassification() != Glyph.SPACE_CHAR)
      {
        target[1] = new RenderableText(getLayoutContext(), baselineInfo,
                glyphs, i, length - i, script, isForceLinebreak());
//        Log.debug("Text[1]: " + getRawText() + " " + pos + " -> " + i + ":" + (length - i));
        break;

      }
    }

    if (target[0] == null)
    {
      Log.debug("This box is not spittable here");
      target[0] = new SpacerRenderNode();
      if (target[1] == null)
      {
        target[1] = derive(true);
        return target;
      }
    }
    return target;
  }

  /**
   * Returns the nearest break-point that occurrs before that position. If the
   * position already is a break point, return that point. If there is no break
   * opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   * (This causes the split to behave correctly; this moves all non-splittable
   * elements down to the next free area.)
   *
   * @param axis     the axis.
   * @param position the maximum position
   * @return the best break position.
   */
  public long getBestBreak(int axis, long position)
  {
    if (axis == getMinorAxis())
    {
      return 0;
    }

    long width = 0;
    long optimumWidth = 0;

    final int lastPos = Math.min(glyphs.length, offset + length);
    for (int i = offset; i < lastPos; i++)
    {
      Glyph glyph = glyphs[i];
      Spacing spacing = glyph.getSpacing();

      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_NEVER)
      {
        width += glyph.getWidth() + spacing.getMinimum() - glyph.getKerning();
        continue;
      }
      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_LINE)
      {
        // ok, we've found a linebreak. Stop poking around and break
        return width;
      }
      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_WORD)
      {
        optimumWidth = width;
      }

      width += glyph.getWidth() + spacing.getMinimum() - glyph.getKerning();
      if (width <= position)
      {
        continue;
      }
      break;
    }
    return optimumWidth;
  }

  /**
   * Returns the first break-point in that element. If there is no break
   * opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   *
   * @param axis the axis.
   * @return the first break position.
   */
  public long getFirstBreak(int axis)
  {
    if (axis == getMinorAxis())
    {
      return 0;
    }

    long width = 0;

    final int lastPos = Math.min(glyphs.length, offset + length);
    for (int i = offset; i < lastPos; i++)
    {
      Glyph glyph = glyphs[i];
      Spacing spacing = glyph.getSpacing();
      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_LINE)
      {
        // ok, we've found a linebreak. Stop poking around and break
        if (i == lastPos - 1)
        {
          // no break opportinity ...
          return 0;
        }
        return width;
      }
      if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_WORD)
      {
        if (i == lastPos - 1)
        {
          // no break opportinity ...
          return 0;
        }
        return width;
      }

      width += glyph.getWidth() + spacing.getMinimum() - glyph.getKerning();
    }
    return 0;
  }

  public long getMinimumChunkSize(int axis)
  {
    if (axis == getMinorAxis())
    {
      return height;
    }
    return minimumChunkWidth;
  }

//  public long getMinimumSize(int axis)
//  {
//    if (axis == getMinorAxis())
//    {
//      return height;
//    }
//    return minimumWidth;
//  }

  public long getPreferredSize(int axis)
  {
    if (axis == getMinorAxis())
    {
      return height;
    }
    return preferredWidth;
  }
//
//  public long getMaximumSize(int axis)
//  {
//    if (axis == getMinorAxis())
//    {
//      return height;
//    }
//    return maximumWidth;
//  }

  public void validate()
  {
    // do nothing ...
    setState(RenderNodeState.FINISHED);
    setHeight(height);
    setWidth(preferredWidth);
  }

  public BreakAfterEnum getBreakAfterAllowed(final int axis)
  {
    if (axis == getMinorAxis())
    {
      return BreakAfterEnum.BREAK_ALLOW;
    }

    if (isForceLinebreak())
    {
      return BreakAfterEnum.BREAK_ALLOW;
    }

    if (glyphs.length > 0)
    {
      final int breakWeight = glyphs[glyphs.length - 1].getBreakWeight();
      if (breakWeight != BreakOpportunityProducer.BREAK_NEVER)
      {
        return BreakAfterEnum.BREAK_ALLOW;
      }
    }
    return BreakAfterEnum.BREAK_DISALLOW;
  }

  public String getRawText()
  {
    Glyph[] gs = getGlyphs();
    int length = getLength();
    StringBuffer b = new StringBuffer();
    for (int i = getOffset(); i < length; i++)
    {
      Glyph g = gs[i];
      b.append((char) (0xffff & g.getCodepoint()));
      int[] extraCPs = g.getExtraChars();
      for (int j = 0; j < extraCPs.length; j++)
      {
        int extraCP = extraCPs[j];
        b.append(extraCP);
      }
    }
    return b.toString();
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(boolean deep)
  {
    return (RenderableText) super.derive(deep);
  }

  public int getBreakability(int axis)
  {
    if (axis == getMajorAxis())
    {
      return HARD_BREAKABLE;
    }
    return UNBREAKABLE;
  }

  /**
   * Checks, whether this node will cause breaks in its parent. While
   * 'isForcedSplitNeeded' checks, whether an element should be splitted, this
   * method checks, whether this element would be a valid reason to split.
   * <p/>
   * Text, that contains linebreaks at the end of the line, not be split by
   * itself, but will cause splits in the parent, if it is followed by some more
   * text.
   *
   * @param isEndOfLine
   * @return
   */
  public boolean isForcedSplitRequested(boolean isEndOfLine)
  {
    if (isEndOfLine)
    {
      return false;
    }
    return forceLinebreak;
  }

  public boolean isEmpty()
  {
    return length == 0;
  }

  public boolean isDiscardable()
  {
    if (forceLinebreak)
    {
      return false;
    }

    return glyphs.length == 0;
  }

  public CSSValue getVerticalAlignment()
  {
    return VerticalAlign.BASELINE;
  }

  /**
   * Returns the baseline info for the given node. This can be null, if the node
   * does not have any baseline info.
   *
   * @return
   */
  public ExtendedBaselineInfo getBaselineInfo()
  {
    return baselineInfo;
  }
}
