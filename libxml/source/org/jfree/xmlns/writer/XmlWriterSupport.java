/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libxml/
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
 * $Id: XmlWriterSupport.java,v 1.8 2007/01/19 15:23:44 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import org.jfree.util.FastStack;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.common.AttributeList;

/**
 * A support class for writing XML files.
 *
 * @author Thomas Morgner
 */
public class XmlWriterSupport
{
  private static class ElementLevel
  {
    private String namespace;
    private String prefix;
    private String tagName;
    private Properties namespaces;

    public ElementLevel(final String namespace,
                        final String prefix,
                        final String tagName,
                        final Properties namespaces)
    {
      this.prefix = prefix;
      this.namespace = namespace;
      this.tagName = tagName;
      this.namespaces = namespaces;
    }

    public ElementLevel(final String tagName,
                        final Properties namespaces)
    {
      this.namespaces = namespaces;
      this.tagName = tagName;
    }

    public String getPrefix()
    {
      return prefix;
    }

    public String getNamespace()
    {
      return namespace;
    }

    public String getTagName()
    {
      return tagName;
    }

    public Properties getNamespaces()
    {
      return namespaces;
    }
  }


  /**
   * A constant for controlling the indent function.
   */
  public static final int OPEN_TAG_INCREASE = 1;

  /**
   * A constant for controlling the indent function.
   */
  public static final int CLOSE_TAG_DECREASE = 2;

  /**
   * A constant for controlling the indent function.
   */
  public static final int INDENT_ONLY = 3;

  /**
   * A constant for close.
   */
  public static final boolean CLOSE = true;

  /**
   * A constant for open.
   */
  public static final boolean OPEN = false;

  /**
   * The line separator.
   */
  private static String lineSeparator;

  /**
   * A list of safe tags.
   */
  private TagDescription safeTags;

  /**
   * The indent level for that writer.
   */
  private FastStack openTags;

  /**
   * The indent string.
   */
  private String indentString;

  private boolean lineEmpty;

  private boolean alwaysAddNamespace;
  private boolean assumeDefaultNamespace;

  /**
   * Default Constructor. The created XMLWriterSupport will not have no safe
   * tags and starts with an indention level of 0.
   */
  public XmlWriterSupport()
  {
    this(new DefaultTagDescription(), "  ");
  }


  /**
   * Creates a new support instance.
   *
   * @param safeTags     the tags that are safe for line breaks.
   * @param indentString the indent string.
   */
  public XmlWriterSupport(final TagDescription safeTags,
                          final String indentString)
  {
    if (indentString == null)
    {
      throw new NullPointerException("IndentString must not be null");
    }
    if (safeTags == null)
    {
      throw new NullPointerException("SafeTags must not be null");
    }

    this.safeTags = safeTags;
    this.openTags = new FastStack();
    this.indentString = indentString;
    this.lineEmpty = true;
  }

  public boolean isAlwaysAddNamespace()
  {
    return alwaysAddNamespace;
  }

  public void setAlwaysAddNamespace(final boolean alwaysAddNamespace)
  {
    this.alwaysAddNamespace = alwaysAddNamespace;
  }

  /**
   * Returns the line separator.
   *
   * @return the line separator.
   */
  public static String getLineSeparator()
  {
    if (lineSeparator == null)
    {
      try
      {
        lineSeparator = System.getProperty("line.separator", "\n");
      }
      catch (SecurityException se)
      {
        lineSeparator = "\n";
      }
    }
    return lineSeparator;
  }

  /**
   * Writes an opening XML tag that has no attributes.
   *
   * @param w    the writer.
   * @param name the tag name.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final Writer w,
                       final String namespaceUri,
                       final String name)
      throws IOException
  {
    writeTag(w, namespaceUri, name, null, OPEN);
  }

  /**
   * Writes a closing XML tag.
   *
   * @param w the writer.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeCloseTag(final Writer w)
      throws IOException
  {
    indentForClose(w);
    ElementLevel level = (ElementLevel) openTags.pop();

    setLineEmpty(false);

    w.write("</");
    final String prefix = level.getPrefix();
    if (prefix != null)
    {
      w.write(prefix);
      w.write(":");
      w.write(level.getTagName());
    }
    else
    {
      w.write(level.getTagName());
    }
    w.write(">");

    doEndOfLine(w);
  }

  public void writeNewLine(Writer writer)
      throws IOException
  {
    if (isLineEmpty() == false)
    {
      writer.write(getLineSeparator());
      setLineEmpty(true);
    }
  }


  public boolean isLineEmpty()
  {
    return lineEmpty;
  }

  /**
   * A marker flag to track, wether the current line is empty.
   *
   * @param lineEmpty
   */
  public void setLineEmpty(boolean lineEmpty)
  {
    this.lineEmpty = lineEmpty;
  }

