package org.jfree.report;

public class AnchorElement extends Element
{
  public static final String CONTENT_TYPE = "X-Anchor";

  public AnchorElement ()
  {
  }

  /**
   * Defines the content-type for this element. The content-type is used as a hint how to
   * process the contents of this element. An element implementation should restrict
   * itself to the content-type set here, or the reportprocessing may fail or the element
   * may not be printed.
   * <p/>
   * An element is not allowed to change its content-type after ther report processing has
   * started.
   * <p/>
   * If an content-type is unknown to the output-target, the processor should ignore the
   * content or clearly document its internal reprocessing. Ignoring is preferred.
   *
   * @return the content-type as string.
   */
  public String getContentType ()
  {
    return CONTENT_TYPE;
  }
}
