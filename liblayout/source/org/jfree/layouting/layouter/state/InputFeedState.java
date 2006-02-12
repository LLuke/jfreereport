/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * InputFeedState.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: InputFeedState.java,v 1.1 2006/02/12 21:49:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.state;

import org.jfree.layouting.model.LayoutElement;

/**
 * Creation-Date: 05.12.2005, 18:15:48
 *
 * @author Thomas Morgner
 */
public class InputFeedState implements InputSavePoint
{
  // these elements are the contents of the stack
  private LayoutElement[] openElements;
  private int state;
  private long currentId;
  private Object clientData;

  public InputFeedState(final LayoutElement[] openElements,
                        final int state,
                        final long currentId,
                        final Object clientData)
  {
    this.clientData = clientData;
    this.openElements = openElements;
    this.state = state;
    this.currentId = currentId;
  }

  public LayoutElement[] getOpenElements()
  {
    return openElements;
  }

  public int getState()
  {
    return state;
  }

  public long getCurrentId()
  {
    return currentId;
  }

  public Object getClientData()
  {
    return clientData;
  }
}
