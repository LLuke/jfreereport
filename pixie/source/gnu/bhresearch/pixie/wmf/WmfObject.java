package gnu.bhresearch.pixie.wmf;

public interface WmfObject
{
  public static final int OBJ_PEN = 1;
  public static final int OBJ_BRUSH = 2;
  public static final int OBJ_PALETTE = 3;
  public static final int OBJ_FONT = 4;
  public static final int OBJ_REGION = 5;

  public int getType ();
}
