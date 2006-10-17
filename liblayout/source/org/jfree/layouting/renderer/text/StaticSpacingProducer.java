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
 * NoSpacingProducer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StaticSpacingProducer.java,v 1.1 2006/07/11 13:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.text;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;

/**
 * Creation-Date: 11.06.2006, 18:37:39
 *
 * @author Thomas Morgner
 */
public class StaticSpacingProducer implements SpacingProducer
{
  private static class StaticSpacingProducerState implements State
  {
    private Spacing spacing;

    public StaticSpacingProducerState()
    {
    }

    public Spacing getSpacing()
    {
      return spacing;
    }

    public void setSpacing(final Spacing spacing)
    {
      this.spacing = spacing;
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
      return new StaticSpacingProducer(spacing);
    }
  }

  private Spacing spacing;

  public StaticSpacingProducer(final Spacing spacing)
  {
    if (spacing == null)
    {
      this.spacing = Spacing.EMPTY_SPACING;
    }
    else
    {
      this.spacing = spacing;
    }
  }

  public Spacing createSpacing(int codePoint)
  {
    return spacing;
  }

  public State saveState() throws StateException
  {
    StaticSpacingProducerState state = new StaticSpacingProducerState();
    state.setSpacing(spacing);
    return state;
  }
}
