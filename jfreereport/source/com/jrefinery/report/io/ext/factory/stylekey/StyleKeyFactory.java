/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * --------------------
 * StyleKeyFactory.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleKeyFactory.java,v 1.10 2003/06/04 21:09:08 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.stylekey;

import java.util.Iterator;

import com.jrefinery.report.targets.style.StyleKey;
import org.jfree.xml.factory.objects.ClassFactory;

/**
 * A style key factory.
 * 
 * @author Thomas Morgner
 */
public interface StyleKeyFactory
{
  /** 
   * Returns a style key.
   * 
   * @param name  the name.
   * 
   * @return The style key.
   */
  public StyleKey getStyleKey (String name);

  /**
   * Creates an object.
   * 
   * @param k  the style key.
   * @param value  the value.
   * @param c  the class.
   * @param cf the class factory used to create the basic object.
   *
   * @return The object.
   */
  public Object createBasicObject(StyleKey k, String value, Class c, ClassFactory cf);

  /**
   * Returns an iterator that provides access to the registered keys.
   * 
   * @return The iterator.
   */
  public Iterator getRegisteredKeys();
}
