/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * -------------------
 * HtmlFilesystem.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlFilesystem.java,v 1.5 2005/01/25 00:13:23 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.ImageContainer;
import org.jfree.report.modules.output.table.html.ref.HtmlReference;

/**
 * The HtmlFilesystem provides an abstraction layer for the various storage methods
 * implemented for the HtmlProducer.
 * <p/>
 * <ul> <li>{@link DirectoryHtmlFilesystem}<p> Writes the generated Html-File and the
 * supplementary data files (images and external Stylesheet definition) into a directory.
 * The data files can be written into a separated data directory. <li>{link
 * StreamHtmlFilesystem}<p> Writes a single generated Html-File into the supplied stream.
 * The Stylesheet is inlined in the html file, no other external data files are generated.
 * Images, which are loaded from an valid URL are included in the file, any other images
 * are ignored. <li>{link ZIPHtmlFilesystem}<p> Similiar to the DirectoryHtmlFilesystem,
 * the generated Html-File and the supplementary data files (images and external
 * Stylesheet definition) into a directory in a ZIP-File. The data files can be written
 * into a separated data directory within the ZIP-File.
 *
 * @author Thomas Morgner
 */
public interface HtmlFilesystem
{
  /**
   * The root stream is used to write the main HTML-File. Any external content is
   * referenced from this file.
   *
   * @return the output stream of the main HTML file.
   *
   * @throws IOException if an IO error occured, while providing the root stream.
   */
  public OutputStream getRootStream ()
          throws IOException;

  /**
   * Creates a HtmlReference for ImageData. If the target filesystem does not support this
   * reference type, return an empty content reference, but never null.
   *
   * @param reference the image reference containing the data.
   * @return the generated HtmlReference, never null.
   *
   * @throws IOException if IO errors occured while creating the reference.
   */
  public HtmlReference createImageReference (ImageContainer reference)
          throws IOException;

  /**
   * Creates a HtmlReference for StyleSheetData. If the target filesystem does not support
   * external stylesheets, return an inline stylesheet reference.
   *
   * @param styleSheet the stylesheet data, which should be referenced.
   * @return the generated HtmlReference, never null.
   *
   * @throws IOException if IO errors occured while creating the reference.
   */
  public HtmlReference createCSSReference (String styleSheet)
          throws IOException;

  /**
   * Close the Filesystem and write any buffered content. The filesystem will not be
   * accessed, after close was called.
   *
   * @throws IOException if the close operation failed.
   */
  public void close ()
          throws IOException;
}
