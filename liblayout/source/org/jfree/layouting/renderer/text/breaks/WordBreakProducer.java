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

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;

/**
 * This is the standard behaviour for HTML.It breaks texts at word boundaries.
 *
 * @author Thomas Morgner
 */
public class WordBreakProducer extends LineBreakProducer
{
  private static class WordBreakProducerState extends LineBreakProducerState
  {
    public WordBreakProducerState()
    {
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
      WordBreakProducer producer = new WordBreakProducer();
      update(producer);
      return producer;
    }
  }

  public WordBreakProducer()
  {
  }

  public int createBreakOpportunity(int codepoint)
  {
    final int breakOpportunity = super.createBreakOpportunity(codepoint);
    if (breakOpportunity != BREAK_NEVER)
    {
      return breakOpportunity;
    }

    // cheating here for now. Needs an implementation.
    if (Character.isWhitespace((char) codepoint))
    {
      return BREAK_WORD;
    }

    return BREAK_CHAR;
  }

  public State saveState() throws StateException
  {
    return new WordBreakProducerState();
  }
}
