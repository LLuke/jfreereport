/**
 * Date: Jan 25, 2003
 * Time: 5:52:00 AM
 *
 * $Id: HtmlTextCellData.java,v 1.2 2003/01/27 03:17:43 taqua Exp $
 */
package com.jrefinery.report.targets.table.rtf;

import com.lowagie.text.Cell;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;

import java.awt.geom.Rectangle2D;

public class RTFTextCellData extends RTFCellData
{
  private String value;

  public RTFTextCellData(Rectangle2D outerBounds, String value, RTFCellStyle style)
  {
    super(outerBounds, style);
    if (value == null) throw new NullPointerException();
    this.value = value;
  }

  public Cell getCell()
    throws DocumentException
  {
    Cell cell = new Cell();
    cell.setBorderWidth(0);

    Chunk chunk = new Chunk(value);
    getStyle().applyTextStyle(chunk);
    Paragraph paragraph  = new Paragraph();
    paragraph.add(chunk);
    cell.addElement(paragraph);
    return cell;
  }

  public boolean isBackground()
  {
    return false;
  }
}
