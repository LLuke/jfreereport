package org.jfree.report.dev.locales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.base.BaseBoot;
import org.jfree.util.LineBreakIterator;
import org.jfree.util.Log;

public class EditableProperties
{
  private static class SortableNode implements Comparable
  {
    private int propertyIndex;
    private int subIndex;

    public SortableNode (final int propertyIndex, final int subIndex)
    {
      this.propertyIndex = propertyIndex;
      this.subIndex = subIndex;
    }

    public int getPropertyIndex ()
    {
      return propertyIndex;
    }

    public int getSubIndex ()
    {
      return subIndex;
    }

    public int compareTo (final Object o)
    {
      if (o instanceof SortableNode)
      {
        final SortableNode sn = (SortableNode) o;
        if (propertyIndex < sn.propertyIndex)
        {
          return -1;
        }
        else if (propertyIndex > sn.propertyIndex)
        {
          return +1;
        }
        if (subIndex < sn.subIndex)
        {
          return -1;
        }
        else if (subIndex > sn.subIndex)
        {
          return +1;
        }
        return 0;
      }
      return 0;
    }
  }

  private static class CommentNode extends SortableNode
  {
    private String comment;

    public CommentNode (final int propertyIndex, final int subIndex, final String comment)
    {
      super(propertyIndex, subIndex);
      this.comment = comment;
    }

    public String getComment ()
    {
      return comment;
    }
  }

  private static class PropertyNode extends SortableNode
  {
    private String key;
    private String value;
    private CommentNode commentNodes;

    public PropertyNode (final int propertyIndex, final int subIndex,
                         final String key, final String value)
    {
      super(propertyIndex, subIndex);
      if (key == null)
      {
        throw new NullPointerException();
      }
      this.key = key;
      if (value == null)
      {
        throw new NullPointerException();
      }
      this.value = value;
    }

    public String getKey ()
    {
      return key;
    }

    public String getValue ()
    {
      return value;
    }

    public void setValue (final String value)
    {
      if (value == null)
      {
        throw new NullPointerException();
      }
      this.value = value;
    }

    public CommentNode getCommentNodes ()
    {
      return commentNodes;
    }

    public void setCommentNodes (final CommentNode commentNodes)
    {
      this.commentNodes = commentNodes;
    }
  }

  /**
   * A constant defining that text should be escaped in a way which is suitable for
   * property keys.
   */
  private static final int ESCAPE_KEY = 0;
  /**
   * A constant defining that text should be escaped in a way which is suitable for
   * property values.
   */
  private static final int ESCAPE_VALUE = 1;
  /**
   * A constant defining that text should be escaped in a way which is suitable for
   * property comments.
   */
  private static final int ESCAPE_COMMENT = 2;

  private ArrayList contents;
  private int propertyIndex;
  private int subIndex;

  public EditableProperties ()
  {
    contents = new ArrayList();
  }

  public synchronized void write (final OutputStream out)
          throws UnsupportedEncodingException
  {
    final PrintWriter prt = new PrintWriter(new OutputStreamWriter(out, "iso-8859-1"));
    for (int i = 0; i < contents.size(); i++)
    {
      final Object o = contents.get(i);
      if (o instanceof CommentNode)
      {
        final CommentNode cmn = (CommentNode) o;
        writeDescription(cmn.getComment(), prt);
      }
      else if (o instanceof PropertyNode)
      {
        final PropertyNode pn = (PropertyNode) o;
        if (pn.getCommentNodes() != null)
        {
          final CommentNode cmn = pn.getCommentNodes();
          writeDescription(cmn.getComment(), prt);
        }
        saveConvert(pn.getKey(), ESCAPE_KEY, prt);
        prt.print("=");
        saveConvert(pn.getValue(), ESCAPE_VALUE, prt);
        prt.println();
      }
      else
      {
        prt.println();
      }
    }
    prt.flush();
  }

