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
 * ---------
 * Band.java
 * ---------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: Band.java,v 1.6 2003/08/25 14:29:28 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Changed band height from Number --> float (DG);
 * 05-Mar-2002 : Changed the constructors from public --> protected (DG);
 * 10-May-2002 : Declared Abstract, Removed complex constructors
 * 11-May-2002 : Bug: when adding multiple data fields referencing to the same column in the
 *               data model only the first field was filled with data on populateElements.
 * 20-May-2002 : Changed to support new drawing scheme. The state of the OutputTarget is stored
 *               before any element starts to draw and restored afterwards. This will greatly
 *               reduce sideeffects from changed fonts or paints which are not restored by the
 *               element.
 * 26-May-2002 : Elements are now stored ordered. Updated drawing to reflect new element property
 *               "Visible".
 * 04-Jun-2002 : Public methods throw exceptions on illegal values. Documentation update.
 * 04-Jul-2002 : Serializable and Cloneable
 * 08-Aug-2002 : Band visibility support added. Bands can be hidden using the visible-property
 * 22-Aug-2002 : Height contains now a special value (-100) to adjust the band to fit the available
 *               pageheight.  This is a temporary fix and gets reomved with the next layout update.
 * 31-Aug-2002 : Removed separate stoage of Function and ReportDataSource elements. This stuff was
 *               no longer in use.
 * 05-Sep-2002 : Documentation
 * 13-Sep-2002 : Ran checkstyle and fixed reported issues
 * 05-Nov-2002 : BugFixes I: dynamic feature Q&D fixes
 * 20-Nov-2002 : BugFixed II: more dynamic feature Q&D fixes
 * 02-Dec-2002 : Removed the drawing and changed Band to use StyleSheets. Band is now an Element-
 *               descentend. No more dynamic fixes, as it is not handled here anymore :)
 * 06-Dec-2002 : Documentation update.
 * 03-Jan-2002 : More Javadocs (DG);
 * 23-Jan-2003 : Bug in the clone() method, element parent was not updated.
 * 04-Feb-2003 : fixed composition operations (add element (int idx, Element e))
 */

package org.jfree.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.report.layout.BandLayoutManager;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.BandDefaultStyleSheet;
import org.jfree.report.style.BandStyleSheet;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.InvalidStyleSheetCollectionException;
import org.jfree.report.style.StyleSheetCollection;