  /**
   * Writes an opening XML tag with an attribute/value pair.
   *
   * @param w              the writer.
   * @param name           the tag name.
   * @param attributeName  the attribute name.
   * @param attributeValue the attribute value.
   * @param close          controls whether the tag is closed.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final Writer w,
                       final String namespace,
                       final String name,
                       final String attributeName,
                       final String attributeValue,
                       final boolean close)
      throws IOException
  {
    if (attributeName != null)
    {
      final AttributeList attr = new AttributeList();
      attr.setAttribute(namespace, attributeName, attributeValue);
      writeTag(w, namespace, name, attr, close);
    }
    else
    {
      writeTag(w, namespace, name, null, close);
    }
  }

  /**
   * Writes an opening XML tag along with a list of attribute/value pairs.
   *
   * @param w          the writer.
   * @param name       the tag name.
   * @param attributes the attributes.
   * @param close      controls whether the tag is closed.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final Writer w,
                       final String namespaceUri,
                       final String name,
                       AttributeList attributes,
                       final boolean close)
      throws IOException
  {
    if (name == null)
    {
      throw new NullPointerException();
    }

    indent(w);
    setLineEmpty(false);

    final Properties namespaces;
    if (openTags.isEmpty())
    {
      namespaces = new Properties();
    }
    else
    {
      ElementLevel parent = (ElementLevel) openTags.peek();
      namespaces = new Properties(parent.getNamespaces());
    }

    if (attributes != null)
    {
      final Iterator attrs = attributes.iterator();
      while (attrs.hasNext())
      {
        final AttributeList.AttributeEntry entry =
            (AttributeList.AttributeEntry) attrs.next();
        final String prefix = entry.getName();
        if ("xmlns".equals(prefix))
        {
          if (entry.getNamespace() == null || "".equals(entry.getNamespace()))
          {
            namespaces.setProperty(entry.getValue(), "");
          }
        }
        else if (AttributeList.XMLNS_NAMESPACE.equals(entry.getNamespace()))
        {
          namespaces.setProperty(entry.getValue(), prefix);
        }
      }
    }

    w.write("<");

    if (namespaceUri == null)
    {
      w.write(name);
      openTags.push(new ElementLevel(name, namespaces));
    }
    else
    {
      String nsPrefix = namespaces.getProperty(namespaceUri);
      if (nsPrefix == null)
      {
        throw new IllegalArgumentException("Namespace " + namespaceUri + " is not defined.");
      }
      if ("".equals(nsPrefix))
      {
        w.write(name);
        openTags.push(new ElementLevel(namespaceUri, null, name, namespaces));
      }
      else
      {
        w.write(nsPrefix);
        w.write(":");
        w.write(name);
        openTags.push(new ElementLevel(namespaceUri, nsPrefix, name, namespaces));
      }
    }

    if (attributes != null)
    {
      final Iterator keys = attributes.iterator();
      while (keys.hasNext())
      {
        final AttributeList.AttributeEntry entry =
            (AttributeList.AttributeEntry) keys.next();
        w.write(" ");
        w.write(buildAttributeName(entry, namespaces));
        w.write("=\"");
        w.write(normalize(entry.getValue(), true));
        w.write("\"");
      }
    }

    if (close)
    {
      w.write("/>");

      openTags.pop();
      doEndOfLine(w);
    }
    else
    {
      w.write(">");
      doEndOfLine(w);
    }
  }

  private void doEndOfLine(Writer w)
      throws IOException
  {
    if (openTags.isEmpty())
    {
      writeNewLine(w);
    }
    else
    {
      ElementLevel level = (ElementLevel) openTags.peek();
      if (getTagDescription().hasCData
          (level.getNamespace(), level.getTagName()) == false)
      {
        writeNewLine(w);
      }
    }
  }

  private String buildAttributeName(AttributeList.AttributeEntry entry,
                                    Properties namespaces)
  {
    ElementLevel currentElement = (ElementLevel) openTags.peek();
    if (isAlwaysAddNamespace() == false &&
        ObjectUtilities.equal(currentElement.getNamespace(), entry.getNamespace()))
    {
      return entry.getName();
    }

    final String name = entry.getName();
    final String namespaceUri = entry.getNamespace();
    if (namespaceUri == null)
    {
      return name;
    }

    if (AttributeList.XMLNS_NAMESPACE.equals(namespaceUri))
    {
      // its a namespace declaration.
      if ("".equals(name))
      {
        return "xmlns";
      }
      else
      {
        return "xmlns:" + name;
      }
    }

    final String namespacePrefix = namespaces.getProperty(namespaceUri);

    if (namespacePrefix != null && "".equals(namespacePrefix) == false)
    {
      return namespacePrefix + ":" + name;
    }
    else
    {
      return name;
    }
  }

  /**
   * Normalises a string, replacing certain characters with their escape
   * sequences so that the XML text is not corrupted.
   *
   * @param s the string.
   * @return the normalised string.
   */
  public static String normalize(final String s,
                                 final boolean transformNewLine)
  {
    if (s == null)
    {
      return "";
    }
    final StringBuffer str = new StringBuffer();
    final int len = s.length();

    for (int i = 0; i < len; i++)
    {
      final char ch = s.charAt(i);

      switch (ch)
      {
        case '<':
        {
          str.append("&lt;");
          break;
        }
        case '>':
        {
          str.append("&gt;");
          break;
        }
        case '&':
        {
          str.append("&amp;");
          break;
        }
        case '"':
        {
          str.append("&quot;");
          break;
        }
        case '\n':
        {
          if (transformNewLine)
          {
            str.append("&#000a");
          }
          else
          {
            str.append('\n');
          }
        }
        case '\r':
        {
          if (transformNewLine)
          {
            str.append("&#000d");
          }
          else
          {
            str.append('\r');
          }
        }
        default :
        {
          str.append(ch);
        }
      }
    }

    return (str.toString());
  }

