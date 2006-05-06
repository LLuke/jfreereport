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
 * PropertySheetInspector.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: PropertySheetInspector.java,v 1.1 2005/06/01 21:28:23 mimil Exp $
 *
 * Changes (from 2005-06-01)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.tester;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class PropertySheetInspector extends JPanel
{
  public PropertySheetInspector (final Object bean)
  {
    super(null);
    if (bean == null)
    {
      throw new NullPointerException("The bean must not be null.");
    }
    setLayout(new GridBagLayout()); //todo: do better
    final GridBagConstraints constraint = new GridBagConstraints();
    constraint.fill = GridBagConstraints.HORIZONTAL;
    constraint.ipadx = 5;

    try
    {
      final BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

      final PropertyDescriptor[] beanDescriptors = beanInfo.getPropertyDescriptors();

      //for (PropertyDescriptor desc : beanDescriptors)
      for (int i = 0; i < beanDescriptors.length; i++)
      {
        final PropertyDescriptor desc = beanDescriptors[i];
        final PropertyEditor editor = getEditor(bean, desc);

        if (editor != null)
        {    //a suitable editor found
          constraint.gridx = 0;
          constraint.weightx = 0.;
          constraint.anchor = GridBagConstraints.WEST;
          add(new JLabel(desc.getName()), constraint);    //add the name
          constraint.anchor = GridBagConstraints.CENTER;
          constraint.gridx = 1;
          constraint.weightx = 1.;
          add(getEditorComponent(editor), constraint);    //add the edit component
        }
      }
    }
    catch (IntrospectionException e)
    {
      e.printStackTrace();
    }
  }


  public JComponent getEditorComponent (final PropertyEditor editor)
  {
    if (!editor.supportsCustomEditor())
    {    //use standard editor component
      final String[] tags = editor.getTags();
      if (tags != null) //if supports tags
      {
        //make a combo with tag values
        final JComboBox combo = new JComboBox(tags);
        combo.setSelectedItem(editor.getAsText());  //setting to the current value
        //adding actions
        combo.addItemListener(new ItemListener()
        {
          public void itemStateChanged (ItemEvent e)
          {
            editor.setAsText((String) combo.getSelectedItem()); //should fire a property change in the editor
          }
        });

        return combo;
      }
      else
      {
        //else make a textfield
        final JTextField field = new JTextField(editor.getAsText());
        //adding action
        field.getDocument().addDocumentListener(new DocumentListener()
        {
          //goret mode !
          public void insertUpdate (DocumentEvent e)
          {
            try
            {
              editor.setAsText(field.getText());
            }
            catch (IllegalArgumentException exception)
            {
            }
          }

          public void removeUpdate (DocumentEvent e)
          {
            try
            {
              editor.setAsText(field.getText());
            }
            catch (IllegalArgumentException exception)
            {
            }
          }

          public void changedUpdate (DocumentEvent e)
          {
          }
        });

        return field;
      }
    }
    else
    {    //else a custom editor was provided
      //we will do a button that popups the custom editor
      final JButton button = new JButton();

      if (!editor.isPaintable())
      { //if it should only show a text
        button.setText(buttonText(editor.getAsText()));

      }
      else
      {    //if it have to draw its icon
        button.setIcon(new Icon()
        {
          public void paintIcon (Component c, Graphics g, int x, int y)
          {
            g.translate(x, y);
            Rectangle r = new Rectangle(0, 0, getIconWidth(), getIconHeight());
            Color oldColor = g.getColor();
            g.setColor(Color.black);
            editor.paintValue(g, r);
            g.setColor(oldColor);
            g.translate(-x, -y);
          }

          public int getIconWidth ()
          {
            return 16;
          }

          public int getIconHeight ()
          {
            return 16;
          }
        });
      }

      //adding the action
      button.addActionListener(new ActionListener()
      {
        public void actionPerformed (ActionEvent e)
        {
          JOptionPane.showMessageDialog(PropertySheetInspector.this, editor.getCustomEditor());
          if (!editor.isPaintable())
          {
            button.setText(buttonText(editor.getAsText()));
          }
          else
          {
            button.repaint();
          }
        }
      });

      return button;
    }
  }


  public PropertyEditor getEditor (final Object bean, final PropertyDescriptor desc)
  {
    final PropertyEditor editor;
    final Method readMethod = desc.getReadMethod();
    final Method writeMethod = desc.getWriteMethod();

    //we only want readable and writable methods
    if (readMethod == null || writeMethod == null)
    {
      return null;
    }

    final Class propertyEditorClass = desc.getPropertyEditorClass();
    if (propertyEditorClass == null)
    {   //no custom editor, so the common java editor manager will handle it
      //gets the editor for the type of this descriptor
      editor = PropertyEditorManager.findEditor(desc.getPropertyType());
    }
    else
    {    //the custom editor
      try
      {
        editor = (PropertyEditor) propertyEditorClass.newInstance();
      }
      catch (InstantiationException e)
      {
        e.printStackTrace();
        return null;
      }
      catch (IllegalAccessException e)
      {
        e.printStackTrace();
        return null;
      }
    }

    if (editor == null)
    {    //no editor
      return null;
    }

    try
    {
      //getting the current value
      final Object value = readMethod.invoke(bean, new Object[]{});
      if (value != null)
      {
        editor.setValue(value);
      }

      //adding action
      editor.addPropertyChangeListener(new PropertyChangeListener()
      {
        public void propertyChange (PropertyChangeEvent evt)
        {
          try
          {
            writeMethod.invoke(bean, new Object[]{editor.getValue()});
          }
          catch (IllegalAccessException e)
          {
            e.printStackTrace();
          }
          catch (InvocationTargetException e)
          {
            e.printStackTrace();
          }
        }
      });

      editor.addPropertyChangeListener(new PropertyChangeListener()
      {
        public void propertyChange (PropertyChangeEvent evt)
        {
          firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
      });
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
      return null;
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      return null;
    }

    return editor;
  }

  private static String buttonText (final String text)
  {
    if (text == null || text.equals(""))
    {
      return " ";
    }
    else
    {
      return text;
    }
  }

}

