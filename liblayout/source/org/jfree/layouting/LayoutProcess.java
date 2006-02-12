/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * LayoutProcess.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: LayoutProcess.java,v 1.1 2006/02/12 21:38:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.12.2005 : Initial version
 */
package org.jfree.layouting;

import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.model.ContextId;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.PageContext;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.normalizer.Normalizer;

/**
 * Creation-Date: 05.12.2005, 18:03:25
 *
 * @author Thomas Morgner
 */
public interface LayoutProcess
{
  public StyleResolver getStyleResolver();

  public InputFeed getInputFeed();

  public ContextId generateContextId (long parent);

  public long getLastId ();

  /**
   * The document context holds global information, like the used stylesheets.
   * It also holds the caches for loading external contents.
   *
   * @return the document context.
   */
  public DocumentContext getDocumentContext();
  public PageContext getPageContext();

  public OutputProcessorMetaData getOutputMetaData();
  public OutputProcessor getOutputProcessor();

  public Normalizer getNormalizer();
}
