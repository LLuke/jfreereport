/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 *
 */
package com.jrefinery.report.ext.barcode;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.text.CharacterIterator;

import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.util.Log;

public class DefaultFontMetrics extends FontMetrics
{
  private FontMetrics backend;

  public DefaultFontMetrics(final Font font)
  {
    super(font);
    backend = G2OutputTarget.createEmptyGraphics().getFontMetrics(font);
  }

  /**
   * Gets the <code>Font</code> described by this
   * <code>FontMetrics</code> object.
   * @return    the <code>Font</code> described by this
   * <code>FontMetrics</code> object.
   */
  public Font getFont()
  {
    return backend.getFont();
  }

  /**
   * Determines the <em>standard leading</em> of the
   * <code>Font</code> described by this <code>FontMetrics</code>
   * object.  The standard leading, or
   * interline spacing, is the logical amount of space to be reserved
   * between the descent of one line of text and the ascent of the next
   * line. The height metric is calculated to include this extra space.
   * @return    the standard leading of the <code>Font</code>.
   * @see   #getHeight()
   * @see   #getAscent()
   * @see   #getDescent()
   */
  public int getLeading()
  {
    return backend.getLeading();
  }

  /**
   * Determines the <em>font ascent</em> of the <code>Font</code>
   * described by this <code>FontMetrics</code> object. The font ascent
   * is the distance from the font's baseline to the top of most
   * alphanumeric characters. Some characters in the <code>Font</code>
   * might extend above the font ascent line.
   * @return     the font ascent of the <code>Font</code>.
   * @see        #getMaxAscent()
   */
  public int getAscent()
  {
    return backend.getAscent();
  }

  /**
   * Determines the <em>font descent</em> of the <code>Font</code>
   * described by this
   * <code>FontMetrics</code> object. The font descent is the distance
   * from the font's baseline to the bottom of most alphanumeric
   * characters with descenders. Some characters in the
   * <code>Font</code> might extend
   * below the font descent line.
   * @return     the font descent of the <code>Font</code>.
   * @see        #getMaxDescent()
   */
  public int getDescent()
  {
    return backend.getDescent();
  }

  /**
   * Gets the standard height of a line of text in this font.  This
   * is the distance between the baseline of adjacent lines of text.
   * It is the sum of the leading + ascent + descent.  There is no
   * guarantee that lines of text spaced at this distance are
   * disjoint; such lines may overlap if some characters overshoot
   * either the standard ascent or the standard descent metric.
   * @return    the standard height of the font.
   * @see       #getLeading()
   * @see       #getAscent()
   * @see       #getDescent()
   */
  public int getHeight()
  {
    return backend.getHeight();
  }

  /**
   * Determines the maximum ascent of the <code>Font</code>
   * described by this <code>FontMetrics</code> object.  No character
   * extends further above the font's baseline than this height.
   * @return    the maximum ascent of any character in the
   * <code>Font</code>.
   * @see       #getAscent()
   */
  public int getMaxAscent()
  {
    return backend.getMaxAscent();
  }

  /**
   * Determines the maximum descent of the <code>Font</code>
   * described by this <code>FontMetrics</code> object.  No character
   * extends further below the font's baseline than this height.
   * @return    the maximum descent of any character in the
   * <code>Font</code>.
   * @see       #getDescent()
   */
  public int getMaxDescent()
  {
    return backend.getMaxDescent();
  }

  /**
   * For backward compatibility only.
   * @see #getMaxDescent()
   * @deprecated As of JDK version 1.1.1,
   * replaced by <code>getMaxDescent()</code>.
   */
  public int getMaxDecent()
  {
    return backend.getMaxDecent();
  }

  /**
   * Gets the maximum advance width of any character in this
   * <code>Font</code>.  The advance is the
   * distance from the leftmost point to the rightmost point on the
   * string's baseline.  The advance of a <code>String</code> is
   * not necessarily the sum of the advances of its characters.
   * @return    the maximum advance width of any character
   *            in the <code>Font</code>, or <code>-1</code> if the
   *            maximum advance width is not known.
   */
  public int getMaxAdvance()
  {
    return backend.getMaxAdvance();
  }

