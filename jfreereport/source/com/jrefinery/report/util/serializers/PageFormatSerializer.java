/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * PageFormatSerializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageFormatSerializer.java,v 1.1 2003/06/01 20:35:26 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 01.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.util.serializers;

import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jrefinery.report.util.PageFormatFactory;
import com.jrefinery.report.util.SerializeMethod;

public class PageFormatSerializer implements SerializeMethod
{
  public PageFormatSerializer()
  {
  }

  public void writeObject(Object o, ObjectOutputStream out) throws IOException
  {
    PageFormat pf = (PageFormat) o;
    out.writeObject(PageFormatFactory.getInstance().resolvePageFormat(pf));
  }

  public Object readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    Object[] pageFormatResolve = (Object[]) in.readObject();
    return PageFormatFactory.getInstance().createPageFormat(pageFormatResolve);
  }

  public Class getObjectClass()
  {
    return PageFormat.class;
  }
}
