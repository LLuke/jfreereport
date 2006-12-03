/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text.breaks;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;

/**
 * This produces linebreaks when a CR/LF is encountered. This corresponds to
 * the expected behaviour of HTML-pre elements.
 *
 * @author Thomas Morgner
 */
public class LineBreakProducer implements BreakOpportunityProducer
{
  protected static class LineBreakProducerState implements State
  {
    private int lastCodePoint;

    public LineBreakProducerState()
    {
    }

    public int getLastCodePoint()
    {
      return lastCodePoint;
    }

    public void setLastCodePoint(final int lastCodePoint)
    {
      this.lastCodePoint = lastCodePoint;
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
      LineBreakProducer pr = new LineBreakProducer();
      update(pr);
      return pr;
    }

    protected void update (LineBreakProducer producer)
    {
      producer.lastCodePoint = lastCodePoint;
    }
  }

  private int lastCodePoint;

  public LineBreakProducer()
  {
  }

  /** Signals the start of text. Resets the state to the initial values. */
  public void startText()
  {
    lastCodePoint = 0;
  }

  public State saveState() throws StateException
  {
    LineBreakProducerState state = new LineBreakProducerState();
    state.setLastCodePoint(lastCodePoint);
    return state;
  }

  public int createBreakOpportunity(int codepoint)
  {
    if (codepoint == START_OF_TEXT)
    {
      lastCodePoint = 0;
    }

    // well, that's not fully correct, we should detect single \r as well.
    if (codepoint == '\n')
    {
      return BREAK_LINE;
    }
    return BREAK_NEVER;
  }
}
