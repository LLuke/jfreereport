/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.renderer.process;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.computed.ComputedToken;
import org.jfree.layouting.layouter.content.computed.CounterToken;
import org.jfree.layouting.layouter.content.computed.VariableToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedCounterToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedStringToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedToken;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.renderer.CounterStore;
import org.jfree.layouting.renderer.StringStore;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableTextBox;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.text.RenderableTextFactory;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 29.11.2006, 21:56:47
 *
 * @author Thomas Morgner
 */
public class UpdateTokensStep extends IterateStructuralProcessStep
{
  private LayoutProcess layoutProcess;
  private DocumentContext documentContext;
  private StringStore stringStore;
  private CounterStore counterStore;
  private CodePointBuffer buffer;

  public UpdateTokensStep()
  {
  }

  public void compute(final LogicalPageBox pageBox,
                      final LayoutProcess layoutProcess,
                      final StringStore stringStore,
                      final CounterStore counterStore)
  {
    this.layoutProcess = layoutProcess;
    this.documentContext = this.layoutProcess.getDocumentContext();
    this.stringStore = stringStore;
    this.counterStore = counterStore;
    try
    {
      startProcessing(pageBox);
    }
    catch (IllegalStateException ise)
    {
      // ignore ..
      Log.error("Unable to update tokens..");
    }
    this.documentContext = null;
    this.stringStore = null;
    this.counterStore = null;
  }

  protected boolean startInlineBox(InlineRenderBox box)
  {
    if (box instanceof RenderableTextBox)
    {
      final RenderableTextBox textBox = (RenderableTextBox) box;
      final ResolvedToken resolvedToken = textBox.getResolvedToken();
      final String resolvedText = resolveToken(resolvedToken);
      if (ObjectUtilities.equal(textBox.getResolvedText(), resolvedText))
      {
        return false;
      }

      textBox.clear();
      final State textFactory = textBox.getTextFactory();
      try
      {
        final RenderableTextFactory rtf =
            (RenderableTextFactory) textFactory.restore(layoutProcess);
        final RenderNode[] text =
            createText(resolvedText, textBox.getLayoutContext(), rtf);

        for (int i = 0; i < text.length; i++)
        {
          textBox.addGeneratedChild(text[i]);
        }
        final RenderNode[] renderNodes = rtf.finishText();
        for (int i = 0; i < renderNodes.length; i++)
        {
          textBox.addGeneratedChild(renderNodes[i]);
        }

      }
      catch (StateException e)
      {
        // this should never happen ..
        Log.error ("Failed to resolve token ");
        return false;
      }
      return false;
    }
    return true;
  }

  private RenderNode[] createText(final String str,
                                  final LayoutContext context,
                                  final RenderableTextFactory textFactory)
  {
    if (buffer != null)
    {
      buffer.setCursor(0);
    }
    buffer = Utf16LE.getInstance().decodeString(str, buffer);
    final int[] data = buffer.getBuffer();

    return textFactory.createText(data, 0, buffer.getLength(), context);
  }

  protected String resolveToken(final ResolvedToken resolvedToken)
  {
    if (resolvedToken instanceof ResolvedStringToken)
    {
      final ResolvedStringToken stringToken =
          (ResolvedStringToken) resolvedToken;
      final ComputedToken parent = stringToken.getParent();
      if (parent instanceof VariableToken)
      {
        final VariableToken vt = (VariableToken) parent;
        final String variable = vt.getVariable();
        final CSSValue stringPolicy = documentContext.getStringPolicy(variable);
        return stringStore.get(variable, stringPolicy);
      }
    }
    else if (resolvedToken instanceof ResolvedCounterToken)
    {
      final ResolvedCounterToken ct = (ResolvedCounterToken) resolvedToken;
      final CounterToken parent = ct.getParent();
      final String name = parent.getName();
      final CounterStyle style = parent.getStyle();
      final CSSValue counterPolicy = documentContext.getCounterPolicy(name);
      final Integer counterValue = counterStore.get(name, counterPolicy);
      return style.getCounterValue(counterValue.intValue());
    }

    return resolvedToken.getText();
  }
}