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
 * $Id: PageDrawable.java,v 1.1 2006/07/11 13:54:24 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.pageable.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.pageable.BorderShapeFactory;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.text.Glyph;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;

/**
 * Creation-Date: 25.06.2006, 16:27:28
 *
 * @author Thomas Morgner
 */
public class PageDrawable implements Drawable
{
  private RenderBox rootBox;
  private AWTFontRegistry fontRegistry;

  public PageDrawable(final RenderBox rootBox)
  {
    this.rootBox = rootBox;
    this.fontRegistry = new AWTFontRegistry();
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(Graphics2D g2, Rectangle2D area)
  {
    g2.setPaint(Color.WHITE);
    g2.fill(area);

    g2.translate(-area.getX(), -area.getY());
    //g2.scale(4, 4);
    final Rectangle2D.Double bounds = new Rectangle2D.Double
            (rootBox.getX() / 1000f, rootBox.getY() / 1000f,
                    rootBox.getWidth() / 1000f, rootBox.getHeight() / 1000f);
    g2.setPaint(Color.BLACK);
    g2.draw(bounds);
    drawBox(g2, rootBox, 0);
  }



  public void drawBox(Graphics2D g2, RenderBox box, int level)
  {
    if (box.getState() == RenderNodeState.UNCLEAN)
    {
      Log.warn("Box is unclean: " + level + ": " + box + " (" + box.getParent() + ")");
    }

    final double x = StrictGeomUtility.toExternalValue
            (box.getX() + box.getMargins().getLeft());
    final double y = StrictGeomUtility.toExternalValue
            (box.getY() + box.getMargins().getTop());
    final double w = StrictGeomUtility.toExternalValue
            (box.getWidth() - box.getMargins().getLeft() +
                    box.getMargins().getRight());
    final double h = StrictGeomUtility.toExternalValue
            (box.getHeight() - box.getMargins().getTop() +
                    box.getMargins().getBottom());

    BorderShapeFactory borderShapeFactory = new BorderShapeFactory(box);
    borderShapeFactory.generateBorder(g2);

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
      childs = childs.getNext();
    }
  }

  public AWTFontRegistry getFontRegistry()
  {
    return fontRegistry;
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
            (int) fontSpecification.getFontSize() ));

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
    if (box.isKillMePlease())
    {
      throw new IllegalStateException();
    }

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
    b.append(", padding=");
    b.append(box.getPaddings());
    b.append("}");

    Log.debug(b.toString());

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
      childs = childs.getNext();
    }
  }

  private void printText(final RenderableText text, final int level)
  {
    if (text.isKillMePlease())
    {
      throw new IllegalStateException();
    }

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
    b.append(", text=");

    Glyph[] gs = text.getGlyphs();
    int length = text.getOffset() + text.getLength();
    for (int i = text.getOffset(); i < length; i++)
    {
      Glyph g = gs[i];
      b.append(glpyhToString(g));
    }
    b.append("}");
    Log.debug(b.toString());
  }

}
