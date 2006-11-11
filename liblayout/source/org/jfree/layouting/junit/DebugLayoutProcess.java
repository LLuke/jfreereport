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
 * DebugLayoutProcess.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DebugLayoutProcess.java,v 1.1 2006/07/11 13:51:01 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.junit;

import org.jfree.layouting.AbstractLayoutProcess;
import org.jfree.layouting.StateException;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.output.OutputProcessor;

/**
 * Dont use that class in the real world. Its a debugging support class.
 *
 * @author Thomas Morgner
 * @deprecated 
 */
public class DebugLayoutProcess extends AbstractLayoutProcess
{
  protected static class DebugLayoutProcessState extends AbstractLayoutProcessState
  {
    public DebugLayoutProcessState()
    {
    }

    protected AbstractLayoutProcess create(OutputProcessor outputProcessor)
        throws StateException
    {
      return new DebugLayoutProcess(outputProcessor);
    }
  }

  public DebugLayoutProcess(OutputProcessor outputProcessor)
  {
    super(outputProcessor);
  }

  protected InputFeed createInputFeed()
  {
    return new DefaultInputFeed(this);
  }

  protected AbstractLayoutProcessState createState() throws StateException
  {
    final DebugLayoutProcessState state = new DebugLayoutProcessState();
    fillState(state);
    return state;
  }
}
