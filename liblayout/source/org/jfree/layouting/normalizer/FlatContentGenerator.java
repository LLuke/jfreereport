package org.jfree.layouting.normalizer;

import java.util.Stack;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.normalizer.common.display.ContentBox;
import org.jfree.layouting.normalizer.common.display.ContentNode;
import org.jfree.layouting.normalizer.common.display.ContentText;

/**
 * Breaks down the content into a flat structure. Block content is no longer contained in
 * other block content, but tables and lines can still be nested.
 * <p/>
 * This transformation is needed for plain document output, like the StarWriter export. It
 * creates its own ambugities, as divs with multiple borders cannot be expressed with this
 * system.
 * <p/>
 * So a smart engine would interfere here and would generated nested tables instead.
 */
public abstract class FlatContentGenerator implements ContentGenerator
{
  // This is the initial state, we've seen nothing yet
  private static final int STATE_FLOW = 0;
  // We've seen a block context recently.
  private static final int STATE_BLOCK = 1;
  // We've seen a inline context
  private static final int STATE_INLINE = 2;

  private static class ElementContext
  {
    private ContentBox lastBlockBox;
    private int state;
    private Stack inlineBlocks;
  }

  private Stack flowContexts;

  private ElementContext lastContext;

  public FlatContentGenerator ()
  {
    this.flowContexts = new Stack();
  }

  /**
   * Receives the information, that the document processing has been started. This is
   * fired only once.
   */
  public void documentStarted ()
  {
    lastContext = new ElementContext();
  }

  /**
   * Receives notification, that a new flow has started. A new flow is started for each
   * flowing or absolutly positioned element.
   *
   * @param box
   */
  public void flowStarted (ContentBox box)
  {
    flowContexts.push(lastContext);
    lastContext = new ElementContext();
  }

  public final void nodeStarted (ContentBox box)
  {
    if (box.getDisplayRole() == DisplayRole.INLINE)
    {
      if (lastContext.state == STATE_FLOW)
      {
        throw new IllegalStateException("This is not normalized content!");
      }
      if (lastContext.state == STATE_BLOCK)
      {
        lastContext.state = STATE_INLINE;
        nodeBlockStarted(lastContext.lastBlockBox, lastContext.inlineBlocks.size());
      }
      nodeInlineStarted(box,0);
    }
    else // assume a block context ...
    {
      if (lastContext.state == STATE_INLINE)
      {
        lastContext.inlineBlocks.push(box);
        nodeBlockStarted(box, lastContext.inlineBlocks.size());
      }
      lastContext.state = STATE_BLOCK;
    }
  }

  protected abstract void nodeBlockStarted (ContentBox box, int level);

  protected abstract void nodeInlineStarted (ContentBox box, int level);

  public final void nodeProcessable (ContentNode node)
  {
    if (node instanceof ContentText)
    {
      nodeText((ContentText) node);
    }
    else
    {
      nodeExternalContent(node);
    }
  }

  protected abstract void nodeExternalContent (ContentNode node);

  protected abstract void nodeText (ContentText box);

  protected abstract void nodeInlineFinished (ContentBox box);

  protected abstract void nodeBlockFinished (ContentBox box);

  public final void nodeFinished (ContentBox box)
  {
    if (box.getDisplayRole() == DisplayRole.INLINE)
    {
      nodeInlineFinished(box);
    }

    if (lastContext.state == STATE_FLOW)
    {
      throw new IllegalStateException("This is not normalized content!");
    }

    DisplayRole displayRole = box.getParent().getDisplayRole();
    if (lastContext.state == STATE_BLOCK)
    {
      if (displayRole == DisplayRole.INLINE)
      {
        lastContext.state = STATE_INLINE;
      }
    }
    else if (lastContext.state == STATE_INLINE)
    {
      if (displayRole == DisplayRole.BLOCK)
      {
        // this is the last inline context, that is going to be finished
      }
    }
  }

  public void flowFinished (ContentBox box)
  {
    lastContext = (ElementContext) flowContexts.pop();
  }

  public void documentFinished ()
  {
    if (flowContexts.isEmpty() == false)
    {
      throw new IllegalStateException("Flow contexts have not been finished.");
    }
  }

}
