/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.junit;

import org.jfree.layouting.modules.output.graphics.GraphicsOutputProcessor;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;

/**
 * Creation-Date: 19.11.2006, 13:58:31
 *
 * @author Thomas Morgner
 */
public class IllegalDisplayModelTest
{
  public static final String NAMESPACE = "http://www.w3.org/1999/xhtml";

  private IllegalDisplayModelTest()
  {
  }

  public static void main(String[] args) throws InputFeedException
  {
    final LibLayoutBoot instance = LibLayoutBoot.getInstance();
    instance.start();
    GraphicsOutputProcessor op = new GraphicsOutputProcessor(instance.getGlobalConfig());
    DefaultLayoutProcess dlp = new DefaultLayoutProcess(op);
    InputFeed inputFeed = dlp.getInputFeed();
    inputFeed.startDocument();
      inputFeed.startElement(NAMESPACE, "body");
      inputFeed.addContent(" ");
        inputFeed.startElement(NAMESPACE, "span");
        inputFeed.addContent(" ");
          inputFeed.startElement(NAMESPACE, "div");
          inputFeed.addContent("Invalid");
          inputFeed.endElement();
        inputFeed.addContent("x");
        inputFeed.endElement();
      inputFeed.addContent("y");
      inputFeed.endElement();
    inputFeed.endDocument();

//
//    dlp = new DefaultLayoutProcess(op);
//    inputFeed.startDocument();
//    inputFeed.startElement(NAMESPACE, "body");
//    inputFeed.addContent(" ");
//    inputFeed.startElement(NAMESPACE, "span");
//    inputFeed.addContent(" ");
//    inputFeed.startElement(NAMESPACE, "div");
//    inputFeed.addContent("Invalid");
//    inputFeed.endElement();
//    inputFeed.addContent(" ");
//    inputFeed.endElement();
//    inputFeed.addContent(" ");
//    inputFeed.startElement(NAMESPACE, "div");
//    inputFeed.addContent(" ");
//    inputFeed.startElement(NAMESPACE, "div");
//    inputFeed.addContent("Valid");
//    inputFeed.endElement();
//    inputFeed.addContent(" ");
//    inputFeed.endElement();
//    inputFeed.addContent(" ");
//    inputFeed.endElement();
//    inputFeed.addContent(" ");
//    inputFeed.endDocument();
//
  }
}
