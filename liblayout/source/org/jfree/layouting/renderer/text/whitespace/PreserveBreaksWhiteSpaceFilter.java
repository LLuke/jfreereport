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
 * PreserveBreaksWhiteSpaceFilter.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PreserveBreaksWhiteSpaceFilter.java,v 1.4 2006/07/30 13:13:47 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.text.whitespace;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.renderer.text.ClassificationProducer;

/**
 * Creation-Date: 11.06.2006, 20:18:00
 *
 * @author Thomas Morgner
 */
public class PreserveBreaksWhiteSpaceFilter implements WhiteSpaceFilter
{
  private static class PreserveBreaksWhiteSpaceFilterState implements State
  {
    private boolean collapse;

    public PreserveBreaksWhiteSpaceFilterState()
    {
    }

    public boolean isCollapse()
    {
      return collapse;
    }

    public void setCollapse(final boolean collapse)
    {
      this.collapse = collapse;
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
            throws StateException
    {
      PreserveBreaksWhiteSpaceFilter filter = new PreserveBreaksWhiteSpaceFilter();
      filter.collapse = collapse;
      return filter;
    }
  }

  private boolean collapse;

  public PreserveBreaksWhiteSpaceFilter()
  {
  }

  /**
   * Filters the whitespaces. This method returns '-1', if the whitespace should
   * be removed from the stream; otherwise it presents a replacement character.
   * If the codepoint is no whitespace at all, the codepoint is returned
   * unchanged.
   *
   * @param codepoint
   * @return
   */
  public int filter(int codepoint)
  {
    if (isLinebreak(codepoint))
    {
      collapse = true;
      return codepoint;
    }

    if (isWhitespace(codepoint))
    {
      if (collapse == true)
      {
        return WhiteSpaceFilter.STRIP_WHITESPACE;
      }
      else
      {
        collapse = true;
        return ' ';
      }
    }

    if (codepoint == ClassificationProducer.START_OF_TEXT)
    {
      collapse = true;
      return WhiteSpaceFilter.STRIP_WHITESPACE;
    }
    else if (codepoint == ClassificationProducer.END_OF_TEXT)
    {
      return WhiteSpaceFilter.STRIP_WHITESPACE;
    }

    collapse = false;
    return codepoint;
  }

  private boolean isWhitespace(final int codepoint)
  {
    final char ch = (char) (codepoint & 0xFFFF);
    return Character.isWhitespace(ch);
  }

  protected boolean isLinebreak (int codepoint)
  {
    if (codepoint == 0xa || codepoint == 0xd)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public State saveState() throws StateException
  {
    final PreserveBreaksWhiteSpaceFilterState state =
            new PreserveBreaksWhiteSpaceFilterState();
    state.setCollapse(collapse);
    return state;
  }
}