/**
 * A report band is a collection which can contain other Report-Elements.
 * A band contains a list of elements to be displayed, and represents one section of a
 * report (the report header or footer, the page header or footer, the group header or footer,
 * or the items within a group).
 * <P>
 * The elements in a report band can contain fixed values, field values from the dataset, or
 * function values. The elements are not required to have unique names.
 * <p>
 * This implementation is not synchronized, to take care that you externally synchronize
 * it when using multiple threads.
 * <p>
 * A band's contents should not be modified after the report processing starts,
 * so don't add Elements to the band's contained in or aquired from an report-state.
 * <p>
 * Bands contain a master stylesheet for all element contained in that band. This
 * StyleSheet is registered in the child when the element is added to the band.
 * <p>
 * Bands now extend the Element-class, so it is possible to stack bands into another
 * band. Trying to add a parent of an band as child to the band, will result in an
 * exception.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class Band extends Element implements Serializable, Cloneable
{
  /**
   * the defined content type for the band. The content type is used when selecting
   * the correct display-method for an element.
   */
  public static final String CONTENT_TYPE = "X-container";

  /** All the elements for this band, stored by name. */
  private ArrayList allElements;

  /** Cached elements. */
  private transient Element[] allElementsCached;

  /** The default style-sheet for the elements contained in the band. */
  private ElementStyleSheet bandDefaults;

  /** The prefix for anonymous bands, bands without an userdefined name. */
  public static final String ANONYMOUS_BAND_PREFIX = "anonymousBand@";

  /**
   * Constructs a new band (initially empty).
   */
  public Band()
  {
    getStyle().addDefaultParent(BandDefaultStyleSheet.getBandDefaultStyle());

    final BandLayoutManager layout = new StaticLayoutManager();
    getStyle().setStyleProperty(BandLayoutManager.LAYOUTMANAGER, layout);

    setName(ANONYMOUS_BAND_PREFIX + super.hashCode());
    allElements = new ArrayList();

    // band style sheets are not accessed by names. Names are important
    // for the xml-parser when stacking the stylesheets together.
    bandDefaults = new BandStyleSheet("band-default");
    bandDefaults.setAllowCaching(true);
  }

  /**
   * Returns the layout manager for the band.
   *
   * @return The layout manager.
   */
  public BandLayoutManager getLayout()
  {
    return (BandLayoutManager) getStyle().getStyleProperty(BandLayoutManager.LAYOUTMANAGER);
  }

  /**
   * Sets the band layout manager.
   *
   * @param layoutManager  the layout manager.
   */
  public void setLayout(final BandLayoutManager layoutManager)
  {
    getStyle().setStyleProperty(BandLayoutManager.LAYOUTMANAGER, layoutManager);
  }

  /**
   * Returns the default style sheet for all children of this band. This style sheet
   * is used to define a set of base (or default) properties for all elements.
   *
   * @return the default style sheet.
   */
  public ElementStyleSheet getBandDefaults()
  {
    return bandDefaults;
  }

  /**
   * Adds a report element to the band.
   *
   * @param element  the element (<code>null</code> not permitted).
   *
   * @throws NullPointerException if the element is <code>null</code> or contains <code>null</code>
   *                              values.
   */
  public synchronized void addElement(final Element element)
  {
    addElement(allElements.size(), element);
  }

  /**
   * Adds a report element to the band. The element will be inserted on the specified position.
   *
   * @param position  the position where to insert the element
   * @param element  the element that should be added
   *
   * @throws NullPointerException if the given element is null
   * @throws IllegalArgumentException if the position is invalid, either negative or
   * greater than the number of elements in this band.
   */
  public synchronized void addElement(final int position, final Element element)
  {
    if (position < 0)
    {
      throw new IllegalArgumentException("Position < 0");
    }
    if (position > allElements.size())
    {
      throw new IllegalArgumentException("Position < 0");
    }
    if (element == null)
    {
      throw new NullPointerException("Band.addElement(...): element is null.");
    }

    // check for component loops ...
    if (element instanceof Band)
    {
      Band band = this;
      while (band != null)
      {
        if (band == element)
        {
          throw new IllegalArgumentException("adding container's parent to itself");
        }
        band = band.getParent();
      }
    }

    // remove the element from its old parent ..
    // this is the default AWT behaviour when adding Components to Container
    if (element.getParent() != null)
    {
      if (element.getParent() == this)
      {
        // already a child, wont add twice ...
        return;
      }

      element.getParent().removeElement(element);
    }

    // add the element, update the childs Parent and the childs stylesheet.
    allElements.add(position, element);
    allElementsCached = null;
    // first register the element with the collection ...
    if (getStyleSheetCollection() != null)
    {
      element.registerStyleSheetCollection(getStyleSheetCollection());
    }
    // then add the parents, or the band's parent will be unregistered ..
    element.getStyle().addDefaultParent(getBandDefaults());
    element.setParent(this);
    invalidateLayout();
  }

  /**
   * Adds a collection of elements to the band.
   *
   * @param elements  the element collection.
   *
   * @throws NullPointerException if the collection given is <code>null</code> or
   * the collection contains <code>null</code> elements.
   */
  public synchronized void addElements(final Collection elements)
  {
    if (elements == null)
    {
      throw new NullPointerException("Band.addElements(...): collection is null.");
    }

    final Iterator iterator = elements.iterator();
    while (iterator.hasNext())
    {
      final Element element = (Element) iterator.next();
      addElement(element);
    }
  }

  /**
   * Returns the first element in the list that is registered by the given name.
   *
   * @param name  the element name.
   *
   * @return the first element with the specified name, or <code>null</code> if there is no
   *         such element.
   *
   * @throws NullPointerException if the given name is null.
   */
  public Element getElement(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Band.getElement(...): name is null.");
    }

    final Iterator it = allElements.iterator();
    while (it.hasNext())
    {
      final Element e = (Element) it.next();
      if (e.getName() != null)
      {
        if (e.getName().equals(name))
        {
          return e;
        }
      }
    }
    return null;
  }

  /**
   * Removes an element from the band.
   * <p>
   * You should not use this method on a band acquired from a <code>ReportState</code> or
   * <code>Function</code>.
   *
   * @param e  the element to be removed.
   */
  public void removeElement(final Element e)
  {
    if (e == null)
    {
      throw new NullPointerException();
    }
    if (e.getParent() != this)
    {
      // this is none of my childs, ignore the request ...
      return;
    }

    if (getStyleSheetCollection() != null)
    {
      e.unregisterStyleSheetCollection(getStyleSheetCollection());
    }
    e.getStyle().removeDefaultParent(getBandDefaults());
    e.setParent(null);
    allElements.remove(e);
    allElementsCached = null;
    invalidateLayout();
  }

  /**
   * Returns all child-elements of this band as immutable list.
   *
   * @return an immutable list of all registered elements for this band.
   */
  public List getElements()
  {
    return Collections.unmodifiableList(allElements);
  }

  /**
   * Returns the number of elements in this band.
   *
   * @return the number of elements of this band.
   */
  public int getElementCount()
  {
    return allElements.size();
  }

  /**
   * Returns an array of the elements in the band. This method never returns null.
   *
   * @return the elements.
   */
  public synchronized Element[] getElementArray()
  {
    if (allElementsCached == null)
    {
      Element[] elements = new Element[allElements.size()];
      elements = (Element[]) allElements.toArray(elements);
      allElementsCached = elements;
    }
    return allElementsCached;
  }

  /**
   * Returns the element stored add the given index.
   *
   * @param index the element position within this band
   * @return the element
   * @throws IndexOutOfBoundsException if the index is invalid.
   */
  public Element getElement(final int index)
  {
    if (allElementsCached == null)
    {
      Element[] elements = new Element[allElements.size()];
      elements = (Element[]) allElements.toArray(elements);
      allElementsCached = elements;
    }
    return allElementsCached[index];
  }

  /**
   * Returns a string representation of the band and all the elements it contains, useful
   * mainly for debugging purposes.
   *
   * @return a string representation of this band.
   */
  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append(this.getClass().getName());
    b.append("={name=\"");
    b.append(getName());
    b.append("\", size=\"");
    b.append(allElements.size());
    b.append("\"}");
    return b.toString();
  }

  /**
   * Clones this band and all elements contained in this band.
   *
   * @return the clone of this band.
   *
   * @throws CloneNotSupportedException if this band or an element contained in this band does not
   *                                    support cloning.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final Band b = (Band) super.clone();
    b.bandDefaults = bandDefaults.getCopy();
    final int elementSize = allElements.size();
    b.allElements = new ArrayList(elementSize);
    b.allElementsCached = new Element[elementSize];
    b.setParent(null);

    final ElementStyleSheet myBandDefaults = bandDefaults;
    final ElementStyleSheet cloneBandDefaults = b.bandDefaults;

    if (allElementsCached != null)
    {
      for (int i = 0; i < allElementsCached.length; i++)
      {
        final Element eC = (Element) allElementsCached[i].clone();
        b.allElements.add(eC);
        b.allElementsCached[i] = eC;
        eC.setParent(b);
        eC.getStyle().removeDefaultParent(myBandDefaults);
        eC.getStyle().addDefaultParent(cloneBandDefaults);
      }
    }
    else
    {
      for (int i = 0; i < elementSize; i++)
      {
        final Element e = (Element) allElements.get(i);
        final Element eC = (Element) e.clone();
        b.allElements.add(eC);
        b.allElementsCached[i] = eC;
        eC.setParent(b);
        eC.getStyle().removeDefaultParent(myBandDefaults);
        eC.getStyle().addDefaultParent(cloneBandDefaults);
      }
    }
    return b;
  }

  /**
   * Returns the content type of the element. For bands, the content type is by
   * default &quot;X-container&quot;.
   *
   * @return the content type
   */
  public String getContentType()
  {
    return CONTENT_TYPE;
  }

  /**
   * Invalidates the layout. This method is called whenever a new element has been
   * added. You should also call this method if you modified one of the elements of
   * the band (eg. redefined the max, min or preferred size).
   */
  public void invalidateLayout()
  {
    getLayout().invalidateLayout(this);
    if (getParent() != null)
    {
      getParent().invalidateLayout();
    }
  }

  /**
   * Handles the unregistration of the stylesheet collection.
   */
  protected void handleUnregisterStyleSheetCollection()
  {
    final Element[] elements = getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].unregisterStyleSheetCollection(getStyleSheetCollection());
    }
    //getStyleSheetCollection().remove(getBandDefaults());
    getBandDefaults().unregisterStyleSheetCollection(getStyleSheetCollection());
    super.handleUnregisterStyleSheetCollection();
  }

  /**
   * Handles the registration of the stylesheet collection.
   */
  protected void handleRegisterStyleSheetCollection()
  {
    final Element[] elements = getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].registerStyleSheetCollection(getStyleSheetCollection());
    }
    //getStyleSheetCollection().addStyleSheet(getBandDefaults());
    getBandDefaults().registerStyleSheetCollection(getStyleSheetCollection());
    super.handleRegisterStyleSheetCollection();
  }

  /**
   * Updates the stylesheet collection for this element and all substylesheets.
   * This method must be called after the element was cloned, to make sure that
   * all stylesheets are registered properly.
   * <p>
   * If you don't call this function after cloning prepare to be doomed.
   * This method will replace all inherited stylesheets with clones from the stylesheet
   * collection.
   *
   * @param sc the stylesheet collection that contains the updated information and
   * that should be assigned with that element.
   * @throws NullPointerException if the given stylesheet collection is null.
   * @throws InvalidStyleSheetCollectionException
   * if there is an other stylesheet collection already registered with that element.
   */
  public void updateStyleSheetCollection(final StyleSheetCollection sc)
      throws InvalidStyleSheetCollectionException
  {
    if (sc == null)
    {
      throw new NullPointerException("StyleSheetCollection is null.");
    }
    if (getStyleSheetCollection() != null)
    {
      throw new NullPointerException("There is a stylesheet collection already registered.");
    }

    final Element[] elements = getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].updateStyleSheetCollection(sc);
    }

    sc.updateStyleSheet(getBandDefaults());
    super.updateStyleSheetCollection(sc);

    registerStyleSheetCollection(sc);
  }

  /**
   * Returns, whether the page layout manager should perform a pagebreak
   * before this page is printed. This will have no effect on empty pages
   * or if the band is no root-level band.
   *
   * @return true, if to force a pagebreak before this band is printed, false
   * otherwise
   */
  public boolean isPagebreakBeforePrint()
  {
    return getStyle().getBooleanStyleProperty
        (BandStyleSheet.PAGEBREAK_BEFORE);
  }

  /**
   * Defines, whether the page layout manager should perform a pagebreak
   * before this page is printed. This will have no effect on empty pages
   * or if the band is no root-level band.
   *
   * @param pagebreakBeforePrint set to true, if to force a pagebreak before
   * this band is printed, false otherwise
   */
  public void setPagebreakBeforePrint(boolean pagebreakBeforePrint)
  {
    getStyle().setBooleanStyleProperty
        (BandStyleSheet.PAGEBREAK_BEFORE, pagebreakBeforePrint);
  }

  /**
   * Returns, whether the page layout manager should perform a pagebreak
   * before this page is printed. This will have no effect on empty pages
   * or if the band is no root-level band.
   *
   * @return true, if to force a pagebreak before this band is printed, false
   * otherwise
   */
  public boolean isPagebreakAfterPrint()
  {
    return getStyle().getBooleanStyleProperty
        (BandStyleSheet.PAGEBREAK_AFTER);
  }

  /**
   * Defines, whether the page layout manager should perform a pagebreak
   * before this page is printed. This will have no effect on empty pages
   * or if the band is no root-level band.
   *
   * @param pagebreakAfterPrint set to true, if to force a pagebreak before 
   * this band is printed, false otherwise
   */
  public void setPagebreakAfterPrint(boolean pagebreakAfterPrint)
  {
    getStyle().setBooleanStyleProperty
        (BandStyleSheet.PAGEBREAK_AFTER, pagebreakAfterPrint);
  }
}
