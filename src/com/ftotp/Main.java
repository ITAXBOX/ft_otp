package com.ftotp;

import com.ftotp.core.FtOtp;
import com.ftotp.exception.UserException;
import com.ftotp.ui.TerminalUI;

// This is a TOTP application main class
// It provides a command-line interface for generating key files and printing OTP codes
// similar to Google Authenticator or other 2FA apps
public class Main {
	public static void main(String[] args) {
		if (args.length != 2 || !("-g".equals(args[0]) || "-k".equals(args[0]) || "-q".equals(args[0]))) {
			TerminalUI.printBanner();
			TerminalUI.printUsage();
			System.exit(1);
		}
		try {
			if ("-g".equals(args[0]))
				FtOtp.generate(args[1]);
			else if ("-k".equals(args[0]))
				FtOtp.printOtp(args[1]);
			else
				FtOtp.displayQRCode(args[1]);
		} catch (UserException e) {
			TerminalUI.printError(e.getMessage());
			System.exit(2);
		} catch (Exception e) {
			TerminalUI.printError("unexpected error: " + e.getMessage());
			System.exit(3);
		}
	}
}
