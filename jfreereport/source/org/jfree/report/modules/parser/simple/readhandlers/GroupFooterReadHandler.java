package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.util.Log;
import org.jfree.xml.ParserUtil;
import org.xml.sax.SAXException;

public class GroupFooterReadHandler extends RootLevelBandReadHandler
{
  public GroupFooterReadHandler (final Band band)
  {
    super(band);
  }


  /**
   * Starts parsing.
   *
   * @param attr the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attr)
          throws SAXException
  {
    super.startParsing(attr);
    handlePagebreakAttr(attr);
    handleFixedPosition(attr);
  }

  private void handlePagebreakAttr (final PropertyAttributes attr)
  {
    final String ownPageAttr = attr.getValue("pagebreak");
    if (ownPageAttr != null)
    {
      Log.warn ("The 'pagebreak' attribute of the <group-footer> tag is deprecated. " +
              "Use the 'pagebreak-after' attribute instead.");
      final boolean ownPage = ParserUtil.parseBoolean(ownPageAttr, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_AFTER, ownPage);
    }
  }
}
