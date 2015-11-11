package chatSelector;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class Main {
	private static int THIS_PORTNO = 0;
	
	static List<HostDescription> hostListConnectedTo = null;
	static List<HostDescription> hostListConnectedFrom = null;

	public static void main(String[] args) throws UnknownHostException, IOException {

		if (args.length == 0) {
			System.out.println(" Invalild Arguments: Provide argument.");
			System.exit(1);
		} 
		else
			THIS_PORTNO = Integer.parseInt(args[0]);
			
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
				System.out.println(" ---> Selected: MyIP");
				System.out.println(myIP());
				break;
			case 2: // MyPort
				System.out.println(" ---> Selected: MyPort");
				System.out.println(myPort());
				break;
			case 3: // Connect
				System.out.println(" ---> Selected : Connect <destinationIP> <portno>");
				System.out.println("\t Destination IP (String): ");
				String connectToServerIP = scanner.next();
				System.out.println("\t Destionaton PortNo (Int):");
				int connectToDestinationPort = scanner.nextInt();
				
				//check if entered ip nd port are already in list
				
				SocketChannel clientSocket = TCPClient.connectServer(connectToServerIP, connectToDestinationPort);
				
				System.out.println("registered: "+clientSocket.isRegistered()); // false
        		System.out.println("local address : "+clientSocket.getLocalAddress().toString()); // ex. 127.0.0.1 4542
        		System.out.println(" port : "+clientSocket.socket().getPort()); // ex 52519
        		System.out.println("remote address : "+clientSocket.getRemoteAddress().toString()); // ex 127.0.0.1  52519
        		System.out.println("is connected: "+ clientSocket.isConnected()); // true
        		System.out.println("Accepted new connection from client: " + clientSocket);
        		System.out.println("inet add: "+ clientSocket.socket().getInetAddress().toString()); // ex. 127.0.0.1 
        		
				if(hostListConnectedTo == null){
					hostListConnectedTo = new ArrayList<HostDescription>();
				}
				hostListConnectedTo.add(new HostDescription(connectToServerIP, connectToDestinationPort, clientSocket));
				break;
			case 4: // List
				System.out.println(" ---> Selected: List\n");
				System.out.println("\t 'Connected To' List:-\n");

				if(hostListConnectedTo==null || hostListConnectedTo.size()==0){
					System.out.println("\t No server connections present..\n");
				}else{
					for (Iterator iterator = hostListConnectedTo.iterator(); iterator.hasNext();) {
						HostDescription hostDescription = (HostDescription) iterator.next();
						
						System.out.println("\t Host:"+hostDescription.getHostID()+"\t"+hostDescription.getIpAddress()+"\t"+hostDescription.getPortNo());
					}
				}
				
				//Connected from list
				System.out.println("\n\t 'Connected From' List:-\n");
				
				if(hostListConnectedFrom==null || hostListConnectedFrom.size()==0){
					System.out.println("\t No server connections present..\n");
				}else{
					for (Iterator iterator = hostListConnectedFrom.iterator(); iterator.hasNext();) {
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
				//check if entered connection id is present in list
				int flag=0;
				if(hostListConnectedTo!=null){
					System.out.println("in if");
					Iterator<HostDescription> it = hostListConnectedTo.iterator(); 
					while(it.hasNext())
					{System.out.println("in while");
						HostDescription host = it.next();
						if(host.getHostID() == connectionID)
						{	flag=1;
						System.out.println("in inner if");
							HostDescription  fetchedHostDesc = host;
							System.out.println("host desc=="+fetchedHostDesc.getHostID());
							TCPClient.send(fetchedHostDesc, "");
							break;
						}
						
					}			
					
				}
				if(flag==0)
					System.out.println("Invalid Connection Id! Please see the list of connections..");
				break; 
			
			case 6:// Terminate
				System.out.println("---> Selected : terminate <connection id>");
				System.out.println("\t ConnectionID (int): ");
				int terminateConnectionID = scanner.nextInt();
				// check if entered connection id exist in list
				if(hostListConnectedTo==null)
					System.out.println("Invalid Connection Id! 'Connected To' List is empty..");
				
				if(hostListConnectedTo!=null){
					int flag1=0;
					Iterator<HostDescription> it2 = hostListConnectedTo.iterator(); 
					while(it2.hasNext())
					{
						HostDescription host = it2.next();
						
						 if(host.getHostID() == terminateConnectionID )
						{
							//HostDescription  fetchedHostDesc = host;
							TCPClient.disconnectServer(host);
							it2.remove();
							flag1=1;
							break;
						}
						
					}
					if(flag1==0)
						System.out.println("Invalid Connection Id! Please see the 'Connected To' List..");
				}
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
		System.out.println("1) MyIp : displays the ip address of the host\n");
		System.out.println("2) MyPort : displays the port number of listening port\n");
		System.out.println("3) Connect <destination> <port no> : : Establishes new TCP connection to the specified destination IP address;Displays error message if connection fails\n");
		System.out.println("4) List : Displays list of all IP addresses and port numbers of all peers the process is connected\n");
		System.out.println("5) Terminate <connection id.> :stops the connection of the specified id;error messge appears if valid connection does not exist;message displays if remote machines terminates connection\n");
		System.out.println("6) Send<connection id.> <message>  : sends message to respective id specified in the list\n");
		System.out.println("7) Exit : closes all connections and terminate the process\n");

	}

	private static int myPort() {
		return THIS_PORTNO;
	}

	private static String myIP() {
		try {
			Inet4Address thisIP = (Inet4Address) Inet4Address.getLocalHost(); //method to display ip of host
			return thisIP.getHostAddress().toString();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}

