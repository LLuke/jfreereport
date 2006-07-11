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
 * StageOneTest.java
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
package org.jfree.layouting.output.junit;

import org.jfree.layouting.ChainingLayoutProcess;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.StateException;
import org.jfree.layouting.junit.DebugLayoutProcess;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;
import org.jfree.layouting.namespace.Namespaces;

/**
 * Creation-Date: 10.06.2006, 16:13:40
 *
 * @author Thomas Morgner
 */
public class StageOneTest
{
  public StageOneTest()
  {
  }

  public static void main(String[] args)
          throws InputFeedException, StateException
  {
    LibLayoutBoot.getInstance().start();

    final StageOnePageableOutputProcessor outputProcessor =
            new StageOnePageableOutputProcessor();
    LayoutProcess dpl = new ChainingLayoutProcess
            (new DebugLayoutProcess(outputProcessor));
    final InputFeed infeed = dpl.getInputFeed();
    infeed.startDocument();
    infeed.startElement(Namespaces.XHTML_NAMESPACE, "body");
//    infeed.addContent("Pre Paga");
//
//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "div");
//    infeed.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "page: p001");
//    infeed.addContent("Page one");
//    infeed.endElement();
//
//    infeed.addContent("Between Page");
//
//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "div");
//    infeed.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "page: p002");
//    infeed.addContent("Page two");
//    infeed.endElement();
//
//    LayoutProcessState state = dpl.saveState();
//    Log.debug ("**********************************************************");
//
//    infeed.addContent("Between-1234");
//
//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "table");
//    infeed.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "float: left");
//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "tr");
//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "td");
//    infeed.endElement();
//    infeed.endElement();
//
//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "tr");
//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "td");
//    infeed.endElement();
//    infeed.endElement();
//    infeed.endElement();

    infeed.startElement(Namespaces.XHTML_NAMESPACE, "div");
    infeed.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "page: p003");
    infeed.addContent("Pre..");
    infeed.startElement(Namespaces.XHTML_NAMESPACE, "span");
    infeed.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "clear: both");
    infeed.addContent("PAGE three");
    infeed.endElement();
    infeed.addContent("..post..box..office");

//    infeed.startElement(Namespaces.XHTML_NAMESPACE, "span");
////    infeed.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "");
//    infeed.addContent("Floating ");
    infeed.endElement();
//
//    infeed.endElement();
//
//    infeed.addContent("Post-Page");

    infeed.endElement(); // body ..

    infeed.endDocument();

//
//    Log.debug ("**********************************************************");
//
//    LayoutProcess p2 = state.restore(outputProcessor);
//    InputFeed infeed2 = p2.getInputFeed();
//    infeed2.addContent("Between-Page");
//
//    infeed2.startElement(Namespaces.XHTML_NAMESPACE, "td");
//    infeed2.setAttribute(Namespaces.XHTML_NAMESPACE, "style", "page: p003");
//    infeed2.addContent("Page three");
//    infeed2.endElement();
//
//    infeed2.addContent("Post-Page");
//
//    infeed2.endElement(); // body ..
//
//    infeed2.endDocument();
//
  }
}
