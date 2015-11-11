package chatSelector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TCPModule {

	
}

class TCPServer {
	
	// Get selector
    static Selector selector ;
    static ServerSocketChannel welcomeSocket;
	
	public static void createServer(int portNo) throws IOException {
		
		selector = Selector.open();

        System.out.println("Selector open: " + selector.isOpen());

        // Get server socket channel and register with selector
        welcomeSocket = ServerSocketChannel.open();
        
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", portNo);
        welcomeSocket.bind(hostAddress);
        
        welcomeSocket.configureBlocking(false);
        int ops = welcomeSocket.validOps();
        SelectionKey selectKy = welcomeSocket.register(selector, ops, null); // we have to set read here 
        
        
        for (;;) {

            System.out.println("Waiting for select...");
            int noOfKeys = selector.select(); // 

            System.out.println("Number of selected keys: " + noOfKeys);

            Set selectedKeys = selector.selectedKeys();
            Iterator iter = selectedKeys.iterator();

            while (iter.hasNext()) {
            	
                SelectionKey ky = (SelectionKey) iter.next();
              //  System.out.println("key :"+ky.toString()+" \t"+ky.channel().isOpen());
                
                if (ky.isAcceptable()) {
                	//System.out.println("ACCEPT");
                    // Accept the new client connection
                	System.out.println("------------Server Listening----------");
                    SocketChannel connectionSocket = welcomeSocket.accept();
                    connectionSocket.configureBlocking(false);
//
//                    // Add the new connection to the selector
                   SelectionKey clientkey = connectionSocket.register(selector, SelectionKey.OP_READ);
                  
            		System.out.println("registered: "+connectionSocket.isRegistered()); // false
            		System.out.println("local address : "+connectionSocket.getLocalAddress().toString()); // ex. 127.0.0.1 4542
            		System.out.println(" port : "+connectionSocket.socket().getPort()); // ex 52519
            		System.out.println("remote address : "+connectionSocket.getRemoteAddress().toString()); // ex 127.0.0.1  52519
            		System.out.println("is connected: "+ connectionSocket.isConnected()); // true
            		System.out.println("Accepted new connection from client: " + connectionSocket);
            		System.out.println("inet add: "+ connectionSocket.socket().getInetAddress().toString()); // ex. 127.0.0.1 
            		
            		if( Main.hostListConnectedFrom == null)
        				Main.hostListConnectedFrom = new ArrayList<HostDescription>();
        			
        			Main.hostListConnectedFrom.add(new HostDescription(connectionSocket.socket().getInetAddress().toString(), connectionSocket.socket().getPort(), connectionSocket));
                }
                
                else if (ky.isReadable()) {
                	System.out.println("READ");
                    // Read the data from client

                    SocketChannel client = (SocketChannel) ky.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String output = new String(buffer.array()).trim();
                    
                    System.out.println("from client: " + client.getRemoteAddress());
                    System.out.println("Message read from client: " + output);
	
                    if (output.equals("terminate")) {

                        client.close();
                        
                        
                        System.out.println("Client messages are complete; close.");
                    }

                } // end if (ky...)
                System.out.println("HERE");
                iter.remove();
               // iter.remove();

            } // end while loop

        } // end for loop
        
	}
}

class TCPClient {
	/*public static void createClient(String serverIP, int portNO) throws UnknownHostException, IOException {
		String sentence = "";
		String modifiedSentence;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		Socket clientSocket = new Socket(serverIP, portNO);

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		do {
			System.out.println("Client: Input from console:");
			sentence = inFromUser.readLine();

			outToServer.writeBytes(sentence + "\n");
			modifiedSentence = inFromServer.readLine();
			System.out.println("Client:  From Server: " + modifiedSentence);

		} while (!sentence.equals("terminate"));

		clientSocket.close();
		System.exit(0);
	}*/

	public static SocketChannel connectServer(String serverIP, int portNo) throws IOException {
//		Socket clientSocket = null;
//		try {
//			clientSocket = new Socket(serverIP, portNo);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		InetSocketAddress hostAddress = new InetSocketAddress(serverIP, portNo);
        SocketChannel client = SocketChannel.open(hostAddress);
		
		return client;
	}

	public static void send(HostDescription host1, String msgTerminate) throws IOException {
		SocketChannel clientSocket = host1.getSocket();
		
		System.out.println("here..............." + clientSocket);
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String clientMsg;
		if(msgTerminate ==  null || msgTerminate.equals(""))
		{
		System.out.println("\t Message (String):");
		clientMsg = inFromUser.readLine();
		}
		else
			clientMsg = msgTerminate;
		
		
		byte [] message = clientMsg.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);
		clientSocket.write(buffer);
		buffer.clear();
		
		if(clientMsg.equals("terminate"))
			clientSocket.close();
		
		//DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		// get the desired channel from from list using this passed channel
		//SocketChannel requiredChannel = clientSocket;
		
//		if(Main.hostListConnectedFrom == null)
//			System.out.println("list is null...............");
//		if( Main.hostListConnectedFrom != null)
//		{
//			Iterator<HostDescription> it = Main.hostListConnectedFrom.iterator(); 
//			while(it.hasNext())
//			{
//				HostDescription host = it.next();
//				
//				System.out.println("cli ==="+ clientSocket.getLocalAddress().toString());
//				System.out.println("host==="+ host.getSocket().getRemoteAddress().toString());
//				
//				if( clientSocket.getLocalAddress().toString().equals(host.getSocket().getRemoteAddress().toString()) )
//				{
//					requiredChannel = host.getSocket();
//					
//				}
//				
//			}		
//			
//		}
		
//		if(requiredChannel == null)
//			System.out.println("nullll...............");
		
//		if( Main.hostListConnectedFrom != null)
//		{
//			Iterator<HostDescription> it = Main.hostListConnectedFrom.iterator(); 
//			while(it.hasNext())
//			{
//				HostDescription host = it.next();
//				
//				if( (host.getSocket().socket().getInetAddress() == clientSocket.socket().getInetAddress()) &&
//						(host.getSocket().socket().getLocalPort() == clientSocket.socket().getLocalPort()) )
//				{
//					requiredChannel = host.getSocket();
//					
//				}
//				
//			}		
//			
//		}
		
		

        
		

		//outToServer.writeBytes(sentenceClientSending + "\n");
		//String sentenceServerSent = inFromServer.readLine();
		//System.out.println("Client:  From Server: " + sentenceServerSent);

	}

	
	public static void disconnectServer(HostDescription hostDesc) throws IOException {
		SocketChannel clientSocket = hostDesc.getSocket();
		
		send(hostDesc, "terminate");
//		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//		//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//		
//		//outToServer.writeBytes("Host:"+hostDesc.getHostID()+"\t"+hostDesc.getIpAddress()+ "\t"+hostDesc.getPortNo()+ "is terminated ...\n");
//		outToServer.writeBytes("terminate");
		
	}
}

class ServerThread extends Thread {
	private int portNO;

	public ServerThread(Object portNO) {
		super();
		this.portNO = (int) portNO;
	}

	@Override
	public void run() {
		try {
			TCPServer.createServer(portNO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
