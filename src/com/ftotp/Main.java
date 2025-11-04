package com.ftotp;

import com.ftotp.core.FtOtp;
import com.ftotp.exception.UserException;

public class Main {
	public static void main(String[] args) {
		if (args.length != 2 || !("-g".equals(args[0]) || "-k".equals(args[0]))) {
			System.err.println("Usage: ./ft_otp -g <hex_key_file> | -k <ft_otp.key>");
			System.exit(1);
		}
		try {
			if ("-g".equals(args[0])) {
				FtOtp.generate(args[1]);
			} else {
				FtOtp.printOtp(args[1]);
			}
		} catch (UserException e) {
			System.err.println("./ft_otp: error: " + e.getMessage());
			System.exit(2);
		} catch (Exception e) {
			System.err.println("./ft_otp: unexpected error: " + e.getMessage());
			System.exit(3);
		}
	}
}
