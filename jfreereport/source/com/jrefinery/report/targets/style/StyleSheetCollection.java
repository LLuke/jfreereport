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
 * StyleSheetCollection.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheetCollection.java,v 1.5 2003/06/23 14:36:57 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12-Jun-2003 : Initial version
 *
 */

package com.jrefinery.report.targets.style;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jrefinery.report.util.HashNMap;
import com.jrefinery.report.util.InstanceID;
import com.jrefinery.report.util.Log;

/**
 * The StyleSheet collection is assigned to all Elements, all StyleSheets and
 * to the JFreeReport and the ReportDefinition objects.
 *
 * ?? ID may be invalid, reconsider this ...
 */
public class StyleSheetCollection implements Cloneable, Serializable
{
  private static class StyleCollectionEntry implements Serializable
  {
    private int referenceCount;
    private ElementStyleSheet styleSheet;

    public StyleCollectionEntry(ElementStyleSheet styleSheet)
    {
      this (0, styleSheet);
    }

    public StyleCollectionEntry(int referenceCount, ElementStyleSheet styleSheet)
    {
      this.referenceCount = referenceCount;
      this.styleSheet = styleSheet;
    }

    public int getReferenceCount()
    {
      return referenceCount;
    }

    public void setReferenceCount(int referenceCount)
    {
      this.referenceCount = referenceCount;
    }

    public ElementStyleSheet getStyleSheet()
    {
      return styleSheet;
    }
  }

  private HashNMap styleSheets;

  public StyleSheetCollection()
  {
    styleSheets = new HashNMap();
  }

  public void addStyleSheet (ElementStyleSheet es)
  {
    addStyleSheet(es, true);
  }

