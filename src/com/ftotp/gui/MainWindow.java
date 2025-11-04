package com.ftotp.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
	
	private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
	private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
	private static final Color TEXT_COLOR = new Color(44, 62, 80);
	
	private JTabbedPane tabbedPane;
	
	public MainWindow() {
		setTitle("FT_OTP - Time-Based One-Time Password");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		
		// Set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		initComponents();
	}
	
	private void initComponents() {
		// Main container
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(BACKGROUND_COLOR);
		
		// Header
		mainPanel.add(createHeader(), BorderLayout.NORTH);
		
		// Tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
		
		tabbedPane.addTab("🔑 Generate Key", new GenerateKeyPanel());
		tabbedPane.addTab("📱 Get OTP", new GetOTPPanel());
		tabbedPane.addTab("📲 QR Code", new QRCodePanel());
		
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		
		// Footer
		mainPanel.add(createFooter(), BorderLayout.SOUTH);
		
		add(mainPanel);
	}
	
	private JPanel createHeader() {
		JPanel header = new JPanel();
		header.setBackground(PRIMARY_COLOR);
		header.setPreferredSize(new Dimension(0, 80));
		header.setLayout(new BorderLayout());
		
		JLabel titleLabel = new JLabel("FT_OTP", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		titleLabel.setForeground(Color.WHITE);
		
		JLabel subtitleLabel = new JLabel("Time-Based One-Time Password Generator", SwingConstants.CENTER);
		subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		subtitleLabel.setForeground(new Color(236, 240, 241));
		
		JPanel titlePanel = new JPanel(new GridLayout(2, 1));
		titlePanel.setBackground(PRIMARY_COLOR);
		titlePanel.add(titleLabel);
		titlePanel.add(subtitleLabel);
		
		header.add(titlePanel, BorderLayout.CENTER);
		
		return header;
	}
	
	private JPanel createFooter() {
		JPanel footer = new JPanel();
		footer.setBackground(TEXT_COLOR);
		footer.setPreferredSize(new Dimension(0, 30));
		
		JLabel footerLabel = new JLabel("© 2025 FT_OTP v1.0 | Secure Authentication", SwingConstants.CENTER);
		footerLabel.setForeground(Color.WHITE);
		footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		
		footer.add(footerLabel);
		
		return footer;
	}
	
	public static void launch() {
		SwingUtilities.invokeLater(() -> {
			MainWindow window = new MainWindow();
			window.setVisible(true);
		});
	}
}
