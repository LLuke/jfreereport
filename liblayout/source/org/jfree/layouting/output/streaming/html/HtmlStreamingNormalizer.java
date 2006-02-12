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
 * HtmlStreamingNormalizer.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: HtmlStreamingNormalizer.java,v 1.1 2006/02/12 21:40:17 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.01.2006 : Initial version
 */
package org.jfree.layouting.output.streaming.html;

import java.io.OutputStream;
import java.io.PrintStream;

import org.jfree.layouting.normalizer.streaming.StreamingNormalizer;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.model.LayoutReplacedElement;

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
      return;
    }

    outstream.print("<");
    outstream.print(element.getName());
    final String[] attrNames = element.getAttributeNames();
    for (int i = 0; i < attrNames.length; i++)
    {
      String attrName = attrNames[i];
      Object attrValue = element.getAttribute(attrName);
      outstream.print(" ");
      outstream.print(attrName);
      outstream.print("=\"");
      outstream.print(attrValue);
      outstream.print("\"");
    }
    outstream.println(">");
  }

  public void addText(LayoutTextNode text)
  {
    final String s = new String(text.getData(), text.getOffset(), text.getLength());
    outstream.print(s);
  }

  public void addReplacedElement(LayoutReplacedElement element)
  {
    // we ignore that one, as all information leading to that element should
    // be included in the 'startElement' call already.
  }

  public void endElement(final LayoutElement element)
  {
    if (element.getName() == null)
    {
      // this is an anonymous/generated element. Ignore it.
      return;
    }

    outstream.print("</");
    outstream.print(element.getName());
    outstream.println(">");
  }

  public void endDocument()
  {
    outstream.println("<!-- Finito -->");
    outstream.flush();
  }
}
