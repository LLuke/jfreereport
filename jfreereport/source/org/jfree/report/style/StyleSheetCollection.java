/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * StyleSheetCollection.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheetCollection.java,v 1.1 2003/07/07 22:44:09 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12-Jun-2003 : Initial version
 *
 */

package org.jfree.report.style;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jfree.report.util.HashNMap;
import org.jfree.report.util.InstanceID;

/**
 * The StyleSheet collection is assigned to all Elements, all StyleSheets and
 * to the JFreeReport and the ReportDefinition objects. This collection is used
 * to coordinate the deep cloning of the stylesheets of an report. As bonus
 * functionality it allows simplified access to the stylesheets.
 *
 * @author Thomas Morgner
 */
public class StyleSheetCollection implements Cloneable, Serializable
{
  /**
   * A style collection entry. This class holds the stylesheet and an reference
   * count to the contained stylesheet. A stylesheet can only be removed if
   * the reference count is 0.
   */
  private static class StyleCollectionEntry implements Serializable
  {
    /** The reference count of the stylesheet. */
    private int referenceCount;
    /** The stylesheet. */
    private ElementStyleSheet styleSheet;

    /**
     * Creates a new StyleCollectionEntry for the stylesheet with an reference
     * count of 0.
     *
     * @param styleSheet The ElementStyleSheet that should be stored in that entry.
     */
    public StyleCollectionEntry(final ElementStyleSheet styleSheet)
    {
      this(0, styleSheet);
    }

    /**
     * Creates a new StyleCollectionEntry for the stylesheet and with the given
     * reference count.
     *
     * @param styleSheet The ElementStyleSheet that should be stored in that entry.
     * @param referenceCount the initial reference count.
     * @throws IllegalArgumentException if the reference count is negative.
     * @throws NullPointerException if the given stylesheet is null.
     */
    public StyleCollectionEntry(final int referenceCount, final ElementStyleSheet styleSheet)
    {
      if (referenceCount < 0)
      {
        throw new IllegalArgumentException("Initial reference count may not be negative.");
      }
      if (styleSheet == null)
      {
        throw new NullPointerException("StyleSheet for the entry must not be null.");
      }
      this.referenceCount = referenceCount;
      this.styleSheet = styleSheet;
    }

    /**
     * Returns the reference count.
     *
     * @return the reference count.
     */
    public int getReferenceCount()
    {
      return referenceCount;
    }

    /**
     * Defines the new reference count for this stylesheet.
     *
     * @param referenceCount the reference count.
     * @throws IllegalArgumentException if the reference count is negative.
     */
    public void setReferenceCount(final int referenceCount)
    {
      if (referenceCount < 0)
      {
        throw new IllegalArgumentException("Initial reference count may not be negative.");
      }
      this.referenceCount = referenceCount;
    }

    /**
     * Returns the stylesheet stored in that entry.
     *
     * @return the stylesheet.
     */
    public ElementStyleSheet getStyleSheet()
    {
      return styleSheet;
    }
  }

  /** The stylesheet storage. */
  private HashNMap styleSheets;

  /**
   * DefaultConstructor.
   */
  public StyleSheetCollection()
  {
    styleSheets = new HashNMap();
  }

  /**
   * Adds the given stylesheet to this collection. This throw an
   * IllegalArgumentException if the stylesheet is already added to another
   * stylesheet collection.
   *
   * @param es the element stylesheet
   * @throws NullPointerException if the given stylesheet is null.
   */
  public void addStyleSheet(final ElementStyleSheet es)
  {
    addStyleSheet(es, true);
  }

  /**
   * Adds the given stylesheet to this collection. This throw an
   * IllegalArgumentException if the stylesheet is already added to another
   * stylesheet collection.
   *
   * @param es the element stylesheet that should be added.
   * @param updateRefs true, if the reference counts should be recomputed,
   * false otherwise.
   * @throws IllegalStateException if the stylesheet is already added, but has
   * a different stylesheet collection assigned.
   * @throws NullPointerException if the given element stylesheet is null.
   */
  protected void addStyleSheet(final ElementStyleSheet es, final boolean updateRefs)
  {
    if (es == null)
    {
      throw new NullPointerException("Element stylesheet to be added must not be null.");
    }
    if (contains(es) == false)
    {
      styleSheets.add(es.getName(), new StyleCollectionEntry(es));
      es.registerStyleSheetCollection(this);

      addParents(es);
      if (updateRefs)
      {
        updateReferences();
      }
    }
    // /** assert: stylesheet collection set ...
    else
    {
      if (es.isGlobalDefault() == false)
      {
        if (es.getStyleSheetCollection() != this)
        {
          throw new IllegalStateException("Invalid StyleSheet detected!" + es.getClass());
        }
      }
    }
    //  */
  }

