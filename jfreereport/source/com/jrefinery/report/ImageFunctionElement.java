/**
 *
 *  Date: 26.06.2002
 *  ImageFunctionElement.java
 *  ------------------------------
 *  26.06.2002 : ...
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;
import com.jrefinery.report.util.Log;

public class ImageFunctionElement extends Element
{
  public ImageFunctionElement ()
  {
  }

  /**
   * Draws the element at its location relative to the band co-ordinates supplied.
   *
   * @param target The target on which to print.
   * @param band The band.
   */
  public void draw (OutputTarget target, Band band) throws OutputTargetException
  {
    Object o = getValue();
    if (o instanceof ImageReference)
    {
      // set the paint...
      target.setPaint (getPaint (band));
      target.drawImage ((ImageReference)o);
    }
    else
    {
      Log.debug ("No image Reference");
    }
  }

}