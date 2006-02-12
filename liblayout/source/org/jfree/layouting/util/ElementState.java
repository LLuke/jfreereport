package org.jfree.layouting.util;

/**
 * Creation-Date: 31.10.2005, 13:05:57
 *
 * @author Thomas Morgner
 */
public final class ElementState implements Comparable
{
  /**
   * The element has been constructed and is not closed.
   */
  public static final ElementState INPUT_NEW = new ElementState(0);
  /**
   * The element itself is closed, but one of the childs is not yet finished.
   * (Check, whether and how we can implement that in a performant way.)
   */
  public static final ElementState INPUT_CLOSE_PENDING = new ElementState(1);
  /**
   * The element construction is fully done. The element will not be changed
   * anymore.
   */
  public static final ElementState INPUT_CLOSED = new ElementState(2);
  /**
   * The stylesheet cascade has been resolved. Each element has a set of
   * absolute or relative properties - no inheritance needs to be considered
   * anymore.
   */
  public static final ElementState LAYOUT_CASCADED = new ElementState(3);
  /**
   * The generated content has been added. Generated content is text only.
   * It cannot generate new document nodes other than CDATA sections.
   */
  public static final ElementState LAYOUT_GENERATED = new ElementState(4);
  /**
   * The stylesheet properties have been computed. All missing style properties
   * must be resolved during the actual layouting process. This involves
   * building the anonymous boxes needed for the layouting and doing all the
   * pagination.
   */
  public static final ElementState LAYOUT_COMPUTED = new ElementState(5);
  /**
   * Everything is done. The fully computed page can be saved and reused
   * later or that document part can be forwarded to the output engine.
   */
  public static final ElementState LAYOUT_DONE = new ElementState(6);

  /**
   * The weight specifies how the layouting process has proceeded so far. 
   */
  private int weight;

  private ElementState (int weight)
  {
    this.weight = weight;
  }

  public int compareTo(final Object o)
  {
    ElementState es = (ElementState) o;
    if (weight < es.weight)
    {
      return -1;
    }
    if (weight > es.weight)
    {
      return 1;
    }
    return 0;
  }
}
