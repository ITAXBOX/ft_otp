package com.ftotp.qr;

import com.ftotp.ui.Colors;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public final class TerminalQRRenderer {

	private TerminalQRRenderer() {

	}

	public static void render(String data, int size) throws WriterException {
		BitMatrix matrix = generateQRCodeMatrix(data, size);
		String qrCode = matrixToString(matrix);
		System.out.println(qrCode);
	}

	private static BitMatrix generateQRCodeMatrix(String data, int size) throws WriterException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 2);

		return qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size, hints);
	}

	private static String matrixToString(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		StringBuilder result = new StringBuilder();

		for (int y = 0; y < height; y += 2) {
			for (int x = 0; x < width; x++) {
				boolean topBlock = matrix.get(x, y);
				boolean bottomBlock = (y + 1 < height) && matrix.get(x, y + 1);

				if (topBlock && bottomBlock) {
					result.append("█");
				} else if (topBlock) {
					result.append("▀");
				} else if (bottomBlock) {
					result.append("▄");
				} else {
					result.append(" ");
				}
			}
			result.append("\n");
		}

		return result.toString();
	}

	public static void displayWithBorder(String data, String label, String issuer, int period, int size)
			throws WriterException {
		BitMatrix matrix = generateQRCodeMatrix(data, size);
		String qrCode = matrixToString(matrix);

		// Calculate box width based on QR code width
		String[] lines = qrCode.split("\n");
		int qrWidth = lines.length > 0 ? lines[0].length() : 0;
		int width = qrWidth + 8; // Add padding on both sides

		System.out.println();
		printLine(Colors.BOLD_CYAN + "╔", "═", "╗" + Colors.RESET, width);
		printCentered(Colors.BOLD_CYAN + "📲  SCAN QR CODE  📲  " + Colors.RESET, width + 11);
		printLine(Colors.BOLD_CYAN + "╠", "═", "╣" + Colors.RESET, width);
		System.out.println(Colors.BOLD_CYAN + "║" + Colors.RESET + center("", width - 2) + Colors.BOLD_CYAN + "║" + Colors.RESET);
		printCentered(Colors.CYAN + "             Use your authenticator app to scan:       " + Colors.RESET, width + 9);
		System.out.println(Colors.BOLD_CYAN + "║" + Colors.RESET + center("", width - 2) + Colors.BOLD_CYAN + "║" + Colors.RESET);

		for (String line : lines) {
			int padding = (width - 2 - line.length()) / 2;
			int rightPadding = width - 2 - padding - line.length();
			// Ensure no negative padding
			if (padding < 0) padding = 0;
			if (rightPadding < 0) rightPadding = 0;
			System.out.println(Colors.BOLD_CYAN + "║" + Colors.RESET + " ".repeat(padding) + Colors.BOLD_WHITE + line + Colors.RESET + " ".repeat(rightPadding) + Colors.BOLD_CYAN + "║" + Colors.RESET);
		}

		System.out.println(Colors.BOLD_CYAN + "║" + Colors.RESET + center("", width - 2) + Colors.BOLD_CYAN + "║" + Colors.RESET);
		printLine(Colors.BOLD_CYAN + "╠", "─", "╣" + Colors.RESET, width);
		printCentered(Colors.BOLD_YELLOW + "📱 Account Information" + Colors.RESET, width + 11);
		printLine(Colors.BOLD_CYAN + "╠", "─", "╣" + Colors.RESET, width);
		printCentered(Colors.GREEN + "👤 Account: " + Colors.WHITE + label + Colors.RESET, width + 14);
		printCentered(Colors.GREEN + "🏢 Issuer:  " + Colors.WHITE + issuer + Colors.RESET, width + 14);
		printCentered(Colors.GREEN + "🔐 Type:    " + Colors.WHITE + "TOTP (Time-based)" + Colors.RESET, width + 14);
		printCentered(Colors.GREEN + "⏱  Period:  " + Colors.WHITE + period + " seconds" + Colors.RESET, width + 14);
		System.out.println(Colors.BOLD_CYAN + "║" + Colors.RESET + center("", width - 2) + Colors.BOLD_CYAN + "║" + Colors.RESET);

		printLine(Colors.BOLD_CYAN + "╚", "═", "╝" + Colors.RESET, width);
		System.out.println();
	}

	private static void printLine(String left, String middle, String right, int width) {
		System.out.println(left + middle.repeat(width - 2) + right);
	}

	private static void printCentered(String text, int width) {
		System.out.println("║" + center(text, width - 2) + "║");
	}

	private static String center(String text, int width) {
		int padding = (width - text.length()) / 2;
		int rightPadding = width - text.length() - padding;
		return " ".repeat(padding) + text + " ".repeat(rightPadding);
	}
}
