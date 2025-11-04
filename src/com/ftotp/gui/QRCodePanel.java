package com.ftotp.gui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRCodePanel extends JPanel {
	
	private JTextField keyFileField;
	private JPasswordField passphraseField;
	private JLabel qrLabel;
	private JButton generateButton;
	private JLabel accountLabel;
	private JLabel issuerLabel;
	
	public QRCodePanel() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setBackground(new Color(236, 240, 241));
		
		add(createInputPanel(), BorderLayout.NORTH);
		add(createQRDisplayPanel(), BorderLayout.CENTER);
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
		JLabel titleLabel = new JLabel("📲 Generate QR Code");
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
		generateButton = createStyledButton("Generate QR Code", new Color(142, 68, 173));
		generateButton.addActionListener(e -> generateQRCode());
		panel.add(generateButton, gbc);
		
		return panel;
	}
	
	private JPanel createQRDisplayPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(new Color(236, 240, 241));
		panel.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		// QR Display Box
		JPanel qrBox = new JPanel(new BorderLayout(10, 10));
		qrBox.setBackground(Color.WHITE);
		qrBox.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(142, 68, 173), 3),
			new EmptyBorder(30, 30, 30, 30)
		));
		
		// Header
		JLabel headerLabel = new JLabel("Scan with your authenticator app", SwingConstants.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setForeground(new Color(127, 140, 141));
		qrBox.add(headerLabel, BorderLayout.NORTH);
		
		// QR Code
		qrLabel = new JLabel();
		qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
		qrLabel.setVerticalAlignment(SwingConstants.CENTER);
		qrLabel.setPreferredSize(new Dimension(300, 300));
		
		// Placeholder
		JLabel placeholderLabel = new JLabel("📱 QR Code will appear here", SwingConstants.CENTER);
		placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholderLabel.setForeground(new Color(189, 195, 199));
		qrLabel.add(placeholderLabel);
		
		qrBox.add(qrLabel, BorderLayout.CENTER);
		
		// Info Panel
		JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		
		accountLabel = new JLabel("👤 Account: --", SwingConstants.CENTER);
		accountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		infoPanel.add(accountLabel);
		
		issuerLabel = new JLabel("🏢 Issuer: --", SwingConstants.CENTER);
		issuerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		infoPanel.add(issuerLabel);
		
		qrBox.add(infoPanel, BorderLayout.SOUTH);
		
		panel.add(qrBox, BorderLayout.CENTER);
		
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
	
	private void generateQRCode() {
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
		
		SwingWorker<BufferedImage, Void> worker = new SwingWorker<>() {
			@Override
			protected BufferedImage doInBackground() throws Exception {
				// Simulate QR generation - in real implementation, call FtOtp methods
				Thread.sleep(500);
				String uri = "otpauth://totp/aitawi?secret=JBSWY3DPEHPK3PXP&issuer=ft_otp&algorithm=SHA1&digits=6&period=30";
				return generateQRCodeImage(uri, 300, 300);
			}
			
			@Override
			protected void done() {
				try {
					BufferedImage qrImage = get();
					qrLabel.removeAll();
					qrLabel.setIcon(new ImageIcon(qrImage));
					qrLabel.revalidate();
					qrLabel.repaint();
					
					accountLabel.setText("👤 Account: aitawi");
					issuerLabel.setText("🏢 Issuer: ft_otp");
					
				} catch (Exception e) {
					showError("Error generating QR code: " + e.getMessage());
				}
				generateButton.setEnabled(true);
				passphraseField.setText("");
			}
		};
		
		worker.execute();
	}
	
	private BufferedImage generateQRCodeImage(String text, int width, int height) throws WriterException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 1);
		
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
			}
		}
		
		return image;
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
