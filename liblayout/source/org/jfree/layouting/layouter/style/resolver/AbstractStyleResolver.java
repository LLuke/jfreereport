package org.jfree.layouting.layouter.style.resolver;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.context.ContextId;
import org.jfree.layouting.layouter.context.DefaultLayoutContext;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.LayoutStyleImpl;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.values.AttrValueFunction;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;

public abstract class AbstractStyleResolver implements StyleResolver
{
  protected static abstract class AbstractStyleResolverState implements State
  {
    private LayoutStyle initialStyle;
    private StyleKey[] keys;

    public AbstractStyleResolverState()
    {
    }

    public LayoutStyle getInitialStyle()
    {
      return initialStyle;
    }

    public void setInitialStyle(final LayoutStyle initialStyle)
    {
      this.initialStyle = initialStyle;
    }

    protected abstract AbstractStyleResolver create();


    public StatefullComponent restore(LayoutProcess layoutProcess)
            throws StateException
    {
      AbstractStyleResolver resv = create();
      fill(resv, layoutProcess);
      return resv;
    }

    protected void fill(final AbstractStyleResolver resolver,
                        final LayoutProcess layoutProcess)
    {
      resolver.initialStyle = initialStyle;
      resolver.layoutProcess = layoutProcess;
      resolver.documentContext = layoutProcess.getDocumentContext();
      resolver.namespaces = layoutProcess.getDocumentContext().getNamespaces();
      resolver.keys = keys;
      resolver.attrFunction = new AttrValueFunction();
    }

    public StyleKey[] getKeys()
    {
      return keys;
    }

    public void setKeys(final StyleKey[] keys)
    {
      this.keys = keys;
    }
  }

  //private StyleSheet defaultStyleSheet;
  private LayoutStyle initialStyle;
  private LayoutProcess layoutProcess;
  private DocumentContext documentContext;
  private NamespaceCollection namespaces;
  private StyleKey[] keys;
  private AttrValueFunction attrFunction;


  protected AbstractStyleResolver()
  {
  }

  public LayoutContext createAnonymousContext(final ContextId id,
                                              final LayoutContext parent)
  {
    LayoutElement parentElement = new LayoutElement(null, null, parent);
    DefaultLayoutContext anonymousContext = new DefaultLayoutContext
            (id, Namespaces.LIBLAYOUT_NAMESPACE,
                    "anonymous-context", null, new AttributeMap());
    LayoutElement anonymousElement = new LayoutElement
            (parentElement, null, anonymousContext);
    resolveOutOfContext(anonymousElement);
    return anonymousContext;
  }

  public void initialize(LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
    this.documentContext = layoutProcess.getDocumentContext();
    this.namespaces = documentContext.getNamespaces();
    this.attrFunction = new AttrValueFunction();
  }

  protected void loadInitialStyle()
  {
    this.initialStyle = new LayoutStyleImpl();
    try
    {
      final ResourceManager manager = layoutProcess.getResourceManager();
      final Resource resource = manager.createDirectly
              ("res://org/jfree/layouting/initial.css", StyleSheet.class);
      final StyleSheet initialStyleSheet = (StyleSheet) resource.getResource();

      int rc = initialStyleSheet.getRuleCount();
      for (int i = 0; i < rc; i++)
      {
        StyleRule rule = initialStyleSheet.getRule(i);
        if (rule instanceof CSSDeclarationRule)
        {
          final CSSDeclarationRule drule = (CSSDeclarationRule) rule;
          copyStyleInformation(initialStyle, drule, null);
        }
      }
    }
    catch (Exception e)
    {
      // Not yet handled ...
      // e.printStackTrace();
    }

    final DocumentContext documentContext = layoutProcess.getDocumentContext();
    documentContext.setMetaAttribute(DocumentContext.INITIAL_STYLE, initialStyle);
  }

