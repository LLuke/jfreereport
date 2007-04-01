/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text.breaks;

import org.jfree.layouting.renderer.text.ClassificationProducer;

/**
 * Checks for break-opportunities. The break opportunity is always a break after
 * the last codepoint, under the condition, that this codepoint does not belong
 * to a grapheme cluster.
 * <p/>
 * This means, if we test the sequence 'ab', we cannot be sure that the letter
 * 'a' is breakable, unless we've seen 'b' and have verified that 'b' is no
 * extension or formatting character.
 * <p/>
 * To use this producer properly, make sure that no extension characters get
 * fed into it.
 *
 * @author Thomas Morgner
 */
public interface BreakOpportunityProducer extends ClassificationProducer
{
  /** Never do any breaking. */
  public static final int BREAK_NEVER = 0;
  /** Breaks allowed, it is an generic position. */
  public static final int BREAK_CHAR = 1;
  /** Break allowed, this is after a syllable is complete. */
  public static final int BREAK_SYLLABLE = 2;
  /**
   * Break allowed, this is after a word is complete or a whitespace has been
   * encountered.
   */
  public static final int BREAK_WORD = 3;
  /**
   * Break allowed, this is after a line is complete or a forced linebreak has
   * been encountered.
   */
  public static final int BREAK_LINE = 4;

  public int createBreakOpportunity(int codepoint);
}
