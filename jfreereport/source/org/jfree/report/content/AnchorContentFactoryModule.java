package org.jfree.report.content;

import org.jfree.report.Anchor;
import org.jfree.report.AnchorElement;
import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictPoint;

public class AnchorContentFactoryModule implements ContentFactoryModule
{
  public AnchorContentFactoryModule ()
  {
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType the content type.
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent (final String contentType)
  {
    return contentType.equals(AnchorElement.CONTENT_TYPE);
  }

  /**
   * Creates content for an element.
   *
   * @param e      the element.
   * @param bounds the bounds.
   * @param ot     the output target.
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the Content creation.
   */
  public Content createContentForElement (final Element e,
                                          final ElementLayoutInformation bounds,
                                          final LayoutSupport ot)
          throws ContentCreationException
  {
    final StrictPoint absPos = bounds.getAbsolutePosition();
    final Object o = e.getValue();
    return createAnchor(o, absPos);
  }

  public static Content createAnchor (final Object o, final StrictPoint position)
  {
    if (o instanceof Anchor)
    {
      return new AnchorContent((Anchor) o, position);
    }
    else if (o != null)
    {
      final Anchor a = new Anchor(String.valueOf(o));
      return new AnchorContent(a, position);
    }
    else
    {
      return EmptyContent.getDefaultEmptyContent();
    }
  }
}
