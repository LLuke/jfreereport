package org.jfree.layouting.normalizer;

import org.jfree.layouting.normalizer.common.display.ContentNode;
import org.jfree.layouting.normalizer.common.display.ContentBox;

/**
 * The content generator is the third stage content processor.
 * This part is responsible to process the generated display model
 * and to render it to the output medium. This may or may not involve
 * page breaking.
 */
public interface ContentGenerator
{
  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   */
  public void documentStarted();

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void flowStarted (ContentBox box);
  public void nodeStarted (ContentBox box);
  public void nodeProcessable (ContentNode node);
  public void nodeFinished (ContentBox box);
  public void flowFinished (ContentBox box);
  public void documentFinished();
}
