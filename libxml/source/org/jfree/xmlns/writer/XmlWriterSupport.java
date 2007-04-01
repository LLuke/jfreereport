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
 * $Id: XmlWriterSupport.java,v 1.14 2007/03/26 17:32:30 taqua Exp $
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
  private int additionalIndent;

  private boolean alwaysAddNamespace;
  private boolean assumeDefaultNamespace;
  private Properties impliedNamespaces;
  private boolean writeFinalLinebreak;

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
    this.writeFinalLinebreak = true;
  }

  public boolean isAlwaysAddNamespace()
  {
    return alwaysAddNamespace;
  }

  public void setAlwaysAddNamespace(final boolean alwaysAddNamespace)
  {
    this.alwaysAddNamespace = alwaysAddNamespace;
  }


  public int getAdditionalIndent()
  {
    return additionalIndent;
  }

  public void setAdditionalIndent(final int additionalIndent)
  {
    this.additionalIndent = additionalIndent;
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
   * @param w            the writer.
   * @param namespaceUri the namespace URI for the element.
   * @param name         the tag name.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final Writer w,
                       final String namespaceUri,
                       final String name)
      throws IOException
  {
    writeTag(w, namespaceUri, name, null, XmlWriterSupport.OPEN);
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
    final ElementLevel level = (ElementLevel) openTags.pop();

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

  /**
   * Writes a linebreak to the writer.
   *
   * @param writer the writer.
   * @throws IOException if there is a problem writing to the character stream.
   */
  public void writeNewLine(final Writer writer)
      throws IOException
  {
    if (isLineEmpty() == false)
    {
      writer.write(XmlWriterSupport.getLineSeparator());
      setLineEmpty(true);
    }
  }

  /**
   * Checks, whether the currently generated line of text is empty.
   *
   * @return true, if the line is empty, false otherwise.
   */
  public boolean isLineEmpty()
  {
    return lineEmpty;
  }

  /**
   * A marker flag to track, wether the current line is empty. This influences
   * the indention.
   *
   * @param lineEmpty defines, whether the current line should be treated as
   *                  empty line.
   */
  public void setLineEmpty(final boolean lineEmpty)
  {
    this.lineEmpty = lineEmpty;
  }

  /**
   * Writes an opening XML tag with an attribute/value pair.
   *
   * @param w              the writer.
   * @param namespace      the namespace URI for the element
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
   * Adds an implied namespace to the document. Such a namespace is not
   * explicitly declared, it is assumed that the xml-parser knows the prefix by
   * some other means. Using implied namespaces for standalone documents is
   * almost always a bad idea.
   *
   * @param uri    the uri of the namespace.
   * @param prefix the defined prefix.
   */
  public void addImpliedNamespace(final String uri, final String prefix)
  {
    if (prefix == null)
    {
      if (impliedNamespaces == null)
      {
        return;
      }
      impliedNamespaces.remove(uri);
    }
    else
    {
      if (impliedNamespaces == null)
      {
        impliedNamespaces = new Properties();
      }
      impliedNamespaces.setProperty(uri, prefix);
    }
  }

  /**
   * Copies all currently declared namespaces of the given XmlWriterSupport
   * instance as new implied namespaces into this instance.
   *
   * @param writerSupport the Xml-writer from where to copy the declared
   *                      namespaces.
   */
  public void copyNamespaces(final XmlWriterSupport writerSupport)
  {
    if (impliedNamespaces == null)
    {
      impliedNamespaces = new Properties();
    }

    if (writerSupport.openTags.isEmpty() == false)
    {
      final ElementLevel parent = (ElementLevel) writerSupport.openTags.peek();
      //noinspection UseOfPropertiesAsHashtable
      impliedNamespaces.putAll(parent.getNamespaces());
    }

    if (writerSupport.impliedNamespaces != null)
    {
      //noinspection UseOfPropertiesAsHashtable
      impliedNamespaces.putAll(writerSupport.impliedNamespaces);
    }
  }

  public boolean isNamespaceDefined(final String uri)
  {
    if (impliedNamespaces != null)
    {
      if (impliedNamespaces.containsKey(uri))
      {
        return true;
      }
    }
    if (openTags.isEmpty())
    {
      return false;
    }
    final ElementLevel parent = (ElementLevel) openTags.peek();
    return parent.getNamespaces().containsKey(uri);
  }

  public boolean isNamespacePrefixDefined(final String prefix)
  {
    if (impliedNamespaces != null)
    {
      if (impliedNamespaces.containsValue(prefix))
      {
        return true;
      }
    }
    if (openTags.isEmpty())
    {
      return false;
    }
    final ElementLevel parent = (ElementLevel) openTags.peek();
    return parent.getNamespaces().containsValue(prefix);
  }


  public Properties getNamespaces()
  {
    if (openTags.isEmpty())
    {
      final Properties namespaces = new Properties();
      if (impliedNamespaces != null)
      {
        //noinspection UseOfPropertiesAsHashtable
        namespaces.putAll(impliedNamespaces);
      }
      return namespaces;
    }

    final ElementLevel parent = (ElementLevel) openTags.peek();
    final Properties namespaces = new Properties();
    //noinspection UseOfPropertiesAsHashtable
    namespaces.putAll(parent.getNamespaces());
    return namespaces;
  }

  /**
   * Writes an opening XML tag along with a list of attribute/value pairs.
   *
   * @param w            the writer.
   * @param namespaceUri the namespace uri for the element (can be null).
   * @param name         the tag name.
   * @param attributes   the attributes.
   * @param close        controls whether the tag is closed.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeTag(final Writer w,
                       final String namespaceUri,
                       final String name,
                       final AttributeList attributes,
                       final boolean close)
      throws IOException
  {
    if (name == null)
    {
      throw new NullPointerException();
    }

    indent(w);
    setLineEmpty(false);

    final Properties namespaces = getNamespaces();

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
      final String nsPrefix = namespaces.getProperty(namespaceUri);
      if (nsPrefix == null)
      {
        throw new IllegalArgumentException(
            "Namespace " + namespaceUri + " is not defined.");
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
        openTags.push(new ElementLevel(namespaceUri, nsPrefix, name,
            namespaces));
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
        w.write(XmlWriterSupport.normalize(entry.getValue(), true));
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

  /**
   * Conditionally writes an end-of-line character. The End-Of-Line is only
   * written, if the tag description indicates that the currently open element
   * does not expect any CDATA inside. Writing a newline for CDATA-elements may
   * have sideeffects.
   *
   * @param w the writer.
   * @throws java.io.IOException if there is an I/O problem.
   */
  private void doEndOfLine(final Writer w)
      throws IOException
  {
    if (openTags.isEmpty())
    {
      if (isWriteFinalLinebreak())
      {
        writeNewLine(w);
      }
    }
    else
    {
      final ElementLevel level = (ElementLevel) openTags.peek();
      if (getTagDescription().hasCData
          (level.getNamespace(), level.getTagName()) == false)
      {
        writeNewLine(w);
      }
    }
  }

  /**
   * Processes a single attribute and searches for namespace declarations. If a
   * namespace declaration is found, it is returned in a normalized way. If
   * namespace processing is active, the attribute name will be fully qualified
   * with the prefix registered for the attribute's namespace URI.
   *
   * @param entry      the attribute enty.
   * @param namespaces the currently known namespaces.
   * @return the normalized attribute name.
   */
  private String buildAttributeName(final AttributeList.AttributeEntry entry,
                                    final Properties namespaces)
  {
    final ElementLevel currentElement = (ElementLevel) openTags.peek();
    if (isAlwaysAddNamespace() == false &&
        ObjectUtilities.equal(currentElement.getNamespace(),
            entry.getNamespace()))
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
   * @param s                the string.
   * @param transformNewLine true, if a newline in the string should be
   *                         converted into a character entity.
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
        case'<':
        {
          str.append("&lt;");
          break;
        }
        case'>':
        {
          str.append("&gt;");
          break;
        }
        case'&':
        {
          str.append("&amp;");
          break;
        }
        case'"':
        {
          str.append("&quot;");
          break;
        }
        case'\n':
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
        case'\r':
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
        default:
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
      for (int i = 0; i < additionalIndent; i++)
      {
        writer.write(this.indentString);
        // 4 spaces, we could also try tab,
        // but I do not know whether this works
        // with our XML edit pane
      }
      return;
    }

    final ElementLevel level = (ElementLevel) openTags.peek();
    if (getTagDescription().hasCData(level.getNamespace(),
        level.getTagName()) == false)
    {
      doEndOfLine(writer);

      for (int i = 0; i < this.openTags.size(); i++)
      {
        writer.write(this.indentString);
        // 4 spaces, we could also try tab,
        // but I do not know whether this works
        // with our XML edit pane
      }

      for (int i = 0; i < additionalIndent; i++)
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
      for (int i = 0; i < additionalIndent; i++)
      {
        writer.write(this.indentString);
        // 4 spaces, we could also try tab,
        // but I do not know whether this works
        // with our XML edit pane
      }
      return;
    }

    final ElementLevel level = (ElementLevel) openTags.peek();
    if (getTagDescription().hasCData(level.getNamespace(),
        level.getTagName()) == false)
    {
      doEndOfLine(writer);

      for (int i = 1; i < this.openTags.size(); i++)
      {
        writer.write(this.indentString);
        // 4 spaces, we could also try tab,
        // but I do not know whether this works
        // with our XML edit pane
      }
      for (int i = 0; i < additionalIndent; i++)
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

  /**
   * Writes a comment into the generated xml file.
   *
   * @param writer  the writer.
   * @param comment the comment text
   * @throws IOException if there is a problem writing to the character stream.
   */
  public void writeComment(final Writer writer, final String comment)
      throws IOException
  {
    if (openTags.isEmpty() == false)
    {
      final ElementLevel level = (ElementLevel) openTags.peek();
      if (getTagDescription().hasCData
          (level.getNamespace(), level.getTagName()) == false)
      {
        indent(writer);
      }
    }

    setLineEmpty(false);

    writer.write("<!-- ");
    writer.write(XmlWriterSupport.normalize(comment, false));
    writer.write(" -->");
    doEndOfLine(writer);
  }

  /**
   * Checks, whether attributes of the same namespace as the current element
   * should be written without a prefix. Attributes without a prefix are
   * considered to be not in any namespace at all. How to treat such attributes
   * is implementation dependent. (Appendix A; Section 6.2 of the XmlNamespaces
   * recommendation)
   *
   * @return true, if attributes in the element's namespace should be written
   *         without a prefix, false to write all attributes with a prefix.
   */
  public boolean isAssumeDefaultNamespace()
  {
    return assumeDefaultNamespace;
  }

  /**
   * Defines, whether attributes of the same namespace as the current element
   * should be written without a prefix. Attributes without a prefix are
   * considered to be not in any namespace at all. How to treat such attributes
   * is implementation dependent. (Appendix A; Section 6.2 of the XmlNamespaces
   * recommendation)
   *
   * @param assumeDefaultNamespace true, if attributes in the element's
   *                               namespace should be written without a prefix,
   *                               false to write all attributes with a prefix.
   */
  public void setAssumeDefaultNamespace(final boolean assumeDefaultNamespace)
  {
    this.assumeDefaultNamespace = assumeDefaultNamespace;
  }

  /**
   * Returns the current indention level.
   *
   * @return the indention level.
   */
  public int getCurrentIndentLevel()
  {
    return additionalIndent + openTags.size();
  }


  public void setWriteFinalLinebreak(final boolean writeFinalLinebreak)
  {
    this.writeFinalLinebreak = writeFinalLinebreak;
  }

  public boolean isWriteFinalLinebreak()
  {
    return writeFinalLinebreak;
  }
}