  /**
   * Writes a descriptive comment into the given print writer.
   *
   * @param text   the text to be written. If it contains more than one line, every line
   *               will be prepended by the comment character.
   * @param writer the writer that should receive the content.
   */
  private void writeDescription (final String text, final PrintWriter writer)
  {
    // check if empty content ... this case is easy ...
    if (text.length() == 0)
    {
      return;
    }

    final LineBreakIterator iterator = new LineBreakIterator(text);
    while (iterator.hasNext())
    {
      writer.print("#");
      saveConvert((String) iterator.next(), ESCAPE_COMMENT, writer);
      writer.println();
    }
  }

  private boolean isKeyValueSeparator (final char c)
  {
    return isStrictKeyValueSeparator(c) || isWhiteSpaceChar(c);
  }

  private boolean isStrictKeyValueSeparator (final char c)
  {
    if (c == '=' || c == ':')
      return true;
    return false;
  }

  private boolean isWhiteSpaceChar(final char c)
  {
    if (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f')
    {
      return true;
    }
    return false;
  }

  public synchronized void load (final InputStream inStream)
          throws IOException
  {
    contents.clear();
    propertyIndex = 0;
    subIndex = 0;
    final BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "iso-8859-1"));
    while (true)
    {
      // Get next line
      String line = in.readLine();
      if (line == null)
      {
        // EndOfFile
        return;
      }

      if (line.length() == 0)
      {
        // totally empty lines ..
        contents.add (new SortableNode(propertyIndex, subIndex));
        Log.debug("ADD --");
        continue;
      }

      // Find start of key
      final int keyStart = findKeyStart (line);
      if (keyStart == -1)
      {
        if (line.startsWith("#"))
        {
          contents.add(new CommentNode(propertyIndex, subIndex, line.substring(1)));
        }
        else
        {
          contents.add(new CommentNode(propertyIndex, subIndex, line));
        }
        subIndex += 1;
        Log.debug("ADD #" + line);
        continue;
      }

      // is this a comment line?
      final char firstChar = line.charAt(keyStart);
      if ((firstChar == '#') || (firstChar == '!'))
      {
        contents.add(new CommentNode(propertyIndex, subIndex, line.substring(keyStart + 1)));
        subIndex += 1;
        Log.debug("ADD #" + line);
        continue;
      }

      line = readCompleteLine(line, in);
      final int len = line.length();
      // Find separation between key and value
      int separatorIndex;
      for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++)
      {
        final char currentChar = line.charAt(separatorIndex);
        if (currentChar == '\\')
        {
          separatorIndex++;
        }
        else if (isKeyValueSeparator(currentChar))
        {
          break;
        }
      }

      // Skip over whitespace after key if any
      int valueIndex;
      for (valueIndex = separatorIndex; valueIndex < len; valueIndex++)
      {
        if (isWhiteSpaceChar(line.charAt(valueIndex)) == false)
        {
          break;
        }
      }

      // Skip over one non whitespace key value separators if any
      if (valueIndex < len)
      {
        if (isStrictKeyValueSeparator(line.charAt(valueIndex)))
        {
          valueIndex++;
        }
      }

      // Skip over white space after other separators if any
      while (valueIndex < len)
      {
        if (isWhiteSpaceChar(line.charAt(valueIndex)) == false)
        {
          break;
        }
        valueIndex++;
      }
      String key = line.substring(keyStart, separatorIndex);
      String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

