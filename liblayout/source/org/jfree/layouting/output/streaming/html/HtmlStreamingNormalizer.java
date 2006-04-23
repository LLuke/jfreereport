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
 * HtmlStreamingNormalizer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: HtmlStreamingNormalizer.java,v 1.2 2006/04/17 20:51:20 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.streaming.html;

import java.io.OutputStream;
import java.io.PrintStream;

import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.normalizer.streaming.StreamingNormalizer;
import org.jfree.layouting.util.AttributeMap;

/**
 * Creation-Date: 02.01.2006, 19:52:34
 *
 * @author Thomas Morgner
 */
public class HtmlStreamingNormalizer implements StreamingNormalizer
{
  private PrintStream outstream;

  public HtmlStreamingNormalizer(final OutputStream outstream)
  {
    this.outstream = new PrintStream(outstream);
  }

  public void startDocument()
  {
    outstream.println("<!-- doctype here -->");
  }

  public void startElement(LayoutElement element)
  {
    if (element.getName() == null)
    {
      // this is an anonymous/generated element. Ignore it.
      outstream.println("<!-- Annonymous element -->");
      return;
    }

    outstream.print("<");
    outstream.print(element.getNamespace() + ":" + element.getName());
    final AttributeMap attributeMap = element.getAttributes();
    final String[] attrNamespaces = attributeMap.getNameSpaces();

    for (int i = 0; i < attrNamespaces.length; i++)
    {
      final String attrNamespace = attrNamespaces[i];
      // todo: Hey, we need access to all defined namespaces and their shortcuts
      //
    }
    outstream.print(">");
  }

  public void addText(LayoutTextNode text)
  {
    final String s = new String(text.getData(), text.getOffset(), text.getLength());
    outstream.print(s);
    text.clearFromParent();
  }

  public void addReplacedElement(LayoutElement element)
  {
    // we ignore that one, as all information leading to that element should
    // be included in the 'startElement' call already.
    outstream.println("<replaced:element:" + element + "/>");
  }

  public void endElement(final LayoutElement element)
  {
    if (element.getName() == null)
    {
      // this is an anonymous/generated element. Ignore it.
      element.clearFromParent();
      return;
    }

    outstream.print("</");
    outstream.print(element.getNamespace() + ":" + element.getName());
    outstream.println(">");

    element.clearFromParent();

  }

  public void endDocument()
  {
    outstream.println("<!-- Finito -->");
    outstream.flush();
  }
}
