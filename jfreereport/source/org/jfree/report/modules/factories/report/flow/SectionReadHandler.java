/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
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
 * SectionReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SectionReadHandler.java,v 1.1 2006/04/18 11:45:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.factories.report.flow;

import java.util.ArrayList;

import org.jfree.report.JFreeReportInfo;
import org.jfree.report.modules.factories.common.XmlReadHandler;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.Section;
import org.jfree.report.structure.StaticText;
import org.jfree.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 14:45:57
 *
 * @author Thomas Morgner
 */
public class SectionReadHandler extends ElementReadHandler
{
  private static interface NodeWrapper
  {
    public Node getNode();
  }

  private static class TextNodeWrapper implements NodeWrapper
  {
    private StaticText text;

    public TextNodeWrapper(final String text)
    {
      this.text = new StaticText(text);
    }

    public Node getNode()
    {
      return text;
    }
  }

  private static class ElementWrapper implements NodeWrapper
  {
    private ElementReadHandler readHandler;

    public ElementWrapper(final ElementReadHandler readHandler)
    {
      this.readHandler = readHandler;
    }

    public Node getNode()
    {
      return readHandler.getElement();
    }
  }

  private String namespace;
  private String tagName;
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


  /**
   * Creates a new generic read handler. The given namespace and tagname can be
   * arbitary values and should not be confused with the ones provided by the
   * XMLparser itself.
   *
   * @param namespace
   * @param tagName
   */
  public SectionReadHandler(final String namespace,
                            final String tagName)
  {
    this();
    this.namespace = namespace;
    this.tagName = tagName;
    this.section = new Section();
    this.section.setNamespace(namespace);
    this.section.setType(tagName);
  }

  public String getSectionNamespace()
  {
    return namespace;
  }

  public String getSectionTagName()
  {
    return tagName;
  }

  protected Element getElement()
  {
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
      final int attrLength = attrs.getLength();
      for (int i = 0; i < attrLength; i++)
      {
        final String uri = attrs.getURI(i);
        final String local = attrs.getLocalName(i);
        if (FlowReportFactoryModule.NAMESPACE.equals(uri) == false)
        {
          getElement().setAttribute(uri, local, attrs.getValue(i));
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
      nodes.add(new TextNodeWrapper(textBuffer.toString()));
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
      else if ("content".equals(tagName))
      {
        final ContentElementReadHandler contentHandler = new ContentElementReadHandler();
        nodes.add(new ElementWrapper(contentHandler));
        return contentHandler;
      }
      else if ("section".equals(tagName))
      {
        final SectionReadHandler genericHandler =
                new SectionReadHandler
                        (JFreeReportInfo.REPORT_NAMESPACE, tagName);
        nodes.add(new ElementWrapper(genericHandler));
        return genericHandler;
      }
      else if ("detail-section".equals(tagName))
      {
        final SectionReadHandler genericHandler =
                new DetailSectionReadHandler();
        nodes.add(new ElementWrapper(genericHandler));
        return genericHandler;
      }
      else if ("out-of-order-section".equals(tagName))
      {
        final OutOfOrderSectionReadHandler genericHandler =
                new OutOfOrderSectionReadHandler();
        nodes.add(new ElementWrapper(genericHandler));
        return genericHandler;
      }
      else if ("page-header".equals(tagName))
      {
        final PageHeaderReadHandler genericHandler =
                new PageHeaderReadHandler();
        nodes.add(new ElementWrapper(genericHandler));
        return genericHandler;
      }
      else if ("page-footer".equals(tagName))
      {
        final PageFooterReadHandler genericHandler =
                new PageFooterReadHandler();
        nodes.add(new ElementWrapper(genericHandler));
        return genericHandler;
      }
      else if ("group".equals(tagName))
      {
        final GroupReadHandler genericHandler =
                new GroupReadHandler();
        nodes.add(new ElementWrapper(genericHandler));
        return genericHandler;
      }
      else if ("sub-report".equals(tagName))
      {
        final SubReportReadHandler genericHandler =
                new SubReportReadHandler();
        nodes.add(new ElementWrapper(genericHandler));
        return genericHandler;
      }
      else
      {
        return null;
      }
    }
    else // something else ..
    {
      final SectionReadHandler genericHandler =
              new SectionReadHandler(uri, tagName);
      nodes.add(new ElementWrapper(genericHandler));
      return genericHandler;
    }
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
      nodes.add(new TextNodeWrapper(textBuffer.toString()));
      textBuffer = null;
    }

    final Section section = (Section) getElement();
    configureElement(section);

    for (int i = 0; i < nodes.size(); i++)
    {
      final NodeWrapper wrapper = (NodeWrapper) nodes.get(i);
      section.addNode(wrapper.getNode());
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
