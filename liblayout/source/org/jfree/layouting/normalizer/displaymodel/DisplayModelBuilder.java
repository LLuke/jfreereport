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
 * DisplayModelNormalizer.java
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

package org.jfree.layouting.normalizer.displaymodel;

import java.util.ArrayList;
import java.util.Stack;
import java.io.IOException;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.generator.ContentGenerator;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.util.Log;

/**
 * Builds the display model. The display model guarantees, that block and inline
 * content are no longer mixed within a single element. Anonymous block and line
 * boxes are inserted where ever needed.
 * <p/>
 * The DisplayModelBuilder is the place to handle RunIns (later).
 */
public class DisplayModelBuilder implements ModelBuilder
{
  protected class DisplayModelBuilderState implements State
  {
    private DisplayNode[] stackContents;
    private State contentGeneratorState;

    public DisplayModelBuilderState()
    {
    }

    public DisplayNode[] getStackContents()
    {
      return stackContents;
    }

    public void setStackContents(final DisplayNode[] stackContents)
    {
      this.stackContents = stackContents;
    }

    public State getContentGeneratorState()
    {
      return contentGeneratorState;
    }

    public void setContentGeneratorState(final State contentGeneratorState)
    {
      this.contentGeneratorState = contentGeneratorState;
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
      final ContentGenerator cg = (ContentGenerator)
              contentGeneratorState.restore(layoutProcess);
      final DisplayModelBuilder dbm = new DisplayModelBuilder(cg, layoutProcess);
      try
      {
        dbm.restoreStack(stackContents);
      }
      catch (NormalizationException e)
      {
        throw new StateException("Failed to restore the state.");
      }
      return dbm;
    }
  }

  private Stack stack;
  private ContentGenerator contentGenerator;
  private LayoutProcess layoutProcess;

  public DisplayModelBuilder(final ContentGenerator contentGenerator,
                             final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    if (contentGenerator == null)
    {
      throw new NullPointerException();
    }
    this.contentGenerator = contentGenerator;
    this.layoutProcess = layoutProcess;
    this.stack = new Stack();
  }

  public ContentGenerator getContentGenerator()
  {
    return contentGenerator;
  }

  protected DisplayElement getCurrentContext() throws NormalizationException
  {
    if (stack.isEmpty())
    {
      throw new IllegalStateException
              ("Stack is empty; In a sane environment, this never happens.");
    }

    DisplayElement currentBox = (DisplayElement) stack.peek();
    if (currentBox.isFinished() == false)
    {
      return currentBox;
    }

    // recurse down the stack, until we find a non-finished element
    // this is our currently active block context. We 'pop' all elements
    // from the stack into a separate list.
    try
    {
      final ArrayList contexts = new ArrayList();

      while (currentBox.isFinished())
      {
        // remove the known finished element from the stack ...
        stack.pop();
        // but add it to our list of elements that should be reopened
        contexts.add(currentBox);
        // now look at the next one...
        currentBox = (DisplayElement) stack.peek();
      }

      // From there we go up, re-adding new elements (in reverse order)
      for (int i = contexts.size() - 1; i >= 0; i -= 1)
      {
        final DisplayElement contentBox = (DisplayElement) contexts.get(i);
        // the derived element is a split element, and thus has to use a
        // different border here (the internal-border instead of the external
        // ones.
        final DisplayElement derived = (DisplayElement) contentBox.derive();
        currentBox.add(derived);
        stack.push(derived);
        currentBox = derived;
      }

      // hey, incidentally, this is a similiar structure to what the
      // openoffice guys do. So I cant be totally wrong here .
      return currentBox;
    }
    catch (Exception e)
    {
      throw new NormalizationException("Something went terribly wrong here", e);
    }
  }

  public void startDocument(final PageContext pageContext)
          throws NormalizationException
  {
    contentGenerator.startedDocument(pageContext);
  }


