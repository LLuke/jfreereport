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
 * ComputeStaticPropertiesStep.java
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
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.BorderEdge;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.BoxLayoutProperties;
import org.jfree.layouting.renderer.model.NodeLayoutProperties;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.util.Log;

/**
 * This step computes the defined margins, the border and paddings. Before
 * margins and borders can be computed, this derives the block-context-width.
 * <p/>
 * The block-context-width is a precomputed value and defaults to the total page
 * width (or when there is no such width, a predefined value). The
 * block-context-width is independent of the final size of the element.
 * <p/>
 * Table-Cells establish their own context, if the width is known.
 * (This adds the dependency, that the table structure must be validated before
 * the static properties can be computed.) If the width is not known, the
 * block-context-width will be undefined (zero) unless a new known context
 * gets established.
 *
 * @author Thomas Morgner
 */
public class ComputeStaticPropertiesStep extends IterateVisualProcessStep
{
  private LogicalPageBox root;

  public ComputeStaticPropertiesStep()
  {
  }

  public void compute(LogicalPageBox root)
  {
    this.root = root;
    startProcessing(root);
    this.root = null;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    startProcessing(box.getPool());
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    final NodeLayoutProperties nlp = box.getNodeLayoutProperties();

    if (nlp.isFinished())
    {
      return true;
    }

    computeBlockContextWidth(box);

    final BoxLayoutProperties blp = box.getBoxLayoutProperties();
    final RenderLength bcw = nlp.getBlockContextWidth();
    final long rbcw = bcw.resolve(0);
    final BoxDefinition boxDefinition = box.getBoxDefinition();

    if (box instanceof TableCellRenderBox)
    {
      // Table cells have borders and paddings, but no margins ..
      computeBorder(boxDefinition, blp, rbcw);
      // the computed width of table-cells is not known yet ...
      // This is computed later ..

      // We have to check the column-model for that and have to see
      // whether that one has some hints. If the cell has an explicit
      // width, we can use that one.
      final RenderLength preferredWidth = boxDefinition.getPreferredWidth();
      if (preferredWidth == RenderLength.AUTO)
      {
        // The cell itself did not define anything. But maybe the column
        // had a definition?
        final TableCellRenderBox cellBox = (TableCellRenderBox) box;
        final TableColumnModel columnModel = cellBox.getTable().getColumnModel();
        final int colIdx = cellBox.getColumnIndex();
        final int maxIdx = colIdx + cellBox.getColSpan();
        if (maxIdx >= columnModel.getColumnCount())
        {
          // No definition. Therefore the cell will not have a computed width.
          // (The effective width will be auto-computed in later steps.)
          // Yeah, we *could* start guessing, but I dont support our user's
          // lazyness. Shall they suffer from their own mis-definitions.
          nlp.setComputedWidth(RenderLength.AUTO);
        }
        else
        {
          long width = 0;
          for (int i = colIdx; i < maxIdx; i++)
          {
            // Check, whether one of the columns will have an autoWidth --
            final TableColumn column = columnModel.getColumn(i);
            final RenderLength definedWidth = column.getDefinedWidth();
            if (definedWidth == RenderLength.AUTO)
            {
              // No go.
              width = -1;
              break;
            }

            width += definedWidth.resolve(rbcw);
          }
          if (width > 0)
          {
            nlp.setComputedWidth(new RenderLength(width, false));
          }
          else
          {
            nlp.setComputedWidth(RenderLength.AUTO);
          }
        }
      }
      else
      {
        nlp.setComputedWidth(preferredWidth.resolveToRenderLength(rbcw));
      }
      nlp.setFinished(true);
      return true;
    }

    if (box instanceof TableRowRenderBox ||
        box instanceof TableSectionRenderBox)
    {
      // rows and sections have neither paddings, margins or borders ..
      // See 17.6.1 of [CSS21]
      nlp.setComputedWidth(bcw);
      nlp.setFinished(true);
      return true;
    }

    // Every other Block-Level-element.
    computeBorder(boxDefinition, blp, rbcw);

    // For reference: The following formula defines the behaviour on AUTO
    //
    // (width of containing block) =
    //          margin-left + border-left + padding-left + width +
    //          padding-right + border-right + margin-right

    // On horizontal flow: If margin-top or -bottom is auto, then
    // the margin resolves to zero
    blp.setMarginTop(boxDefinition.getMarginTop().resolve(rbcw));
    blp.setMarginBottom(boxDefinition.getMarginBottom().resolve(rbcw));

    // According to the box-model, there are five cases
    // Case1: None of Width and Margin-left-right is auto.
    final RenderLength marginLeft = boxDefinition.getMarginLeft();
    final RenderLength marginRight = boxDefinition.getMarginRight();
    final RenderLength preferredWidth = boxDefinition.getPreferredWidth();
    if (preferredWidth != RenderLength.AUTO)
    {
      if (marginLeft != RenderLength.AUTO &&
          marginRight != RenderLength.AUTO)
      {
        long mlValue = marginLeft.resolve(rbcw);
        long mrValue = marginRight.resolve(rbcw);
        long pwValue = preferredWidth.resolve(rbcw);

        // Sub-Case 1: The defined values satisfy the constraints defined by
        //             the formula
        if (mlValue + mrValue + pwValue == rbcw)
        {
          // fine. Accept these values
          blp.setMarginLeft(mlValue);
          blp.setMarginRight(mrValue);
          nlp.setComputedWidth(new RenderLength(pwValue, false));
        }
        // Sub-Case 2: They dont ..
        else if (box.isDirectionLTR())
        {
          blp.setMarginLeft(mlValue);
          nlp.setComputedWidth(new RenderLength(pwValue, false));
          blp.setMarginRight(rbcw - mlValue - pwValue);
        }
        else
        {
          blp.setMarginLeft(rbcw - mrValue - pwValue);
          nlp.setComputedWidth(new RenderLength(pwValue, false));
          blp.setMarginRight(mrValue);
        }
        nlp.setFinished(true);
        return true;
      }

      // If exactly one of width, margin-left or margin-right is 'auto',
      // its value is computed from the equation.
      if (marginLeft == RenderLength.AUTO &&
          marginRight != RenderLength.AUTO)
      {
        long mrValue = marginRight.resolve(rbcw);
        long pwValue = preferredWidth.resolve(rbcw);

        blp.setMarginLeft(rbcw - mrValue - pwValue);
        nlp.setComputedWidth(new RenderLength(pwValue, false));
        blp.setMarginRight(mrValue);
        nlp.setFinished(true);
        return true;
      }

      if (marginLeft != RenderLength.AUTO &&
          marginRight == RenderLength.AUTO)
      {
        long mlValue = marginLeft.resolve(rbcw);
        long pwValue = preferredWidth.resolve(rbcw);

        blp.setMarginLeft(mlValue);
        nlp.setComputedWidth(new RenderLength(pwValue, false));
        blp.setMarginRight(rbcw - mlValue - pwValue);
        nlp.setFinished(true);
        return true;
      }
    }
    // If width and one or both margins are 'auto', the margins that
    // are 'auto' are set to 0 and the equation is solved for width.
    if (preferredWidth == RenderLength.AUTO)
    {
      long mlValue = marginLeft.resolve(rbcw);
      long mrValue = marginRight.resolve(rbcw);

      blp.setMarginLeft(mlValue);
      blp.setMarginRight(mrValue);
      if (box instanceof TableRenderBox)
      {
        nlp.setComputedWidth(RenderLength.AUTO);
      }
      else if (bcw == RenderLength.AUTO)
      {
        nlp.setComputedWidth(RenderLength.AUTO);
      }
      else
      {
        nlp.setComputedWidth(new RenderLength((rbcw - mlValue - mrValue), false));
      }

      nlp.setFinished(true);
      return true;
    }

    // If both margin-left and margin-right are 'auto', the equation is
    // solved under the extra constraint that margin-left = margin-right.
    long pwValue = preferredWidth.resolve(rbcw);

    final long margins = rbcw - pwValue;
    final long mlValue = margins / 2;
    blp.setMarginLeft(mlValue);
    blp.setMarginRight(margins - mlValue);
    nlp.setComputedWidth(new RenderLength(pwValue, false));
    nlp.setFinished(true);
    return true;
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    computeBlockContextWidth(node);

    final NodeLayoutProperties nlp = node.getNodeLayoutProperties();
    final RenderLength bcw = nlp.getBlockContextWidth();
    nlp.setComputedWidth(bcw);
    nlp.setFinished(true);
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    final NodeLayoutProperties nlp = box.getNodeLayoutProperties();
    if (nlp.isFinished() != false)
    {
      return true;
    }

    computeBlockContextWidth(box);

    final BoxLayoutProperties blp = box.getBoxLayoutProperties();
    final RenderLength bcw = nlp.getBlockContextWidth();
    final long rbcw = bcw.resolve(0);
    final BoxDefinition boxDefinition = box.getBoxDefinition();

    computeBorder(boxDefinition, blp, rbcw);

    // as defined by the box-model
    blp.setMarginTop(0);
    blp.setMarginBottom(0);
    // margin-auto gets resolved to zero
    blp.setMarginLeft(boxDefinition.getMarginLeft().resolve(rbcw));
    blp.setMarginRight(boxDefinition.getMarginRight().resolve(rbcw));
    nlp.setComputedWidth(RenderLength.AUTO);
    nlp.setFinished(true);
    return true;
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    NodeLayoutProperties nlp = node.getNodeLayoutProperties();
    if (nlp.isFinished())
    {
      return;
    }

    computeBlockContextWidth(node);
    

    nlp.setFinished(true);
  }