  /**
   * Returns the advance width of the specified character in this
   * <code>Font</code>.  The advance is the
   * distance from the leftmost point to the rightmost point on the
   * character's baseline.  Note that the advance of a
   * <code>String</code> is not necessarily the sum of the advances
   * of its characters.
   * @param ch the character to be measured
   * @return    the advance width of the specified <code>char</code>
   *                 in the <code>Font</code> described by this
   *			<code>FontMetrics</code> object.
   * @see       #charsWidth(char[], int, int)
   * @see       #stringWidth(java.lang.String)
   */
  public int charWidth(final int ch)
  {
    return backend.charWidth(ch);
  }

  /**
   * Returns the advance width of the specified character in this
   * <code>Font</code>.  The advance is the
   * distance from the leftmost point to the rightmost point on the
   * character's baseline.  Note that the advance of a
   * <code>String</code> is not necessarily the sum of the advances
   * of its characters.
   * @return     the advance width of the specified <code>char</code>
   *                  in the <code>Font</code> described by this
   *			<code>FontMetrics</code> object.
   * @see        #charsWidth(char[], int, int)
   * @see        #stringWidth(java.lang.String)
   */
  public int charWidth(final char ch)
  {
    return backend.charWidth(ch);
  }

  /**
   * Returns the total advance width for showing the specified
   * <code>String</code> in this <code>Font</code>.  The advance
   * is the distance from the leftmost point to the rightmost point
   * on the string's baseline.
   * <p>
   * Note that the total advance width returned from this method
   * does not take into account the rendering context.  Therefore,
   * the anti-aliasing and fractional metrics hints can affect the
   * value of the advance.  When enabling the anti-aliasing and
   * fractional metrics hints, use
   * <code>getStringBounds(String, Graphics)</code>
   * instead of this method.  The advance of a <code>String</code> is
   * not necessarily the sum of the advances of its characters.
   * <p>
   * @param str the <code>String</code> to be measured
   * @return    the advance width of the specified <code>String</code>
   *                  in the <code>Font</code> described by this
   *			<code>FontMetrics</code>.
   * @see       #bytesWidth(byte[], int, int)
   * @see       #charsWidth(char[], int, int)
   * @see       #getStringBounds(java.lang.String, java.awt.Graphics)
   */
  public int stringWidth(final String str)
  {
    return backend.stringWidth(str);
  }

  /**
   * Returns the total advance width for showing the specified array
   * of characters in this <code>Font</code>.  The advance is the
   * distance from the leftmost point to the rightmost point on the
   * string's baseline.  The advance of a <code>String</code>
   * is not necessarily the sum of the advances of its characters.
   * This is equivalent to measuring a <code>String</code> of the
   * characters in the specified range.
   * @param data the array of characters to be measured
   * @param off the start offset of the characters in the array
   * @param len the number of characters to be measured from the array
   * @return    the advance width of the subarray of the specified
   *               <code>char</code> array in the font described by
   *               this <code>FontMetrics</code> object.
   * @see       #charWidth(int)
   * @see       #charWidth(char)
   * @see       #bytesWidth(byte[], int, int)
   * @see       #stringWidth(java.lang.String)
   */
  public int charsWidth(final char[] data, final int off, final int len)
  {
    return backend.charsWidth(data, off, len);
  }

  /**
   * Returns the total advance width for showing the specified array
   * of bytes in this <code>Font</code>.  The advance is the
   * distance from the leftmost point to the rightmost point on the
   * string's baseline.  The advance of a <code>String</code>
   * is not necessarily the sum of the advances of its characters.
   * This is equivalent to measuring a <code>String</code> of the
   * characters in the specified range.
   * @param data the array of bytes to be measured
   * @param off the start offset of the bytes in the array
   * @param len the number of bytes to be measured from the array
   * @return    the advance width of the subarray of the specified
   *               <code>byte</code> array in the <code>Font</code>
   *			described by
   *               this <code>FontMetrics</code> object.
   * @see       #charsWidth(char[], int, int)
   * @see       #stringWidth(java.lang.String)
   */
  public int bytesWidth(final byte[] data, final int off, final int len)
  {
    return backend.bytesWidth(data, off, len);
  }

