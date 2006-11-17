/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting;

import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;

/**
 * Creation-Date: 10.11.2006, 15:16:40
 *
 * @author Thomas Morgner
 */
public class DefaultLayoutProcess extends AbstractLayoutProcess
{
  protected static class DefaultLayoutProcessState extends AbstractLayoutProcess.AbstractLayoutProcessState
  {
    public DefaultLayoutProcessState()
    {
    }

    protected AbstractLayoutProcess create(OutputProcessor outputProcessor)
        throws StateException
    {
      return new DefaultLayoutProcess(outputProcessor);
    }
  }

  public DefaultLayoutProcess(OutputProcessor outputProcessor)
  {
    super(outputProcessor);
  }

  protected InputFeed createInputFeed()
  {
    return new DefaultInputFeed(this);
  }

  protected AbstractLayoutProcess.AbstractLayoutProcessState createState() throws StateException
  {
    final DefaultLayoutProcessState state = new DefaultLayoutProcessState();
    fillState(state);
    return state;
  }
}