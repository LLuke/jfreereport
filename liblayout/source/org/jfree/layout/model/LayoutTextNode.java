package org.jfree.layouting.model;

import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessor;

/**
 * This represents a text node (also known as CDATA) in the document model.
 * TextNodes always have the same style as their parent element. So we can
 * reuse the parent element's style and do not have to create a new one
 * (or have to compute the style).
 */
public class LayoutTextNode extends LayoutNode
{
  private char[] data;
  private int offset;
  private int length;

  public LayoutTextNode(final ContextId id,
                        final OutputProcessor outputProcessor,
                        final char[] data,
                        final int offset,
                        final int length)
  {
    super(id, outputProcessor);
    if (data == null)
    {
      throw new NullPointerException("Data must not be null");
    }
    if (offset < 0)
    {
      throw new IndexOutOfBoundsException("Offset is invalid");
    }
    if (length < 0)
    {
      throw new IndexOutOfBoundsException("Length is invalid");
    }
    if (offset + length > data.length)
    {
      throw new IndexOutOfBoundsException("StringLength violation");
    }
    this.data = data;
    this.offset = offset;
    this.length = length;
  }

  public LayoutStyle getStyle()
  {
    LayoutElement parent = getParent();
    if (parent == null)
    {
      return null;
    }
    return parent.getStyle();
  }

  public char[] getData()
  {
    return data;
  }

  public int getOffset()
  {
    return offset;
  }

  public int getLength()
  {
    return length;
  }

  public String toString ()
  {
    return "LayoutText={text=" + new String (data, offset, length) + "}";
  }

}
