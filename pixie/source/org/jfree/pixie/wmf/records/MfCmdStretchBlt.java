package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * The StretchBlt function copies a bitmap from a source rectangle
 * into a destination rectangle, stretching or compressing the bitmap
 * to fit the dimensions of the destination rectangle, if necessary.
 * The system stretches or compresses the bitmap according to the
 * stretching mode currently set in the destination device context.
 * <p>
 * This method is not implemented.
 * todo ask wine about this function implementation ...
 */
public class MfCmdStretchBlt extends MfCmd
{
  public MfCmdStretchBlt ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param metafile the meta file.
   */
  public void replay (WmfFile file)
  {
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdStretchBlt ();
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
    System.out.println ("Old StretchBlt is not yet implemented.");
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[OLD_STRECH_BLT] is not implemented");
    return b.toString ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.OLD_STRETCH_BLT;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged()
  {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged()
  {
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    throw new RecordCreationException("[OLD_STRETCH_BLT] is not implemented.");
  }
}
