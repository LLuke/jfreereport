/**
 * Date: Nov 30, 2002
 * Time: 8:23:00 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.output;

import com.lowagie.text.pdf.BaseFont;

import java.awt.Font;

public class PDFFontRecord
{
  private Font awtFont;
  private BaseFont baseFont;
  private String logicalName;
  private boolean embedded;
  private String encoding;

  public PDFFontRecord()
  {
  }

  public PDFFontRecordKey createKey ()
  {
    return new PDFFontRecordKey(getLogicalName(), getEncoding());
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }

  public boolean isEmbedded()
  {
    return embedded;
  }

  public void setEmbedded(boolean embedded)
  {
    this.embedded = embedded;
  }

  public String getLogicalName()
  {
    return logicalName;
  }

  public void setLogicalName(String logicalName)
  {
    this.logicalName = logicalName;
  }

  public Font getAwtFont()
  {
    return awtFont;
  }

  public void setAwtFont(Font awtFont)
  {
    this.awtFont = awtFont;
  }

  public BaseFont getBaseFont()
  {
    return baseFont;
  }

  public void setBaseFont(BaseFont baseFont)
  {
    this.baseFont = baseFont;
  }

  public float getFontHeight()
  {
    if (awtFont == null) return -1;
    return getAwtFont().getSize2D();
  }
}
