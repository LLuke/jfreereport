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
 * $Id: ExcelOutputProcessor.java,v 1.2 2006/11/13 19:14:05 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.excel;

import java.io.OutputStream;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.output.AbstractOutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 12.11.2006, 15:36:56
 *
 * @author Thomas Morgner
 */
public class ExcelOutputProcessor extends AbstractOutputProcessor
{
  private OutputStream outputStream;

  public ExcelOutputProcessor(final Configuration configuration,
                              final OutputStream outputStream)
  {
    super(configuration);
    this.outputStream = outputStream;
  }

  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  public OutputProcessorMetaData getMetaData()
  {
    return null;
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    return null;
  }

  protected void processPageContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPage)
  {

  }
}
