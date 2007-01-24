/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * DefaultSessionProviderReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.ext.modules.data.hibernate.parser;

import org.jfree.report.ext.modules.data.hibernate.DefaultSessionProvider;
import org.jfree.report.ext.modules.data.hibernate.SessionProvider;
import org.jfree.xmlns.parser.AbstractXmlReadHandler;
import org.xml.sax.SAXException;

/**
 * Creation-Date: Jan 22, 2007, 2:34:37 PM
 *
 * @author Thomas Morgner
 */
public class DefaultSessionProviderReadHandler
    extends AbstractXmlReadHandler implements SessionProviderReadHandler
{
  public DefaultSessionProviderReadHandler()
  {
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   */
  public Object getObject() throws SAXException
  {
    return getProvider();
  }

  public SessionProvider getProvider()
  {
    return new DefaultSessionProvider();
  }
}
