package chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class TCPModule {

	public static int PORTNO = 4541;
	public static String SERVER_IP = "localhost";
	public static int SERVER_PORT = 4542;

	public static void main(String[] args) throws IOException, InterruptedException {

		if (args.length != 3) {
			System.out.println(" Invalild Arguments: Provide argument.");
			System.exit(1);
		} else {
			PORTNO = Integer.parseInt(args[0]);
			SERVER_IP = args[1];
			SERVER_PORT = Integer.parseInt(args[2]);
			// check portno is of length 4 and in range.
		}

		new ServerThread(PORTNO).start();
		// Thread.sleep(10000);
		TCPClient.createClient(SERVER_IP, SERVER_PORT);
	}
}

class TCPServer {
	

	public static void createServer(int portNo) throws IOException {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(portNo);

		while (true) {
			System.out.println("******Server .. Listening : ");
			Socket connectionSocket = welcomeSocket.accept(); // Blocking
			System.out.println("-----> Connection: "+connectionSocket.getPort()+"\t add: "+connectionSocket.getInetAddress());											// statement

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			do { // do while till terminate called.

				System.out.println("Server: Waiting for client to request..");
				clientSentence = inFromClient.readLine(); // Blocking statement

				System.out.println("Server ..... Received: " + clientSentence);
				if (clientSentence.equals("terminate")) {
					System.out.println("Server: Bye bye client...");
					outToClient.writeBytes(clientSentence);
				} else {
					capitalizedSentence = clientSentence.toUpperCase() + '\n';
					outToClient.writeBytes(capitalizedSentence);

				}
			} while (!clientSentence.equals("terminate"));

			connectionSocket.close();
		}
	}
}

class TCPClient {
	public static void createClient(String serverIP, int portNO) throws UnknownHostException, IOException {
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
	}

	public static Socket connectServer(String serverIP, int portNo) {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(serverIP, portNo);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clientSocket;
	}

	public static void send(Socket clientSocket) throws IOException {
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Client: Input from console:");
		String sentenceClientSending = inFromUser.readLine();

		outToServer.writeBytes(sentenceClientSending + "\n");
		/*String sentenceServerSent = inFromServer.readLine();
		System.out.println("Client:  From Server: " + sentenceServerSent);*/

	}

	public static void disconnectServer(Socket clientSocket) throws IOException {
		clientSocket.close();
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
