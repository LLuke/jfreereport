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
 * DOMWriter.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DOMWriter.java,v 1.4 2003/09/10 18:20:24 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config.xml;

import org.jfree.xml.writer.SafeTagList;
import org.jfree.xml.writer.XMLWriterSupport;

/**
 * A XML-Writer utility class, that helps when writing an XML document
 * from an arbitary data source.
 *  
 * @author Thomas Morgner
 */
public final class DOMWriter extends XMLWriterSupport
{
  /**
   * The safeTag list marks all tags which can be followed by an linebreak.
   * 
   * @return the safetag list for this document type.
   */
  private static SafeTagList createSafeTagList()
  {
    final SafeTagList list = new SafeTagList();
    list.add("config-description");
    list.add("key");
    list.add("class");
    list.add("enum");
    list.add("description", false, true);
    list.add("text", false, true);
    return list;
  }

  /**
   * Hidden default constructor. Initializes the writer with the internal
   * safetag list.
   */
  private DOMWriter()
  {
    super(createSafeTagList(), 0);
  }

  /** The singleton instance of this writer. */  
  private static DOMWriter singleton;
  
  /** 
   * Returns the singleton instance of this DOM writer.
   * 
   * @return the instance.
   */
  public static DOMWriter getInstance()
  {
    if (singleton == null)
    {
      singleton = new DOMWriter();
    }
    return singleton;
  }
}
