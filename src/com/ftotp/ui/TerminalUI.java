package com.ftotp.ui;

public final class TerminalUI {
	
	private TerminalUI() {
	}

	public static void printBanner() {
		System.out.println();
		System.out.println(Colors.BOLD_CYAN + "╔════════════════════════════════════════════════════════════╗" + Colors.RESET);
		System.out.println(Colors.BOLD_CYAN + "║" + Colors.BOLD_WHITE + "                         FT_OTP v1.0                        " + Colors.BOLD_CYAN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_CYAN + "║" + Colors.CYAN + "              Time-Based One-Time Password Generator        " + Colors.BOLD_CYAN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_CYAN + "╚════════════════════════════════════════════════════════════╝" + Colors.RESET);
		System.out.println();
	}

	public static void printSuccess(String message) {
		System.out.println(Colors.BOLD_GREEN + "✓ " + message + Colors.RESET);
	}

	public static void printError(String message) {
		System.err.println(Colors.BOLD_RED + "✗ Error: " + Colors.RED + message + Colors.RESET);
	}

	public static void printInfo(String message) {
		System.out.println(Colors.CYAN + "ℹ " + message + Colors.RESET);
	}

	public static void printWarning(String message) {
		System.out.println(Colors.YELLOW + "⚠ " + message + Colors.RESET);
	}

	public static void printSection(String title) {
		System.out.println();
		System.out.println(Colors.BOLD_CYAN + "─── " + title + " " + Colors.RESET);
		System.out.println();
	}

	public static void printOTP(String otp) {
		int width = 40;
		System.out.println();
		System.out.println(Colors.BOLD_GREEN + "╔" + "═".repeat(width - 2) + "╗" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + Colors.RESET + center("YOUR OTP CODE", width - 2) + Colors.BOLD_GREEN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "╠" + "═".repeat(width - 2) + "╣" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + Colors.RESET + "              " + Colors.BOLD_WHITE + Colors.BG_BLUE + "  " + otp + "  " + Colors.RESET + "              " + Colors.BOLD_GREEN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "╚" + "═".repeat(width - 2) + "╝" + Colors.RESET);
		System.out.println(Colors.DIM + "  ⏱  Valid for 30 seconds" + Colors.RESET);
		System.out.println();
	}

	public static void printKeyGenerated(String filename) {
		System.out.println();
		System.out.println(Colors.BOLD_GREEN + "╔════════════════════════════════════════════════╗" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + Colors.RESET + center("KEY GENERATED SUCCESSFULLY", 48) + Colors.BOLD_GREEN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "╠════════════════════════════════════════════════╣" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + Colors.RESET + "  🔑 " + Colors.BOLD_WHITE + "File saved:" + Colors.RESET + " " + Colors.CYAN + filename + " ".repeat(37 - filename.length() - 6) + Colors.BOLD_GREEN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + " ".repeat(48) + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + Colors.RESET + "  " + Colors.YELLOW + "Next steps:" + Colors.RESET + " ".repeat(35) + Colors.BOLD_GREEN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + Colors.RESET + "    • Generate QR code: " + Colors.CYAN + "./ft_otp -q " + filename + " ".repeat(51 - 23 - 12 - filename.length() - 4) + Colors.BOLD_GREEN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "║" + Colors.RESET + "    • Get OTP: " + Colors.CYAN + "./ft_otp -k " + filename + " ".repeat(51 - 14 - 12 - filename.length() - 4) + Colors.BOLD_GREEN + "║" + Colors.RESET);
		System.out.println(Colors.BOLD_GREEN + "╚════════════════════════════════════════════════╝" + Colors.RESET);
		System.out.println();
	}

	public static void printUsage() {
		System.out.println();
		System.out.println(Colors.BOLD_WHITE + "Usage:" + Colors.RESET);
		System.out.println("  " + Colors.CYAN + "./ft_otp" + Colors.RESET + " " + Colors.YELLOW + "-g" + Colors.RESET + " <hex_key_file>  " + Colors.DIM + "Generate encrypted key file" + Colors.RESET);
		System.out.println("  " + Colors.CYAN + "./ft_otp" + Colors.RESET + " " + Colors.YELLOW + "-k" + Colors.RESET + " <key_file>      " + Colors.DIM + "Generate current OTP code" + Colors.RESET);
		System.out.println("  " + Colors.CYAN + "./ft_otp" + Colors.RESET + " " + Colors.YELLOW + "-q" + Colors.RESET + " <key_file>      " + Colors.DIM + "Display QR code for mobile apps" + Colors.RESET);
		System.out.println();
		System.out.println(Colors.BOLD_WHITE + "Examples:" + Colors.RESET);
		System.out.println("  " + Colors.DIM + "./ft_otp -g key.txt" + Colors.RESET);
		System.out.println("  " + Colors.DIM + "./ft_otp -q ft_otp.key" + Colors.RESET);
		System.out.println("  " + Colors.DIM + "./ft_otp -k ft_otp.key" + Colors.RESET);
		System.out.println();
	}

	private static String center(String text, int width) {
		int padding = (width - text.length()) / 2;
		int rightPadding = width - text.length() - padding;
		return " ".repeat(padding) + text + " ".repeat(rightPadding);
	}
}
