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
 * AbstractImageElementReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ImageElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public abstract class AbstractImageElementReadHandler extends AbstractElementReadHandler
{
  private static final String SCALE_ATT = "scale";
  private static final String KEEP_ASPECT_RATIO_ATT = "keepAspectRatio";

  protected AbstractImageElementReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);
    handleScale(atts);
    handleKeepAspectRatio(atts);
  }

  protected void handleScale (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(SCALE_ATT);
    if (booleanValue != null)
    {
      final ImageElementFactory elementFactory = (ImageElementFactory) getElementFactory();
      elementFactory.setScale(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleKeepAspectRatio (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(KEEP_ASPECT_RATIO_ATT);
    if (booleanValue != null)
    {
      final ImageElementFactory elementFactory = (ImageElementFactory) getElementFactory();
      elementFactory.setKeepAspectRatio(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }
}
