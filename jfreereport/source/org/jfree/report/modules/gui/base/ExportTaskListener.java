/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ExportTaskListener.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ExportTaskListener.java,v 1.3 2004/05/07 14:29:21 mungady Exp $
 *
 * Changes 
 * -------------------------
 * 18.10.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.base;

import java.util.EventListener;

/**
 * An export task listener will be informed about the state of an exporttask.
 *
 * @author Thomas Morgner
 */
public interface ExportTaskListener extends EventListener
{
  /**
   * Informs the listener, that the export was completed without errors.
   *
   * @param task the export task which was completed.
   */
  public void taskDone (ExportTask task);

  /**
   * Informs the listener, that the export was aborted by the user.
   *
   * @param task the export task which was aborted.
   */
  public void taskAborted (ExportTask task);

  /**
   * Informs the listener, that the export failed due to errors.
   *
   * @param task the export task which failed.
   */
  public void taskFailed (ExportTask task);

  /**
   * Informs the listener, that the export will be processed later as there is no worker
   * available. The listener has now the option to abort the task. (TODO: This is not yet
   * implemented).
   *
   * @param task the export task which was completed.
   */
  public void taskWaiting (ExportTask task);
}
