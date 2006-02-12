package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * http://www.w3.org/TR/css3-border/
 *
 * Creation-Date: 27.10.2005, 21:40:14
 *
 * Border-breaks are specified using single values. The CSS3 specification does
 * not define explicit properties for the break-borders, but using the composite
 * definition is ugly.
 *
 * @author Thomas Morgner
 */
public class BorderStyleKeys 
{
  private BorderStyleKeys()
  {
  }

  public static StyleKey BACKGROUND_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-color", false, false, false);

  /** This expects a list of images. How to handle that? */
  public static StyleKey BACKGROUND_IMAGE =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-image", false, false, true);

  public static StyleKey BACKGROUND_REPEAT =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-repeat", false, false, true);

  /**
   * BackgroundAttachment needs scrolling, and thus we do not implement this
   * style-attribute yet.
   */
  public static StyleKey BACKGROUND_ATTACHMENT =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-attachment", false, false, true);

  /**
   * The position is always specified in numeric values. The constants are
   * mapped by the parser.
   */
  public static StyleKey BACKGROUND_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-position", false, false, true);

  public static StyleKey BACKGROUND_ORIGIN =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-origin", false, false, true);

  public static StyleKey BACKGROUND_CLIP =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-clip", false, false, true);

  public static StyleKey BACKGROUND_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-size", false, false, true);

  public static StyleKey BACKGROUND_BREAK =
          StyleKeyRegistry.getRegistry().createKey
                  ("background-break", false, false, false);


  public static StyleKey BORDER_IMAGE =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-image", false, false, true);

  /**
   * Set the border around the content and padding of a box.
   * Padding is between content and border. Background expands over
   * the padding up to the border.
   * <p>
   * Values given may not be negative. If percentages are given, all paddings
   * are relative to the <strong>width</strong> of the parent (if the
   * flow is horizontal, else the height is used).
   */
  public static StyleKey BORDER_TOP_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-top-width", false, false, false);

  public static StyleKey BORDER_LEFT_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-left-width", false, false, false);

  public static StyleKey BORDER_BOTTOM_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-bottom-width", false, false, false);

  public static StyleKey BORDER_RIGHT_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-right-width", false, false, false);

  public static StyleKey BORDER_TOP_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-top-color", false, false, false);

  public static StyleKey BORDER_LEFT_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-left-color", false, false, false);

  public static StyleKey BORDER_BOTTOM_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-bottom-color", false, false, false);

  public static StyleKey BORDER_RIGHT_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-right-color", false, false, false);

  public static StyleKey BORDER_TOP_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-top-style", false, false, false);

  public static StyleKey BORDER_LEFT_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-left-style", false, false, false);

  public static StyleKey BORDER_BOTTOM_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-bottom-style", false, false, false);

  public static StyleKey BORDER_RIGHT_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-right-style", false, false, false);

  public static StyleKey BORDER_BOTTOM_RIGHT_RADIUS =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-bottom-right-radius", false, false, false);

  public static StyleKey BORDER_TOP_LEFT_RADIUS=
          StyleKeyRegistry.getRegistry().createKey
                  ("border-top-left-radius", false, false, false);

  public static StyleKey BORDER_BOTTOM_LEFT_RADIUS =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-bottom-left-radius", false, false, false);

  public static StyleKey BORDER_TOP_RIGHT_RADIUS =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-top-right-radius", false, false, false);

  public static StyleKey BORDER_BREAK_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-break-width", false, true, false);

  public static StyleKey BORDER_BREAK_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-break-color", false, true, false);

  public static StyleKey BORDER_BREAK_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-break-style", false, true, false);

  public static StyleKey BOX_SHADOW =
          StyleKeyRegistry.getRegistry().createKey
                  ("box-shadow", false, false, false);

}
