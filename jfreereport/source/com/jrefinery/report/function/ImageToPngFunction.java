/**
 *
 *  Date: 26.06.2002
 *  ImageToPngFunction.java
 *  ------------------------------
 *  26.06.2002 : ...
 */
package com.jrefinery.report.function;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.util.bytearray.ByteArrayRegistry;
import com.keypoint.PngEncoder;

import javax.swing.table.TableModel;
import java.awt.Image;
import java.net.URL;

/**
 * HackVersion, don't use except you know what you are doing. :)
 *
 * Bugs: - The last image is not deregistered.
 *       - Not tested
 *       - I have not clue how this thing behaves in PDF generation...
 */
public class ImageToPngFunction extends AbstractFunction
{
  private ImageReference currentReference;
  private String lastName;

  private PngEncoder encoder;

  public ImageToPngFunction()
  {
    ByteArrayRegistry.registerProtocol();
    this.encoder = new PngEncoder();
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return getProperty("field");
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param The field name (null not permitted).
   */
  public void setField (String field)
  {
    if (field == null)
      throw new NullPointerException ();
    setProperty ("field", field);
  }

  /**
   * Return the current function value.
   * <P>
   * The value depends (obviously) on the function implementation.   For example, a page counting
   * function will return the current page number.
   *
   * @return The value of the function.
   */
  public Object getValue ()
  {
    return currentReference;
  }

  /**
   * Returns the current value for the data source.
   *
   * @return The value.
   */
  public void updateValue (Object o)
  {
    currentReference = null;
    if (lastName != null) ByteArrayRegistry.getInstance().remove(lastName);
    if (o == null)
      return;

    if (o instanceof Image == false)
      return;

    Image img = (Image) o;

    encoder.setImage(img);
    byte[] data = encoder.pngEncode();
    String filename = img.toString() + ".png";
    ByteArrayRegistry.getInstance().put(filename, data);
    try
    {
      lastName = null;
      currentReference = new ImageReference (new URL ("bytearray://" + filename));
      lastName = filename;
    }
    catch (Exception e)
    {
    }
  }

  /**
   * Receives notification that a group has finished.
   * <P>
   * Maps the groupFinished-method to the legacy function endGroup (int).
   *
   * @param event Information about the event.
   */
  public void groupFinished (ReportEvent event)
  {
    updateValue(extractField(event));

  }

  /**
   * Receives notification that a group has started.
   * <P>
   * Maps the groupStarted-method to the legacy function startGroup (int).
   *
   * @param event Information about the event.
   */
  public void groupStarted (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  /**
   * Receives notification that a page has ended.
   * <P>
   * Maps the pageFinished-method to the legacy function endPage (int).
   *
   * @param event Information about the event.
   */
  public void pageFinished (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  /**
   * Receives notification that a page has started.
   * <P>
   * Maps the pageStarted-method to the legacy function startPage (int).
   *
   * @param event Information about the event.
   */
  public void pageStarted (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  /**
   * Receives notification that the report has finished.
   * <P>
   * Maps the reportFinished-method to the legacy function endReport ().
   *
   * @param event Information about the event.
   */
  public void reportFinished (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  /**
   * Receives notification that the report has started.
   * <P>
   * Maps the reportStarted-method to the legacy function startReport ().
   *
   * @param event Information about the event.
   */
  public void reportStarted (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  /**
   * The itemBand is finished, the report starts to close open groups.
   */
  public void itemsFinished (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  /**
   * The ItemsSection is beeing processed. The next events will be itemsAdvanced events until the
   * itemsFinished event is raised.
   */
  public void itemsStarted (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  /**
   * Receives notification that a row of data is being processed.
   * <P>
   * Maps the itemsAdvanced-method to the legacy function advanceItems (int).
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    updateValue(extractField(event));
  }

  private Object extractField (ReportEvent event)
  {
    TableModel data = event.getReport().getData ();
    int row = event.getState().getCurrentDisplayItem();

    Object fieldValue = null;
    for (int c = 0; c < data.getColumnCount (); c++)
    {
      if (getField().equals (data.getColumnName (c)))
      {
        fieldValue = data.getValueAt (row, c);
        break;
      }
    }
    return fieldValue;
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   * <P>
   * The default implementation checks that the function name is not null, and calls the
   * isInitialized() method (now deprecated).
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize () throws FunctionInitializeException
  {
    super.initialize ();
    if (getField() == null) throw new FunctionInitializeException("No Field defined.");
  }

  public int getCompressionLevel ()
  {
    return encoder.getCompressionLevel();
  }

  public void setCompressionLevel (int level)
  {
    encoder.setCompressionLevel(level);
  }

  public int getFilter ()
  {
    return encoder.getFilter ();
  }

  public void setFilter (int filter)
  {
    encoder.setFilter(filter);
  }

  public boolean getEncodeAlpha ()
  {
    return encoder.getEncodeAlpha();
  }

  public void setEncodeAlpha (boolean a)
  {
    encoder.setEncodeAlpha(a);
  }

}