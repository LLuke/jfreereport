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
 * AbstractLayoutProcess.java
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
package org.jfree.layouting;

import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.model.ContextId;
import org.jfree.layouting.model.DefaultDocumentContext;
import org.jfree.layouting.model.DefaultPageContext;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.PageContext;
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
  private InputFeed inputFeed;
  private long nextId;
  private DocumentContext documentContext;
  private OutputProcessor outputProcessor;
  private PageContext pageContext;

  protected AbstractLayoutProcess(OutputProcessor outputProcessor)
  {
    if (outputProcessor == null)
    {
      throw new NullPointerException();
    }

    this.outputProcessor = outputProcessor;
    this.documentContext = new DefaultDocumentContext();
    this.pageContext = new DefaultPageContext();
  }

  public OutputProcessorMetaData getOutputMetaData()
  {
    return outputProcessor.getMetaData();
  }

  public OutputProcessor getOutputProcessor ()
  {
    return outputProcessor;
  }

  public StyleResolver getStyleResolver()
  {
    return DocumentContextUtility.getStyleResolver(documentContext);
  }

  public InputFeed getInputFeed()
  {
    if (inputFeed == null)
    {
      inputFeed = createInputFeed();
    }
    return inputFeed;
  }

  protected abstract InputFeed createInputFeed();

  public ContextId generateContextId(long parent)
  {
    nextId += 1;
    return new ContextId (nextId, parent);
  }

  protected void setNextId (long id)
  {
    nextId = id;
  }

  public long getLastId()
  {
    return nextId;
  }

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

  public PageContext getPageContext ()
  {
    return pageContext;
  }

  public void setPageContext (PageContext pageContext)
  {
    this.pageContext = pageContext;
  }

  public ResourceManager getResourceManager()
  {
    return DocumentContextUtility.getResourceManager(documentContext);
  }
}
