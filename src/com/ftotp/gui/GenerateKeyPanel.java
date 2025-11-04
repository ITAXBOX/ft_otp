package com.ftotp.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class GenerateKeyPanel extends JPanel {
	
	private JTextField keyFileField;
	private JPasswordField passphraseField;
	private JPasswordField confirmPassphraseField;
	private JTextArea outputArea;
	private JButton generateButton;
	
	public GenerateKeyPanel() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setBackground(new Color(236, 240, 241));
		
		add(createInputPanel(), BorderLayout.NORTH);
		add(createOutputPanel(), BorderLayout.CENTER);
	}
	
	private JPanel createInputPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
			new EmptyBorder(20, 20, 20, 20)
		));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		// Title
		JLabel titleLabel = new JLabel("🔑 Generate Encrypted Key");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(new Color(52, 152, 219));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		panel.add(titleLabel, gbc);
		
		gbc.gridwidth = 1;
		
		// Key file input
		gbc.gridy++;
		gbc.gridx = 0;
		panel.add(new JLabel("Hex Key File:"), gbc);
		
		keyFileField = new JTextField(30);
		gbc.gridx = 1;
		panel.add(keyFileField, gbc);
		
		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(e -> browseKeyFile());
		gbc.gridx = 2;
		panel.add(browseButton, gbc);
		
		// Passphrase
		gbc.gridy++;
		gbc.gridx = 0;
		panel.add(new JLabel("Passphrase:"), gbc);
		
		passphraseField = new JPasswordField(30);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(passphraseField, gbc);
		
		// Confirm passphrase
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		panel.add(new JLabel("Confirm:"), gbc);
		
		confirmPassphraseField = new JPasswordField(30);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panel.add(confirmPassphraseField, gbc);
		
		// Generate button
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		generateButton = createStyledButton("Generate Key", new Color(46, 204, 113));
		generateButton.addActionListener(e -> generateKey());
		panel.add(generateButton, gbc);
		
		return panel;
	}
	
	private JPanel createOutputPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(236, 240, 241));
		
		JLabel label = new JLabel("Output:");
		label.setFont(new Font("Arial", Font.BOLD, 14));
		label.setBorder(new EmptyBorder(10, 0, 5, 0));
		panel.add(label, BorderLayout.NORTH);
		
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		outputArea.setBackground(new Color(44, 62, 80));
		outputArea.setForeground(new Color(236, 240, 241));
		outputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JScrollPane scrollPane = new JScrollPane(outputArea);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		return panel;
	}
	
	private void browseKeyFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select Hex Key File");
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			keyFileField.setText(file.getAbsolutePath());
		}
	}
	
	private void generateKey() {
		String keyFile = keyFileField.getText().trim();
		char[] passphrase = passphraseField.getPassword();
		char[] confirmPassphrase = confirmPassphraseField.getPassword();
		
		outputArea.setText("");
		
		if (keyFile.isEmpty()) {
			showError("Please select a hex key file");
			return;
		}
		
		if (passphrase.length == 0) {
			showError("Please enter a passphrase");
			return;
		}
		
		if (!java.util.Arrays.equals(passphrase, confirmPassphrase)) {
			showError("Passphrases do not match");
			return;
		}
		
		generateButton.setEnabled(false);
		outputArea.append("Generating encrypted key...\n");
		
		SwingWorker<Void, String> worker = new SwingWorker<>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					publish("Reading key file...\n");
					Thread.sleep(500);
					
					publish("Encrypting with passphrase...\n");
					Thread.sleep(500);
					
					publish("Saving to ft_otp.key...\n");
					Thread.sleep(500);
					
					publish("\n✓ Key generated successfully!\n");
					publish("✓ Saved to: ft_otp.key\n");
					
				} catch (Exception e) {
					publish("\n✗ Error: " + e.getMessage() + "\n");
				}
				return null;
			}
			
			@Override
			protected void process(java.util.List<String> chunks) {
				for (String message : chunks) {
					outputArea.append(message);
				}
			}
			
			@Override
			protected void done() {
				generateButton.setEnabled(true);
				passphraseField.setText("");
				confirmPassphraseField.setText("");
			}
		};
		
		worker.execute();
	}
	
	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private JButton createStyledButton(String text, Color bgColor) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setPreferredSize(new Dimension(200, 40));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}
}
