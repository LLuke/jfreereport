/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * AbstractInputFeed.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: AbstractInputFeed.java,v 1.1 2006/02/12 21:49:31 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.feed;

import java.util.Stack;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.normalizer.Normalizer;
import org.jfree.layouting.model.ContextId;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.model.DocumentMetaNode;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.DefaultDocumentMetaNode;
import org.jfree.layouting.layouter.state.InputFeedState;

/**
 * Creation-Date: 05.12.2005, 18:19:03
 *
 * @author Thomas Morgner
 */
public abstract class AbstractInputFeed implements InputFeed
{
  public static final int DOCUMENT_STARTING = 0;
  public static final int META_EXPECTED = 1;
  public static final int META_PROCESSING = 2;
  public static final int META_NODE_START = 3;
  public static final int META_NODE_ATTRIBUTES = 4;
  public static final int ELEMENT_EXPECTED = 5;
  public static final int ELEMENT_STARTED = 6;
  public static final int ELEMENT_ATTRIBUTES = 7;
  public static final int ELEMENT_CONTENT = 8;
  public static final int DOCUMENT_FINISHED = 9;

  private static final String[] STATE_NAMES = new String[]{
    "DOCUMENT_STARTING", "META_EXPECTED", "META_PROCESSING",
    "META_NODE_START", "META_NODE_ATTRIBUTES",
    "ELEMENT_EXPECTED", "ELEMENT_STARTED", "ELEMENT_ATTRIBUTES",
    "ELEMENT_CONTENT", "DOCUMENT_FINISHED"
  };

  private int state;
  private boolean [][] validStateTransitions;
  private LayoutElement document;
  private Stack elements;
  private Object savePointData;
  private LayoutElement currentElement;
  private LayoutProcess process;
  private InputFeedState savePoint;
  private DocumentMetaNode metaNode;
  private DocumentContext documentContext;

  public AbstractInputFeed(final LayoutProcess process)
  {
    this.process = process;
    this.documentContext = process.getDocumentContext();
    elements = new Stack();

    validStateTransitions = new boolean[10][];
    // after startDocument we expect metadata...
    validStateTransitions[DOCUMENT_STARTING] = new boolean[]{
            false, true, false,
            false, false, false,
            false, false, false, false, false
    };
    // either we get meta-data or the first element
    validStateTransitions[META_EXPECTED] = new boolean[]{
            false, false, true, false, false,
            false, true, false, false, false
    };
    // we either get more meta-data or proceed to the element processing
    validStateTransitions[META_PROCESSING] = new boolean[]{
            false, false, true, true, false,
            true, false, false, false, false, false
    };
    // now, either we get attributes or the meta-node is finished
    validStateTransitions[META_NODE_START] = new boolean[]{
            false, false, false, false, true,
            false, false, false, false, false
    };
    // we expect more attributes or the end of the node
    validStateTransitions[META_NODE_ATTRIBUTES] = new boolean[]{
            false, false, true, false, true,
            false, false, false, false, false
    };
    validStateTransitions[ELEMENT_EXPECTED] = new boolean[]{
            false, false, false, false, false,
            true, true, false, false, true
    };
    validStateTransitions[ELEMENT_STARTED] = new boolean[]{
            false, false, false, false, false,
            true, true, true, true, true
    };
    validStateTransitions[ELEMENT_ATTRIBUTES] = new boolean[]{
            false, false, false, false, false,
            true, true, true, true, true
    };
    validStateTransitions[ELEMENT_CONTENT] = new boolean[]{
            false, false, false, false, false,
            true, true, false, true, true
    };
    validStateTransitions[DOCUMENT_FINISHED] = new boolean[]{
            false, false, false, false, false,
            false, false, false, false, false
    };

    state = DOCUMENT_STARTING;
  }

  public AbstractInputFeed(final LayoutProcess process, final InputFeedState state)
  {
    this(process);
    if (state != null)
    {
      LayoutElement[] elements = state.getOpenElements();
      for (int i = 0; i < elements.length; i++)
      {
        this.elements.add (elements[i]);
      }
      if (elements.length > 0)
      {
        this.document = elements[0];
        this.currentElement = elements[elements.length - 1];
      }
      this.state = state.getState();
    }
  }

  private int checkState(int newState)
  {
    if (validStateTransitions[state][newState] == false)
    {
      throw new IllegalStateException(
              "illegal transition from " + STATE_NAMES[state] + " to " + STATE_NAMES[newState]);
    }
    int oldState = this.state;
    this.state = newState;
    return oldState;
  }

  public final void startDocument()
  {
    checkState(META_EXPECTED);
    performStartDocument();
  }

  protected void performStartDocument()
  {
    document = new LayoutElement(process.generateContextId(-1),
            process.getOutputProcessor(), "@document@");
    currentElement = document;
    elements.push(document);
  }

  public final void startMetaInfo()
  {
    checkState(META_PROCESSING);
    performStartMetaInfo();
  }

  protected void performStartMetaInfo()
  {
  }

  public final void addMetaAttribute(String name, Object attr)
  {
    checkState(META_PROCESSING);
    performAddMetaAttribute(name, attr);
  }

