/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * CommentHandler.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CommentHandler.java,v 1.2 2003/07/21 20:46:56 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 20-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.base;

import java.util.ArrayList;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * The comment handler is used to collect all XML comments from the 
 * SAX parser. The parser implementation must support comments to make
 * this feature work.
 * 
 * @author Thomas Morgner
 */
public class CommentHandler implements LexicalHandler
{
  /** A constant marking a comment on the opening tag. */
  public static final String OPEN_TAG_COMMENT = "parser.comment.open";
  /** A constant marking a comment on the closing tag. */
  public static final String CLOSE_TAG_COMMENT = "parser.comment.close";

  /** A list containing all collected comments. */
  private ArrayList comment;
  /** a flag marking whether the SAX parser is currently working in the DTD. */
  private boolean inDTD;

  /**
   * DefaultConstructor.
   */
  public CommentHandler()
  {
    comment = new ArrayList();
  }

  /**
   * Report the start of DTD declarations, if any.
   *
   * <p>This method is empty.</p>
   *
   * @param name The document type name.
   * @param publicId The declared public identifier for the
   *        external DTD subset, or null if none was declared.
   * @param systemId The declared system identifier for the
   *        external DTD subset, or null if none was declared.
   * @exception org.xml.sax.SAXException The application may raise an
   *            exception.
   * @see #endDTD
   * @see #startEntity
   */
  public void startDTD(String name, String publicId,
                       String systemId)
      throws SAXException
  {
    inDTD = true;
  }

  /**
   * Report the end of DTD declarations.
   *
   * <p>This method is empty.</p>
   *
   * @exception org.xml.sax.SAXException The application may raise an exception.
   * @see #startDTD
   */
  public void endDTD()
      throws SAXException
  {
    inDTD = false;
  }

  /**
   * Report the beginning of some internal and external XML entities.
   *
   * <p>This method is empty.</p>
   *
   * @param name The name of the entity.  If it is a parameter
   *        entity, the name will begin with '%', and if it is the
   *        external DTD subset, it will be "[dtd]".
   * @exception org.xml.sax.SAXException The application may raise an exception.
   * @see #endEntity
   * @see org.xml.sax.ext.DeclHandler#internalEntityDecl
   * @see org.xml.sax.ext.DeclHandler#externalEntityDecl
   */
  public void startEntity(String name)
      throws SAXException
  {
  }

  /**
   * Report the end of an entity.
   *
   * <p>This method is empty.</p>
   *
   * @param name The name of the entity that is ending.
   * @exception org.xml.sax.SAXException The application may raise an exception.
   * @see #startEntity
   */
  public void endEntity(String name)
      throws SAXException
  {
  }

  /**
   * Report the start of a CDATA section.
   *
   * <p>This method is empty.</p>
   *
   * @exception org.xml.sax.SAXException The application may raise an exception.
   * @see #endCDATA
   */
  public void startCDATA()
      throws SAXException
  {
  }

  /**
   * Report the end of a CDATA section.
   *
   * <p>This method is empty.</p>
   *
   * @exception org.xml.sax.SAXException The application may raise an exception.
   * @see #startCDATA
   */
  public void endCDATA()
      throws SAXException
  {
  }

  /**
   * Report an XML comment anywhere in the document.
   *
   * <p>This callback will be used for comments inside or outside the
   * document element, including comments in the external DTD
   * subset (if read).  Comments in the DTD must be properly
   * nested inside start/endDTD and start/endEntity events (if
   * used).</p>
   *
   * @param ch An array holding the characters in the comment.
   * @param start The starting position in the array.
   * @param length The number of characters to use from the array.
   * @exception org.xml.sax.SAXException The application may raise an exception.
   */
  public void comment(char ch[], int start, int length)
      throws SAXException
  {
    if (inDTD == false)
    {
      comment.add(new String(ch, start, length));
    }
    //Log.debug ("Comment: String: " + comment);
  }

  /**
   * Returns all collected comments as string array.
   * @return the array containing all comments.
   */
  public String[] getComments()
  {
    if (comment.isEmpty())
    {
      return null;
    }
    return (String[]) comment.toArray(new String[comment.size()]);
  }

  /**
   * Clears all comments.
   */
  public void clearComments ()
  {
    comment.clear();
  }
}
