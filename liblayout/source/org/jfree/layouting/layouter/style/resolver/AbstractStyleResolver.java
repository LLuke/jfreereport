package org.jfree.layouting.layouter.style.resolver;

import java.util.Iterator;

import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.Resource;

public abstract class AbstractStyleResolver implements StyleResolver
{
  //private StyleSheet defaultStyleSheet;
  private LayoutStyle initialStyle;
  private LayoutProcess layoutProcess;
  private DocumentContext documentContext;
  private NamespaceCollection namespaces;
  private StyleKey[] keys;

  public void initialize (LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
    this.documentContext = layoutProcess.getDocumentContext();
    this.namespaces = documentContext.getNamespaces();
  }

  protected void loadInitialStyle ()
  {
    this.initialStyle = new LayoutStyle(null);
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
          copyStyleInformation(initialStyle, drule);
        }
      }
    }
    catch (Exception e)
    {
      // Not yet handled ...
      e.printStackTrace();
    }
  }

  protected void copyStyleInformation (LayoutStyle target, CSSDeclarationRule rule)
  {
    final StyleRule parentRule = rule.getParentRule();
    if (parentRule instanceof CSSDeclarationRule)
    {
      copyStyleInformation(target, (CSSDeclarationRule) parentRule);
    }

    Iterator keys = rule.getPropertyKeys();
    while (keys.hasNext())
    {
      StyleKey key = (StyleKey) keys.next();
      CSSValue value = rule.getPropertyCSSValue(key);
      target.setValue(key, value);
    }
  }

  protected LayoutProcess getLayoutProcess ()
  {
    return layoutProcess;
  }

  protected LayoutStyle getInitialStyle ()
  {
    return initialStyle;
  }

  protected DocumentContext getDocumentContext ()
  {
    return documentContext;
  }

  protected StyleKey[] getKeys ()
  {
    if (keys == null)
    {
      keys = StyleKeyRegistry.getRegistry().getKeys(new StyleKey[0]);
    }
    return keys;
  }

  protected NamespaceCollection getNamespaces ()
  {
    return namespaces;
  }
}
