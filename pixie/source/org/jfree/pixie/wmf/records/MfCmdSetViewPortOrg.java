package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Point;

/**
 * Defines the upper left corner of the viewport. The size of the
 * viewport is defined using setViewportExt.
 * <p>
 * SetViewportOrgEx specifies which device point maps to the logical
 * point (0,0). It has the effect of shifting the axes so that the
 * logical point (0,0) no longer refers to the upper-left corner.
 */
public class MfCmdSetViewPortOrg extends MfCmd
{
  private static final int RECORD_SIZE = 2;
  private static final int POS_Y = 0;
  private static final int POS_X = 1;

  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdSetViewPortOrg ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    Point p = getScaledTarget ();
    state.setViewportOrg (p.x, p.y);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdSetViewPortOrg ();
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_VIEWPORT_ORG] target=");
    b.append (getTarget ());
    return b.toString ();
  }

  /**
   * Reads the command data from the given record and adjusts the internal
   * parameters according to the data parsed.
   * <p>
   * After the raw record was read from the datasource, the record is parsed
   * by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord (MfRecord record)
  {
    int y = record.getParam (POS_Y);
    int x = record.getParam (POS_X);
    setTarget (x, y);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    MfRecord record = new MfRecord(RECORD_SIZE);
    Point p = getTarget();
    record.setParam(POS_X, p.x);
    record.setParam(POS_Y, p.y);
    return record;
  }

  public Point getTarget ()
  {
    return new Point (x, y);
  }

  public void setTarget (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Point getScaledTarget ()
  {
    return new Point (scaled_x, scaled_y);
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.SET_VIEWPORT_ORG;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
  }
}