      // Convert then store key and value
      key = loadConvert(key);
      value = loadConvert(value);
      Log.debug("ADD " + key + " = " + value);
      contents.add(new PropertyNode(propertyIndex, subIndex, key, value));
      subIndex = 0;
      propertyIndex += 1;
    }
  }

  private String readCompleteLine (final String firstLine, final BufferedReader in)
    throws IOException
  {
    if (continueLine(firstLine) == false)
    {
      return firstLine;
    }

    final StringBuffer retval = new StringBuffer();
    retval.append(firstLine.substring(0, firstLine.length() - 1));

    String line = firstLine;
    while (continueLine(line))
    {
      String nextLine = in.readLine();
      if (nextLine == null)
      {
        nextLine = "";
      }
      // Advance beyond whitespace on new line
      int startIndex;
      for (startIndex = 0; startIndex < nextLine.length(); startIndex++)
      {
        if (isWhiteSpaceChar(nextLine.charAt(startIndex)) == false)
        {
          break;
        }
      }
      retval.append(nextLine.substring(startIndex));
      line = nextLine;
    }
    return retval.toString();
  }

  private int findKeyStart (final String line)
  {
    final int len = line.length();
    for (int keyStart = 0; keyStart < len; keyStart++)
    {
      if (isWhiteSpaceChar(line.charAt(keyStart)) == false)
      {
        return keyStart;
      }
    }

    return -1;
  }

  /**
   * Returns true if the given line is a line that must
   * be appended to the next line
   */
  private boolean continueLine (final String line)
  {
    int slashCount = 0;
    int index = line.length() - 1;
    while ((index >= 0) && (line.charAt(index--) == '\\'))
    {
      slashCount++;
    }
    return (slashCount % 2 == 1);
  }

  /**
   * Converts encoded &#92;uxxxx to unicode chars
   * and changes special saved chars to their original forms
   */
  private String loadConvert (final String theString)
  {
    final int len = theString.length();
    final StringBuffer outBuffer = new StringBuffer(len);

    for (int x = 0; x < len;)
    {
      char aChar = theString.charAt(x++);
      if (aChar == '\\')
      {
        aChar = theString.charAt(x++);
        if (aChar == 'u')
        {
          // Read the xxxx
          int value = 0;
          for (int i = 0; i < 4; i++)
          {
            aChar = theString.charAt(x++);
            switch (aChar)
            {
              case '0':
              case '1':
              case '2':
              case '3':
              case '4':
              case '5':
              case '6':
              case '7':
              case '8':
              case '9':
                value = (value << 4) + aChar - '0';
                break;
              case 'a':
              case 'b':
              case 'c':
              case 'd':
              case 'e':
              case 'f':
                value = (value << 4) + 10 + aChar - 'a';
                break;
              case 'A':
              case 'B':
              case 'C':
              case 'D':
              case 'E':
              case 'F':
                value = (value << 4) + 10 + aChar - 'A';
                break;
              default:
                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
            }
          }
          outBuffer.append((char) value);
        }
        else
        {
          if (aChar == 't')
          {
            aChar = '\t';
          }
          else if (aChar == 'r')
          {
            aChar = '\r';
          }
          else if (aChar == 'n')
          {
            aChar = '\n';
          }
          else if (aChar == 'f')
          {
            aChar = '\f';
          }
          outBuffer.append(aChar);
        }
      }
      else
      {
        outBuffer.append(aChar);
      }
    }
    return outBuffer.toString();
  }


  /**
   * Performs the necessary conversion of an java string into a property escaped string.
   *
   * @param text       the text to be escaped
   * @param escapeMode the mode that should be applied.
   * @param writer     the writer that should receive the content.
   */
  private void saveConvert (final String text, final int escapeMode,
                            final PrintWriter writer)
  {
    final char[] string = text.toCharArray();
    final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7',
                             '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    for (int x = 0; x < string.length; x++)
    {
      final char aChar = string[x];
      switch (aChar)
      {
        case ' ':
          {
            if ((escapeMode != ESCAPE_COMMENT) &&
                    (x == 0 || escapeMode == ESCAPE_KEY))
            {
              writer.print('\\');
            }
            writer.print(' ');
            break;
          }
        case '\\':
          {
            writer.print('\\');
            writer.print('\\');
            break;
          }
        case '\t':
          {
            if (escapeMode == ESCAPE_COMMENT)
            {
              writer.print(aChar);
            }
            else
            {
              writer.print('\\');
              writer.print('t');
            }
            break;
          }
        case '\n':
          {
            writer.print('\\');
            writer.print('n');
            break;
          }
        case '\r':
          {
            writer.print('\\');
            writer.print('r');
            break;
          }
        case '\f':
          {
            if (escapeMode == ESCAPE_COMMENT)
            {
              writer.print(aChar);
            }
            else
            {
              writer.print('\\');
              writer.print('f');
            }
            break;
          }
        case '#':
        case '"':
        case '!':
        case '=':
        case ':':
          {
            if (escapeMode == ESCAPE_COMMENT)
            {
              writer.print(aChar);
            }
            else
            {
              writer.print('\\');
              writer.print(aChar);
            }
            break;
          }
        default:
          if ((aChar < 0x0020) || (aChar > 0x007e))
          {
            writer.print('\\');
            writer.print('u');
            writer.print(hexChars[(aChar >> 12) & 0xF]);
            writer.print(hexChars[(aChar >> 8) & 0xF]);
            writer.print(hexChars[(aChar >> 4) & 0xF]);
            writer.print(hexChars[aChar & 0xF]);
          }
          else
          {
            writer.print(aChar);
          }
      }
    }
  }

  public String getProperty (final String key)
  {
    return getProperty(key, null);
  }

  public synchronized String getProperty (final String key, final String defaultValue)
  {
    for (int i = 0; i < contents.size(); i++)
    {
      final Object o = contents.get(i);
      if (o instanceof PropertyNode == false)
      {
        continue;
      }

      final PropertyNode pn = (PropertyNode) o;
      if (pn.getKey().equals(key) == false)
      {
        continue;
      }

      return pn.getValue();
    }
    return defaultValue;
  }

  public synchronized void setProperty (final String key, final String value)
  {
    for (int i = 0; i < contents.size(); i++)
    {
      final Object o = contents.get(i);
      if (o instanceof PropertyNode == false)
      {
        continue;
      }

      final PropertyNode pn = (PropertyNode) o;
      if (pn.getKey().equals(key) == false)
      {
        continue;
      }
      if (value == null)
      {
        for (int id = 0; id <= pn.getSubIndex(); id++)
        {
          // also remove the comments for that node..
          contents.remove(getNode(pn.getPropertyIndex(), id));
        }
        return;
      }
      pn.setValue(value);
      return;
    }
    // not found in the existing properties - so create a new entry
    contents.add (new PropertyNode(propertyIndex, subIndex, key, value));
    propertyIndex += 1;
    subIndex = 0;
  }

  public synchronized boolean containsKey (final String key)
  {
    return getNode(key) != null;
  }

  public synchronized String[] getKeys ()
  {
    final ArrayList keys = new ArrayList();
    for (int i = 0; i < contents.size(); i++)
    {
      final Object o = contents.get(i);
      if (o instanceof PropertyNode == false)
      {
        continue;
      }

      final PropertyNode pn = (PropertyNode) o;
      keys.add(pn.getKey());
    }
    return (String[]) keys.toArray(new String[keys.size()]);
  }

  private SortableNode getNode (final int propertyIndex, final int subIndex)
  {
    for (int i = 0; i < contents.size(); i++)
    {
      final SortableNode o = (SortableNode) contents.get(i);
      if (o.subIndex == subIndex && o.propertyIndex == propertyIndex)
      {
        return o;
      }
    }
    return null;
  }

  private PropertyNode getNode (final String key)
  {
    for (int i = 0; i < contents.size(); i++)
    {
      final SortableNode o = (SortableNode) contents.get(i);
      if (o instanceof PropertyNode == false)
      {
        continue;
      }

      final PropertyNode pn = (PropertyNode) o;
      if (key.equals(pn.getKey()))
      {
        return pn;
      }
    }
    return null;
  }

  public synchronized void sort ()
  {
    final String[] keys = getKeys();
    Arrays.sort (keys);

    final ArrayList sortedContents = new ArrayList();
    for (int i = 0; i < keys.length; i++)
    {
      final String key = keys[i];
      final PropertyNode pn = getNode(key);
      for (int x = 0; x <= pn.getSubIndex(); x++)
      {
        final Object node = getNode(pn.getPropertyIndex(), x);
        sortedContents.add(node);
        contents.remove(node);
      }
    }

    // now the content should only contain the trailing comments..
    sortedContents.addAll(contents);
    contents = sortedContents;
  }

  public static void main (final String[] args)
          throws IOException
  {
    BaseBoot.getInstance().start();
    final EditableProperties ep = new EditableProperties();
    final File f = new File ("/home/src/jfreereport/head/jfreereport/source/org/jfree/report/modules/gui/base/resources/jfreereport-resources.properties");
    final FileInputStream fin = new FileInputStream(f);
    ep.load(fin);
    fin.close();

    ep.write(System.err);
  }
}
