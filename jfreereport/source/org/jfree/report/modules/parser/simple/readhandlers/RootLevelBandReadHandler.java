package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.simple.FontFactory;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RootLevelBandReadHandler extends BandReadHandler
{
  /** Literal text for an XML attribute. */
  public static final String FIXED_POSITION_ATTRIBUTE = "fixed-position";

  /** Literal text for an XML attribute. */
  public static final String PAGEBREAK_BEFORE_ATTR = "pagebreak-before";

  /** Literal text for an XML attribute. */
  public static final String HEIGHT_ATTRIBUTE = "height";

  /** Literal text for an XML attribute. */
  public static final String PAGEBREAK_AFTER_ATTRIBUTE = "pagebreak-after";

  /** Literal text for an XML attribute. */
  public static final String ALIGNMENT_ATT = "alignment";

  /** Literal text for an XML attribute. */
  public static final String VALIGNMENT_ATT = "vertical-alignment";


  public RootLevelBandReadHandler (final Band band)
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
    handleHeight(attr);

    if (isManualBreakAllowed())
    {
      handleBreakAfter(attr);
      handleBreakBefore(attr);
    }

    handleVAlign(attr);
    handleHAlign(attr);

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(getBand().getStyle(), fi);

  }

  protected boolean isManualBreakAllowed()
  {
    return true;
  }

  private void handleHAlign (final Attributes attr)
          throws SAXException
  {
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }
  }

  private void handleVAlign (final Attributes attr)
          throws SAXException
  {
    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
  }

  protected void handleFixedPosition (final Attributes attr)
          throws SAXException
  {
    final String fixedPos = attr.getValue(FIXED_POSITION_ATTRIBUTE);
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!");
      getBand().getStyle().setStyleProperty(BandStyleKeys.FIXED_POSITION,
              new Float (fixedPosValue));
    }
  }

  private void handleHeight (final Attributes attr)
  {
    final String height = attr.getValue(HEIGHT_ATTRIBUTE);
    if (height != null)
    {
      final float heightValue = ParserUtil.parseFloat(height, 0);
      getBand().getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
          new FloatDimension(0, heightValue));
    }
  }

  private void handleBreakBefore (final Attributes attr)
  {
    final String breakBeforeAttr = attr.getValue(PAGEBREAK_BEFORE_ATTR);
    if (breakBeforeAttr != null)
    {
      final boolean breakBefore = ParserUtil.parseBoolean(breakBeforeAttr, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_BEFORE, breakBefore);
    }
  }

  private void handleBreakAfter (final Attributes attr)
  {
    final String breakAfterAttr = attr.getValue(PAGEBREAK_AFTER_ATTRIBUTE);
    if (breakAfterAttr != null)
    {
      final boolean breakAfter = ParserUtil.parseBoolean(breakAfterAttr, false);
      getBand().getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_AFTER, breakAfter);
    }
  }
}
