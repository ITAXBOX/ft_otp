package com.ftotp.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class GetOTPPanel extends JPanel {
	
	private JTextField keyFileField;
	private JPasswordField passphraseField;
	private JLabel otpLabel;
	private JProgressBar timeBar;
	private JButton generateButton;
	private Timer refreshTimer;
	
	public GetOTPPanel() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setBackground(new Color(236, 240, 241));
		
		add(createInputPanel(), BorderLayout.NORTH);
		add(createOTPDisplayPanel(), BorderLayout.CENTER);
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
		JLabel titleLabel = new JLabel("📱 Generate OTP Code");
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
		panel.add(new JLabel("Key File:"), gbc);
		
		keyFileField = new JTextField("ft_otp.key", 30);
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
		
		// Generate button
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		generateButton = createStyledButton("Get OTP", new Color(52, 152, 219));
		generateButton.addActionListener(e -> generateOTP());
		panel.add(generateButton, gbc);
		
		return panel;
	}
	
	private JPanel createOTPDisplayPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(new Color(236, 240, 241));
		panel.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		// OTP Display Box
		JPanel otpBox = new JPanel();
		otpBox.setLayout(new BoxLayout(otpBox, BoxLayout.Y_AXIS));
		otpBox.setBackground(Color.WHITE);
		otpBox.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
			new EmptyBorder(40, 40, 40, 40)
		));
		
		JLabel titleLabel = new JLabel("YOUR OTP CODE");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		titleLabel.setForeground(new Color(127, 140, 141));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		otpBox.add(titleLabel);
		
		otpBox.add(Box.createVerticalStrut(20));
		
		otpLabel = new JLabel("------");
		otpLabel.setFont(new Font("Arial", Font.BOLD, 64));
		otpLabel.setForeground(new Color(52, 152, 219));
		otpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		otpBox.add(otpLabel);
		
		otpBox.add(Box.createVerticalStrut(20));
		
		timeBar = new JProgressBar(0, 30);
		timeBar.setValue(30);
		timeBar.setStringPainted(true);
		timeBar.setString("30s remaining");
		timeBar.setPreferredSize(new Dimension(300, 25));
		timeBar.setForeground(new Color(46, 204, 113));
		timeBar.setAlignmentX(Component.CENTER_ALIGNMENT);
		otpBox.add(timeBar);
		
		otpBox.add(Box.createVerticalStrut(10));
		
		JLabel infoLabel = new JLabel("⏱  Valid for 30 seconds");
		infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		infoLabel.setForeground(new Color(127, 140, 141));
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		otpBox.add(infoLabel);
		
		panel.add(otpBox, BorderLayout.CENTER);
		
		return panel;
	}
	
	private void browseKeyFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select Key File");
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			keyFileField.setText(file.getAbsolutePath());
		}
	}
	
	private void generateOTP() {
		String keyFile = keyFileField.getText().trim();
		char[] passphrase = passphraseField.getPassword();
		
		if (keyFile.isEmpty()) {
			showError("Please select a key file");
			return;
		}
		
		if (passphrase.length == 0) {
			showError("Please enter passphrase");
			return;
		}
		
		generateButton.setEnabled(false);
		
		SwingWorker<String, Void> worker = new SwingWorker<>() {
			@Override
			protected String doInBackground() throws Exception {
				// Simulate OTP generation
				Thread.sleep(500);
				return String.format("%06d", (int)(Math.random() * 1000000));
			}
			
			@Override
			protected void done() {
				try {
					String otp = get();
					otpLabel.setText(otp);
					startCountdown();
				} catch (Exception e) {
					showError("Error generating OTP: " + e.getMessage());
				}
				generateButton.setEnabled(true);
				passphraseField.setText("");
			}
		};
		
		worker.execute();
	}
	
	private void startCountdown() {
		if (refreshTimer != null) {
			refreshTimer.stop();
		}
		
		timeBar.setValue(30);
		
		refreshTimer = new Timer(1000, e -> {
			int value = timeBar.getValue() - 1;
			timeBar.setValue(value);
			timeBar.setString(value + "s remaining");
			
			if (value <= 10) {
				timeBar.setForeground(new Color(231, 76, 60));
			}
			
			if (value <= 0) {
				refreshTimer.stop();
				otpLabel.setText("EXPIRED");
				otpLabel.setForeground(new Color(231, 76, 60));
			}
		});
		
		refreshTimer.start();
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
