package org.jfree.layouting.model.content;

/**
 * This is the base for all specified content in CSS files.
 * This both may lookup content from elsewhere or may hold
 * static content as string.
 */
public interface ContentToken
{
  /** Plain text */
  public static final int STRING_CONTENT = 1;
  /** The defined child elements, if any. */
  public static final int CONTENTS_CONTENT = 2;
  /** Images or drawables. */
  public static final int DRAWABLE_CONTENT = 3;
  /** Images or drawables. */
  public static final int EXTERNAL_CONTENT = 4;
  public static final int OPEN_QUOTE = 5;
  public static final int CLOSE_QUOTE = 6;

  public int getType();
}