  /**
   * Tests, whether the stylesheet is contained in that collection.
   *
   * @param es the stylesheet that is searched.
   * @return true, if the stylesheet is contained in that collection, false otherwise.
   */
  private boolean contains(final ElementStyleSheet es)
  {
    if (styleSheets.containsKey(es.getName()))
    {
      final Iterator it = styleSheets.getAll(es.getName());
      while (it.hasNext())
      {
        final StyleCollectionEntry se = (StyleCollectionEntry) it.next();
        final ElementStyleSheet ces = se.getStyleSheet();
        if (ces.getId() == es.getId())
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns all stylesheets for a given name or null, if there is no such stylesheet
   * registered.
   *
   * @param name the name of the stylesheet.
   * @return the found stylesheets as object array or null.
   */
  public ElementStyleSheet[] getAll(final String name)
  {
    final StyleCollectionEntry[] data = (StyleCollectionEntry[])
        styleSheets.toArray(name, new StyleCollectionEntry[styleSheets.getValueCount(name)]);
    if (data.length == 0)
    {
      return null;
    }
    final ElementStyleSheet[] retval = new ElementStyleSheet[data.length];
    for (int i = 0; i < data.length; i++)
    {
      final StyleCollectionEntry se = data[i];
      retval[i] = se.getStyleSheet();
    }
    return retval;
  }

  /**
   * Returns the first element stylesheet with that name.
   *
   * @param name the name of the searched stylesheet.
   * @return the stylesheet or null, if there is no such stylesheet.
   */
  public ElementStyleSheet getFirst(final String name)
  {
    final StyleCollectionEntry se = (StyleCollectionEntry) styleSheets.getFirst(name);
    if (se == null)
    {
      return null;
    }
    return se.getStyleSheet();
  }

  /**
   * Clones this stylesheet collection and all stylesheets contained in that
   * collection. The stylesheets of this collection get cloned and reassigned
   * after the cloning.
   *
   * @return the cloned collection.
   * @throws CloneNotSupportedException if cloning failed.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final StyleSheetCollection col = (StyleSheetCollection) super.clone();
    col.styleSheets = new HashNMap();
    // clone all contained stylesheets ...
    Iterator it = styleSheets.keySet().iterator();
    StyleCollectionEntry[] allElements = new StyleCollectionEntry[0];
    while (it.hasNext())
    {
      final Object key = it.next();
      final int len = styleSheets.getValueCount(key);
      allElements = (StyleCollectionEntry[]) styleSheets.toArray(key, allElements);
      for (int i = 0; i < len; i++)
      {
        final StyleCollectionEntry se = allElements[i];
        final ElementStyleSheet es = se.getStyleSheet();
        final ElementStyleSheet esCopy = es.getCopy();
        col.styleSheets.add(esCopy.getName(),
            new StyleCollectionEntry(se.getReferenceCount(), esCopy));
      }
    }

    // next reconnect the stylesheets
    // the default parents dont need to be updated, as they are shared among all
    // stylesheets ...
    it = col.styleSheets.keySet().iterator();
    while (it.hasNext())
    {
      final Object key = it.next();
      final int len = col.styleSheets.getValueCount(key);
      allElements = (StyleCollectionEntry[]) col.styleSheets.toArray(key, allElements);
      for (int ai = 0; ai < len; ai++)
      {
        final StyleCollectionEntry se = allElements[ai];
        final ElementStyleSheet es = se.getStyleSheet();

        final List parents = es.getParents();
        // reversed add order .. last parent must be added first ..
        final ElementStyleSheet[] parentArray =
            (ElementStyleSheet[]) parents.toArray(new ElementStyleSheet[parents.size()]);
        for (int i = parentArray.length - 1; i >= 0; i--)
        {
          final String name = parentArray[i].getName();
          final InstanceID id = parentArray[i].getId();
          es.removeParent(parentArray[i]);
          final StyleCollectionEntry seParent = col.findStyleSheet(name, id);
          if (seParent == null)
          {
            throw new IllegalStateException
                ("A parent of an stylesheet was not found in this collection.");
          }
          es.addParent(seParent.getStyleSheet());
        }
      }
    }
    // until now, all cloned stylesheets are unregistered ...
    // restore the registration now ...
    it = col.styleSheets.keySet().iterator();
    while (it.hasNext())
    {
      final Object key = it.next();
      final int len = col.styleSheets.getValueCount(key);
      allElements = (StyleCollectionEntry[]) col.styleSheets.toArray(key, allElements);
      for (int i = 0; i < len; i++)
      {
        final StyleCollectionEntry se = allElements[i];
        final ElementStyleSheet es = se.getStyleSheet();
        es.registerStyleSheetCollection(col);
      }
    }

    return col;
  }

  /**
   * Searches for a stylesheet with the same name and id. This method returns
   * null, if no such stylesheet exists.
   *
   * @param name the name of the stylesheet.
   * @param id the instance id of the stylesheet
   * @return the found stylesheet entry or null if not found.
   */
  private StyleCollectionEntry findStyleSheet(final String name, final InstanceID id)
  {
    final int len = styleSheets.getValueCount(name);
    if (len == 0)
    {
      return null;
    }

    final StyleCollectionEntry[] data = (StyleCollectionEntry[])
        styleSheets.toArray(name, new StyleCollectionEntry[len]);

    for (int i = 0; i < data.length; i++)
    {
      final StyleCollectionEntry es = data[i];
      if (es.getStyleSheet().getId() == id)
      {
        return es;
      }
    }
    return null;
  }

  /**
   * Updates a stylesheet reference from this collection. This is usually done after
   * a clone() operation to update the parents of the given element stylesheet.
   * <p>
   * This operation will remove all parents of the stylesheet and repace them with
   * stylesheets from this collection with the same name and Id.
   *
   * @param es the elements stylesheet that should be replaced.
   */
  public void updateStyleSheet(final ElementStyleSheet es)
  {
    if (es.getStyleSheetCollection() != null)
    {
      throw new IllegalArgumentException
          ("This stylesheet instance is already registered with an collection.");
    }
    if (contains(es) == false)
    {
      throw new IllegalArgumentException("This stylesheet is not in the collection.");
    }
    else
    {
      final StyleCollectionEntry entry = findStyleSheet(es.getName(), es.getId());
      styleSheets.remove(es.getName(), entry);
      styleSheets.add(es.getName(), new StyleCollectionEntry(entry.getReferenceCount(), es));

      final List parents = es.getParents();
      // reversed add order .. last parent must be added first ..
      final ElementStyleSheet[] parentArray =
          (ElementStyleSheet[]) parents.toArray(new ElementStyleSheet[parents.size()]);
      for (int i = parentArray.length - 1; i >= 0; i--)
      {
        final String name = parentArray[i].getName();
        final InstanceID id = parentArray[i].getId();
        es.removeParent(parentArray[i]);
        final StyleCollectionEntry seParent = findStyleSheet(name, id);
        if (seParent == null)
        {
          throw new IllegalStateException
              ("A parent of an stylesheet was not found in this collection.");
        }
        es.addParent(seParent.getStyleSheet());
      }
      es.registerStyleSheetCollection(this);
    }
  }

  /**
   * Adds all parents of the given stylesheet recursivly to this collection.
   *
   * @param es the element style sheet whose parents should be added.
   */
  protected void addParents(final ElementStyleSheet es)
  {
    final List parents = es.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      final ElementStyleSheet esp = (ElementStyleSheet) parents.get(i);
      addStyleSheet(esp, false);
    }
    final List defaultParents = es.getDefaultParents();
    for (int i = 0; i < defaultParents.size(); i++)
    {
      final ElementStyleSheet esp = (ElementStyleSheet) defaultParents.get(i);
      addStyleSheet(esp, false);
    }
  }

  /**
   * Updates the reference count of all stylesheets.
   * This method is expensive and is (like the whole class) a candidate for
   * an performance redesign ...
   */
  protected void updateReferences()
  {
    Iterator keyIterator = styleSheets.keys();
    StyleCollectionEntry[] allElements = new StyleCollectionEntry[0];
    while (keyIterator.hasNext())
    {
      // reset the reference count ...
      final Object key = keyIterator.next();
      final int len = styleSheets.getValueCount(key);
      allElements = (StyleCollectionEntry[]) styleSheets.toArray(key, allElements);
      for (int i = 0; i < len; i++)
      {
        final StyleCollectionEntry se = allElements[i];
        se.setReferenceCount(0);
      }
    }

    keyIterator = styleSheets.keys();
    while (keyIterator.hasNext())
    {
      // compute the reference count ...
      final Object key = keyIterator.next();
      final int len = styleSheets.getValueCount(key);
      allElements = (StyleCollectionEntry[]) styleSheets.toArray(key, allElements);
      for (int ai = 0; ai < len; ai++)
      {
        final StyleCollectionEntry se = allElements[ai];
        final ElementStyleSheet es = se.getStyleSheet();

        final List parents = es.getParents();
        for (int i = 0; i < parents.size(); i++)
        {
          final ElementStyleSheet esp = (ElementStyleSheet) parents.get(i);
          final StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
          if (sep == null)
          {
            throw new NullPointerException("StyleSheet '" + esp.getName() + "' is not known.");
          }
          sep.setReferenceCount(sep.getReferenceCount() + 1);
        }
        final List defaultParents = es.getDefaultParents();
        for (int i = 0; i < defaultParents.size(); i++)
        {
          final ElementStyleSheet esp = (ElementStyleSheet) defaultParents.get(i);
          final StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
          sep.setReferenceCount(sep.getReferenceCount() + 1);
        }
      }
    }
    // Styles with an RefCount of 0 can be removed if requested...
  }

  /**
   * Returns true, if removing the stylesheet was successfull, false
   * if the stylesheet is still referenced and will not be removed.
   *
   * @param es the element stylesheet that should be removed.
   * @return true, if the stylesheet was removed, false otherwise.
   */
  public boolean remove(final ElementStyleSheet es)
  {
    return remove(es, true);
  }

  /**
   * Returns true, if the stylesheet if removing was successfull, false
   * if the stylesheet is still referenced and won't be removed.
   *
   * @param update true, if the reference counts should be updated, false otherwise.
   * @param es the element stylesheet that should be removed.
   * @return true, if the stylesheet was removed, false otherwise.
   */
  protected boolean remove(final ElementStyleSheet es, final boolean update)
  {
    if (contains(es) == false)
    {
      return true;
    }
    else
    {
      final StyleCollectionEntry se = findStyleSheet(es.getName(), es.getId());
      if (se.getReferenceCount() != 0)
      {
        return false;
      }

      // finally remove the stylesheet itself ...
      if (styleSheets.remove(es.getName(), se) == false)
      {
        return false;
      }
      else
      {
        es.unregisterStyleSheetCollection(this);
      }

      // check whether we can remove the parents ...
      final List parents = es.getParents();
      for (int i = 0; i < parents.size(); i++)
      {
        final ElementStyleSheet esp = (ElementStyleSheet) parents.get(i);
        final StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
        sep.setReferenceCount(sep.getReferenceCount() - 1);
        remove(esp, false);
      }
      final List defaultParents = es.getDefaultParents();
      for (int i = 0; i < defaultParents.size(); i++)
      {
        final ElementStyleSheet esp = (ElementStyleSheet) defaultParents.get(i);
        final StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
        sep.setReferenceCount(sep.getReferenceCount() - 1);
        remove(esp, false);
      }
      if (update)
      {
        updateReferences();
      }
      return true;
    }

  }

  /**
   * Returns the names of all registered stylesheets as iterator. There can
   * be more than one stylesheet be registered with a certain name.
   *
   * @return the names of all stylesheets.
   */
  public Iterator keys()
  {
    final Set keySet = styleSheets.keySet();
    return Collections.unmodifiableSet(keySet).iterator();
  }
}
