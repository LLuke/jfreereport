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
 * ------------------------------
 * StyleSheetCollection.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: StyleSheetCollection.java,v 1.6 2004/05/07 08:14:24 mungady Exp $
 *
 * Changes
 * -------------------------
 * 12-Jun-2003 : Initial version
 *
 */

package org.jfree.report.style;

import java.io.Serializable;
import java.util.HashMap;

import org.jfree.report.util.InstanceID;

/**
 * The stylesheet collection manages all global stylesheets. It does not
 * contain the element stylesheets.
 * <p>
 * The stylesheet collection does not accept foreign stylesheets.
 *
 * @author Thomas Morgner
 */
public class StyleSheetCollection implements Cloneable, Serializable
{
  protected static class ManagedStyleSheetCarrier implements StyleSheetCarrier
  {
    private InstanceID styleSheetID;
    private ManagedStyleSheet styleSheet;
    private ManagedStyleSheet self;

    public ManagedStyleSheetCarrier (final ManagedStyleSheet parent,
                                     final ManagedStyleSheet self)
    {
      this.styleSheetID = parent.getId();
      this.styleSheet = parent;
      this.self = self;
      self.addListener(styleSheet);
    }

    public ElementStyleSheet getStyleSheet ()
    {
      if (styleSheet != null)
      {
        return styleSheet;
      }
      // just after the cloning ...
      final StyleSheetCollection col = self.getStyleSheetCollection();
      styleSheet = (ManagedStyleSheet) col.getStyleSheetByID(styleSheetID);
      if (styleSheet == null)
      {
        // should not happen in a sane environment ..
        throw new IllegalStateException
                ("Stylesheet was not valid after restore operation.");
      }
      return styleSheet;
    }

    public void invalidate ()
    {
      self.removeListener(getStyleSheet());
    }

    public boolean isSame (final ElementStyleSheet style)
    {
      return style.getId().equals(styleSheetID);
    }

    public Object clone ()
            throws CloneNotSupportedException
    {
      final ManagedStyleSheetCarrier o =
              (ManagedStyleSheetCarrier) super.clone();
      o.styleSheet = null;
      return o;
    }
  }

  protected static class ManagedStyleSheet extends ElementStyleSheet
  {
    private StyleSheetCollection styleSheetCollection;

    /**
     *
     * @param name
     * @param collection the stylesheet collection that created this stylesheet, or null, if
     *                   it is a foreign or private stylesheet.
     */
    public ManagedStyleSheet (final String name, final StyleSheetCollection collection)
    {
      super(name);
      if (collection == null)
      {
        throw new NullPointerException();
      }
      this.styleSheetCollection = collection;
    }

    /**
     * Adds a parent style-sheet. This method adds the parent to the beginning of the list,
     * and guarantees, that this parent is queried first.
     *
     * @param parent the parent (<code>null</code> not permitted).
     */
    public void addParent (final ManagedStyleSheet parent)
    {
      super.addParent(0, parent);
    }

    /**
     * Adds a parent style-sheet. Parents on a lower position are queried before any parent
     * with an higher position in the list.
     *
     * @param position the position where to insert the parent style sheet
     * @param parent   the parent (<code>null</code> not permitted).
     * @throws IndexOutOfBoundsException if the position is invalid (pos &lt; 0 or pos &gt;=
     *                                   numberOfParents)
     */
    public void addParent (final int position,
                                        final ManagedStyleSheet parent)
    {
      super.addParent(position, parent);
    }

    /**
     * Creates and returns a copy of this object. After the cloning, the new StyleSheet is
     * no longer registered with its parents.
     *
     * @return a clone of this instance.
     *
     * @see Cloneable
     */
    public Object clone () throws CloneNotSupportedException
    {
      final ManagedStyleSheet ms = (ManagedStyleSheet) super.clone();
      ms.styleSheetCollection = null;

      final StyleSheetCarrier[] sheets = ms.getParentReferences();
      for (int i = 0; i < sheets.length; i++)
      {
        final ManagedStyleSheetCarrier msc = (ManagedStyleSheetCarrier) sheets[i];
        msc.self = ms;
      }
      return ms;
    }

    protected StyleSheetCarrier createCarrier (final ElementStyleSheet styleSheet)
    {
      if (styleSheet instanceof ManagedStyleSheet == false)
      {
        throw new IllegalArgumentException
                ("Only stylesheets that are managed by this stylesheet collection can be added");
      }
      final ManagedStyleSheet ms = (ManagedStyleSheet) styleSheet;
      // yes, only this object, no clone, not logical the same, we mean PHYSICAL IDENTITY
      if (ms.getStyleSheetCollection() != getStyleSheetCollection())
      {
        throw new IllegalArgumentException
                ("Only stylesheets that are managed by this stylesheet collection can be added");
      }
      return new ManagedStyleSheetCarrier(ms, this);
    }

    public ManagedStyleSheet createManagedCopy (final StyleSheetCollection collection)
      throws CloneNotSupportedException
    {
      final ManagedStyleSheet es = (ManagedStyleSheet) getCopy();
      es.setStyleSheetCollection(collection);
      return es;
    }

    public StyleSheetCollection getStyleSheetCollection ()
    {
      return styleSheetCollection;
    }

    protected void setStyleSheetCollection (final StyleSheetCollection styleSheetCollection)
    {
      this.styleSheetCollection = styleSheetCollection;
    }
  }

  /** The stylesheet storage. */
  private HashMap styleSheets;
  private HashMap styleSheetsByID;

  /**
   * DefaultConstructor.
   */
  public StyleSheetCollection()
  {
    styleSheets = new HashMap();
    styleSheetsByID = new HashMap();
  }

  /**
   *
   * @throws NullPointerException if the given stylesheet is null.
   */
  public ElementStyleSheet createStyleSheet(final String name)
  {
    if (styleSheets.containsKey(name))
    {
      return (ElementStyleSheet) styleSheets.get(name);
    }
    final ElementStyleSheet value = new ManagedStyleSheet(name, this);
    styleSheets.put (name, value);
    styleSheetsByID.put (value.getId(), value);
    return value;
  }

  public ElementStyleSheet getStyleSheet (final String name)
  {
    return (ElementStyleSheet) styleSheets.get (name);
  }

  public ElementStyleSheet getStyleSheetByID (final InstanceID name)
  {
    return (ElementStyleSheet) styleSheetsByID.get (name);
  }

  public Object clone () throws CloneNotSupportedException
  {
    final StyleSheetCollection sc = (StyleSheetCollection) super.clone();
    sc.styleSheets = (HashMap) styleSheets.clone();
    sc.styleSheetsByID = (HashMap) styleSheetsByID.clone();

    final ManagedStyleSheet[] styles = (ManagedStyleSheet[]) styleSheets.values().toArray(new ManagedStyleSheet[styleSheets.size()]);
    final ManagedStyleSheet[] styleClones = (ManagedStyleSheet[]) styles.clone();
    // create the clones ...
    for (int i = 0; i < styles.length; i++)
    {
      final ManagedStyleSheet clone = styles[i].createManagedCopy(sc);
      sc.styleSheets.put (clone.getName(), clone);
      sc.styleSheetsByID.put (clone.getId(), clone);
      styleClones[i] = clone;
    }
    return sc;
  }
}
