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
 * External8BitEncoding.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 29.04.2006 : Initial version
 */
package org.jfree.fonts.encoding;

import java.util.Locale;

import org.jfree.resourceloader.Resource;

/**
 * Creation-Date: 29.04.2006, 14:49:06
 *
 * @author Thomas Morgner
 */
public final class ExternalEncoding implements Encoding
{

  private String name;
  private EncodingCore core;

  /**
   * We keep a stong reference to our source, so that this thing won't be
   * recycled as long as one instance is in use.
   */
  private Resource resource;

  public ExternalEncoding(final String name,
                          final EncodingCore core,
                          final Resource resource)
  {
    this.name = name;
    this.core = core;
    this.resource = resource;
  }

  public String getName()
  {
    return name;
  }

  public String getName(Locale locale)
  {
    return name;
  }

  public boolean isUnicodeCharacterSupported(final int c)
  {
    return core.isUnicodeCharacterSupported(c);
  }

  public Resource getResource()
  {
    return resource;
  }

  public ByteBuffer encode(final CodePointBuffer text, final ByteBuffer buffer)
          throws EncodingException
  {
    return core.encode(text, buffer);
  }

  public ByteBuffer encode(final CodePointBuffer text, final ByteBuffer buffer,
                           final EncodingErrorType errorHandling)
          throws EncodingException
  {
    return core.encode(text, buffer, errorHandling);
  }

  public CodePointBuffer decode(final ByteBuffer text,
                                final CodePointBuffer buffer) throws
          EncodingException
  {
    return core.decode(text, buffer);
  }

  public CodePointBuffer decode(final ByteBuffer text,
                                final CodePointBuffer buffer,
                                final EncodingErrorType errorHandling)
          throws EncodingException
  {
    return core.decode(text, buffer, errorHandling);
  }
}
