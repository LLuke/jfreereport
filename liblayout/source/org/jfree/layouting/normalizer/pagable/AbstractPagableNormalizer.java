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
 * PagableNormalizer.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: AbstractPagableNormalizer.java,v 1.1 2006/02/12 21:43:11 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.01.2006 : Initial version
 */
package org.jfree.layouting.normalizer.pagable;

import org.jfree.layouting.normalizer.Normalizer;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.model.LayoutReplacedElement;
import org.jfree.layouting.model.PageContext;

/**
 * Creation-Date: 02.01.2006, 18:26:59
 *
 * @author Thomas Morgner
 */
public class AbstractPagableNormalizer implements Normalizer
{
  private PageContext pageContext;

  public AbstractPagableNormalizer()
  {
  }

  public void startElement(LayoutElement element)
  {

  }

  public void addText(LayoutTextNode text)
  {

  }

  public void addReplacedElement(LayoutReplacedElement element)
  {

  }

  public void endElement(final LayoutElement element)
  {

  }

  public void startDocument()
  {

  }

  public void endDocument()
  {

  }
}