  private void computeBorder(final BoxDefinition boxDefinition,
                             final BoxLayoutProperties slp, final long bcw)
  {
    final Border border = boxDefinition.getBorder();
    slp.setBorderTop(resolveBorderEdge(bcw, border.getTop()));
    slp.setBorderLeft(resolveBorderEdge(bcw, border.getLeft()));
    slp.setBorderBottom(resolveBorderEdge(bcw, border.getBottom()));
    slp.setBorderRight(resolveBorderEdge(bcw, border.getRight()));

    slp.setPaddingTop(boxDefinition.getPaddingTop().resolve(bcw));
    slp.setPaddingLeft(boxDefinition.getPaddingLeft().resolve(bcw));
    slp.setPaddingBottom(boxDefinition.getPaddingBottom().resolve(bcw));
    slp.setPaddingRight(boxDefinition.getPaddingRight().resolve(bcw));
  }

  protected void computeBlockContextWidth (RenderNode node)
  {
    NodeLayoutProperties slp = node.getNodeLayoutProperties();
    // grab the block-context width ..
    final RenderBox blockContext = node.getParentBlockContext();
    if (blockContext == null)
    {
      slp.setBlockContextWidth(new RenderLength(root.getPageWidth(), false));
    }
    else
    {
      final NodeLayoutProperties layoutProperties =
              blockContext.getNodeLayoutProperties();
      // the computed width will never be a percentage ..
      final RenderLength computedWidth = layoutProperties.getComputedWidth();
      slp.setBlockContextWidth(computedWidth);
    }
  }

  private long resolveBorderEdge (final long bcw, BorderEdge borderEdge)
  {
    if (BorderStyle.NONE.equals(borderEdge.getBorderStyle()))
    {
      return 0;
    }
    return borderEdge.getWidth().resolve(bcw);
  }

  protected void finishInlineLevelBox(final RenderBox box)
  {
    super.finishInlineLevelBox(box);
  }


}
