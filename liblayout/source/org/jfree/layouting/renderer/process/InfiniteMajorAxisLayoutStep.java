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
 * InfiniteMajorAxisLayoutStep.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: InfiniteMajorAxisLayoutStep.java,v 1.2 2006/10/22 14:58:26 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import java.util.Stack;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxLayoutProperties;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.NodeLayoutProperties;
import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.SpacerRenderNode;
import org.jfree.layouting.renderer.model.StaticBoxLayoutProperties;
import org.jfree.layouting.renderer.model.FinishedRenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.process.valign.BoxAlignContext;
import org.jfree.layouting.renderer.process.valign.InlineBlockAlignContext;
import org.jfree.layouting.renderer.process.valign.NodeAlignContext;
import org.jfree.layouting.renderer.process.valign.TextElementAlignContext;
import org.jfree.layouting.renderer.process.valign.VerticalAlignmentProcessor;

/**
 * This process-step computes the vertical alignment and corrects the
 * y-positions whereever needed.
 * <p/>
 * This will only work, if the minor-axis step has been executed.
 *
 * @author Thomas Morgner
 */
public class InfiniteMajorAxisLayoutStep
    extends IterateVisualProcessStep
{
  public class ParagraphBreakState
  {
    private Object suspendItem;
    private Stack contexts;
    private ParagraphRenderBox paragraph;

    public ParagraphBreakState(final ParagraphRenderBox paragraph)
    {
      if (paragraph == null)
      {
        throw new NullPointerException();
      }
      this.paragraph = paragraph;
      this.contexts = new Stack();
    }

    public ParagraphRenderBox getParagraph()
    {
      return paragraph;
    }

    public Object getSuspendItem()
    {
      return suspendItem;
    }

    public void setSuspendItem(final Object suspendItem)
    {
      this.suspendItem = suspendItem;
    }

    public boolean isSuspended()
    {
      return suspendItem != null;
    }

    public BoxAlignContext getCurrentLine()
    {
      if (contexts.isEmpty())
      {
        return null;
      }
      return (BoxAlignContext) contexts.peek();
    }

    public void openContext (BoxAlignContext context)
    {
      if (contexts.isEmpty() == false)
      {
        final BoxAlignContext boxAlignContext =
            (BoxAlignContext) contexts.peek();
        boxAlignContext.addChild(context);
      }
      contexts.push (context);
    }

    public BoxAlignContext closeContext()
    {
      return (BoxAlignContext) contexts.pop();
    }
  }


  private ParagraphBreakState breakState;
  private RenderBox continuedElement;

  public InfiniteMajorAxisLayoutStep()
  {
  }

  public void compute(LogicalPageBox pageBox)
  {
    startProcessing(pageBox);
  }

  /**
   * Continues processing. The renderbox must have a valid x-layout (that is: X,
   * content-X1, content-X2 and Width)
   *
   * @param parent
   * @param box
   */
  public void continueComputation(RenderBox box)
  {
    if (box.getContentAreaX2() == 0 || box.getWidth() == 0)
    {
      throw new IllegalStateException("Box must be layouted a bit ..");
    }

    this.continuedElement = box;
    startProcessing(box);
    this.continuedElement = null;
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    // first, compute the position. The position is global, not relative to a
    // parent or so. Therefore a child has no connection to the parent's
    // effective position, when it is painted.

    if (box != continuedElement)
    {
      computeYPosition(box);
      // We have an valid y position now
      // we do not recompute the y-position of the continued element yet as we
      // would not have all context-information here (and it had been done in
      // the calling step anyway)
    }

    if (breakState == null)
    {
      if (box instanceof ParagraphRenderBox)
      {
        final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;
        // We cant cache that ... the shift operations later would misbehave
        // One way around would be to at least store the layouted offsets
        // (which should be immutable as long as the line did not change its
        // contents) and to reapply them on each run. This is cheaper than
        // having to compute the whole v-align for the whole line.
        breakState = new ParagraphBreakState(paragraphBox);
      }

      return true;
    }

    // No breakstate and not being suspended? Why this?
    if (breakState.isSuspended() == false)
    {
      throw new IllegalStateException("This cannot be.");
    }

    // this way or another - we are suspended now. So there is no need to look
    // at the children anymore ..
    return false;
  }

  private void computeYPosition(final RenderNode node)
  {
    long marginTop = 0;
    if (node instanceof RenderBox)
    {
      RenderBox box = (RenderBox) node;
      BoxLayoutProperties blp = box.getBoxLayoutProperties();
      marginTop = blp.getEffectiveMarginTop();
    }

    // The y-position of a box depends on the parent.
    RenderBox parent = node.getParent();

    // A table row is something special. Although it is a block box,
    // it layouts its children from left to right
    if (parent instanceof TableRowRenderBox)
    {
      final StaticBoxLayoutProperties blp = parent.getStaticBoxLayoutProperties();
      final long insetTop = (blp.getBorderTop() + blp.getPaddingTop());

      node.setY(marginTop + insetTop + parent.getY());
    }
    // If the box's parent is a block box ..
    else if (parent instanceof BlockRenderBox)
    {
      final RenderNode prev = node.getPrev();
      if (prev != null)
      {
        // we have a silbling. Position yourself directly below your silbling ..
        node.setY(marginTop + prev.getY() + prev.getHeight());
      }
      else
      {
        final StaticBoxLayoutProperties blp = parent.getStaticBoxLayoutProperties();
        final long insetTop = (blp.getBorderTop() + blp.getPaddingTop());

        node.setY(marginTop + insetTop + parent.getY());
      }
    }
    // The parent is a inline box.
    else if (parent != null)
    {
      final StaticBoxLayoutProperties blp = parent.getStaticBoxLayoutProperties();
      final long insetTop = (blp.getBorderTop() + blp.getPaddingTop());

      node.setY(marginTop + insetTop + parent.getY());
    }
    else
    {
      node.setY(marginTop);
    }
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    // Check the height. Set the height.
    final NodeLayoutProperties nlp = box.getNodeLayoutProperties();
    final RenderLength computedWidth = nlp.getComputedWidth();
    final RenderLength preferredHeight = box.getBoxDefinition().getPreferredHeight();
    final long computedHeight =
        preferredHeight.resolve(computedWidth.resolve(0));

    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final long insetBottom = blp.getBorderBottom() + blp.getPaddingBottom();

    final RenderNode lastChildNode = box.getLastChild();
    if (lastChildNode != null)
    {
      // grab the node's y2
      final long childY2;
      if (lastChildNode instanceof RenderBox)
      {
        RenderBox childBox = (RenderBox) lastChildNode;
        childY2 = lastChildNode.getY() + lastChildNode.getHeight() +
            childBox.getBoxLayoutProperties().getEffectiveMarginBottom();
      }
      else
      {
        childY2 = lastChildNode.getY() + lastChildNode.getHeight();
      }
      final long effectiveHeight = (childY2 - box.getY()) + insetBottom;
      final long height = Math.max(effectiveHeight, computedHeight);
      box.setHeight(height);
    }
    else
    {
      final long insetTop = blp.getBorderTop() + blp.getBorderTop();
      box.setHeight(Math.max(computedHeight, insetTop + insetBottom));
    }

    if (breakState != null)
    {
      final Object suspender = breakState.getSuspendItem();
      if (box.getInstanceId() == suspender)
      {
        breakState.setSuspendItem(null);
        return;
      }
      if (suspender != null)
      {
        return;
      }

      if (box instanceof ParagraphRenderBox)
      {
        // finally update the change tracker ..
        ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
        paraBox.setMajorLayoutAge(paraBox.getLineboxContainer().getChangeTracker());

        breakState = null;
      }
    }

  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    // todo: Inline level boxes may have margins ...
    computeYPosition(box);
    computeBaselineInfo(box);

    if (breakState == null)
    {
      // ignore .. should not happen anyway ..
      return true;
    }

    if (breakState.isSuspended())
    {
      return false;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.openContext(new BoxAlignContext(box));
      return true;
    }

    breakState.getCurrentLine().addChild(new InlineBlockAlignContext(box));
    breakState.setSuspendItem(box.getInstanceId());
    return false;
  }

  private void computeBaselineInfo(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if (node instanceof RenderableText)
      {
        // grab the baseline info from there ...
        RenderableText text = (RenderableText) node;
        box.setBaselineInfo(text.getBaselineInfo());
        break;
      }

      node = node.getNext();
    }

    if (box.getBaselineInfo() == null)
    {
      // If we have no baseline info here, ask the parent. If that one has none
      // either, then we cant do anything about it.
      box.setBaselineInfo(box.getNominalBaselineInfo());
    }
  }


  protected void finishInlineLevelBox(final RenderBox box)
  {
    if (breakState == null)
    {
      return;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.closeContext();
      return;
    }

    final Object suspender = breakState.getSuspendItem();
    if (box.getInstanceId() == suspender)
    {
      breakState.setSuspendItem(null);
      return;
    }

    if (suspender != null)
    {
      return;
    }

    if (box instanceof ParagraphRenderBox)
    {
      throw new IllegalStateException("This cannot be.");
    }
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    computeYPosition(node);

    if (breakState == null || breakState.isSuspended())
    {
      return;
    }

    if (node instanceof RenderableText)
    {
      breakState.getCurrentLine().addChild
          (new TextElementAlignContext((RenderableText) node));
    }
    else if (node instanceof SpacerRenderNode)
    {
      breakState.getCurrentLine().addChild(new NodeAlignContext(node));
    }
    else
    {
      breakState.getCurrentLine().addChild(new NodeAlignContext(node));
    }
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    // This could be anything, text, or an image.
    computeYPosition(node);

    if (node instanceof FinishedRenderNode)
    {
      FinishedRenderNode fnode = (FinishedRenderNode) node;
      node.setHeight(fnode.getLayoutedHeight());
    }
    // Tables can have spacer nodes in weird positions. Actually it is less
    // expensive to filter them here than to kill them earlier.
    // Heck, given infinite time and resources, I will filter them earlier ..
//    throw new IllegalStateException
//        ("Block Level nodes are somewhat illegal: " + node);
//    node.setHeight(node.getNodeLayoutProperties().getMaximumBoxHeight());
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // Process the direct childs of the paragraph
    // Each direct child represents a line ..

    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // all childs of the linebox container must be inline boxes. They
      // represent the lines in the paragraph. Any other element here is
      // a error that must be reported
      if (node instanceof ParagraphPoolBox == false)
      {
        throw new IllegalStateException("Encountered " + node.getClass());
      }
      final ParagraphPoolBox inlineRenderBox = (ParagraphPoolBox) node;
      startLine(inlineRenderBox);
      processBoxChilds(inlineRenderBox);
      finishLine(inlineRenderBox);

      node = node.getNext();
    }

  }

  protected void startLine(ParagraphPoolBox box)
  {
    computeYPosition(box);

    if (breakState == null)
    {
      return;
    }

    if (breakState.isSuspended())
    {
      return;
    }

    breakState.openContext(new BoxAlignContext(box));
  }

  protected void finishLine(ParagraphPoolBox inlineRenderBox)
  {
    if (breakState == null || breakState.isSuspended())
    {
      return;
    }

    final BoxAlignContext boxAlignContext = breakState.closeContext();


    // This aligns all direct childs. Once that is finished, we have to
    // check, whether possibly existing inner-paragraphs are still valid
    // or whether moving them violated any of the inner-pagebreak constraints.
    final VerticalAlignmentProcessor processor = new VerticalAlignmentProcessor();

    final StaticBoxLayoutProperties blp = inlineRenderBox.getStaticBoxLayoutProperties();
    final long insetTop = (blp.getBorderTop() + blp.getPaddingTop());

    final long contentAreaY1 = inlineRenderBox.getY() + insetTop;
    final RenderLength lineHeight = inlineRenderBox.getLineHeight();
    final RenderLength bcw =
        inlineRenderBox.getNodeLayoutProperties().getBlockContextWidth();
    processor.align (boxAlignContext, contentAreaY1,
        lineHeight.resolve(bcw.resolve(0)));
  }

  protected void finishOtherBox(RenderBox box)
  {
    if (breakState != null)
    {
      Object suspender = breakState.getSuspendItem();
      if (box.getInstanceId() == suspender)
      {
        breakState.setSuspendItem(null);
      }
    }
  }
}