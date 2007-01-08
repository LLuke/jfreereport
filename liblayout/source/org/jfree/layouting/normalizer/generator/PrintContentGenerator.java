/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id: PrintContentGenerator.java,v 1.8 2006/12/05 15:13:45 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.generator;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.util.Log;

/**
 * Simply prints each incoming call.
 *
 * @author Thomas Morgner
 */
public class PrintContentGenerator implements ContentGenerator
{
  private static class PrintContentGeneratorState implements State
  {
    private State renderer;

    public PrintContentGeneratorState(final State renderer)
    {
      this.renderer = renderer;
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
            throws StateException
    {
      final Renderer renderer = (Renderer) this.renderer.restore(layoutProcess);
      return new PrintContentGenerator(renderer);
    }
  }

  private Renderer renderer;

  public PrintContentGenerator(final LayoutProcess layoutProcess)
  {
    this.renderer = layoutProcess.getOutputProcessor().createRenderer(layoutProcess);
  }

  public PrintContentGenerator(final Renderer renderer)
  {
    this.renderer = renderer;
  }

  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   */
  public void startedDocument(final PageContext pageContext)
  {
    Log.debug("<document>");
    renderer.startedDocument(pageContext);
  }

  public void startedFlow(final LayoutContext element)
      throws NormalizationException
  {
    Log.debug("<flow tag='" + element.getTagName() + "'>");
    renderer.startedFlow(element);
  }

  public void startedTable(final LayoutContext element)
      throws NormalizationException
  {
    Log.debug("<table>");
    renderer.startedTable(element);
  }

  public void startedTableColumnGroup(final LayoutContext element)
          throws NormalizationException
  {
    Log.debug("<table-col-group>");
    renderer.startedTableColumnGroup(element);
  }

  public void startedTableColumn(final LayoutContext element)
          throws NormalizationException
  {
    Log.debug("<table-col>");
    renderer.startedTableColumn(element);
  }

  public void startedTableSection(final LayoutContext element)
      throws NormalizationException
  {
    Log.debug("<table-section>");
    renderer.startedTableSection(element);
  }

  public void startedTableRow(final LayoutContext element)
      throws NormalizationException
  {
    Log.debug("<table-row>");
    renderer.startedTableRow(element);
  }

  public void startedTableCell(final LayoutContext element)
      throws NormalizationException
  {
    Log.debug("<table-cell>");
    renderer.startedTableCell(element);
  }

  public void startedBlock(final LayoutContext element)
      throws NormalizationException
  {
    final String tagName = element.getTagName();
    Log.debug("<block tag='" + tagName + "'>");
    renderer.startedBlock(element);
  }

  public void startedRootInline(final LayoutContext element)
          throws NormalizationException
  {
    final String tagName = element.getTagName();
    Log.debug("<paragraph tag='" + tagName + "'>");
    renderer.startedRootInline(element);
  }

  public void startedMarker(final LayoutContext element)
          throws NormalizationException
  {
    Log.debug("<marker>");
    renderer.startedMarker(element);
  }

  public void startedInline(final LayoutContext element)
      throws NormalizationException
  {
    final String tagName = element.getTagName();
    Log.debug("<inline tag='" + tagName + "'>");
    renderer.startedInline(element);
  }

  public void addContent(final LayoutContext node, final ContentToken token)
      throws NormalizationException
  {
    Log.debug("<content>" + token + "</content>");
    renderer.addContent(node, token);
  }

  public void finishedInline() throws NormalizationException
  {
    Log.debug("</inline>");
    renderer.finishedInline();
  }

  public void finishedMarker() throws NormalizationException
  {
    Log.debug("</marker>");
    renderer.finishedMarker();
  }

  public void finishedRootInline() throws NormalizationException
  {
    Log.debug("</paragraph>");
    renderer.finishedRootInline();
  }

  public void finishedBlock() throws NormalizationException
  {
    Log.debug("</block>");
    renderer.finishedBlock();
  }

  public void finishedTableCell() throws NormalizationException
  {
    Log.debug("</table-cell>");
    renderer.finishedTableCell();
  }

  public void finishedTableRow() throws NormalizationException
  {
    Log.debug("</table-row>");
    renderer.finishedTableRow();
  }

  public void finishedTableSection() throws NormalizationException
  {
    Log.debug("</table-section>");
    renderer.finishedTableSection();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    Log.debug("</table-col>");
    renderer.finishedTableColumn();
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    Log.debug("</table-col-group>");
    renderer.finishedTableColumnGroup();
  }

  public void finishedTable() throws NormalizationException
  {
    Log.debug("</table>");
    renderer.finishedTable();
  }

  public void finishedFlow() throws NormalizationException
  {
    Log.debug("</flow>");
    renderer.finishedFlow();
  }

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void finishedDocument() throws NormalizationException
  {
    Log.debug("</document>");
    renderer.finishedDocument();
  }

  public State saveState() throws StateException
  {
    return new PrintContentGeneratorState(renderer.saveState());
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    renderer.handlePageBreak(pageContext);
  }

  public void startedPassThrough(final LayoutContext element)
      throws NormalizationException
  {
    Log.debug("<pass-through>");
    renderer.startedPassThrough(element);
  }

  public void addPassThroughContent(final LayoutContext node,
                                    final ContentToken token)
      throws NormalizationException
  {
    Log.debug("<pass-through-content>" + token + "</pass-through-content>");
    renderer.addPassThroughContent(node, token);
  }

  public void finishedPassThrough() throws NormalizationException
  {
    Log.debug("</pass-through>");
    renderer.finishedPassThrough();
  }

  public void startedTableCaption(final LayoutContext context)
      throws NormalizationException
  {
    Log.debug("<table-caption>");
    renderer.startedTableCaption(context);
  }

  public void finishedTableCaption() throws NormalizationException
  {
    Log.debug("</table-caption>");
    renderer.finishedTableCaption();
  }

  public Renderer getRenderer()
  {
    return renderer;
  }
}
