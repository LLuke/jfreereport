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
 * ---------
 * Band.java
 * ---------
 * (C)opyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: Band.java,v 1.53 2003/04/24 18:08:43 taqua Exp $
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

package com.jrefinery.report;

import java.awt.geom.Dimension2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.style.BandDefaultStyleSheet;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

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
 * band.
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
  private Element[] allElementsCached;

  /** The default style-sheet for the elements contained in the band. */
  private ElementStyleSheet bandDefaults;

  /**
   * Constructs a new band (initially empty).
   */
  public Band()
  {
    getStyle().addDefaultParent(BandDefaultStyleSheet.getBandDefaultStyle());

    BandLayoutManager layout = new StaticLayoutManager();
    getStyle().setStyleProperty(BandLayoutManager.LAYOUTMANAGER, layout);

    setName("anonymousBand@" + hashCode());
    allElements = new ArrayList();

    // band style sheets are not accessed by names. Names are important
    // for the xml-parser when stacking the stylesheets together.
    bandDefaults = new BandStyleSheet("default");
    bandDefaults.setAllowCaching(true);
  }

  /**
   * Returns the layout manager for the band.
   * 
   * @return The layout manager.
   */
  public BandLayoutManager getLayout ()
  {
    return (BandLayoutManager) getStyle().getStyleProperty(BandLayoutManager.LAYOUTMANAGER);
  }

  /**
   * Sets the band layout manager.
   * 
   * @param layoutManager  the layout manager.
   */
  public void setLayout (BandLayoutManager layoutManager)
  {
    getStyle().setStyleProperty(BandLayoutManager.LAYOUTMANAGER, layoutManager);
  }

  /**
   * Returns the default style sheet for all children of this band. This style sheet
   * is used to define a set of base (or default) properties for all elements.
   *
   * @return the default style sheet.
   */
  public ElementStyleSheet getBandDefaults ()
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
  public synchronized void addElement (Element element)
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
  public synchronized void addElement(int position, Element element)
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
    element.getStyle().addDefaultParent(getBandDefaults());
    element.setParent(this);
  }

  /**
   * Adds a collection of elements to the band.
   *
   * @param elements  the element collection.
   *
   * @throws NullPointerException if the collection given is <code>null</code> or
   * the collection contains <code>null</code> elements.
   */
  public synchronized void addElements(Collection elements)
  {
    if (elements == null)
    {
      throw new NullPointerException("Band.addElements(...): collection is null.");
    }

    Iterator iterator = elements.iterator();
    while (iterator.hasNext())
    {
      Element element = (Element) iterator.next();
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
  public Element getElement(String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Band.getElement(...): name is null.");
    }

    Iterator it = allElements.iterator();
    while (it.hasNext())
    {
      Element e = (Element) it.next();
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
  public void removeElement (Element e)
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
    e.getStyle().removeDefaultParent(getBandDefaults());
    e.setParent(null);
    allElements.remove(e);
    allElementsCached = null;
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
  public int getElementCount ()
  {
    return allElements.size();
  }

  /**
   * Returns an array of the elements in the band.
   *
   * @return the elements.
   */
  public synchronized Element[] getElementArray ()
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
  public Element getElement (int index)
  {
    return (Element) allElements.get(index);
  }

  /**
   * Returns a string representation of the band and all the elements it contains, useful
   * mainly for debugging purposes.
   *
   * @return a string representation of this band.
   */
  public String toString()
  {
    StringBuffer b = new StringBuffer();
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
    Band b = (Band) super.clone();
    b.bandDefaults = (ElementStyleSheet) bandDefaults.clone();
    int elementSize = allElements.size();
    b.allElements = new ArrayList(elementSize);
    b.allElementsCached = new Element[elementSize];

    ElementStyleSheet myBandDefaults = getBandDefaults();
    ElementStyleSheet cloneBandDefaults = b.getBandDefaults();

    if (allElementsCached != null)
    {
      for (int i = 0; i < allElementsCached.length; i++)
      {
        Element eC = (Element) allElementsCached[i].clone();
        b.allElements.add (eC);
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
        Element e = (Element) allElements.get(i);
        Element eC = (Element) e.clone();
        b.allElements.add (eC);
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

  /// DEPRECATED METHODS //////////////////////////////////////////////////////////////////////////

  /**
   * Defines the minimum height of the band.
   * <p>
   * This property is deprecated, please don't use it anymore.
   * The minimum height can be defined using the MINIMUMSIZE property in the
   * ElementStyleSheet if needed.
   * <p>
   * Using this method will remove any previously set minimumsize of this band.
   *
   * @param height the new height. The minimum width is set to '0'.
   * @deprecated do not manipulate the element properties that way, use a stylesheet
   */
  public void setHeight (float height)
  {
    getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, height));
  }

  /**
   * Queries the minimum size of this band and returns the height portion.
   *
   * @return the minimum height of this band.
   * @deprecated do not manipulate the element properties that way, use a stylesheet
   * and an suitable layoutmanager using the correct stylesheet properties ...
   */
  public float getHeight ()
  {
    Dimension2D d = (Dimension2D) getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                                              new FloatDimension(0, 0));
    return (float) d.getHeight();
  }
}
