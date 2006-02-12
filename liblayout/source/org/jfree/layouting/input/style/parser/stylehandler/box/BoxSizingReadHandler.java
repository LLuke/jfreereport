package org.jfree.layouting.input.style.parser.stylehandler.box;

import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.keys.box.BoxSizing;

public class BoxSizingReadHandler extends OneOfConstantsReadHandler
{
  public BoxSizingReadHandler ()
  {
    super(false);
    addValue(BoxSizing.BORDER_BOX);
    addValue(BoxSizing.CONTENT_BOX);
  }


}

