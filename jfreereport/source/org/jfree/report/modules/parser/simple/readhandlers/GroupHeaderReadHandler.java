package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.util.Log;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GroupHeaderReadHandler extends RootLevelBandReadHandler
{
  public GroupHeaderReadHandler (final Band band)
  {
    super(band);
  }

  /**
   * Starts parsing.
   *
   * @param attr the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes attr)
          throws SAXException
  {
    super.startParsing(attr);
    handlePagebreakAttr(attr);
    handleFixedPosition(attr);
    handleRepeat(attr);
  }

  private void handleRepeat (final Attributes attr)
  {
    final String repeat = attr.getValue("repeat");
    if (repeat != null)
    {
      final boolean repeatVal = ParserUtil.parseBoolean(repeat, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.REPEAT_HEADER, repeatVal);
    }
  }

  private void handlePagebreakAttr (final Attributes attr)
  {
    final String ownPageAttr = attr.getValue("pagebreak");
    if (ownPageAttr != null)
    {
      Log.warn ("The 'pagebreak' attribute of the <group-header> tag is deprecated. " +
              "Use the 'pagebreak-before' attribute instead.");
      final boolean ownPage = ParserUtil.parseBoolean(ownPageAttr, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_BEFORE, ownPage);
    }
  }
}
