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
 * StyleSheetCollectionHelper.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheetCollectionHelper.java,v 1.1 2003/06/19 18:50:18 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 19.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.targets.style;

import java.io.Serializable;

public abstract class StyleSheetCollectionHelper implements Serializable
{
  private StyleSheetCollection styleSheetCollection;

  public StyleSheetCollectionHelper()
  {
  }

  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollection;
  }

  public void unregisterStyleSheetCollection (StyleSheetCollection styleSheetCollection)
  {
    if (styleSheetCollection == null)
    {
      throw new NullPointerException();
    }

    /**
     * Do nothing if both stylesheets are equal.
     */
    if (this.styleSheetCollection != styleSheetCollection)
    {
      throw new IllegalArgumentException("This styleCollectio is not known.");
    }

    handleUnregisterStyleSheetCollection();
    this.styleSheetCollection = null;
    return;

  }

  public void registerStyleSheetCollection(StyleSheetCollection styleSheetCollection)
  {
    if (styleSheetCollection == null)
    {
      throw new NullPointerException();
    }

    /**
     * Do nothing if both stylesheets are equal.
     */
    if (this.styleSheetCollection == styleSheetCollection)
    {
      return;
    }

    /**
     * If there is an old stylesheet collection already registered,
     * throw an exception ...
     */
    if (this.styleSheetCollection != null)
    {
      throw new InvalidStyleSheetCollectionException
          ("There is already an other stylesheet collection added.");
    }

    this.styleSheetCollection = styleSheetCollection;
    handleRegisterStyleSheetCollection();
  }

  protected abstract void handleRegisterStyleSheetCollection();

  protected abstract void handleUnregisterStyleSheetCollection();

}