  public void startElement(final LayoutContext layoutContext)
          throws NormalizationException, IOException
  {
    if (stack.isEmpty())
    {
      // Look, mama, we started a new document. Create a flow context.
      // Oh, yes, by that logic we can have more than one root flow, but
      // for our purpose, thats not a limitation nor a bug at all.
      final DisplayRoot newBox = new DisplayRoot(layoutContext);
      newBox.setContentGenerator(contentGenerator);
      newBox.setLayoutProcess(layoutProcess);

      getContentGenerator().startedFlow(newBox);
      stack.push(newBox);
      return;
    }

    DisplayElement currentBox = getCurrentContext();

    final CSSValue floating = layoutContext.getStyle().getValue(
            BoxStyleKeys.FLOAT);

    if (Floating.NONE.equals(floating) == false)
    {
      // display model is always block here. This is fixed and defined.
      DisplayFlowElement newBox =
              new DisplayFlowElement (layoutContext);

      // the flow is connected to the parent, but is not a normal-flow
      // child of the parent. So we set the flow's parent manually, without
      // signaling the addition to the parent itself. The parent still thinks
      // its empty and still preserves its line-box computation state.
      newBox.setParent(currentBox);

      getContentGenerator().startedFlow(newBox);
      stack.push(newBox);
      return;
    }

    final CSSValue displayModel = layoutContext.getStyle().getValue(
            BoxStyleKeys.DISPLAY_MODEL);
    final CSSValue displayRole = layoutContext.getStyle().getValue
            (BoxStyleKeys.DISPLAY_ROLE);

    Log.debug ("Display: " + displayModel + " " + displayRole);
    if (DisplayRole.TABLE_CELL.equals(displayRole))
    {
      final DisplayTableCellElement node = new DisplayTableCellElement(layoutContext);
      currentBox = closeAutoGeneratedContext(currentBox, node);
      currentBox = generateMissingForTableCell(currentBox);
      currentBox.add(node);
      stack.push(node);
    }
    else if (DisplayRole.TABLE_ROW.equals(displayRole))
    {
      final DisplayTableRowElement node = new DisplayTableRowElement(layoutContext);
      currentBox = closeAutoGeneratedContext(currentBox, node);
      currentBox = generateMissingForTableRow(currentBox);
      currentBox.add(node);
      stack.push(node);
    }
    else if (DisplayRole.TABLE_ROW_GROUP.equals(displayRole))
    {
      final DisplayTableSectionElement node = new DisplayTableSectionElement(layoutContext);
      currentBox = closeAutoGeneratedContext(currentBox, node);
      currentBox = generateMissingForTableSection(currentBox);
      currentBox.add(node);
      stack.push(node);
    }
    else if (DisplayModel.TABLE.equals(displayModel))
    {
      final DisplayTableElement node = new DisplayTableElement(layoutContext);
      currentBox = closeAutoGeneratedContext(currentBox, node);
      currentBox.add(node);
      stack.push(node);
    }
    else if (DisplayModel.INLINE_INSIDE.equals(displayModel))
    {
      final DisplayInlineElement node =
              new DisplayInlineElement(layoutContext);
      currentBox = closeAutoGeneratedContext(currentBox, node);
      currentBox.add(node);
      stack.push(node);
    }
    else // we dont handle Ruby yet nor do we have support for absolute-mode right now
    {
      final DisplayBlockElement node =
              new DisplayBlockElement (layoutContext);
      currentBox = closeAutoGeneratedContext(currentBox, node);
      currentBox.add(node);
      stack.push(node);
    }

  }

  private DisplayElement closeAutoGeneratedContext (final DisplayElement context,
                                                    final DisplayElement newElement)
          throws NormalizationException
  {
    if (context.isAutoGenerated() == false)
    {
      return context;
    }

    if (newElement instanceof DisplayTableCellElement)
    {
      if (context instanceof DisplayTableRowElement == false)
      {
        context.markFinished();
        stack.pop();
        return closeAutoGeneratedContext(getCurrentContext(), newElement);
      }
      else
      {
        return context;
      }
    }
    if (newElement instanceof DisplayTableRowElement)
    {
      if (context instanceof DisplayTableSectionElement == false)
      {
        context.markFinished();
        stack.pop();
        return closeAutoGeneratedContext(getCurrentContext(), newElement);
      }
      else
      {
        return context;
      }
    }
    if (newElement instanceof DisplayTableSectionElement)
    {
      if (context instanceof DisplayTableElement == false)
      {
        context.markFinished();
        stack.pop();
        return closeAutoGeneratedContext(getCurrentContext(), newElement);
      }
      else
      {
        return context;
      }
    }

    context.markFinished();
    stack.pop();
    return closeAutoGeneratedContext(getCurrentContext(), newElement);
  }

  /**
   * We want to add a table-cell. Make sure that the current context is a
   * table row. If it is none, generate the missing table structures.
   *
   * @param maybeRow
   * @return
   */
  private DisplayElement generateMissingForTableCell (final DisplayElement maybeRow)
          throws IOException, NormalizationException
  {
    if (maybeRow instanceof DisplayTableRowElement)
    {
      return (DisplayTableRowElement) maybeRow;
    }

    generateMissingForTableRow(maybeRow);

    layoutProcess.getNormalizer().startElement
            (Namespaces.LIBLAYOUT_NAMESPACE, "auto-table-row", new AttributeMap());

    DisplayElement generatedElement = getCurrentContext();
    generatedElement.setAutoGenerated(true);
    return generatedElement;
  }

