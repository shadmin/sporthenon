package com.sporthenon.admin.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sporthenon.admin.window.JFindEntityDialog;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.PicklistItem;
import com.sporthenon.utils.StringUtils;


public class JEntityPicklist extends JPanel implements ItemListener, KeyListener, FocusListener {

	private static final long serialVersionUID = 1L;
	
	private boolean isTextfield = true;
	private String alias;
	private JComboBox<PicklistItem> jCombobox;
	private JTextField jTextField;
	private PicklistItem selectedItem;
	private List<PicklistItem> itemList = new ArrayList<>();
	private JPanel jButtonPanel;
	private JCustomButton jAddButton;
	private JCustomButton jFindButton;
	private JCustomButton jOptionalButton;

	public JEntityPicklist(ActionListener listener, String alias, boolean isTextfield) {
		super();
		this.alias = alias;
		this.isTextfield = isTextfield;
		initialize(listener);
	}

	private void initialize(ActionListener listener) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 21));
        
		jAddButton = new JCustomButton(null, "add.png", null);
		jAddButton.setMargin(new Insets(0, 0, 0, 0));
		jAddButton.setToolTipText("Add");
		jAddButton.setFocusable(false);
		jAddButton.addActionListener(listener);
		jAddButton.setActionCommand(alias + "-add");
		jFindButton = new JCustomButton(null, "find.png", null);
		jFindButton.setMargin(new Insets(0, 0, 0, 0));
		jFindButton.setToolTipText("Find");
		jFindButton.setFocusable(false);
		jFindButton.addActionListener(listener);
		jFindButton.setActionCommand(alias + "-find");
		jOptionalButton = new JCustomButton(null, null, null);
		jOptionalButton.setMargin(new Insets(0, 0, 0, 0));
		jOptionalButton.setFocusable(false);
		jOptionalButton.setVisible(false);
		
        jButtonPanel = new JPanel();
		jButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		jButtonPanel.setPreferredSize(new Dimension(46, 0));
		jButtonPanel.add(jAddButton, null);
		jButtonPanel.add(jFindButton, null);
		jButtonPanel.add(jOptionalButton, null);
        this.add(jButtonPanel, BorderLayout.EAST);
        
        if (isTextfield) {
        	jTextField = new JTextField();
        	jTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        	jTextField.setBorder(BorderFactory.createCompoundBorder(jTextField.getBorder(), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        	jTextField.addKeyListener(this);
        	jTextField.addFocusListener(this);
        	this.add(jTextField, BorderLayout.CENTER);
        }
        else {
            jCombobox = new JComboBox<>();
            jCombobox.setMaximumRowCount(20);
            jCombobox.addItemListener(this);
            this.add(jCombobox, BorderLayout.CENTER);
        }
	}

	public JComboBox<PicklistItem> getCombobox() {
		return jCombobox;
	}
	
	public JTextField getTextField() {
		return jTextField;
	}

	public PicklistItem getSelectedItem() {
		return (isTextfield ? selectedItem : (PicklistItem)jCombobox.getSelectedItem());
	}

	public void setSelectedItem(PicklistItem selectedItem) {
		this.selectedItem = selectedItem;
		if (isTextfield) {
			jTextField.setBorder(BorderFactory.createLineBorder(selectedItem != null ? Color.BLACK : Color.LIGHT_GRAY));
			jTextField.setBorder(BorderFactory.createCompoundBorder(jTextField.getBorder(), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		}
	}

	public JPanel getButtonPanel2() {
		return jButtonPanel;
	}

	public JCustomButton getAddButton() {
		return jAddButton;
	}

	public JCustomButton getFindButton() {
		return jFindButton;
	}

	public JCustomButton getOptionalButton() {
		return jOptionalButton;
	}
	
	public Collection<PicklistItem> getItemList() {
		return itemList;
	}

	public void setItemList(Collection<PicklistItem> items, Object param) {
		this.itemList.clear();
		if (items != null && !items.isEmpty()) {
			for (PicklistItem item : items) {
				if (param == null || item.getParam() == null || (item.getParam() != null && item.getParam().equals(param))) {
					this.itemList.add(item);
				}
			}
		}
		if (!isTextfield) {
			jCombobox.removeAllItems();
			jCombobox.addItem(new PicklistItem(0, ""));
			for (PicklistItem item : itemList) {
				jCombobox.addItem(item);
			}
		}
	}
	
	public void setValue(Integer id) {
		if (isTextfield) {
			if (id == null) {
				setSelectedItem(null);
				jTextField.setText("");
				return;
			}
			for (PicklistItem item : itemList) {
				if (item.getValue() == id) {
					setSelectedItem(item);
					jTextField.setText(item.getText());
					break;
				}
			}
		}
		else {
			if (id == null) {
				jCombobox.setSelectedIndex(0);
				return;
			}
			int x = 0;
			for (int i = 1 ; i < jCombobox.getItemCount() ; i++) {
				x++;
				PicklistItem bean = (PicklistItem) jCombobox.getItemAt(i);
				if (bean.getValue() == id) {
					break;
				}
			}
			if (jCombobox.getItemCount() > 0) {
				jCombobox.setSelectedIndex(x);
			}
		}
	}

	public boolean isTextfield() {
		return isTextfield;
	}

	public void clear() {
		if (isTextfield) {
			jTextField.setText("");
			this.selectedItem = null;
		}
		else {
			jCombobox.setSelectedIndex(0);
		}
	}
	
	public List<PicklistItem> getItemsFromText(String text) {
		List<PicklistItem> list = new ArrayList<>();
		for (PicklistItem item : itemList) {
			if (item.getText().toLowerCase().startsWith(text.toLowerCase())) {
				list.add(item);
			}
		}
		return list;
	}
	
	public void setEnabled(boolean enabled) {
		jCombobox.setEnabled(enabled);
		jAddButton.setEnabled(enabled);
		jFindButton.setEnabled(enabled);
		jOptionalButton.setEnabled(enabled);
	}

	public void itemStateChanged(ItemEvent e) {
		jCombobox.setToolTipText(jCombobox.getSelectedIndex() != -1 ? ((PicklistItem) jCombobox.getSelectedItem()).getText() : "");
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		jTextField.selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (!StringUtils.notEmpty(jTextField.getText())) {
			setSelectedItem(null);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		String value = jTextField.getText();
		if (e.getKeyCode() == KeyEvent.VK_ENTER && StringUtils.notEmpty(value)) {
			String selectedText = jTextField.getSelectedText();
			if (selectedText != null) {
				value = value.replace(selectedText, "");
			}
			List<PicklistItem> items = getItemsFromText(value);
			if (items.size() > 1) {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, this, value, items);
				if (dlg.getSelectedItem() != null) {
					int id = dlg.getSelectedItem().getValue();
					setValue(id);
				}
			}
			else if (items.size() == 1) {
				setSelectedItem(items.get(0));
				jTextField.setText(selectedItem.getText());
			}
		}
		else if (e.getKeyCode() != 8 && StringUtils.notEmpty(value) && value.length() >= 2) {
			List<PicklistItem> items = getItemsFromText(value);
			if (!items.isEmpty()) {
				PicklistItem item = items.get(0);
				setSelectedItem(item);
				jTextField.setText(item.getText());
				jTextField.select(value.length(), item.getText().length());
			}
		}
	}

}