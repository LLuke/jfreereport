package org.jfree.layouting.model.box;

import java.awt.Shape;

import org.jfree.layouting.input.style.keys.box.Fit;

public class ReplacedElementSpecification
{
  /** Replaced content treatment */
  private Shape crop;
  private Fit fit;
  private long fitPositionTop;
  private long fitPositionLeft;

  public ReplacedElementSpecification ()
  {
  }

  public Shape getCrop ()
  {
    return crop;
  }

  public void setCrop (Shape crop)
  {
    this.crop = crop;
  }

  public Fit getFit ()
  {
    return fit;
  }

  public void setFit (Fit fit)
  {
    this.fit = fit;
  }

  public long getFitPositionLeft ()
  {
    return fitPositionLeft;
  }

  public void setFitPositionLeft (long fitPositionLeft)
  {
    this.fitPositionLeft = fitPositionLeft;
  }

  public long getFitPositionTop ()
  {
    return fitPositionTop;
  }

  public void setFitPositionTop (long fitPositionTop)
  {
    this.fitPositionTop = fitPositionTop;
  }


}