  /**
   * Gets the advance widths of the first 256 characters in the
   * <code>Font</code>.  The advance is the
   * distance from the leftmost point to the rightmost point on the
   * character's baseline.  Note that the advance of a
   * <code>String</code> is not necessarily the sum of the advances
   * of its characters.
   * @return    an array storing the advance widths of the
   *                 characters in the <code>Font</code>
   *                 described by this <code>FontMetrics</code> object.
   */
  public int[] getWidths()
  {
    return backend.getWidths();
  }

  /**
   * Checks to see if the <code>Font</code> has uniform line metrics.  A
   * composite font may consist of several different fonts to cover
   * various character sets.  In such cases, the
   * <code>FontLineMetrics</code> objects are not uniform.
   * Different fonts may have a different ascent, descent, metrics and
   * so on.  This information is sometimes necessary for line
   * measuring and line breaking.
   * @return <code>true</code> if the font has uniform line metrics;
   * <code>false</code> otherwise.
   * @see java.awt.Font#hasUniformLineMetrics()
   */
  public boolean hasUniformLineMetrics()
  {
    return backend.hasUniformLineMetrics();
  }

  /**
   * Returns the {@link java.awt.font.LineMetrics} object for the specified
   * <code>String</code> in the specified {@link java.awt.Graphics} context.
   * @param str the specified <code>String</code>
   * @param context the specified <code>Graphics</code> context
   * @return a <code>LineMetrics</code> object created with the
   * specified <code>String</code> and <code>Graphics</code> context.
   * @see java.awt.Font#getLineMetrics(java.lang.String, java.awt.font.FontRenderContext)
   */
  public LineMetrics getLineMetrics(final String str, final Graphics context)
  {
    return backend.getLineMetrics(str, context);
  }

  /**
   * Returns the {@link java.awt.font.LineMetrics} object for the specified
   * <code>String</code> in the specified {@link java.awt.Graphics} context.
   * @param str the specified <code>String</code>
   * @param beginIndex the initial offset of <code>str</code>
   * @param limit the length of <code>str</code>
   * @param context the specified <code>Graphics</code> context
   * @return a <code>LineMetrics</code> object created with the
   * specified <code>String</code> and <code>Graphics</code> context.
   * @see java.awt.Font#getLineMetrics(java.lang.String, int, int, java.awt.font.FontRenderContext)
   */
  public LineMetrics getLineMetrics(final String str,
                                    final int beginIndex, final int limit,
                                    final Graphics context)
  {
    return backend.getLineMetrics(str, beginIndex, limit, context);
  }

  /**
   * Returns the {@link java.awt.font.LineMetrics} object for the specified
   * character array in the specified {@link java.awt.Graphics} context.
   * @param chars the specified character array
   * @param beginIndex the initial offset of <code>chars</code>
   * @param limit the length of <code>chars</code>
   * @param context the specified <code>Graphics</code> context
   * @return a <code>LineMetrics</code> object created with the
   * specified character array and <code>Graphics</code> context.
   * @see java.awt.Font#getLineMetrics(char[], int, int, java.awt.font.FontRenderContext)
   */
  public LineMetrics getLineMetrics(final char[] chars,
                                    final int beginIndex, final int limit,
                                    final Graphics context)
  {
    return backend.getLineMetrics(chars, beginIndex, limit, context);
  }

  /**
   * Returns the {@link java.awt.font.LineMetrics} object for the specified
   * {@link java.text.CharacterIterator} in the specified {@link java.awt.Graphics}
   * context.
   * @param ci the specified <code>CharacterIterator</code>
   * @param beginIndex the initial offset in <code>ci</code>
   * @param limit the end index of <code>ci</code>
   * @param context the specified <code>Graphics</code> context
   * @return a <code>LineMetrics</code> object created with the
   * specified arguments.
   * @see java.awt.Font#getLineMetrics(java.text.CharacterIterator, int, int, java.awt.font.FontRenderContext)
   */
  public LineMetrics getLineMetrics(final CharacterIterator ci,
                                    final int beginIndex, final int limit,
                                    final Graphics context)
  {
    return backend.getLineMetrics(ci, beginIndex, limit, context);
  }

