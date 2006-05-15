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
   * Creates a new XML writer for the specified character stream.  By default, four
   * spaces are used for indentation.
   *
   * @param writer the character stream.
   */
  public XmlWriter (final Writer writer)
  {
    this(writer, "  ");
  }

  /**
   * Creates a new XML writer for the specified character stream.
   *
   * @param writer       the character stream.
   * @param indentString the string used for indentation (should contain white space,
   *                     for example four spaces).
   */
  public XmlWriter (final Writer writer, final String indentString)
  {
    this(writer, new DefaultTagDescription(), indentString);
  }

  /**
   * Creates a new support instance.
   *
   * @param safeTags     the tags that are safe for line breaks.
   * @param indentString the indent string.
   */
  public XmlWriter (final Writer writer,
                    final TagDescription safeTags,
                    final String indentString)
  {
    super(safeTags, indentString);
    if (writer == null)
    {
      throw new NullPointerException("Writer must not be null.");
    }

    this.writer = writer;
  }

  /**
   * Writes the XML declaration that usually appears at the top of every XML file.
   *
   * @throws java.io.IOException if there is a problem writing to the character stream.
   */
  public void writeXmlDeclaration (String encoding)
          throws IOException
  {
    this.writer.write("<?xml version=\"1.0\" encoding=\""+ encoding + "\"?>");
    this.writer.write(getLineSeparator());
  }

  /**
   * Writes an opening XML tag that has no attributes.
   *
   * @param name  the tag name.
   * @param close a flag that controls whether or not the tag is closed immediately.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag (final String namespace,
                        final String name,
                        final boolean close)
          throws IOException
  {
    if (close)
    {
      writeTag(this.writer, namespace, name, null, close);
    }
    else
    {
      writeTag(this.writer, namespace, name);
    }
  }

  /**
   * Writes a closing XML tag.
   *
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeCloseTag ()
          throws IOException
  {
    super.writeCloseTag(this.writer);
  }

  /**
   * Writes an opening XML tag with an attribute/value pair.
   *
   * @param name           the tag name.
   * @param attributeName  the attribute name.
   * @param attributeValue the attribute value.
   * @param close          controls whether the tag is closed.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag (final String namespace,
                        final String name,
                        final String attributeName,
                        final String attributeValue,
                        final boolean close)
          throws IOException
  {
    writeTag(this.writer, namespace, name, attributeName, attributeValue, close);
  }

  /**
   * Writes an opening XML tag along with a list of attribute/value pairs.
   *
   * @param name       the tag name.
   * @param attributes the attributes.
   * @param close      controls whether the tag is closed.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag (final String namespace,
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
  public void writeText (final String text)
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
  public void close ()
          throws IOException
  {
    this.writer.close();
  }

  public void writeComment (String s)
          throws IOException
  {
    super.writeComment(writer, s);
  }
}
