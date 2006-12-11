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
 * HelloWorldElementHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.demo.layoutcontroller.xml;

import org.jfree.report.demo.layoutcontroller.HelloWorldElement;
import org.jfree.report.demo.layoutcontroller.HelloWorldModule;
import org.jfree.report.modules.factories.report.flow.ElementReadHandler;
import org.jfree.report.modules.factories.report.base.NodeReadHandler;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: Dec 10, 2006, 11:02:37 PM
 *
 * @author Thomas Morgner
 */
public class HelloWorldReadHandler extends ElementReadHandler
    implements NodeReadHandler
{
  private HelloWorldElement helloWorldElement;

  public HelloWorldReadHandler()
  {
    helloWorldElement = new HelloWorldElement();
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    super.startParsing(attrs);
    final String text = attrs.getValue(HelloWorldModule.NAMESPACE, "text");
    helloWorldElement.setText(text);
  }

  protected Element getElement()
  {
    return helloWorldElement;
  }

  public Node getNode()
  {
    return getElement();
  }
}
