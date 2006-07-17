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
 * CollapseTest.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.text.junit;

import junit.framework.TestCase;
import org.jfree.layouting.renderer.text.DefaultRenderableTextFactory;
import org.jfree.layouting.renderer.text.SpacingProducer;
import org.jfree.layouting.renderer.text.StaticSpacingProducer;
import org.jfree.layouting.renderer.text.Spacing;
import org.jfree.layouting.renderer.text.RenderableTextFactory;
import org.jfree.layouting.renderer.text.whitespace.WhiteSpaceFilter;
import org.jfree.layouting.renderer.text.whitespace.PreserveWhiteSpaceFilter;
import org.jfree.layouting.renderer.text.whitespace.CollapseWhiteSpaceFilter;
import org.jfree.layouting.renderer.text.breaks.BreakOpportunityProducer;
import org.jfree.layouting.renderer.text.breaks.WordBreakProducer;
import org.jfree.layouting.renderer.text.font.FontSizeProducer;
import org.jfree.layouting.renderer.text.font.StaticFontSizeProducer;
import org.jfree.layouting.renderer.text.font.KerningProducer;
import org.jfree.layouting.renderer.text.font.NoKerningProducer;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.DefaultElementContext;
import org.jfree.layouting.layouter.context.DefaultLayoutContext;
import org.jfree.layouting.layouter.context.ContextId;

/**
 * Creation-Date: 14.07.2006, 14:12:02
 *
 * @author Thomas Morgner
 */
public class CollapseTest extends TestCase
{
  private class MarginRenderableTextFactory extends DefaultRenderableTextFactory
  {
    public MarginRenderableTextFactory(final LayoutProcess layoutProcess)
    {
      super(layoutProcess);
    }

    protected SpacingProducer createSpacingProducer(final LayoutContext layoutContext)
    {
      return new StaticSpacingProducer(Spacing.EMPTY_SPACING);
    }

    protected FontSizeProducer createFontSizeProducer(final LayoutContext layoutContext)
    {
      return new StaticFontSizeProducer(5000, 5000, 4000);
    }

    protected KerningProducer createKerningProducer(final LayoutContext layoutContext)
    {
      return new NoKerningProducer();
    }

    protected BreakOpportunityProducer createBreakProducer(final LayoutContext layoutContext)
    {
      return new WordBreakProducer();
    }

    protected WhiteSpaceFilter createWhitespaceFilter(final LayoutContext layoutContext)
    {
      return new CollapseWhiteSpaceFilter();
    }

  }

  public CollapseTest()
  {
  }

  public CollapseTest(String string)
  {
    super(string);
  }

  protected void setUp() throws Exception
  {
    LibLayoutBoot.getInstance().start();
  }

  public void testMarginCreation()
  {
    DefaultElementContext elementContext = new DefaultElementContext(null);
    DefaultLayoutContext layoutContext = new DefaultLayoutContext
            (new ContextId(0, 0, 0), "Bah", "buh", null, new AttributeMap());

    RenderableTextFactory tr = new CollapseTest.MarginRenderableTextFactory(null);


    int[] text = new int[]{' ', ' ', ' ', ' ', ' ', ' '};

    tr.startText();
    RenderNode[] rts = tr.createText(text, 0, text.length, layoutContext);
    RenderNode[] fts = tr.finishText();

    assertEquals("Seq 1: Length", 0, rts.length);
    assertEquals("Seq 2: Length", 1, fts.length);

    RenderableText rt0 = (RenderableText) rts[0];
    RenderableText rt1 = (RenderableText) rts[1];
    RenderableText ft0 = (RenderableText) fts[0];
    assertEquals("Chunk 1: Length", 1, rt0.getLength());
    assertEquals("Chunk 2: Length", 1, rt1.getLength());
    assertEquals("Chunk 3: Length", 0, ft0.getLength());

  }


}
