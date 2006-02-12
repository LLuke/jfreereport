package org.jfree.layouting.model;

import java.util.HashMap;
import java.io.Serializable;

import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessor;

/**
 * This represents an abstract node in the document model. Each node has a
 * style.
 *
 * @author Thomas Morgner
 */
public abstract class LayoutNode
        implements Serializable, Cloneable
{
  private ContextId contextId;
  private LayoutElement parent;
  private HashMap processAttributes;
  private LayoutNode predecessor;
  private LayoutContext layoutContext;

  protected LayoutNode(final ContextId contextId,
                       final OutputProcessor outputProcessor)
  {
    this.layoutContext = new DefaultLayoutContext(outputProcessor);
    this.contextId = contextId;
    this.processAttributes = new HashMap();
  }

  public void setProcessAttributes (HashMap processAttributes)
  {
    this.processAttributes = processAttributes;
  }

  public Object clone () throws CloneNotSupportedException
  {
    LayoutNode node = (LayoutNode) super.clone();
    node.processAttributes = (HashMap) processAttributes.clone();
    node.parent = null;
    node.predecessor = null;
    return node;
  }
  public LayoutNode getPredecessor()
  {
    return predecessor;
  }

  protected void setPredecessor(final LayoutNode predecessor)
  {
    this.predecessor = predecessor;
  }

  public ContextId getContextId()
  {
    return contextId;
  }

  public void setProcessAttribute (ProcessAttributeName name, Object value)
  {
    if (value == null)
    {
      processAttributes.remove(name);
    }
    else
    {
      processAttributes.put(name, value);
    }
  }

  public Object getProcessAttribute (ProcessAttributeName name)
  {
    return processAttributes.get(name);
  }

  public abstract LayoutStyle getStyle ();

  protected void setParent (LayoutElement parent)
  {
    this.parent = parent;
  }

  public LayoutElement getParent()
  {
    return parent;
  }

  public LayoutContext getLayoutContext ()
  {
    return layoutContext;
  }

  public void setLayoutContext (LayoutContext layoutContext)
  {
    this.layoutContext = layoutContext;
  }
}
