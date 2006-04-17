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
 * LineStyleKeys.java
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
package org.jfree.layouting.input.style.keys.line;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Drop-Initials are left out for now.
 *
 * @author Thomas Morgner
 * @see http://www.w3.org/TR/css3-linebox/
 */
public class LineStyleKeys
{
  /**
   * The 'text-height' property determine the block-progression dimension of the
   * text content area of an inline box.
   */
  public static final StyleKey TEXT_HEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-height", false, true, false);

  /**
   * Either one of the constants 'normal' or 'none' or a length, percentage or
   * number. The computed value is a result of a computation based on the
   * current line-box and therefore is only valid for that particluar linebox.
   */
  public static final StyleKey LINE_HEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-height", false, true, false);

  /**
   * This property determines the line stacking strategy for stacked line boxes
   * within a containing block element. The term 'stack-height' is used in the
   * context of this property description to indicate the block-progression
   * advance for the line boxes.
   */
  public static final StyleKey LINE_STACKING_STRATEGY =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-stacking-strategy",false, true, false);

  /**
   * Ruby is not implemented.
   */
  public static final StyleKey LINE_STACKING_RUBY =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-stacking-ruby",false, true, false);

  /**
   * This is a character level computation, we ignore that for now.
   */
  public static final StyleKey LINE_STACKING_SHIFT =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-stacking-shift",false, true, false);

  public static final StyleKey BASELINE_SHIFT =
          StyleKeyRegistry.getRegistry().createKey
                  ("baseline-shift",false, true, false);


  /**
   * This is a shorthand property for the 'dominant-baseline', 'alignment-baseline'
   * and 'alignment-adjust' properties. It has a different meaning in the context
   * of table cells.
   */
  public static final StyleKey VERTICAL_ALIGN =
          StyleKeyRegistry.getRegistry().createKey
                  ("vertical-align",false, true, false);

  public static final StyleKey INLINE_BOX_ALIGN =
          StyleKeyRegistry.getRegistry().createKey
                  ("inline-box-align",false, false, false);

  /**
   * DominantBaseLine is not implemented.
   */
  public static final StyleKey DOMINANT_BASELINE =
          StyleKeyRegistry.getRegistry().createKey
                  ("dominant-baseline",false, true, false);
  public static final StyleKey ALIGNMENT_BASELINE =
          StyleKeyRegistry.getRegistry().createKey
                  ("alignment-baseline", false, true, false);
  public static final StyleKey ALIGNMENT_ADJUST =
          StyleKeyRegistry.getRegistry().createKey
                  ("alignment-adjust",false, true, false);

  public static final StyleKey DROP_INITIAL_AFTER_ADJUST =
          StyleKeyRegistry.getRegistry().createKey
                  ("drop-initial-after-adjust",false, false, false);
  public static final StyleKey DROP_INITIAL_AFTER_ALIGN =
          StyleKeyRegistry.getRegistry().createKey
                  ("drop-initial-after-align",false, false, false);

  public static final StyleKey DROP_INITIAL_BEFORE_ADJUST =
          StyleKeyRegistry.getRegistry().createKey
                  ("drop-initial-before-adjust",false, false, false);
  public static final StyleKey DROP_INITIAL_BEFORE_ALIGN =
          StyleKeyRegistry.getRegistry().createKey
                  ("drop-initial-before-align",false, false, false);

  public static final StyleKey DROP_INITIAL_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("drop-initial-size",false, false, false);
  public static final StyleKey DROP_INITIAL_VALUE =
          StyleKeyRegistry.getRegistry().createKey
                  ("drop-initial-value",false, false, false);

  private LineStyleKeys()
  {
  }
}
