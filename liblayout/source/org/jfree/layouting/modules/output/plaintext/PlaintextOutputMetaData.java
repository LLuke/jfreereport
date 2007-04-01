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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.plaintext;

import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.layouting.output.AbstractOutputProcessorMetaData;

/**
 * Creation-Date: 13.11.2006, 12:48:51
 *
 * @author Thomas Morgner
 */
public class PlaintextOutputMetaData extends AbstractOutputProcessorMetaData
{
  public PlaintextOutputMetaData(final FontRegistry registry)
  {
    super(new DefaultFontStorage(registry));
  }

  public FontFamily getDefaultFontFamily()
  {
    return null;
  }

  /**
   * The export descriptor is a string that describes the output
   * characteristics. For libLayout outputs, it should start with the output
   * class (one of 'pageable', 'flow' or 'stream'), followed by '/liblayout/'
   * and finally followed by the output type (ie. PDF, Print, etc).
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor()
  {
    return "pageable/plaintext";
  }

  /**
   * An iterative output processor accepts and processes small content chunks.
   * If this method returns false, the output processor will not receive the
   * content until the whole document is processed.
   *
   * @return
   */
  public boolean isIterative()
  {
    return false;
  }
}
