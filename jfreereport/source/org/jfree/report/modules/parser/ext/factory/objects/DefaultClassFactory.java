/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------
 * DefaultClassFactory.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultClassFactory.java,v 1.11 2003/06/27 14:25:19 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.objects;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.FontDefinition;
import org.jfree.xml.factory.objects.JavaBaseClassFactory;

/**
 * A default implementation of the {@link org.jfree.xml.factory.objects.ClassFactory} interface.
 *
 * @author Thomas Morgner
 */
public class DefaultClassFactory extends JavaBaseClassFactory
{
  /**
   * Creates a new factory.
   */
  public DefaultClassFactory()
  {
    registerClass(ElementAlignment.class, new AlignmentObjectDescription());
    registerClass(FontDefinition.class, new FontDefinitionObjectDescription());
    registerClass(PathIteratorSegment.class, new PathIteratorSegmentObjectDescription());
    registerClass(Shape.class, new GeneralPathObjectDescription(Shape.class));
    registerClass(GeneralPath.class, new GeneralPathObjectDescription());
  }
}
