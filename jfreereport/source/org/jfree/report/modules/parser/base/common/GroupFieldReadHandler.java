package org.jfree.report.modules.parser.base.common;

import org.jfree.report.modules.parser.base.PropertyStringReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class GroupFieldReadHandler extends PropertyStringReadHandler
{
  public GroupFieldReadHandler (final CommentHintPath hintPath)
  {
    super(hintPath);
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    getHintPath().addName(getResult());
    super.doneParsing();
  }
}
