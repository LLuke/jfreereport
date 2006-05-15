package org.jfree.xmlns.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.jfree.xmlns.common.AttributeList;
import org.jfree.util.ObjectUtilities;

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
    private String tagName;

    public ElementLevel (String namespace, String tagName)
    {
      this.namespace = namespace;
      this.tagName = tagName;
    }

    public String getNamespace ()
    {
      return namespace;
    }

    public String getTagName ()
    {
      return tagName;
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
  private Stack openTags;

  /**
   * The indent string.
   */
  private String indentString;

  private HashMap namespaces;

  private boolean lineEmpty;

  /**
   * Default Constructor. The created XMLWriterSupport will not have no safe tags and
   * starts with an indention level of 0.
   */
  public XmlWriterSupport ()
  {
    this(new DefaultTagDescription(), "  ");
  }


  /**
   * Creates a new support instance.
   *
   * @param safeTags     the tags that are safe for line breaks.
   * @param indentString the indent string.
   */
  public XmlWriterSupport (final TagDescription safeTags,
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
    this.openTags = new Stack();
    this.indentString = indentString;
    this.namespaces = new HashMap();
    this.lineEmpty = true;
  }

  public void addNamespace (String namespaceUri, String prefix)
  {
    if (namespaceUri == null)
    {
      throw new NullPointerException("Namespace uri");
    }
    if (prefix == null)
    {
      throw new NullPointerException("Prefix");
    }
    namespaces.put(namespaceUri, prefix);
  }


  /**
   * Returns the line separator.
   *
   * @return the line separator.
   */
  public static String getLineSeparator ()
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
  public void writeTag (final Writer w,
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
  public void writeCloseTag (final Writer w)
          throws IOException
  {
    ElementLevel level = (ElementLevel) openTags.pop();

    // check whether the tag contains CData - we ma not indent such tags
    if (getTagDescription().hasCData(level.getNamespace(), level.getTagName()) == false)
    {
      indent(w);
    }
    setLineEmpty(false);

    w.write("</");
    w.write(buildQualifiedName(level.getNamespace(), level.getTagName()));
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


  public boolean isLineEmpty ()
  {
    return lineEmpty;
  }

  public void setLineEmpty (boolean lineEmpty)
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
  public void writeTag (final Writer w,
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
  public void writeTag (final Writer w,
                        final String namespaceUri,
                        final String name,
                        AttributeList attributes,
                        final boolean close)
          throws IOException
  {
    indent(w);
    openTags.push(new ElementLevel(namespaceUri, name));
    setLineEmpty(false);

    w.write("<");
    w.write(name);
    if (openTags.isEmpty() && namespaces.isEmpty() == false)
    {
      Iterator entries = namespaces.entrySet().iterator();
      while (entries.hasNext())
      {
        Map.Entry entry = (Map.Entry) entries.next();
        final String nsUri = (String) entry.getKey();
        final String nsPrfx = (String) entry.getValue();

        if (nsPrfx != null && "".equals(nsPrfx) == false)
        {
          w.write("xmlns: " + nsPrfx);
        }
        else
        {
          w.write("xmlns");
        }

        w.write(" ");
        w.write("=\"");
        w.write(normalize(nsUri, true));
        w.write("\"");
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
        w.write(buildAttributeName(entry));
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

  private void doEndOfLine (Writer w)
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

  private String buildAttributeName (AttributeList.AttributeEntry entry)
  {
    ElementLevel rootElement = (ElementLevel) openTags.peek();
    if (ObjectUtilities.equal(rootElement.getNamespace(), entry.getNamespace()))
    {
      return entry.getName();
    }

    return buildQualifiedName(entry.getNamespace(), entry.getName());
  }

  private String buildQualifiedName (final String namespaceUri,
                                     final String name)
  {
    final String namespacePrefix =
            (String) namespaces.get(namespaceUri);

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
   * Normalises a string, replacing certain characters with their escape sequences so that
   * the XML text is not corrupted.
   *
   * @param s the string.
   * @return the normalised string.
   */
  public static String normalize (final String s,
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
          str.append("&#000a");
        }
        case '\r':
        {
          str.append("&#000d");
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
  public void indent (final Writer writer)
          throws IOException
  {
    if (openTags.isEmpty())
    {
      return;
    }

    doEndOfLine(writer);

    for (int i = 0; i < this.openTags.size(); i++)
    {
      writer.write(this.indentString);
      // 4 spaces, we could also try tab,
      // but I do not know whether this works
      // with our XML edit pane
    }
  }

  /**
   * Returns the list of safe tags.
   *
   * @return The list.
   */
  public TagDescription getTagDescription ()
  {
    return this.safeTags;
  }

  public void writeComment (Writer writer, String s)
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
}
