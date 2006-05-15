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
 * DefaultInputFeed.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultInputFeed.java,v 1.1 2006/04/17 21:01:49 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.feed;

import java.io.IOException;
import java.util.Stack;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.ContextId;
import org.jfree.layouting.model.DefaultDocumentMetaNode;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.DocumentMetaNode;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.normalizer.ContentNormalizer;
import org.jfree.layouting.normalizer.NormalizationException;
import org.jfree.layouting.normalizer.Normalizer;

/**
 * Creation-Date: 05.12.2005, 18:19:03
 *
 * @author Thomas Morgner
 */
public class DefaultInputFeed implements InputFeed
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

  private boolean initialized;
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
  private Normalizer normalizer;

  public DefaultInputFeed (final LayoutProcess process)
  {
    this.process = process;
    this.documentContext = process.getDocumentContext();
    this.elements = new Stack();
    this.normalizer = new ContentNormalizer(process);

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
            true, true, false, true, true
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

  public DefaultInputFeed (final LayoutProcess process, final InputFeedState state)
  {
    this(process);
    if (state != null)
    {
      LayoutElement[] elements = state.getOpenElements();
      for (int i = 0; i < elements.length; i++)
      {
        this.elements.add(elements[i]);
      }
      if (elements.length > 0)
      {
        this.document = elements[0];
        this.currentElement = elements[elements.length - 1];
      }
      this.state = state.getState();
    }
  }

  private int checkState (int newState)
  {
    if (validStateTransitions[state][newState] == false)
    {
      throw new IllegalStateException
              ("illegal transition from " + STATE_NAMES[state] +
                      " to " + STATE_NAMES[newState]);
    }
    int oldState = this.state;
    this.state = newState;
    return oldState;
  }

  public final void startDocument ()
  {
    checkState(META_EXPECTED);
    performStartDocument();
  }

  protected void performStartDocument ()
  {
    document = new LayoutElement(process.generateContextId(-1),
            process.getOutputProcessor(), Namespaces.LIBLAYOUT_NAMESPACE, "@document@");
    currentElement = document;
    elements.push(document);
  }

  public final void startMetaInfo ()
  {
    checkState(META_PROCESSING);
    performStartMetaInfo();
  }

  protected void performStartMetaInfo ()
  {
  }

  public final void addDocumentAttribute (String name, Object attr)
  {
    checkState(META_PROCESSING);
    performAddDocumentAttribute(name, attr);
  }

  protected void performAddDocumentAttribute (String name, Object attr)
  {
    documentContext.setMetaAttribute(name, attr);
  }

  public void startMetaNode ()
  {
    checkState(META_NODE_START);
    performStartMetaNode();
  }

  protected void performStartMetaNode ()
  {
    metaNode = new DefaultDocumentMetaNode();// create new DocumentMetaNode(type)
    documentContext.addMetaNode(metaNode);
  }

  public final void setMetaNodeAttribute (String name, Object attr)
  {
    checkState(META_NODE_ATTRIBUTES);
    performSetMetaNodeAttribute(name, attr);
  }

  protected void performSetMetaNodeAttribute (String name, Object attr)
  {
    metaNode.setMetaAttribute(name, attr);
  }

  public void endMetaNode ()
  {
    checkState(META_PROCESSING);
    performEndMetaNode();
  }

  protected void performEndMetaNode ()
  {
    metaNode = null;
  }

  public final void endMetaInfo ()
          throws InputFeedException
  {
    checkState(ELEMENT_EXPECTED);
    performEndMetaInfo();
  }

  public NamespaceCollection getNamespaceCollection ()
  {
    if (initialized == false)
    {
      throw new IllegalStateException("Not yet!");
    }
    return documentContext.getNamespaces();
  }

  protected void performEndMetaInfo ()
          throws InputFeedException
  {
    try
    {
      initializeDocument();
    }
    catch (Exception e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
  }

  public final void startElement (String namespace, String name)
          throws InputFeedException
  {
    int oldState = checkState(ELEMENT_STARTED);

    if (oldState == META_EXPECTED ||
            oldState == ELEMENT_EXPECTED)
    {
      try
      {
        initializeDocument();
      }
      catch (Exception e)
      {
        throw new InputFeedException("Failed to normalize element", e);
      }
    }
    else if (oldState == ELEMENT_ATTRIBUTES ||
            oldState == ELEMENT_STARTED)
    {
      try
      {
        getNormalizer().startElement(currentElement);
      }
      catch (NormalizationException e)
      {
        throw new InputFeedException("Failed to normalize element", e);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    performStartElement(namespace, name);
  }

  private void initializeDocument ()
          throws IOException, NormalizationException
  {
    if (initialized)
    {
      return;
    }

    // initialize all factories from the given meta-data.
    // the execution order here is important!
    documentContext.initialize();
    getNormalizer().startDocument();
    initialized = true;
  }

  protected void performStartElement (String namespace, String name)
  {
    final ContextId contextId = process.generateContextId(-1);
    LayoutElement newElement = new LayoutElement
            (contextId, process.getOutputProcessor(), namespace, name);
    newElement.setInputSavePoint(getSavePoint());
    currentElement.addChild(newElement);
    elements.push(newElement);
    this.currentElement = newElement;
  }

//  protected void resolveStyle (LayoutElement context)
//  {
//    getNormalizer().startElement(context);
//  }

  public final void setAttribute (String namespace, String name, Object attr)
  {
    checkState(ELEMENT_ATTRIBUTES);
    performSetAttribute(namespace, name, attr);
  }

  protected void performSetAttribute (String namespace, String name, Object attr)
  {
    currentElement.setAttribute(namespace, name, attr);
  }

  public final void addContent (String text)
          throws InputFeedException
  {
    try
    {
      int oldState = checkState(ELEMENT_CONTENT);
      if (oldState == ELEMENT_ATTRIBUTES ||
              oldState == ELEMENT_STARTED)
      {
        getNormalizer().startElement(currentElement);
      }
      else if (oldState == ELEMENT_EXPECTED ||
              oldState == META_EXPECTED)
      {
        initializeDocument();
      }
      //System.out.println("GEN: " + (text));
      performAddContent(text);
    }
    catch (NormalizationException ne)
    {
      throw new InputFeedException("Failed to normalize element", ne);
    }
    catch (IOException ioe)
    {
      throw new InputFeedException("Failed to normalize element", ioe);
    }
  }

  protected void performAddContent (String text)
          throws InputFeedException
  {
    final LayoutTextNode ctx = new LayoutTextNode
            (process.generateContextId(-1),
                    process.getOutputProcessor(),
                    text.toCharArray(), 0, text.length());
    ctx.setInputSavePoint(getSavePoint());
    currentElement.addChild(ctx);
    try
    {
      getNormalizer().addText(ctx);
    }
    catch (NormalizationException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
    catch (IOException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
  }

  public final void endElement ()
          throws InputFeedException
  {
    try
    {
      int oldState = checkState(ELEMENT_EXPECTED);
      if (oldState == ELEMENT_ATTRIBUTES ||
              oldState == ELEMENT_STARTED)
      {
        getNormalizer().startElement(currentElement);
      }
      performEndElement();
    }
    catch (NormalizationException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
    catch (IOException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
  }

  protected void performEndElement ()
          throws IOException, NormalizationException
  {
    elements.pop();
    getNormalizer().endElement(currentElement);
    currentElement = (LayoutElement) elements.peek();
  }

  public final void endDocument ()
          throws InputFeedException
  {
    checkState(DOCUMENT_FINISHED);
    try
    {
      performEndDocument();
    }
    catch (NormalizationException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
    catch (IOException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
  }

  protected void performEndDocument ()
          throws IOException, NormalizationException
  {
    elements.pop();
    if (elements.isEmpty() == false)
    {
      throw new IllegalStateException("Stack is not yet empty: " + elements);
    }
    getNormalizer().endDocument();
    currentElement = null;
  }

  public Object getSavePointData ()
  {
    return savePointData;
  }

  protected InputFeedState getSavePoint ()
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

  public void setSavePointData (final Object savePointData)
  {
    Object oldSavePointData = this.savePointData;
    this.savePointData = savePointData;
    // comparing pointers is intended here
    if (oldSavePointData != savePointData)
    {
      savePoint = null;
    }
  }

  protected LayoutProcess getProcess ()
  {
    return process;
  }

  protected LayoutElement getCurrentElement ()
  {
    return currentElement;
  }

  protected LayoutElement getDocument ()
  {
    return document;
  }

  protected int getState ()
  {
    return state;
  }

  protected Normalizer getNormalizer ()
  {
    return normalizer;
  }
}
