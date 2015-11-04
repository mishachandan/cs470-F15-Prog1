package chat;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
	private static int PORTNO = 0;

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println(" Invalild Arguments: Provide argument.");
			System.exit(1);
		} else {
			PORTNO = Integer.parseInt(args[0]);
			// check portno is of length 4 and in range.
		}
		
		int option = 0;
		do {
			System.out.println(" ------------ Menu --------------");
			System.out.println("1. MyIp");
			System.out.println("2. MyPort");
			System.out.println("3. Help");
			System.out.println("4. Exit");

			Scanner scanner = new Scanner(System.in);
			System.out.print(" Choice: ");
			option = scanner.nextInt();

			switch (option) {
			case 1:
				System.out.println(myIP());
				break;
			case 2:
				System.out.println(myPort());
				break;
			case 3:
				// help();
				break;
			case 4:
				System.out.println(" Have a Nice Day !");
				System.exit(0);
				break;
			default:
				System.out.println(" Invalid option ..");
				break;
			}
		} while (option != 4);
	}

	private static int myPort() {
		return PORTNO;
	}

	private static String myIP() {
		try {
			Inet4Address thisIP = (Inet4Address) Inet4Address.getLocalHost();
			return thisIP.getHostAddress().toString();
			/*
			 * InetAddress thisIP = InetAddress.getLocalHost(); return
			 * thisIP.getHostAddress().toString();
			 */

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
