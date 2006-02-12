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
 * DefaultLayoutContext.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: DefaultLayoutContext.java,v 1.1 2006/02/12 21:43:08 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
 */
package org.jfree.layouting.model;

import org.jfree.layouting.model.border.BackgroundSpecification;
import org.jfree.layouting.model.border.BorderSpecification;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.model.content.ContentSpecification;
import org.jfree.layouting.model.lists.ListSpecification;
import org.jfree.layouting.model.position.PositionSpecification;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.model.box.ReplacedElementSpecification;
import org.jfree.layouting.model.line.LineSpecification;
import org.jfree.layouting.output.OutputProcessor;

/**
 * Creation-Date: 14.12.2005, 13:42:06
 *
 * @author Thomas Morgner
 */
public class DefaultLayoutContext implements LayoutContext
{
  private BackgroundSpecification backgroundSpecification;
  private BorderSpecification borderSpecification;
  private FontSpecification fontSpecification;
  private OutputProcessor outputProcessor;
  private TextSpecification textSpecification;
  private ContentSpecification contentSpecification;
  private ListSpecification listSpecification;
  private PositionSpecification positionSpecification;
  private BoxSpecification boxSpecification;
  private ReplacedElementSpecification replacedElementSpecification;
  private LineSpecification lineSpecification;

  public DefaultLayoutContext(final OutputProcessor outputProcessor)
  {
    if (outputProcessor == null)
    {
      throw new NullPointerException("OutputProcessor must not be null.");
    }
    this.outputProcessor = outputProcessor;
    this.fontSpecification = new FontSpecification(outputProcessor);
    this.backgroundSpecification = new BackgroundSpecification();
    this.borderSpecification = new BorderSpecification();
    this.textSpecification = new TextSpecification();
    this.contentSpecification = new ContentSpecification();
    this.listSpecification = new ListSpecification();
    this.positionSpecification = new PositionSpecification();
    this.boxSpecification = new BoxSpecification();
    this.replacedElementSpecification = new ReplacedElementSpecification();
    this.lineSpecification = new LineSpecification();
  }

  public OutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public BackgroundSpecification getBackgroundSpecification()
  {
    return backgroundSpecification;
  }

  public BorderSpecification getBorderSpecification()
  {
    return borderSpecification;
  }

  public FontSpecification getFontSpecification()
  {
    return fontSpecification;
  }

  public TextSpecification getTextSpecification()
  {
    return textSpecification;
  }

  public ContentSpecification getContentSpecification ()
  {
    return contentSpecification;
  }

  public ListSpecification getListSpecification ()
  {
    return listSpecification;
  }

  public PositionSpecification getPositionSpecification ()
  {
    return positionSpecification;
  }

  public BoxSpecification getBoxSpecification ()
  {
    return boxSpecification;
  }

  public ReplacedElementSpecification getReplacedElementSpecification ()
  {
    return replacedElementSpecification;
  }

  public LineSpecification getLineSpecification ()
  {
    return lineSpecification;
  }
}
