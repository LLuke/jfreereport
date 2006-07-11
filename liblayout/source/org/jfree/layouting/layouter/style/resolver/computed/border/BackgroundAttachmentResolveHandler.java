/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * BackgroundAttachmentResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BackgroundAttachmentResolveHandler.java,v 1.2 2006/04/17 20:51:15 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver.computed.border;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.keys.border.BackgroundAttachment;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.context.BackgroundSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ListOfConstantsResolveHandler;

/**
 * Creation-Date: 11.12.2005, 23:46:01
 *
 * @author Thomas Morgner
 */
public class BackgroundAttachmentResolveHandler
        extends ListOfConstantsResolveHandler
{
  public BackgroundAttachmentResolveHandler()
  {
    addNormalizeValue(BackgroundAttachment.FIXED);
    addNormalizeValue(BackgroundAttachment.LOCAL);
    addNormalizeValue(BackgroundAttachment.SCROLL);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
  }

  protected boolean resolveItem(final LayoutProcess process,
                                LayoutElement currentNode,
                                LayoutStyle style,
                                StyleKey key, int index,
                                CSSConstant item)
  {
    CSSConstant bvalue = (CSSConstant) lookupValue(item);
    BackgroundSpecification backgroundSpecification =
            currentNode.getLayoutContext().getBackgroundSpecification();
    backgroundSpecification.setBackgroundAttachment(index, bvalue);
    return true;
  }
}
