/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * WorkerHandle.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: WorkerHandle.java,v 1.1 2003/10/18 20:50:38 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 18.10.2003 : Initial version
 *  
 */

package org.jfree.report.util;

/**
 * The worker handle is a control structure which allows control over the 
 * worker without exposing the thread object.
 * 
 * @author Thomas Morgner
 */
public class WorkerHandle
{
  /** The worker for this handle. */
  private Worker worker;

  /**
   * Creates a new handle for the given worker.
   * @param worker the worker.
   */
  public WorkerHandle(Worker worker)
  {
    this.worker = worker;
  }

  /**
   * Finishes the worker.
   *
   */
  public void finish()
  {
    worker.finish();
  }
}
