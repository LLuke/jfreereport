/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * GraphicsRenderer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageDrawable.java,v 1.13 2006/11/07 19:53:54 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.pageable.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowInfoStructure;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.cells.TableCell;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.text.Glyph;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;

/**
 * The page drawable is the content provider for the Graphics2DOutputTarget.
 * This component is responsible for rendering the current page to a Graphics2D
 * object.
 *
 * @author Thomas Morgner
 */
public class PageDrawableImpl implements PageDrawable
{
  private RenderBox rootBox;
  private long width;
  private long height;
  private double zoom;

  public PageDrawableImpl(final RenderBox rootBox,
                          final long width,
                          final long height)
  {
    this.rootBox = rootBox;
    this.width = width;
    this.height = height;
  }

  public double getZoom()
  {
    return zoom;
  }

  public void setZoom(final double zoom)
  {
    this.zoom = zoom;
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio
   * aware, these bounds should be used to compute the preferred aspect ratio
   * for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    final double width =
        StrictGeomUtility.toExternalValue((long) (this.width * zoom));
    final double height =
        StrictGeomUtility.toExternalValue((long) (this.height * zoom));
    return new Dimension((int) width, (int) height);
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the
   * drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(Graphics2D g2, Rectangle2D area)
  {
    g2.setPaint(Color.white);
    g2.fill(area);

//    g2.translate(-area.getX(), -area.getY());

    final Rectangle2D.Double bounds = new Rectangle2D.Double
            (rootBox.getX() / 1000f, rootBox.getY() / 1000f,
                    rootBox.getWidth() / 1000f, rootBox.getHeight() / 1000f);
    g2.setPaint(Color.black);
    g2.draw(bounds);
    drawBox(g2, rootBox, 0);
  }

  private void drawDebugBox (Graphics2D g2, RenderBox box)
  {
    if (box instanceof TableCellRenderBox)
    {
      g2.setPaint(Color.yellow);
    }
    else if (box instanceof TableRowRenderBox)
    {
      g2.setPaint(Color.green);
    }
    else if (box instanceof TableSectionRenderBox)
    {
      g2.setPaint(Color.red);
    }
    else if (box instanceof TableRenderBox)
    {
      g2.setPaint(Color.blue);
    }
    else if (box instanceof ParagraphRenderBox)
    {
      g2.setPaint(Color.magenta);
    }
    else if (box instanceof ParagraphPoolBox)
    {
      g2.setPaint(Color.orange);
    }
    else
    {
      g2.setPaint(Color.lightGray);
    }
    final int x = (int) (box.getX() / 1000);
    final int y = (int) (box.getY() / 1000);
    final int w = (int) (box.getWidth() / 1000);
    final int h = (int) (box.getHeight() / 1000);
    g2.drawRect(x, y, w, h);
  }

  public void drawBox(Graphics2D g2, RenderBox box, int level)
  {
    //BorderShapeFactory borderShapeFactory = new BorderShapeFactory(box);
    //borderShapeFactory.generateBorder(g2);
    drawDebugBox(g2, box);

    RenderNode childs = box.getFirstChild();
    while (childs != null)
    {
      if (childs instanceof RenderBox)
      {
        drawBox(g2, (RenderBox) childs, level + 1);
      }
      else if (childs instanceof RenderableText)
      {
        drawText(g2, (RenderableText) childs);
      }
      else if (childs instanceof RenderableReplacedContent)
      {
        drawReplacedContent(g2, (RenderableReplacedContent) childs);
      }
      childs = childs.getNext();
    }
  }

  private void drawReplacedContent(final Graphics2D g2,
                                   final RenderableReplacedContent content)
  {
    Object o = content.getRawObject();
    if (o instanceof Image)
    {
      final int x = (int) StrictGeomUtility.toExternalValue(content.getX());
      final int y = (int) StrictGeomUtility.toExternalValue(content.getY());
      final int width = (int) StrictGeomUtility.toExternalValue(content.getWidth());
      final int height = (int) StrictGeomUtility.toExternalValue(content.getHeight());
      g2.drawImage((Image) o, x, y, width, height, null);
    }
    else if (o instanceof Drawable)
    {
      final double x = (int) StrictGeomUtility.toExternalValue(content.getX());
      final double y = (int) StrictGeomUtility.toExternalValue(content.getY());
      final double width = (int) StrictGeomUtility.toExternalValue(content.getWidth());
      final double height = (int) StrictGeomUtility.toExternalValue(content.getHeight());
      Drawable d = (Drawable) o;
      d.draw(g2, new Rectangle2D.Double(x,y,width, height));
    }
  }

  private void drawText(final Graphics2D g2,
                        final RenderableText renderableText)
  {
    Glyph[] gs = renderableText.getGlyphs();
    long posX = renderableText.getX();
    long posY = renderableText.getY();
    long runningPos = posX;

    final LayoutContext layoutContext = renderableText.getLayoutContext();
    final FontSpecification fontSpecification =
            layoutContext.getFontSpecification();

    int style = Font.PLAIN;
    if (fontSpecification.getFontWeight() > 400)
    {
      style |= Font.BOLD;
    }
    if (fontSpecification.isItalic() || fontSpecification.isOblique())
    {
      style |= Font.ITALIC;
    }

    final CSSColorValue cssColor = (CSSColorValue)
            layoutContext.getStyle().getValue(ColorStyleKeys.COLOR);

    g2.setColor(cssColor);
    g2.setFont(new Font(fontSpecification.getFontFamily(), style,
            (int) fontSpecification.getFontSize()));

    int length = renderableText.getOffset() + renderableText.getLength();
    for (int i = renderableText.getOffset(); i < length; i++)
    {
      Glyph g = gs[i];
      g2.drawString(glpyhToString(g), runningPos / 1000f, (posY + g.getBaseLine()) / 1000f);
      runningPos += g.getWidth();
    }
  }

  public static String glpyhToString(Glyph g)
  {
    StringBuffer b = new StringBuffer();
    b.append((char) (0xffff & g.getCodepoint()));
    int[] extraCPs = g.getExtraChars();
    for (int i = 0; i < extraCPs.length; i++)
    {
      b.append(", ");
      int extraCP = extraCPs[i];
      b.append(extraCP);
    }
    return b.toString();
  }


  public void print()
  {
    printBox(rootBox, 0);
  }

  public void printBox(RenderBox box, int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append(box.getClass().getName());
    b.append("[");
    b.append(Integer.toHexString(System.identityHashCode(box)));
    b.append("]");
    b.append("={x=");
    b.append(box.getX());
    b.append(", y=");
    b.append(box.getY());
    b.append(", width=");
    b.append(box.getWidth());
    b.append(", height=");
    b.append(box.getHeight());
    b.append("}");
    Log.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(box.getNodeLayoutProperties());
    Log.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- boxLayoutProperties=");
    b.append(box.getBoxLayoutProperties());
    Log.debug(b.toString());

    if (box instanceof TableRowRenderBox)
    {
      TableRowRenderBox row = (TableRowRenderBox) box;
      final TableRowInfoStructure rowInfoStructure = row.getRowInfoStructure();

      for (int i = 0; i < rowInfoStructure.getCellCount(); i++)
      {
        TableCell cell = rowInfoStructure.getCellAt(i);
        Log.debug ("CELL: " + i + " = " + cell.getRowSpan() + " " + cell.getColSpan() + " " + cell);
      }
    }
    else if (box instanceof TableRenderBox)
    {
      TableRenderBox table = (TableRenderBox) box;
      final TableColumnModel columnModel = table.getColumnModel();
      for (int i = 0; i < columnModel.getColumnCount(); i++)
      {
        final TableColumn col = columnModel.getColumn(i);
        Log.debug ("COLUMN: EffectiveSize: " + col.getEffectiveSize() +  " Computed Max Width: " + col.getComputedMaximumWidth() + " Computed ChunkSize: " + col.getComputedMinChunkSize());
//        for (int cs = 1; cs < 3; cs++)
//        {
//          Log.debug ("* COLUMN: " + i + "(" + cs + ") " +
//                  col.getPreferredSize(cs) + " " +
//                  col.getMinimumChunkSize(cs));
//        }
//        Log.debug ("COLUMN: " + i + " " +
//                col.getPreferredSize() + " " +
//                col.getMinimumChunkSize());
      }
    }
    else if (box instanceof TableCellRenderBox)
    {
      TableCellRenderBox cellBox = (TableCellRenderBox) box;
      Log.debug ("CELL: Position: " + cellBox.getColumnIndex());
    }
//    else if (box instanceof ParagraphRenderBox)
//    {
//      ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
//      printBox(paraBox.getLineboxContainer(), level + 1);
//    }

    printChilds(box, level);
  }

  private void printChilds(final RenderBox box, final int level)
  {
    RenderNode childs = box.getFirstChild();
    while (childs != null)
    {
      if (childs instanceof RenderBox)
      {
        printBox((RenderBox) childs, level + 1);
      }
      else if (childs instanceof RenderableText)
      {
        printText((RenderableText) childs, level + 1);
      }
      else
      {
        printNode(childs, level + 1);
      }
      childs = childs.getNext();
    }
  }

  private void printNode(final RenderNode node, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append(node.getClass().getName());
    b.append("[");
    b.append(Integer.toHexString(System.identityHashCode(node)));
    b.append("]");
    b.append("={x=");
    b.append(node.getX());
    b.append(", y=");
    b.append(node.getY());
    b.append(", width=");
    b.append(node.getWidth());
    b.append(", height=");
    b.append(node.getHeight());
    b.append("}");
    Log.debug(b.toString());


    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(node.getNodeLayoutProperties());
    Log.debug(b.toString());
  }

  private void printText(final RenderableText text, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("Text");
    b.append("[");
    b.append(Integer.toHexString(System.identityHashCode(text)));
    b.append("]");
    b.append("={x=");
    b.append(text.getX());
    b.append(", y=");
    b.append(text.getY());
    b.append(", width=");
    b.append(text.getWidth());
    b.append(", height=");
    b.append(text.getHeight());
    b.append(", text='");
    b.append(text.getRawText());
    b.append("'}");
    Log.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(text.getNodeLayoutProperties());
    Log.debug(b.toString());
  }

}
