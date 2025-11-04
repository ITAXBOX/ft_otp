package com.ftotp;

import com.ftotp.core.FtOtp;
import com.ftotp.exception.UserException;
import com.ftotp.util.Constants;

// This is a TOTP application main class
// It provides a command-line interface for generating key files and printing OTP codes
// similar to Google Authenticator or other 2FA apps
public class Main {
	public static void main(String[] args) {
		if (args.length != 2 || !("-g".equals(args[0]) || "-k".equals(args[0]))) {
			System.err.println(Constants.USAGE_MESSAGE);
			System.exit(1);
		}
		try {
			if ("-g".equals(args[0]))
				FtOtp.generate(args[1]);
			else
				FtOtp.printOtp(args[1]);
		} catch (UserException e) {
			System.err.println("./ft_otp: error: " + e.getMessage());
			System.exit(2);
		} catch (Exception e) {
			System.err.println("./ft_otp: unexpected error: " + e.getMessage());
			System.exit(3);
		}
	}
}
