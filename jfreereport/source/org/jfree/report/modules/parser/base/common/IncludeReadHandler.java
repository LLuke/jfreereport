package org.jfree.report.modules.parser.base.common;

import java.io.IOException;
import java.net.URL;

import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.IncludeParserFrontend;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class IncludeReadHandler extends AbstractPropertyXmlReadHandler
{
  private URL target;

  public IncludeReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final String file = attrs.getValue("src");
    if (file == null)
    {
      throw new SAXException("Required attribute 'src' is missing.");
    }

    try
    {
      target = new URL(getRootHandler().getContentBase(), file);
      final IncludeParserFrontend parserFrontend =
              new IncludeParserFrontend(getRootHandler());
      parserFrontend.parse(target);
    }
    catch (IOException e)
    {
      throw new SAXException("Failure while including external report definition.", e);
    }
  }

  /**
   * Returns the object for this element (if any).
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return null;
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath("include");
    commentHintPath.addName(target);
    defaultStoreComments(commentHintPath);
  }
}