  protected void copyStyleInformation
          (LayoutStyle target, CSSDeclarationRule rule,
           LayoutElement element)
  {
    try
    {
      final StyleRule parentRule = rule.getParentRule();
      if (parentRule instanceof CSSDeclarationRule)
      {
        copyStyleInformation(target, (CSSDeclarationRule) parentRule, element);
      }

      if (element == null)
      {
        final StyleKey[] propertyKeys = rule.getPropertyKeysAsArray();
        for (int i = 0; i < propertyKeys.length; i++)
        {
          final StyleKey key = propertyKeys[i];
          target.setValue(key, rule.getPropertyCSSValue(key));
        }
        return;
      }

      final StyleKey[] propertyKeys = rule.getPropertyKeysAsArray();
      final CSSValue[] values = new CSSValue[rule.getSize()];
      for (int i = 0; i < values.length; i++)
      {
        final StyleKey key = propertyKeys[i];
        CSSValue value = rule.getPropertyCSSValue(key);
        if (ContentStyleKeys.CONTENT.equals(key) ||
            ContentStyleKeys.STRING_DEFINE.equals(key) ||
            ContentStyleKeys.STRING_SET.equals(key))
        {
          // dont resolve that one ..
          values[i] = value;
        }
        else
        {
          values[i] = resolveValue(value, element);
        }
      }

      for (int i = 0; i < values.length; i++)
      {
        final StyleKey key = propertyKeys[i];
        target.setValue(key, values[i]);
      }
    }
    catch (FunctionEvaluationException e)
    {
      // something went terribly wrong
      Log.debug("Skipping rule, as resolving failed.");
    }

  }

  protected CSSValue resolveValue(CSSValue value, LayoutElement element)
          throws FunctionEvaluationException

  {
    if (element == null)
    {
      return value;
    }
    if (value == null)
    {
      return null;
    }
    if (containsAttrFunction(value) == false)
    {
      return value;
    }

    if (value instanceof CSSAttrFunction)
    {
      // thats plain and simple - resolve it directly.
      return attrFunction.evaluate
              (getLayoutProcess(), element, (CSSFunctionValue) value);
    }
    else if (value instanceof CSSValueList)
    {
      final CSSValueList list = (CSSValueList) value;
      final ArrayList retValus = new ArrayList();
      final int length = list.getLength();
      for (int i = 0; i < length; i++)
      {
        final CSSValue item = list.getItem(i);
        retValus.add(resolveValue(item, element));
      }
      return new CSSValueList(retValus);
    }
    else if (value instanceof CSSValuePair)
    {
      CSSValuePair pair = (CSSValuePair) value;
      return new CSSValuePair
              (resolveValue(pair.getFirstValue(), element),
                      resolveValue(pair.getSecondValue(), element));
    }
    else
    {
      return value;
    }
  }

  protected boolean containsAttrFunction(CSSValue value)
  {
    if (value == null)
    {
      return false;
    }
    if (value instanceof CSSAttrFunction)
    {
      return true;
    }

    if (value instanceof CSSValueList)
    {
      final CSSValueList list = (CSSValueList) value;
      final int length = list.getLength();
      for (int i = 0; i < length; i++)
      {
        final CSSValue item = list.getItem(i);
        if (containsAttrFunction(item))
        {
          return true;
        }
      }
      return false;
    }

    if (value instanceof CSSValuePair)
    {
      CSSValuePair pair = (CSSValuePair) value;
      if (containsAttrFunction(pair.getFirstValue()))
      {
        return true;
      }
      if (containsAttrFunction(pair.getSecondValue()))
      {
        return true;
      }
      return false;
    }
    return false;
  }

  protected LayoutProcess getLayoutProcess()
  {
    return layoutProcess;
  }

  public LayoutStyle getInitialStyle()
  {
    return initialStyle;
  }

  protected DocumentContext getDocumentContext()
  {
    return documentContext;
  }

  protected StyleKey[] getKeys()
  {
    if (keys == null)
    {
      keys = StyleKeyRegistry.getRegistry().getKeys(new StyleKey[0]);
    }
    return keys;
  }

  protected NamespaceCollection getNamespaces()
  {
    return namespaces;
  }

  protected void fillState(AbstractStyleResolverState state)
  {
    state.setInitialStyle(initialStyle);
    state.setKeys(keys);
  }

  protected abstract void resolveOutOfContext(LayoutElement element);

}