  /**
   * We want to add a table-row. Make sure that the current context is a
   * table section. If it is none, generate the missing table structures. The
   * generated section type will be a table-body.
   *
   * @param maybeSection
   * @return
   */
  private DisplayElement generateMissingForTableRow (final DisplayElement maybeSection)
          throws IOException, NormalizationException
  {
    if (maybeSection instanceof DisplayTableSectionElement)
    {
      return (DisplayTableSectionElement) maybeSection;
    }

    generateMissingForTableSection(maybeSection);

    layoutProcess.getNormalizer().startElement
            (Namespaces.LIBLAYOUT_NAMESPACE, "auto-table-body", new AttributeMap());
    DisplayElement generatedElement = getCurrentContext();
    generatedElement.setAutoGenerated(true);
    return generatedElement;
  }

  /**
   * We want to add a table-section. Make sure that the current context is a
   * table. If it is none, generate the missing table maybeTable.
   *
   * @return
   */
  private DisplayElement generateMissingForTableSection (final DisplayElement maybeTable)
          throws IOException, NormalizationException
  {
    if (maybeTable instanceof DisplayTableElement)
    {
      return (DisplayTableElement) maybeTable;
    }

    layoutProcess.getNormalizer().startElement
            (Namespaces.LIBLAYOUT_NAMESPACE, "auto-table", new AttributeMap());
    DisplayElement generatedElement = getCurrentContext();
    generatedElement.setAutoGenerated(true);
    return generatedElement;
  }

  public void addContent(final ContentToken content)
          throws NormalizationException
  {
    final DisplayElement currentBox = getCurrentContext();
    final LayoutContext layoutContext = currentBox.getLayoutContext().derive();
    final DisplayContent node = new DisplayContent(layoutContext, content);
    currentBox.add(node);
  }

  public void endElement()
          throws NormalizationException
  {
    DisplayElement cb = (DisplayElement) stack.pop();
    while (cb.isAutoGenerated() && stack.isEmpty() == false)
    {
      cb.markFinished();
      cb = (DisplayElement) stack.pop();
    }
    Log.debug ("Finishing: " + cb);
    cb.markFinished();
  }

  public void endDocument() throws NormalizationException
  {
    if (stack.isEmpty() == false)
    {
      throw new IllegalStateException(String.valueOf(stack));
    }
    contentGenerator.finishDocument();
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    contentGenerator.handlePageBreak (pageContext);
  }

  protected DisplayElement getRoot()
  {
    if (stack.isEmpty())
    {
      return null;
    }
    return (DisplayElement) stack.get(0);
  }

  public State saveState() throws StateException
  {
    DisplayModelBuilderState dbms = new DisplayModelBuilderState();
    dbms.setStackContents (saveStack());
    dbms.setContentGeneratorState(contentGenerator.saveState());
    return dbms;
  }

  protected DisplayNode[] saveStack () throws StateException
  {
    final int size = stack.size();
    DisplayNode[] nodes = new DisplayNode[size];
    for (int i = 0; i < size; i++)
    {
      final DisplayElement element = (DisplayElement) stack.get(i);
      try
      {
        nodes[i] = (DisplayNode) element.clone();
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException("Unable to save state");
      }
    }
    return nodes;
  }

  protected void restoreStack (DisplayNode[] nodes)
          throws StateException, NormalizationException
  {
    stack.clear();
    DisplayElement parent = null;
    for (int i = 0; i < nodes.length; i++)
    {
      try
      {
        DisplayNode node = (DisplayNode) nodes[i].clone();
        if (node instanceof DisplayRoot)
        {
          DisplayRoot dfe = (DisplayRoot) node;
          dfe.setContentGenerator(contentGenerator);
          dfe.setLayoutProcess(layoutProcess);
        }

        if (parent != null)
        {
          parent.add(node);
        }
        else
        {
          node.setParent(null);
        }

        stack.push(node);

        if (node instanceof DisplayElement)
        {
          parent = (DisplayElement) node;
        }
        else
        {
          parent = null;
        }
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException("Unable to restore state");
      }
    }
  }

  public Renderer getRenderer()
  {
    return contentGenerator.getRenderer();
  }
}
