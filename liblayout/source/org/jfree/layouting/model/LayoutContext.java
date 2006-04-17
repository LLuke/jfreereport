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
 * LayoutContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.model;

import org.jfree.layouting.model.border.BackgroundSpecification;
import org.jfree.layouting.model.border.BorderSpecification;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.model.content.ContentSpecification;
import org.jfree.layouting.model.lists.ListSpecification;
import org.jfree.layouting.model.position.PositionSpecification;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.model.box.ReplacedElementSpecification;
import org.jfree.layouting.model.line.LineSpecification;

/**
 * This is where the computed style goes into.
 *
 * @author Thomas Morgner
 */
public interface LayoutContext
{
  public BackgroundSpecification getBackgroundSpecification();
  public BorderSpecification getBorderSpecification();
  public FontSpecification getFontSpecification();
  public TextSpecification getTextSpecification();
  public ContentSpecification getContentSpecification();

  public ListSpecification getListSpecification();
  public PositionSpecification getPositionSpecification();

  public BoxSpecification getBoxSpecification ();
  public ReplacedElementSpecification getReplacedElementSpecification();

  public LineSpecification getLineSpecification();
}