  /**
   * Indent the line. Called for proper indenting in various places.
   *
   * @param writer the writer which should receive the indentention.
   * @throws java.io.IOException if writing the stream failed.
   */
  public void indent(final Writer writer)
      throws IOException
  {
    if (openTags.isEmpty())
    {
      return;
    }

    ElementLevel level = (ElementLevel) openTags.peek();
    if (getTagDescription().hasCData(level.getNamespace(), level.getTagName()) == false)
    {
      doEndOfLine(writer);

      for (int i = 0; i < this.openTags.size(); i++)
      {
        writer.write(this.indentString);
        // 4 spaces, we could also try tab,
        // but I do not know whether this works
        // with our XML edit pane
      }
    }
  }

  /**
   * Indent the line. Called for proper indenting in various places.
   *
   * @param writer the writer which should receive the indentention.
   * @throws java.io.IOException if writing the stream failed.
   */
  public void indentForClose(final Writer writer)
      throws IOException
  {
    if (openTags.isEmpty())
    {
      return;
    }

    ElementLevel level = (ElementLevel) openTags.peek();
    if (getTagDescription().hasCData(level.getNamespace(), level.getTagName()) == false)
    {
      doEndOfLine(writer);

      for (int i = 1; i < this.openTags.size(); i++)
      {
        writer.write(this.indentString);
        // 4 spaces, we could also try tab,
        // but I do not know whether this works
        // with our XML edit pane
      }
    }
  }

  /**
   * Returns the list of safe tags.
   *
   * @return The list.
   */
  public TagDescription getTagDescription()
  {
    return this.safeTags;
  }

  public void writeComment(Writer writer, String s)
      throws IOException
  {
    if (openTags.isEmpty() == false)
    {
      ElementLevel level = (ElementLevel) openTags.peek();
      if (getTagDescription().hasCData(level.getNamespace(), level.getTagName()) == false)
      {
        indent(writer);
      }
    }

    setLineEmpty(false);

    writer.write("<!-- ");
    writer.write(normalize(s, false));
    writer.write(" -->");
    doEndOfLine(writer);
  }

  public boolean isAssumeDefaultNamespace()
  {
    return assumeDefaultNamespace;
  }

  public void setAssumeDefaultNamespace(final boolean assumeDefaultNamespace)
  {
    this.assumeDefaultNamespace = assumeDefaultNamespace;
  }
}
