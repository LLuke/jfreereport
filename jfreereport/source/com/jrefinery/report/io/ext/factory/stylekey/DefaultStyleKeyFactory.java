/**
 * Date: Jan 9, 2003
 * Time: 9:00:14 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.io.ext.factory.stylekey.AbstractStyleKeyFactory;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;

public class DefaultStyleKeyFactory extends AbstractStyleKeyFactory
{
  public DefaultStyleKeyFactory()
  {
    addKey(ElementStyleSheet.ALIGNMENT);
    addKey(ElementStyleSheet.BOLD);
    addKey(ElementStyleSheet.BOUNDS);
    addKey(ElementStyleSheet.FONT);
    addKey(ElementStyleSheet.FONTSIZE);
    addKey(ElementStyleSheet.ITALIC);
    addKey(ElementStyleSheet.KEEP_ASPECT_RATIO);
    addKey(ElementStyleSheet.MAXIMUMSIZE);
    addKey(ElementStyleSheet.MINIMUMSIZE);
    addKey(ElementStyleSheet.PAINT);
    addKey(ElementStyleSheet.PREFERREDSIZE);
    addKey(ElementStyleSheet.SCALE);
    addKey(ElementStyleSheet.STRIKETHROUGH);
    addKey(ElementStyleSheet.STROKE);
    addKey(ElementStyleSheet.UNDERLINED);
    addKey(ElementStyleSheet.VALIGNMENT);
    addKey(ElementStyleSheet.VISIBLE);
    addKey(BandStyleSheet.DISPLAY_ON_FIRSTPAGE);
    addKey(BandStyleSheet.DISPLAY_ON_LASTPAGE);
    addKey(BandStyleSheet.PAGEBREAK_AFTER);
    addKey(BandStyleSheet.PAGEBREAK_BEFORE);
    addKey(BandStyleSheet.REPEAT_HEADER);
    addKey(ShapeElement.DRAW_SHAPE);
    addKey(ShapeElement.FILL_SHAPE);
  }
}
