package chat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static int THIS_PORTNO = 0;
	static List<HostDescription> hostList = null;

	public static void main(String[] args) throws UnknownHostException, IOException {

		if (args.length == 0) {
			System.out.println(" Invalild Arguments: Provide argument.");
			System.exit(1);
		} else {
			THIS_PORTNO = Integer.parseInt(args[0]);
			// check portno is of length 4 and in range.
		}

		new ServerThread(THIS_PORTNO).start();

		int option = 0;
		do {
			System.out.println(" ------------ Menu --------------");
			System.out.println("1. MyIp");
			System.out.println("2. MyPort");
			System.out.println("3. Connect");
			System.out.println("4. List");
			System.out.println("5. Send");
			System.out.println("6. Terminate");
			System.out.println("7. Exit");
			System.out.println("8. Help");
			System.out.println(" --------------------------------");

			Scanner scanner = new Scanner(System.in);
			System.out.print(" Choice: ");
			option = scanner.nextInt();

			switch (option) {
			case 1: // MyIp
				System.out.println(myIP());
				break;
			case 2: // MyPort
				System.out.println(myPort());
				break;
			case 3: // Connect
				System.out.println(" ---> Selected : Connect <destinationIP> <portno>");
				System.out.println("\t Destination IP (String): ");
				String connectToServerIP = scanner.next();
				System.out.println("\t Destionaton PortNo (Int):");
				int connectToDestinationPort = scanner.nextInt();
				
				Socket clientSocket = TCPClient.connectServer(connectToServerIP, connectToDestinationPort);
				if(hostList == null){
					hostList = new ArrayList<HostDescription>();
				}
				hostList.add(new HostDescription(connectToServerIP, connectToDestinationPort, clientSocket));
				break;
			case 4: // List
				System.out.println(" ---> Selected: List");

				if(hostList==null || hostList.size()==0){
					System.out.println(" No serversocket");
				}else{
					for (Iterator iterator = hostList.iterator(); iterator.hasNext();) {
						HostDescription hostDescription = (HostDescription) iterator.next();
						System.out.println("\t Host:"+hostDescription.getHostID()+"\t"+hostDescription.getIpAddress()+"\t"+hostDescription.getPortNo());
					}
				}
				break;
			case 5: // Send
				System.out.println(" ---> Selected : send <connection id> <message>");
				System.out.println("\t ConnectionID (int): ");
				int connectionID = scanner.nextInt();
//				System.out.println("\t Message (String):");
//				String message = scanner.next();
//				
				if(hostList!=null){
					HostDescription  fetchedHostDesc = hostList.get(connectionID);
					TCPClient.send(fetchedHostDesc.getSocket());
				}
				break; 
			case 6:// Terminate
				break;
			case 7: // Exit
				System.out.println(" Have a Nice Day !");
				System.exit(0);
				break;
			case 8: // Help
				help();
				break;
			default:
				System.out.println(" Invalid option ..");
				break;
			}
		} while (option != 7);
	}

	public static void help() {
		System.out.println("1)MyIp : displays the ip address of the host");
		System.out.println("2)MyPort : displays the port number of listening port");
		System.out.println("3)Connect <destination> <port no> : : Establishes new TCP connection to the specified destination IP address;Displays error message if connection fails");
		System.out.println("4)List : Displays list of all IP addresses and port numbers of all peers the process is connected ");
		System.out.println("5)Terminate <connection id.> :stops the connection of the specified id;error messge appears if valid connection does not exist;message displays if remote machines terminates connection");
		System.out.println("6)Send<connection id.> <message>  : sends message to respective id specified in the list");
		System.out.println("7)Exit : closes all connections and terminate the process");

	}

	private static int myPort() {
		return THIS_PORTNO;
	}

	private static String myIP() {
		try {
			Inet4Address thisIP = (Inet4Address) Inet4Address.getLocalHost(); //method to display ip of host
			return thisIP.getHostAddress().toString();
			/*
			 * InetAddress thisIP = InetAddress.getLocalHost(); return
			 * thisIP.getHostAddress().toString();
			 */

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}
