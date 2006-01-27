/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ResourceLabelReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ResourceLabelReadHandler.java,v 1.5 2005/05/20 16:06:45 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ResourceMessageElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.PropertyStringReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ResourceMessageReadHandler extends AbstractTextElementReadHandler
{
  private PropertyStringReadHandler stringReadHandler;
  private ResourceMessageElementFactory elementFactory;

  public ResourceMessageReadHandler ()
  {
    elementFactory = new ResourceMessageElementFactory();
    stringReadHandler = new PropertyStringReadHandler(null);
  }

  protected TextElementFactory getTextElementFactory ()
  {
    return elementFactory;
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);
    elementFactory.setResourceBase(atts.getValue("resource-base"));
    elementFactory.setNullString(atts.getValue("nullstring"));
    getRootHandler().delegate(stringReadHandler, getTagName(), atts);
  }



  /**
   * Done parsing.
   *
   * @throws SAXException if there is a parsing error.
   * @throws XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    elementFactory.setFormatKey(stringReadHandler.getResult());
    super.doneParsing();
  }
}
