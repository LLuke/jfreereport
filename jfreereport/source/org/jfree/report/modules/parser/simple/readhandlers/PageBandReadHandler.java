package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class PageBandReadHandler extends RootLevelBandReadHandler
{
  /** Literal text for an XML attribute. */
  public static final String ON_FIRST_PAGE_ATTR = "onfirstpage";

  /** Literal text for an XML attribute. */
  public static final String ON_LAST_PAGE_ATTR = "onlastpage";

  public PageBandReadHandler (final Band band)
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
    handleOnFirstPage(attr);
    handleOnLastPage(attr);
  }

  protected boolean isManualBreakAllowed ()
  {
    return false;
  }

  private void handleOnFirstPage (final Attributes attr)
  {
    final String breakBeforeAttr = attr.getValue(ON_FIRST_PAGE_ATTR);
    if (breakBeforeAttr != null)
    {
      final boolean breakBefore = ParserUtil.parseBoolean(breakBeforeAttr, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.DISPLAY_ON_FIRSTPAGE, breakBefore);
    }
  }

  private void handleOnLastPage (final Attributes attr)
  {
    final String breakBeforeAttr = attr.getValue(ON_LAST_PAGE_ATTR);
    if (breakBeforeAttr != null)
    {
      final boolean breakBefore = ParserUtil.parseBoolean(breakBeforeAttr, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.DISPLAY_ON_LASTPAGE, breakBefore);
    }
  }
}
