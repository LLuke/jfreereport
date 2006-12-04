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
 * $Id: AbstractLayoutProcess.java,v 1.7 2006/12/03 18:57:49 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting;

import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.DefaultDocumentContext;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.style.resolver.DefaultStyleResolver;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 05.12.2005, 19:17:21
 *
 * @author Thomas Morgner
 */
public abstract class AbstractLayoutProcess implements LayoutProcess
{
  protected abstract static class AbstractLayoutProcessState
      implements LayoutProcessState
  {
    private State inputFeedState;
    private DocumentContext documentContext;
    private State styleResolver;

    public AbstractLayoutProcessState(AbstractLayoutProcess lp)
        throws StateException
    {
      if (lp.styleResolver != null)
      {
        this.styleResolver = lp.styleResolver.saveState();
      }
      this.documentContext = lp.documentContext;
      if (lp.inputFeed != null)
      {
        this.inputFeedState = lp.inputFeed.saveState();
      }
    }

    protected AbstractLayoutProcess restore(OutputProcessor outputProcessor,
                                            AbstractLayoutProcess layoutProcess)
        throws StateException
    {
      layoutProcess.documentContext = documentContext;
      if (styleResolver != null)
      {
        layoutProcess.styleResolver =
            (StyleResolver) styleResolver.restore(layoutProcess);
      }
      if (inputFeedState != null)
      {
        layoutProcess.inputFeed = (InputFeed) inputFeedState.restore(
            layoutProcess);
      }
      return layoutProcess;
    }
  }

  private InputFeed inputFeed;
  private DocumentContext documentContext;
  private OutputProcessor outputProcessor;
  private StyleResolver styleResolver;

  protected AbstractLayoutProcess(OutputProcessor outputProcessor)
  {
    if (outputProcessor == null)
    {
      throw new NullPointerException();
    }

    this.outputProcessor = outputProcessor;
    this.documentContext = new DefaultDocumentContext();
    this.styleResolver = new DefaultStyleResolver();

  }

  public OutputProcessorMetaData getOutputMetaData()
  {
    return outputProcessor.getMetaData();
  }

  public OutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public InputFeed getInputFeed()
  {
    if (inputFeed == null)
    {
      inputFeed = getOutputProcessor().createInputFeed(this);
    }
    return inputFeed;
  }

  protected abstract InputFeed createInputFeed();

  /**
   * The document context holds global information, like the used stylesheets.
   * It also holds the caches for loading external contents.
   *
   * @return the document context.
   */
  public DocumentContext getDocumentContext()
  {
    return documentContext;
  }

  public ResourceManager getResourceManager()
  {
    return documentContext.getResourceManager();
  }

  public void pageBreakEncountered(final CSSValue pageName,
                                   final PseudoPage[] pseudoPages)
      throws NormalizationException
  {
    getInputFeed().handlePageBreakEncountered(pageName, pseudoPages);
  }

  public boolean isPagebreakEncountered()
  {
    return getInputFeed().isPagebreakEncountered();
  }

//  protected abstract AbstractLayoutProcessState createState()
//      throws StateException;

//  protected void fillState(AbstractLayoutProcessState state) throws
//      StateException
//  {
//    state.setDocumentContext(documentContext);
//    if (inputFeed != null)
//    {
//      state.setInputFeedState(inputFeed.saveState());
//    }
//  }

//
//  public LayoutProcessState saveState() throws StateException
//  {
//    AbstractLayoutProcessState state = createState();
//    fillState(state);
//    return state;
//  }

  public Normalizer getNormalizer()
  {
    return getInputFeed().getCurrentNormalizer();
  }

  public StyleResolver getStyleResolver()
  {
    return styleResolver;
  }
}
