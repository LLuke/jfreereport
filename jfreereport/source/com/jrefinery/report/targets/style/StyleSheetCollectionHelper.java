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
 * ------------------------------
 * StyleSheetCollectionHelper.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheetCollectionHelper.java,v 1.4 2003/06/27 14:25:24 taqua Exp $
 *
 * Changes
 * -------------------------
 * 19-Jun-2003 : Initial version
 */

package com.jrefinery.report.targets.style;

import java.io.Serializable;

/**
 * An abstract helper implementation to make handling the complex registration
 * and unregistration of the StyleSheetCollection easier.
 * <p>
 * If the stylesheet collection is not registered/unregistered properly, it
 * may cause horrible effects like OutOfMemoryExceptions or uncontrollable
 * missbehaviour.
 *
 * @see StyleSheetCollection
 * @see ElementStyleSheet
 * @author Thomas Morgner
 */
public abstract class StyleSheetCollectionHelper implements Serializable
{
  /** The local stylesheet collection managed by this helper implementation. */
  private StyleSheetCollection styleSheetCollection;

  /**
   * DefaultConstructor. Does nothing.
   */
  public StyleSheetCollectionHelper()
  {
  }

  /**
   * Returns the stylesheet collection assigned with this StyleSheetCollectioHelper,
   * or null, if no collection is registered.
   *
   * @return the registered collection or null.
   */
  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollection;
  }

  /**
   * Unregisters the given stylesheet collection from this helper. If this stylesheet
   * collection is not registered with this helper, this method will throw an
   * <code>InvalidStyleSheetCollectionException</code>
   *
   * @param styleSheetCollection the stylesheet collection that should be unregistered.
   * @throws InvalidStyleSheetCollectionException when an other stylesheet was already
   * registered with this helper
   * @throws NullPointerException if the given stylesheet collection is null.
   */
  public void unregisterStyleSheetCollection(final StyleSheetCollection styleSheetCollection)
  {
    if (styleSheetCollection == null)
    {
      throw new NullPointerException();
    }

    if (this.styleSheetCollection == null)
    {
      // already unregistered, do nothing ...
      return;
    }
    /**
     * Do nothing if both stylesheets are equal.
     */
    if (this.styleSheetCollection != styleSheetCollection)
    {
      throw new InvalidStyleSheetCollectionException("This styleCollection is not known.");
    }

    handleUnregisterStyleSheetCollection();
    this.styleSheetCollection = null;
    return;

  }

  /**
   * Registers the given StyleSheet collection with this helper. If there is already
   * another stylesheet collection registered, this method will throw an
   * <code>InvalidStyleSheetCollectionException</code>.
   *
   * @param styleSheetCollection the stylesheet collection that should be registered.
   * @throws InvalidStyleSheetCollectionException if there is already an other
   * stylesheet registered.
   * @throws NullPointerException if the given stylesheet collection is null.
   */
  public void registerStyleSheetCollection(final StyleSheetCollection styleSheetCollection)
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

  /**
   * Handles the stylesheet collection registration.
   */
  protected abstract void handleRegisterStyleSheetCollection();

  /**
   * Handles the stylesheet collection removal.
   */
  protected abstract void handleUnregisterStyleSheetCollection();

}
