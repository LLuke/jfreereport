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
 * FlowingOutputProcessor.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FlowingOutputProcessor.java,v 1.1 2006/02/12 21:40:17 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03.01.2006 : Initial version
 */
package org.jfree.layouting.output.flowing;

import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.normalizer.flowing.FlowGenerationNormalizer;

/**
 * Creation-Date: 03.01.2006, 12:50:20
 *
 * @author Thomas Morgner
 */
public interface FlowingOutputProcessor extends OutputProcessor
{
  public FlowGenerationNormalizer createFlowNormalizer();
}
