/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ImageContainer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ImageContainer.java,v 1.1 2004/03/16 15:34:26 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report;

import java.awt.geom.Rectangle2D;

public interface ImageContainer extends Cloneable
{
  public Rectangle2D getBounds();
  public Rectangle2D getBoundsScaled();
  public float getScaleX();
  public float getScaleY();
  public void setScale (float x, float y);
  public int getImageWidth();
  public int getImageHeight();
  public ImageContainer getPartialInstance (Rectangle2D bounds);
}