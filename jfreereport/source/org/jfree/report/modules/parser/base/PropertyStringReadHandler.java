package org.jfree.report.modules.parser.base;

import org.jfree.report.util.PropertyLookupParser;
import org.xml.sax.SAXException;

public class PropertyStringReadHandler extends AbstractPropertyXmlReadHandler
{
  private class StringLookupParser extends PropertyLookupParser
  {
    public StringLookupParser ()
    {
    }

    protected Object performInitialLookup (final String name)
    {
      return getRootHandler().getHelperObject(name);
    }
  }


  private CommentHintPath hintPath;
  private StringBuffer buffer;
  private StringLookupParser lookupParser;

  public PropertyStringReadHandler (final CommentHintPath hintPath)
  {
    this.hintPath = hintPath;
    this.buffer = new StringBuffer(100);
    this.lookupParser = new StringLookupParser();
  }

  protected void storeComments ()
          throws SAXException
  {
    if (hintPath != null)
    {
      defaultStoreComments(hintPath);
    }
  }

  /**
   * May be null...
   *
   * @return
   */
  protected CommentHintPath getHintPath ()
  {
    return hintPath;
  }

  /**
   * This method is called to process the character data between element tags.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  public void characters (final char[] ch, final int start, final int length)
          throws SAXException
  {
    buffer.append(ch, start, length);
  }

  public String getResult ()
  {
    return lookupParser.translateAndLookup(buffer.toString());
  }


  /**
   * Returns the object for this element.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return getResult();
  }
}
