package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Graphics2D;
import java.awt.Point;

public class MfCmdTextOut extends MfCmd
{
  private int x;
  private int y;
  private String text;
  private int count;
  private int scaled_x;
  private int scaled_y;

  public MfCmdTextOut ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param metafile the meta file.
   */
  public void replay (WmfFile file)
  {
    Point p = getScaledDestination ();
    int x = p.x;
    int y = p.y;

    Graphics2D graphics = file.getGraphics2D ();
    MfDcState state = file.getCurrentState ();

    state.prepareDrawText ();
    graphics.drawString (text, x, y);
    state.postDrawText ();
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdTextOut ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.TEXT_OUT;
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
    int count = record.getParam (0);
    byte[] text = new byte[count];
    for (int i = 0; i < count; i++)
    {
      text[i] = (byte) record.getByte (record.RECORD_HEADER_SIZE + 2 + i);
    }
    String sText = new String (text);
    int y = record.getParam ((int) (Math.ceil (count / 2) + 1));
    int x = record.getParam ((int) (Math.ceil (count / 2) + 2));

    setCount (count);
    setDestination (x, y);
    setText (sText);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    String text = getText();
    int parCntText = (int) Math.ceil(text.length()/2);
    MfRecord record = new MfRecord(parCntText + 3);
    record.setParam(0, text.length());

    byte[] textRaw = text.getBytes();
    for (int i = 0; i < count; i++)
    {
      record.setByte (record.RECORD_HEADER_SIZE + 2 + i, textRaw[i]);
    }

    Point dest = getDestination();
    record.setParam ((int) (Math.ceil (count / 2) + 1), dest.y);
    record.setParam ((int) (Math.ceil (count / 2) + 2), dest.x);
    return record;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[TEXT_OUT] text=");
    b.append (getText ());
    b.append (" destination=");
    b.append (getDestination ());
    b.append (" count=");
    b.append (getCount ());
    return b.toString ();
  }

  public void setDestination (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();

  }

  public Point getDestination ()
  {
    return new Point (x, y);
  }

  public void setText (String text)
  {
    this.text = text;
  }

  public String getText ()
  {
    return text;
  }

  public int getCount ()
  {
    return count;
  }

  public void setCount (int count)
  {
    this.count = count;
  }

  public Point getScaledDestination ()
  {
    return new Point (scaled_x, scaled_y);
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
