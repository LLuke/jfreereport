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
 * TextConfigDescriptionEntry.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TextConfigDescriptionEntry.java,v 1.4 2003/11/07 18:33:52 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

/**
 * The text config description entry represents an configuration
 * key, where users may enter free-form text. 
 * 
 * @author Thomas Morgner
 */
public class TextConfigDescriptionEntry extends ConfigDescriptionEntry
{
  /**
   * Creates a new text description entry for the given configuration
   * key.
   * 
   * @param keyName the keyname of this entry.
   */
  public TextConfigDescriptionEntry(final String keyName)
  {
    super(keyName);
  }
}
