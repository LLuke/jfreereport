/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id: ComputeStaticPropertiesStep.java,v 1.6 2007/04/02 11:41:20 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.BorderEdge;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.StaticBoxLayoutProperties;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;

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
    if (box.getComputedLayoutProperties() != null)
    {
      return true;
    }

    //ComputedLayoutProperties nlp = new ComputedLayoutProperties()
    final RenderLength bcw = computeBlockContextWidth(box);
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
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
        if (colIdx < 0)
        {
          throw new IllegalStateException("Table has not been validated yet.");
        }

        final int maxIdx = colIdx + cellBox.getColSpan();
        if (maxIdx >= columnModel.getColumnCount())
        {
          // No definition. Therefore the cell will not have a computed width.
          // (The effective width will be auto-computed in later steps.)
          // Yeah, we *could* start guessing, but I dont support our user's
          // lazyness. Shall they suffer from their own mis-definitions.
          box.setComputedLayoutProperties
              (new ComputedLayoutProperties(bcw, RenderLength.AUTO));
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
            box.setComputedLayoutProperties
                (new ComputedLayoutProperties(bcw, new RenderLength(width, false)));
          }
          else
          {
            box.setComputedLayoutProperties
                (new ComputedLayoutProperties(bcw, RenderLength.AUTO));
          }
        }
      }
      else
      {
        box.setComputedLayoutProperties
            (new ComputedLayoutProperties(bcw, preferredWidth.resolveToRenderLength(rbcw)));
      }
      return true;
    }

    if (box instanceof TableRowRenderBox ||
        box instanceof TableSectionRenderBox)
    {
      // rows and sections have neither paddings, margins or borders ..
      // See 17.6.1 of [CSS21]
      box.setComputedLayoutProperties
          (new ComputedLayoutProperties(bcw, bcw));
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
        }
        // Sub-Case 2: They dont ..
        else if (box.isDirectionLTR())
        {
          blp.setMarginLeft(mlValue);
          blp.setMarginRight(rbcw - mlValue - pwValue);
        }
        else
        {
          blp.setMarginLeft(rbcw - mrValue - pwValue);
          blp.setMarginRight(mrValue);
        }
        box.setComputedLayoutProperties
            (new ComputedLayoutProperties(bcw, new RenderLength(pwValue, false)));
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
        box.setComputedLayoutProperties
            (new ComputedLayoutProperties(bcw, new RenderLength(pwValue, false)));
        blp.setMarginRight(mrValue);
        return true;
      }

      if (marginLeft != RenderLength.AUTO &&
          marginRight == RenderLength.AUTO)
      {
        final long mlValue = marginLeft.resolve(rbcw);
        final long pwValue = preferredWidth.resolve(rbcw);

        blp.setMarginLeft(mlValue);
        box.setComputedLayoutProperties
            (new ComputedLayoutProperties(bcw, new RenderLength(pwValue, false)));
        blp.setMarginRight(rbcw - mlValue - pwValue);
        return true;
      }
    }
    // If width and one or both margins are 'auto', the margins that
    // are 'auto' are set to 0 and the equation is solved for width.
    else // if (preferredWidth == RenderLength.AUTO)
    {
      final long mlValue = marginLeft.resolve(rbcw);
      final long mrValue = marginRight.resolve(rbcw);

      blp.setMarginLeft(mlValue);
      blp.setMarginRight(mrValue);
      if (box instanceof TableRenderBox)
      {
        box.setComputedLayoutProperties
            (new ComputedLayoutProperties(bcw, RenderLength.AUTO));
      }
      else if (bcw == RenderLength.AUTO)
      {
        box.setComputedLayoutProperties
            (new ComputedLayoutProperties(bcw, RenderLength.AUTO));
      }
      else
      {
        box.setComputedLayoutProperties
            (new ComputedLayoutProperties(bcw,
                new RenderLength((rbcw - mlValue - mrValue), false)));
      }
      return true;
    }

    // If both margin-left and margin-right are 'auto', the equation is
    // solved under the extra constraint that margin-left = margin-right.
    final long pwValue = preferredWidth.resolve(rbcw);

    final long margins = rbcw - pwValue;
    final long mlValue = margins / 2;
    blp.setMarginLeft(mlValue);
    blp.setMarginRight(margins - mlValue);
    box.setComputedLayoutProperties
        (new ComputedLayoutProperties(bcw, new RenderLength(pwValue, false)));
    return true;
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    final RenderLength bcw = computeBlockContextWidth(node);
    node.setComputedLayoutProperties
        (new ComputedLayoutProperties(bcw, bcw));
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    if (box.getComputedLayoutProperties() != null)
    {
      return true;
    }

    final RenderLength bcw = computeBlockContextWidth(box);
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final long rbcw = bcw.resolve(0);
    final BoxDefinition boxDefinition = box.getBoxDefinition();

    computeBorder(boxDefinition, blp, rbcw);

    // as defined by the box-model
    blp.setMarginTop(0);
    blp.setMarginBottom(0);
    // margin-auto gets resolved to zero
    blp.setMarginLeft(boxDefinition.getMarginLeft().resolve(rbcw));
    blp.setMarginRight(boxDefinition.getMarginRight().resolve(rbcw));
    box.setComputedLayoutProperties
        (new ComputedLayoutProperties(bcw, RenderLength.AUTO));
    return true;
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    if (node.getComputedLayoutProperties() != null)
    {
      return;
    }

    final RenderLength bcw = computeBlockContextWidth(node);
    node.setComputedLayoutProperties
        (new ComputedLayoutProperties(bcw, RenderLength.AUTO));
  }

  private void computeBorder(final BoxDefinition boxDefinition,
                             final StaticBoxLayoutProperties slp,
                             final long bcw)
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

  protected RenderLength computeBlockContextWidth (RenderNode node)
  {
    // grab the block-context width ..
    final RenderBox blockContext = node.getParentBlockContext();
    if (blockContext == null)
    {
      return new RenderLength(root.getPageWidth(), false);
    }
    else
    {
      final ComputedLayoutProperties layoutProperties =
              blockContext.getComputedLayoutProperties();
      // the computed width will never be a percentage ..
      return layoutProperties.getComputedWidth();
    }
  }

  private long resolveBorderEdge (final long bcw, final BorderEdge borderEdge)
  {
    if (BorderStyle.NONE.equals(borderEdge.getBorderStyle()))
    {
      return 0;
    }
    return borderEdge.getWidth().resolve(bcw);
  }
}
