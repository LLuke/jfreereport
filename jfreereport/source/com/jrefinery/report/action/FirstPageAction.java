/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * --------------------
 * FirstPageAction.java
 * --------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Joerg Schoemer;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *                   Thomas Morgner;
 *
 * $Id$
 *
 * Changes
 * -------
 * 29-May-2002 : Initial version
 * 05-Jun-2002 : These comments and documentation
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 *
 */
package com.jrefinery.report.action;

import java.util.ResourceBundle;

import com.jrefinery.report.util.AbstractActionDowngrade;
import com.jrefinery.report.util.ActionDowngrade;

/**
 * Creates a new FirstPageAction used by the PreviewPane to directly jump to the first
 * page of the report. This abstract class is used for initializing the default locales,
 * the actual work is done in an internal subclass in PreviewFrame.
 *
 * @author JS
 */
public abstract class FirstPageAction extends AbstractActionDowngrade
{

  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  protected FirstPageAction (ResourceBundle resources)
  {
    putValue (NAME, resources.getString ("action.firstpage.name"));
    putValue (SHORT_DESCRIPTION, resources.getString ("action.firstpage.description"));
    putValue (ActionDowngrade.MNEMONIC_KEY, 
              resources.getObject ("action.firstpage.mnemonic"));
    putValue (ActionDowngrade.ACCELERATOR_KEY, 
              resources.getObject ("action.firstpage.accelerator"));
    putValue (SMALL_ICON, resources.getObject ("action.firstpage.small-icon"));
    putValue ("ICON24", resources.getObject ("action.firstpage.icon"));
  }
}
