package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.util.Log;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportFooterReadHandler extends RootLevelBandReadHandler
{
  public ReportFooterReadHandler (final Band band)
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
    handleOwnPageAttr(attr);
  }

  private void handleOwnPageAttr (final Attributes attr)
  {
    final String ownPageAttr = attr.getValue("ownpage");
    if (ownPageAttr != null)
    {
      Log.warn ("The 'ownpage' attribute of the <report-footer> tag is deprecated. " +
              "Use the 'pagebreak-before' attribute instead.");
      final boolean ownPage = ParserUtil.parseBoolean(ownPageAttr, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_BEFORE, ownPage);
    }
  }

}
