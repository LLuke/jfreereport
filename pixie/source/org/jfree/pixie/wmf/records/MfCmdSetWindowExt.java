package org.jfree.pixie.wmf.records;

import java.awt.Dimension;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * Sets the size of the window.
 */
public class MfCmdSetWindowExt extends MfCmd
{
  private static final int RECORD_SIZE = 2;
  private static final int POS_HEIGHT = 0;
  private static final int POS_WIDTH = 1;

  private int height;
  private int width;
  private int scaled_width;
  private int scaled_height;

  public MfCmdSetWindowExt ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    final MfDcState state = file.getCurrentState();
    final Dimension dim = getScaledDimension();
    state.setWindowExt(dim.width, dim.height);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdSetWindowExt();
  }

  /**
   * Reads the command data from the given record and adjusts the internal parameters
   * according to the data parsed.
   * <p/>
   * After the raw record was read from the datasource, the record is parsed by the
   * concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord (final MfRecord record)
  {
    final int height = record.getParam(POS_HEIGHT);
    final int width = record.getParam(POS_WIDTH);
    setDimension(width, height);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
          throws RecordCreationException
  {
    final MfRecord record = new MfRecord(RECORD_SIZE);
    final Dimension dim = getDimension();
    record.setParam(POS_HEIGHT, dim.height);
    record.setParam(POS_WIDTH, dim.width);
    return record;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[SET_WINDOW_EXT] dimension=");
    b.append(getDimension());
    return b.toString();
  }

  public Dimension getDimension ()
  {
    return new Dimension(width, height);
  }

  public Dimension getScaledDimension ()
  {
    return new Dimension(scaled_width, scaled_height);
  }

  public void setDimension (final int w, final int h)
  {
    this.width = w;
    this.height = h;
    scaleXChanged();
    scaleYChanged();
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_width = getScaledX(width);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_height = getScaledY(height);
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.SET_WINDOW_EXT;
  }

}