  /**
   * Returns the bounds of the specified <code>String</code> in the
   * specified <code>Graphics</code> context.  The bounds is used
   * to layout the <code>String</code>.
   * @param str the specified <code>String</code>
   * @param context the specified <code>Graphics</code> context
   * @return a {@link java.awt.geom.Rectangle2D} that is the bounding box of the
   * specified <code>String</code> in the specified
   * <code>Graphics</code> context.
   * @see java.awt.Font#getStringBounds(java.lang.String, java.awt.font.FontRenderContext)
   */
  public Rectangle2D getStringBounds(final String str, final Graphics context)
  {
    return backend.getStringBounds(str, context);
  }

  /**
   * Returns the bounds of the specified <code>String</code> in the
   * specified <code>Graphics</code> context.  The bounds is used
   * to layout the <code>String</code>.
   * @param str the specified <code>String</code>
   * @param beginIndex the offset of the beginning of <code>str</code>
   * @param limit the length of <code>str</code>
   * @param context the specified <code>Graphics</code> context
   * @return a <code>Rectangle2D</code> that is the bounding box of the
   * specified <code>String</code> in the specified
   * <code>Graphics</code> context.
   * @see java.awt.Font#getStringBounds(java.lang.String, int, int, java.awt.font.FontRenderContext)
   */
  public Rectangle2D getStringBounds(final String str,
                                     final int beginIndex, final int limit,
                                     final Graphics context)
  {
    return backend.getStringBounds(str, beginIndex, limit, context);
  }

  /**
   * Returns the bounds of the specified array of characters
   * in the specified <code>Graphics</code> context.
   * The bounds is used to layout the <code>String</code>
   * created with the specified array of characters,
   * <code>beginIndex</code> and <code>limit</code>.
   * @param chars an array of characters
   * @param beginIndex the initial offset of the array of
   * characters
   * @param limit the length of the array of characters
   * @param context the specified <code>Graphics</code> context
   * @return a <code>Rectangle2D</code> that is the bounding box of the
   * specified character array in the specified
   * <code>Graphics</code> context.
   * @see java.awt.Font#getStringBounds(char[], int, int, java.awt.font.FontRenderContext)
   */
  public Rectangle2D getStringBounds(final char[] chars,
                                     final int beginIndex, final int limit,
                                     final Graphics context)
  {
    return backend.getStringBounds(chars, beginIndex, limit, context);
  }

  /**
   * Returns the bounds of the characters indexed in the specified
   * <code>CharacterIterator</code> in the
   * specified <code>Graphics</code> context.
   * @param ci the specified <code>CharacterIterator</code>
   * @param beginIndex the initial offset in <code>ci</code>
   * @param limit the end index of <code>ci</code>
   * @param context the specified <code>Graphics</code> context
   * @return a <code>Rectangle2D</code> that is the bounding box of the
   * characters indexed in the specified <code>CharacterIterator</code>
   * in the specified <code>Graphics</code> context.
   * @see java.awt.Font#getStringBounds(java.text.CharacterIterator, int, int, java.awt.font.FontRenderContext)
   */
  public Rectangle2D getStringBounds(final CharacterIterator ci,
                                     final int beginIndex, final int limit,
                                     final Graphics context)
  {
    return backend.getStringBounds(ci, beginIndex, limit, context);
  }

  /**
   * Returns the bounds for the character with the maximum bounds
   * in the specified <code>Graphics</code> context.
   * @param context the specified <code>Graphics</code> context
   * @return a <code>Rectangle2D</code> that is the
   * bounding box for the character with the maximum bounds.
   * @see java.awt.Font#getMaxCharBounds(java.awt.font.FontRenderContext)
   */
  public Rectangle2D getMaxCharBounds(final Graphics context)
  {
    return backend.getMaxCharBounds(context);
  }

  public static void main (final String [] args)
  {
    final FontMetrics fm = new DefaultFontMetrics(new Font ("Serif", Font.PLAIN, 10));
    Log.debug ("MaxAdvance: " + fm.getMaxAdvance());
  }
}
