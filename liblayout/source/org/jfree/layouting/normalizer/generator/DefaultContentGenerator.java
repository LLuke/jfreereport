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
 * DefaultContentGenerator.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultContentGenerator.java,v 1.5 2006/11/20 21:01:53 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.generator;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.displaymodel.DisplayBlockElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayContent;
import org.jfree.layouting.normalizer.displaymodel.DisplayElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayFlowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayPassThroughElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayRootInlineElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableCellElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableColumnElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableColumnGroupElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableRowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableSectionElement;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.util.Log;

/**
 * The default content generator produces CSS-compliant content structures (also
 * known as DOM-Trees). This model explicitly allows block elements to contain
 * other block elements and to do all kinds of weird mixing of elements.
 * <p/>
 * The content generator already has all knowledge to compute the manual
 * pagebreaks. So this class could already fire pagebreaks. The next element in
 * the layouting chain will be the Layouter itself. The renderer computes the
 * element positions,knows about the page sizes (and whether we have pages
 * anyway) and more important - keeps track of the current page and the page
 * fill state.
 * <p/>
 * The content generator is conceptionally a part of the normalizer step, and
 * thererfore has access to the raw display model. The renderer receives a more
 * generic view with no object hierarchies at all - if that whould be needed, it
 * has to be rebuilt. (This simplifies serialization, so that most remote calls
 * are now self-contained and do not supply a possibly contradictionary context
 * (in terms of object-identity)).
 *
 * @author Thomas Morgner
 */
public class DefaultContentGenerator implements ContentGenerator
{
  private static class DefaultContentGeneratorState implements State
  {
    private State rendererState;

    public DefaultContentGeneratorState()
    {
    }

    public State getRendererState()
    {
      return rendererState;
    }

    public void setRendererState(final State rendererState)
    {
      this.rendererState = rendererState;
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
      return new DefaultContentGenerator
              ((Renderer) rendererState.restore(layoutProcess));
    }
  }

  private Renderer renderer;

  public DefaultContentGenerator(LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    this.renderer = layoutProcess.getOutputProcessor().createRenderer(layoutProcess);
  }

  protected DefaultContentGenerator(final Renderer renderer)
  {
    Log.debug ("Created: " + renderer);
    this.renderer = renderer;
  }

  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   */
  public void startedDocument(final PageContext pageContext)
  {
    renderer.startDocument(pageContext);
  }

  public void startedFlow(final DisplayFlowElement element)
          throws NormalizationException
  {
    renderer.startedFlow(element.getLayoutContext());
  }

  public void startedTable(final DisplayTableElement element)
          throws NormalizationException
  {
    renderer.startedTable(element.getLayoutContext());
  }

  public void startTableColumnGroup(final DisplayTableColumnGroupElement element)
          throws NormalizationException
  {
    renderer.startedTableColumnGroup(element.getLayoutContext());
  }

  public void startTableColumn(final DisplayTableColumnElement element)
          throws NormalizationException
  {
    renderer.startedTableColumn(element.getLayoutContext());
  }

  public void startedTableSection(final DisplayTableSectionElement element)
          throws NormalizationException
  {
    renderer.startedTableSection(element.getLayoutContext());
  }

  public void startedTableRow(final DisplayTableRowElement element)
          throws NormalizationException
  {
    renderer.startedTableRow(element.getLayoutContext());
  }

  public void startedTableCell(final DisplayTableCellElement element)
          throws NormalizationException
  {
    renderer.startedTableCell(element.getLayoutContext());
  }

  public void startedBlock(final DisplayBlockElement element)
          throws NormalizationException
  {
    renderer.startedBlock(element.getLayoutContext());
  }

  public void startedMarker(final DisplayElement element)
          throws NormalizationException
  {
    renderer.startedMarker(element.getLayoutContext());
  }

  public void startedRootInline(final DisplayRootInlineElement element)
          throws NormalizationException
  {
    renderer.startedRootInline(element.getLayoutContext());
  }

  public void startedInline(final DisplayElement element) throws
          NormalizationException
  {
    renderer.startedInline(element.getLayoutContext());
  }

  public void addContent(final DisplayContent node) throws
          NormalizationException
  {
    renderer.addContent(node.getLayoutContext(), node.getContent());
  }

  public void finishedInline() throws NormalizationException
  {
    renderer.finishedInline();
  }

  public void finishedMarker() throws NormalizationException
  {
    renderer.finishedMarker();
  }

  public void finishedRootInline() throws NormalizationException
  {
    renderer.finishedRootInline();
  }

  public void finishedBlock() throws NormalizationException
  {
    renderer.finishedBlock();
  }

  public void finishedTableCell() throws NormalizationException
  {
    renderer.finishedTableCell();
  }

  public void finishedTableRow() throws NormalizationException
  {
    renderer.finishedTableRow();
  }

  public void finishedTableSection() throws NormalizationException
  {
    renderer.finishedTableSection();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    renderer.finishedTableColumn();
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    renderer.finishedTableColumnGroup();
  }

  public void finishedTable() throws NormalizationException
  {
    renderer.finishedTable();
  }

  public void finishedFlow() throws NormalizationException
  {
    renderer.finishedFlow();
  }

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void finishDocument() throws NormalizationException
  {
    renderer.finishedDocument();
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    renderer.handlePageBreak(pageContext);
  }

  public void startedPassThrough(final DisplayPassThroughElement element)
      throws NormalizationException
  {
    renderer.startedPassThrough (element.getLayoutContext());
  }

  public void addPassThroughContent(final DisplayContent node)
      throws NormalizationException
  {
    renderer.addPassThroughContent(node.getLayoutContext(), node.getContent());
  }

  public void finishPassThrough()
  {
    renderer.finishedPassThrough();
  }

  public State saveState() throws StateException
  {
    DefaultContentGeneratorState state = new DefaultContentGeneratorState();
    state.setRendererState(renderer.saveState());
    return state;
  }

  public Renderer getRenderer()
  {
    return renderer;
  }
}
