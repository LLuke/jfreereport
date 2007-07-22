package org.jfree.fonts.itext;

import java.util.Arrays;

import com.lowagie.text.pdf.BaseFont;
import org.jfree.fonts.encoding.CodePointUtilities;
import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontMetrics;

/**
 * Creation-Date: 22.07.2007, 19:04:00
 *
 * @author Thomas Morgner
 */
public class BaseFontFontMetrics implements FontMetrics
{
  private BaseFont baseFont;
  private float size;
  private float xHeight;
  private char[] cpBuffer;
  private double[] cachedWidths;
  private BaselineInfo[] cachedBaselines;

  public BaseFontFontMetrics(final BaseFont baseFont, final float size)
  {
    this.baseFont = baseFont;
    this.size = size;
    this.cpBuffer = new char[4];
    this.cachedBaselines = new BaselineInfo[256- 32];
    this.cachedWidths = new double[256- 32];
    Arrays.fill(cachedWidths, -1);

    this.xHeight = this.baseFont.getCharBBox('x')[3] * size;
    if (xHeight == 0)
    {
      this.xHeight = (float) getAscent() / 2f;
    }
  }

  public double getAscent()
  {
    return baseFont.getFontDescriptor(BaseFont.AWT_ASCENT, size);
  }

  public double getDescent()
  {
    return baseFont.getFontDescriptor(BaseFont.AWT_DESCENT, size);
  }

  public double getLeading()
  {
    return baseFont.getFontDescriptor(BaseFont.AWT_LEADING, size);
  }

  public double getXHeight()
  {
    return xHeight;
  }

  public double getOverlinePosition()
  {
    return 0;
  }

  public double getUnderlinePosition()
  {
    return 0;
  }

  public double getStrikeThroughPosition()
  {
    return 0;
  }

  public double getMaxAscent()
  {
    return baseFont.getFontDescriptor(BaseFont.BBOXURY, size);
  }

  public double getMaxDescent()
  {
    return baseFont.getFontDescriptor(BaseFont.BBOXLLY, size);
  }

  public double getMaxLeading()
  {
    return getLeading();
  }

  public double getMaxHeight()
  {
    return getMaxAscent() + getMaxDescent() + getMaxLeading();
  }

  public double getMaxCharAdvance()
  {
    return baseFont.getFontDescriptor(BaseFont.AWT_MAXADVANCE, size);
  }

  public double getCharWidth(final int character)
  {
    if (character >=32 && character < 256)
    {
      // can be cached ..
      final int index = character - 32;
      final double cachedWidth = cachedWidths[index];
      if (cachedWidth >= 0)
      {
        return cachedWidth;
      }

      final int retval = CodePointUtilities.toChars(character, cpBuffer, 0);

      if (retval > 0)
      {
        final double width = baseFont.getWidthPoint(new String (cpBuffer), size);
        cachedWidths[index] = width;
        return width;
      }
      else
      {
        cachedWidths[index] = 0;
        return 0;
      }
    }

    final int retval = CodePointUtilities.toChars(character, cpBuffer, 0);

    if (retval > 0)
    {
      return baseFont.getWidthPoint(new String (cpBuffer), size);
    }
    else
    {
      return 0;
    }
  }

  public double getKerning(final int previous, final int codePoint)
  {
    return baseFont.getKerning((char) previous, (char) codePoint);
  }

  public BaselineInfo getBaselines(final int c, BaselineInfo info)
  {
    final boolean cacheable = (c >=32 && c < 256);
    if (cacheable)
    {
      final BaselineInfo fromCache = cachedBaselines[c - 32];
      if (fromCache != null)
      {
        if (info == null)
        {
          info = new BaselineInfo();
        }
        info.update(fromCache);
        return info;
      }
    }

    cpBuffer[0] = (char) (c & 0xFFFF);

    if (info == null)
    {
      info = new BaselineInfo();
    }

    // If we had more data, we could surely create something better. Well, this has to be enough ..
    final double maxAscent = getMaxAscent();
    info.setBaseline(BaselineInfo.MATHEMATICAL, maxAscent - getXHeight());
    info.setBaseline(BaselineInfo.IDEOGRAPHIC, getMaxHeight());
    info.setBaseline(BaselineInfo.MIDDLE, maxAscent / 2);
    info.setBaseline(BaselineInfo.ALPHABETIC, maxAscent);
    info.setBaseline(BaselineInfo.CENTRAL, maxAscent / 2);
    info.setBaseline(BaselineInfo.HANGING, maxAscent - getXHeight());
    info.setDominantBaseline(BaselineInfo.ALPHABETIC);

    if (cacheable)
    {
      final BaselineInfo cached = new BaselineInfo();
      cached.update(info);
      cachedBaselines[c - 32] = cached;
    }

    return info;

  }
}
