/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id: SectionReadHandler.java,v 1.6 2006/12/09 21:19:04 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.modules.factories.report.flow;

import java.util.ArrayList;

import org.jfree.report.modules.factories.report.base.NodeReadHandler;
import org.jfree.report.modules.factories.report.base.NodeReadHandlerFactory;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Section;
import org.jfree.report.structure.StaticText;
import org.jfree.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 14:45:57
 *
 * @author Thomas Morgner
 */
public class SectionReadHandler extends AbstractElementReadHandler
{
  private Section section;
  private StringBuffer textBuffer;
  private ArrayList nodes;
  private ArrayList operationsAfter;
  private ArrayList operationsBefore;
  private String repeat;

  public SectionReadHandler()
  {
    nodes = new ArrayList();
    operationsAfter = new ArrayList();
    operationsBefore = new ArrayList();
  }

  protected Element getElement()
  {
    if (section == null)
    {
      section = new Section();
    }
    return section;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    super.startParsing(attrs);

    final String repeatValue = attrs.getValue(getUri(), "repeat");
    if (repeatValue != null)
    {
      repeat = repeatValue;
    }

    if (FlowReportFactoryModule.NAMESPACE.equals(getUri()) == false)
    {
      final Element element = getElement();
      final int attrLength = attrs.getLength();
      for (int i = 0; i < attrLength; i++)
      {
        final String uri = attrs.getURI(i);
        final String local = attrs.getLocalName(i);
        if (FlowReportFactoryModule.NAMESPACE.equals(uri) == false)
        {
          element.setAttribute(uri, local, attrs.getValue(i));
        }
      }
    }
  }

  protected void configureElement(Element e)
  {
    super.configureElement(e);

    final Section section = (Section) e;
    if (repeat != null)
    {
      section.setRepeat("true".equals(repeat));
    }
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    if (textBuffer != null)
    {
      nodes.add(new StaticText(textBuffer.toString()));
      textBuffer = null;
    }

    final XmlReadHandler elementTypeHanders =
            super.getHandlerForChild(uri, tagName, atts);
    if (elementTypeHanders != null)
    {
      return elementTypeHanders;
    }

    if (FlowReportFactoryModule.NAMESPACE.equals(uri))
    {
      if ("operation-after".equals(tagName))
      {
        final FlowOperationReadHandler frh = new FlowOperationReadHandler();
        operationsAfter.add(frh);
        return frh;
      }
      else if ("operation-before".equals(tagName))
      {
        final FlowOperationReadHandler frh = new FlowOperationReadHandler();
        operationsBefore.add(frh);
        return frh;
      }
    }

    final NodeReadHandlerFactory factory = NodeReadHandlerFactory.getInstance();
    final NodeReadHandler handler = (NodeReadHandler) factory.getHandler(uri, tagName);
    if (handler != null)
    {
      nodes.add(handler);
      return handler;
    }
    return null;
//
//    else if ("content".equals(tagName))
//      {
//        final ContentElementReadHandler contentHandler = new ContentElementReadHandler();
//        nodes.add(new ElementWrapper(contentHandler));
//        return contentHandler;
//      }
//      else if ("section".equals(tagName))
//      {
//        final SectionReadHandler genericHandler =
//                new SectionReadHandler
//                        (JFreeReportInfo.REPORT_NAMESPACE, tagName);
//        nodes.add(new ElementWrapper(genericHandler));
//        return genericHandler;
//      }
//      else if ("detail-section".equals(tagName))
//      {
//        final SectionReadHandler genericHandler =
//                new DetailSectionReadHandler();
//        nodes.add(new ElementWrapper(genericHandler));
//        return genericHandler;
//      }
//      else if ("out-of-order-section".equals(tagName))
//      {
//        final OutOfOrderSectionReadHandler genericHandler =
//                new OutOfOrderSectionReadHandler();
//        nodes.add(new ElementWrapper(genericHandler));
//        return genericHandler;
//      }
//      else if ("page-header".equals(tagName))
//      {
//        final PageHeaderReadHandler genericHandler =
//                new PageHeaderReadHandler();
//        nodes.add(new ElementWrapper(genericHandler));
//        return genericHandler;
//      }
//      else if ("page-footer".equals(tagName))
//      {
//        final PageFooterReadHandler genericHandler =
//                new PageFooterReadHandler();
//        nodes.add(new ElementWrapper(genericHandler));
//        return genericHandler;
//      }
//      else if ("group".equals(tagName))
//      {
//        final GroupReadHandler genericHandler =
//                new GroupReadHandler();
//        nodes.add(new ElementWrapper(genericHandler));
//        return genericHandler;
//      }
//      else if ("sub-report".equals(tagName))
//      {
//        final SubReportReadHandler genericHandler =
//                new SubReportReadHandler();
//        nodes.add(new ElementWrapper(genericHandler));
//        return genericHandler;
//      }
//      else
//      {
//        return null;
//      }
//    }
//    else // something else ..
//    {
//      final SectionReadHandler genericHandler =
//              new SectionReadHandler(uri, tagName);
//      nodes.add(new ElementWrapper(genericHandler));
//      return genericHandler;
//    }
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    if (textBuffer != null)
    {
      nodes.add(new StaticText(textBuffer.toString()));
      textBuffer = null;
    }

    final Section section = (Section) getElement();
    configureElement(section);

    for (int i = 0; i < nodes.size(); i++)
    {
      final Object wrapper = nodes.get(i);
      if (wrapper instanceof StaticText)
      {
        section.addNode((StaticText) wrapper);
      }
      else if (wrapper instanceof NodeReadHandler)
      {
        NodeReadHandler nr = (NodeReadHandler) wrapper;
        section.addNode(nr.getNode());
      }
    }
    for (int i = 0; i < operationsAfter.size(); i++)
    {
      FlowOperationReadHandler handler =
              (FlowOperationReadHandler) operationsAfter.get(i);
      section.addOperationAfter(handler.getOperation());
    }
    for (int i = 0; i < operationsBefore.size(); i++)
    {
      FlowOperationReadHandler handler =
              (FlowOperationReadHandler) operationsBefore.get(i);
      section.addOperationBefore(handler.getOperation());
    }
  }



  /**
   * This method is called to process the character data between element tags.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length.
   * @throws SAXException if there is a parsing error.
   */
  public void characters(final char[] ch, final int start, final int length)
          throws SAXException
  {
    if (textBuffer == null)
    {
      textBuffer = new StringBuffer();
    }
    textBuffer.append(ch, start, length);
  }

}
