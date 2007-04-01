/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns.writer;

import java.io.IOException;
import java.io.Writer;

import org.jfree.xmlns.common.AttributeList;


/**
 * A class for writing XML to a character stream.
 */
public class XmlWriter extends XmlWriterSupport
{
  /**
   * The character stream.
   */
  private Writer writer;

  /**
   * Creates a new XML writer for the specified character stream.  By default,
   * four spaces are used for indentation.
   *
   * @param writer the character stream.
   */
  public XmlWriter(final Writer writer)
  {
    this(writer, "  ");
  }

  /**
   * Default Constructor. The created XMLWriterSupport will not have no safe
   * tags and starts with an indention level of 0.
   *
   * @param writer         the character stream.
   * @param tagDescription the tags that are safe for line breaks.
   */
  public XmlWriter(final Writer writer, final TagDescription tagDescription)
  {
    this(writer, tagDescription, "  ");
  }

  /**
   * Creates a new XML writer for the specified character stream.
   *
   * @param writer       the character stream.
   * @param indentString the string used for indentation (should contain white
   *                     space, for example four spaces).
   */
  public XmlWriter(final Writer writer, final String indentString)
  {
    this(writer, new DefaultTagDescription(), indentString);
  }

  /**
   * Creates a new support instance.
   *
   * @param writer         the character stream.
   * @param tagDescription the tags that are safe for line breaks.
   * @param indentString   the indent string.
   */
  public XmlWriter(final Writer writer,
                   final TagDescription tagDescription,
                   final String indentString)
  {
    super(tagDescription, indentString);
    if (writer == null)
    {
      throw new NullPointerException("Writer must not be null.");
    }

    this.writer = writer;
  }

  /**
   * Writes the XML declaration that usually appears at the top of every XML
   * file.
   *
   * @param encoding the encoding that should be declared (this has to match the
   *                 encoding of the writer, or funny things may happen when
   *                 parsing the xml file later).
   * @throws java.io.IOException if there is a problem writing to the character
   *                             stream.
   */
  public void writeXmlDeclaration(final String encoding)
      throws IOException
  {
    if (encoding == null)
    {
      this.writer.write("<?xml version=\"1.0\"");
      this.writer.write("\"?>");
      this.writer.write(XmlWriterSupport.getLineSeparator());
      return;
    }

    this.writer.write("<?xml version=\"1.0\" encoding=\"");
    this.writer.write(encoding);
    this.writer.write("\"?>");
    this.writer.write(XmlWriterSupport.getLineSeparator());
  }

  /**
   * Writes an opening XML tag that has no attributes.
   *
   * @param namespace the namespace URI for the element
   * @param name      the tag name.
   * @param close     a flag that controls whether or not the tag is closed
   *                  immediately.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final String namespace,
                       final String name,
                       final boolean close)
      throws IOException
  {
    if (close)
    {
      writeTag(this.writer, namespace, name, null, XmlWriterSupport.CLOSE);
    }
    else
    {
      writeTag(this.writer, namespace, name, null, XmlWriterSupport.OPEN);
    }
  }

  /**
   * Writes a closing XML tag.
   *
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeCloseTag()
      throws IOException
  {
    super.writeCloseTag(this.writer);
  }

  /**
   * Writes an opening XML tag with an attribute/value pair.
   *
   * @param namespace      the namespace URI for the element
   * @param name           the tag name.
   * @param attributeName  the attribute name.
   * @param attributeValue the attribute value.
   * @param close          controls whether the tag is closed.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final String namespace,
                       final String name,
                       final String attributeName,
                       final String attributeValue,
                       final boolean close)
      throws IOException
  {
    writeTag(this.writer, namespace, name, attributeName, attributeValue,
        close);
  }

  /**
   * Writes an opening XML tag along with a list of attribute/value pairs.
   *
   * @param namespace  the namespace URI for the element
   * @param name       the tag name.
   * @param attributes the attributes.
   * @param close      controls whether the tag is closed.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final String namespace,
                       final String name,
                       final AttributeList attributes,
                       final boolean close)
      throws IOException
  {
    writeTag(this.writer, namespace, name, attributes, close);
  }

  /**
   * Writes some text to the character stream.
   *
   * @param text the text.
   * @throws IOException if there is a problem writing to the character stream.
   */
  public void writeText(final String text)
      throws IOException
  {
    this.writer.write(text);
    setLineEmpty(false);
  }

  /**
   * Closes the underlying character stream.
   *
   * @throws IOException if there is a problem closing the character stream.
   */
  public void close()
      throws IOException
  {
    this.writer.close();
  }

  /**
   * Writes a comment into the generated xml file.
   *
   * @param comment the comment text
   * @throws IOException if there is a problem writing to the character stream.
   */
  public void writeComment(final String comment)
      throws IOException
  {
    super.writeComment(writer, comment);
  }

  /**
   * Writes a linebreak to the writer.
   *
   * @throws IOException if there is a problem writing to the character stream.
   */
  public void writeNewLine()
      throws IOException
  {
    super.writeNewLine(writer);
  }

  public void flush ()
      throws IOException
  {
    this.writer.flush();
  }

}