  protected void performAddMetaAttribute(String name, Object attr)
  {
    document.setAttribute(name, attr);
  }

  public void startMetaNode(String type)
  {
    checkState(META_NODE_START);
    performStartMetaNode(type);
  }

  protected void performStartMetaNode (String type)
  {
    metaNode = new DefaultDocumentMetaNode();// create new DocumentMetaNode(type)
    documentContext.addMetaNode(metaNode);
  }

  public void setMetaNodeAttribute(String name, Object attr)
  {
    checkState(META_NODE_ATTRIBUTES);
    performSetMetaNodeAttribute(name, attr);
  }

  protected void performSetMetaNodeAttribute (String name, Object attr)
  {
    metaNode.setMetaAttribute(name, attr);
  }

  public void endMetaNode()
  {
    checkState(META_PROCESSING);
    performEndMetaNode();
  }

  protected void performEndMetaNode()
  {
    metaNode = null;
  }

  public final void endMetaInfo()
  {
    checkState(ELEMENT_EXPECTED);
    performEndMetaInfo();
  }

  protected void performEndMetaInfo()
  {

  }

  public final void startElement(String name)
  {
    int oldState = checkState(ELEMENT_STARTED);
    if (oldState == ELEMENT_EXPECTED ||
        oldState == DOCUMENT_STARTING)
    {
      getNormalizer().startDocument();
    }
    else if (oldState == ELEMENT_ATTRIBUTES ||
        oldState == ELEMENT_STARTED)
    {
      resolveStyle(currentElement);
    }
    performStartElement(name);
  }

  protected void performStartElement(String name)
  {
    final ContextId contextId = process.generateContextId(-1);
    LayoutElement newElement = new LayoutElement
            (contextId, process.getOutputProcessor(), name);
    currentElement.addChild(newElement);
    elements.push(newElement);
    getNormalizer().startElement(newElement);
    this.currentElement = newElement;
  }

  protected void resolveStyle (LayoutElement context)
  {
    process.getStyleResolver().resolveStyle(context);
  }

  public final void setAttribute(String name, Object attr)
  {
    checkState(ELEMENT_ATTRIBUTES);
    performSetAttribute(name, attr);
  }

  protected void performSetAttribute(String name, Object attr)
  {
    currentElement.setAttribute(name, attr);
  }

  public final void addContent(String text)
  {
    int oldState = checkState(ELEMENT_STARTED);
    if (oldState == ELEMENT_ATTRIBUTES ||
        oldState == ELEMENT_STARTED)
    {
      resolveStyle(currentElement);
    }
    //System.out.println("GEN: " + (text));
    performAddContent(text);
  }

  protected void performAddContent(String text)
  {
    final LayoutTextNode ctx = new LayoutTextNode
          (process.generateContextId(-1),
                  process.getOutputProcessor(),
                  text.toCharArray(), 0, text.length());
    currentElement.addChild(ctx);
    getNormalizer().addText(ctx);
  }

  public final void endElement()
  {
    int oldState = checkState(ELEMENT_STARTED);
    if (oldState == ELEMENT_ATTRIBUTES ||
        oldState == ELEMENT_STARTED)
    {
      resolveStyle(currentElement);
    }
    performEndElement ();
  }

  protected void performEndElement ()
  {
    // todo Maybe trigger another event to signal that we are done with the element
    elements.pop();
    getNormalizer().endElement(currentElement);
    currentElement = (LayoutElement) elements.peek();
  }

  public final void endDocument()
  {
    checkState(DOCUMENT_FINISHED);
    performEndDocument();
  }

  protected void performEndDocument ()
  {
    elements.pop();
    if (elements.isEmpty() == false)
    {
      throw new IllegalStateException("Stack is " + elements);
    }
    getNormalizer().endDocument();
    currentElement = null;
  }

  public Object getSavePointData()
  {
    return savePointData;
  }

  protected InputFeedState getSavePoint()
  {
    if (savePoint == null)
    {
      try
      {
        LayoutElement[] e = (LayoutElement[])
                elements.toArray(new LayoutElement[elements.size()]);
        for (int i = 0; i < e.length; i++)
        {
          LayoutElement context = e[i];
          e[i] = (LayoutElement) context.clone();
        }
        savePoint = new InputFeedState(e, state, process.getLastId(), savePointData);
      }
      catch (CloneNotSupportedException e1)
      {
        throw new IllegalStateException("Clone not supported. I'm frightened!");
      }
    }
    return savePoint;
  }

  public void setSavePointData(final Object savePointData)
  {
    Object oldSavePointData = this.savePointData;
    this.savePointData = savePointData;
    // comparing pointers is intended here
    if (oldSavePointData != savePointData)
    {
      savePoint = null;
    }
  }

  protected LayoutProcess getProcess()
  {
    return process;
  }

  protected LayoutElement getCurrentElement()
  {
    return currentElement;
  }

  protected LayoutElement getDocument()
  {
    return document;
  }

  protected int getState()
  {
    return state;
  }

  protected Normalizer getNormalizer()
  {
    return process.getNormalizer();
  }
}
