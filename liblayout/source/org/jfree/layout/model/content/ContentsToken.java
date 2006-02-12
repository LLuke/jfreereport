package org.jfree.layouting.model.content;

/**
 * This is a simple placeholder to mark the location where
 * the DOM content should be inserted.
 * <p>
 * On 'string(..)' functions, this is the place holder where
 * the PCDATA of that element is copied into the string.
 * <p>
 * Todo: Maybe we should allow to copy the whole contents,
 * as we would for the move-to function.
 */
public class ContentsToken implements ContentToken
{
  public ContentsToken ()
  {
  }

  public int getType ()
  {
    return ContentToken.CONTENTS_CONTENT;
  }
}