  /**
   * Adds the given stylesheet to this collection. This throw an
   * IllegalArgumentException if the stylesheet is already added to another
   * stylesheet collection.
   *
   * @param es
   * @param updateRefs
   */
  protected void addStyleSheet (ElementStyleSheet es, boolean updateRefs)
  {
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
  private boolean contains (ElementStyleSheet es)
  {
    if (styleSheets.containsKey(es.getName()))
    {
      Iterator it = styleSheets.getAll(es.getName());
      while (it.hasNext())
      {
        StyleCollectionEntry se = (StyleCollectionEntry) it.next();
        ElementStyleSheet ces = se.getStyleSheet();
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
  public ElementStyleSheet[] getAll (String name)
  {
    Object[] data = styleSheets.toArray(name);
    if (data == null)
    {
      return null;
    }
    ElementStyleSheet[] retval = new ElementStyleSheet[data.length];
    for (int i = 0; i < data.length; i++)
    {
      StyleCollectionEntry se = (StyleCollectionEntry) data[i];
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
  public ElementStyleSheet getFirst(String name)
  {
    StyleCollectionEntry se = (StyleCollectionEntry) styleSheets.getFirst(name);
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
    StyleSheetCollection col = (StyleSheetCollection) super.clone();
    col.styleSheets = new HashNMap();
    // clone all contained stylesheets ...
    Iterator it = styleSheets.keySet().iterator();
    while (it.hasNext())
    {
      Object key = it.next();
      Object[] allElements = styleSheets.toArray(key);
      for (int i = 0; i < allElements.length; i++)
      {
        StyleCollectionEntry se = (StyleCollectionEntry) allElements[i];
        ElementStyleSheet es = se.getStyleSheet();
        ElementStyleSheet esCopy = es.getCopy();
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
      Object key = it.next();
      Object[] allElements = col.styleSheets.toArray(key);
      for (int ai = 0; ai < allElements.length; ai++)
      {
        StyleCollectionEntry se = (StyleCollectionEntry) allElements[ai];
        ElementStyleSheet es = se.getStyleSheet();

        List parents = es.getParents();
        // reversed add order .. last parent must be added first ..
        ElementStyleSheet[] parentArray =
            (ElementStyleSheet[]) parents.toArray(new ElementStyleSheet[parents.size()]);
        for (int i = parentArray.length - 1; i >= 0; i--)
        {
          String name = parentArray[i].getName();
          InstanceID id = parentArray[i].getId();
          es.removeParent(parentArray[i]);
          StyleCollectionEntry seParent = col.findStyleSheet(name, id);
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
      Object key = it.next();
      Object[] allElements = col.styleSheets.toArray(key);
      for (int i = 0; i < allElements.length; i++)
      {
        StyleCollectionEntry se = (StyleCollectionEntry) allElements[i];
        ElementStyleSheet es = se.getStyleSheet();
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
  private StyleCollectionEntry findStyleSheet (String name, InstanceID id)
  {
    Object[] data = styleSheets.toArray(name);
    if (data == null)
      return null;

    for (int i = 0; i < data.length; i++)
    {
      StyleCollectionEntry es = (StyleCollectionEntry) data[i];
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
  public void updateStyleSheet(ElementStyleSheet es)
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
      StyleCollectionEntry entry = findStyleSheet(es.getName(), es.getId());
      styleSheets.remove(es.getName(), entry);
      styleSheets.add(es.getName(), new StyleCollectionEntry(entry.getReferenceCount(), es));

      List parents = es.getParents();
      // reversed add order .. last parent must be added first ..
      ElementStyleSheet[] parentArray =
          (ElementStyleSheet[]) parents.toArray(new ElementStyleSheet[parents.size()]);
      for (int i = parentArray.length - 1; i >= 0; i--)
      {
        String name = parentArray[i].getName();
        InstanceID id = parentArray[i].getId();
        es.removeParent(parentArray[i]);
        StyleCollectionEntry seParent = findStyleSheet(name, id);
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
  protected void addParents (ElementStyleSheet es)
  {
    List parents = es.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet esp = (ElementStyleSheet) parents.get(i);
      addStyleSheet(esp, false);
    }
    List defaultParents = es.getDefaultParents();
    for (int i = 0; i < defaultParents.size(); i++)
    {
      ElementStyleSheet esp = (ElementStyleSheet) defaultParents.get(i);
      addStyleSheet(esp, false);
    }
  }

  /**
   * Updates the reference count of all stylesheets.
   * This method is expensive and is (like the whole class) a candidate for
   * an performance redesign ...
   */
  protected void updateReferences ()
  {
    Iterator keyIterator = styleSheets.keys();
    while (keyIterator.hasNext())
    {
      // reset the reference count ...
      Object key = keyIterator.next();
      Object[] data = styleSheets.toArray(key);
      for (int i = 0; i < data.length; i++)
      {
        StyleCollectionEntry se = (StyleCollectionEntry) data[i];
        se.setReferenceCount(0);
      }
    }

    keyIterator = styleSheets.keys();
    while (keyIterator.hasNext())
    {
      // compute the reference count ...
      Object key = keyIterator.next();
      Object[] data = styleSheets.toArray(key);
      for (int ai = 0; ai < data.length; ai++)
      {
        StyleCollectionEntry se = (StyleCollectionEntry) data[ai];
        ElementStyleSheet es = se.getStyleSheet();

        List parents = es.getParents();
        for (int i = 0; i < parents.size(); i++)
        {
          ElementStyleSheet esp = (ElementStyleSheet) parents.get(i);
          StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
          if (sep == null)
          {
            throw new NullPointerException("StyleSheet '" + esp.getName() + "' is not known.");
          }
          sep.setReferenceCount(sep.getReferenceCount() + 1);
        }
        List defaultParents = es.getDefaultParents();
        for (int i = 0; i < defaultParents.size(); i++)
        {
          ElementStyleSheet esp = (ElementStyleSheet) defaultParents.get(i);
          StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
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
  public boolean remove (ElementStyleSheet es)
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
  protected boolean remove (ElementStyleSheet es, boolean update)
  {
    if (contains(es) == false)
    {
      return true;
    }
    else
    {
      StyleCollectionEntry se = findStyleSheet(es.getName(), es.getId());
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
      List parents = es.getParents();
      for (int i = 0; i < parents.size(); i++)
      {
        ElementStyleSheet esp = (ElementStyleSheet) parents.get(i);
        StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
        sep.setReferenceCount(sep.getReferenceCount() - 1);
        remove (esp, false);
      }
      List defaultParents = es.getDefaultParents();
      for (int i = 0; i < defaultParents.size(); i++)
      {
        ElementStyleSheet esp = (ElementStyleSheet) defaultParents.get(i);
        StyleCollectionEntry sep = findStyleSheet(esp.getName(), esp.getId());
        sep.setReferenceCount(sep.getReferenceCount() - 1);
        remove (esp, false);
      }
      if (update)
      {
        updateReferences();
      }
      return true;
    }

  }

  public Iterator keys ()
  {
    Set keySet = styleSheets.keySet();
    return Collections.unmodifiableSet(keySet).iterator();
  }
}
