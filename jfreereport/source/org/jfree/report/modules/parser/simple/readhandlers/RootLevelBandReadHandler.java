/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * RootLevelBandReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.simple.FontFactory;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;
import org.jfree.xml.ParserUtil;
import org.xml.sax.SAXException;

public class RootLevelBandReadHandler extends BandReadHandler
{
  /**
   * Literal text for an XML attribute.
   */
  public static final String FIXED_POSITION_ATTRIBUTE = "fixed-position";

  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGEBREAK_BEFORE_ATTR = "pagebreak-before";

  /**
   * Literal text for an XML attribute.
   */
  public static final String HEIGHT_ATTRIBUTE = "height";

  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGEBREAK_AFTER_ATTRIBUTE = "pagebreak-after";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ALIGNMENT_ATT = "alignment";

  /**
   * Literal text for an XML attribute.
   */
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
  protected void startParsing (final PropertyAttributes attr)
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

  protected boolean isManualBreakAllowed ()
  {
    return true;
  }

  private void handleHAlign (final PropertyAttributes attr)
          throws SAXException
  {
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT,
              ReportParserUtil.parseHorizontalElementAlignment(halign));
    }
  }

  private void handleVAlign (final PropertyAttributes attr)
          throws SAXException
  {
    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT,
              ReportParserUtil.parseVerticalElementAlignment(valign));
    }
  }

  protected void handleFixedPosition (final PropertyAttributes attr)
          throws SAXException
  {
    final String fixedPos = attr.getValue(FIXED_POSITION_ATTRIBUTE);
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!");
      getBand().getStyle().setStyleProperty(BandStyleKeys.FIXED_POSITION,
              new Float(fixedPosValue));
    }
  }

  private void handleHeight (final PropertyAttributes attr)
  {
    final String height = attr.getValue(HEIGHT_ATTRIBUTE);
    if (height != null)
    {
      final float heightValue = ParserUtil.parseFloat(height, 0);
      getBand().getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
              new FloatDimension(0, heightValue));
    }
  }

  private void handleBreakBefore (final PropertyAttributes attr)
  {
    final String breakBeforeAttr = attr.getValue(PAGEBREAK_BEFORE_ATTR);
    if (breakBeforeAttr != null)
    {
      final boolean breakBefore = ParserUtil.parseBoolean(breakBeforeAttr, false);
      getBand().getStyle().setBooleanStyleProperty
              (BandStyleKeys.PAGEBREAK_BEFORE, breakBefore);
    }
  }

  private void handleBreakAfter (final PropertyAttributes attr)
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
