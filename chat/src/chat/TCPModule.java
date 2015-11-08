package chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPModule {
	public static void main(String[] args) throws IOException {
		TCPServer.createServer(4542);
		TCPClient.createClient("localhost", 4542);
	}
 }

class TCPServer {
	public static void createServer(int portNo) throws IOException {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(portNo);

		while (true) {
			System.out.println("Server .. Listening : ");
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Server ..... Received: " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
		}
	}
}

class TCPClient{
	public static void createClient(String serverIP, int portNO) throws UnknownHostException, IOException{
		String sentence;
		String modifiedSentence;
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", portNO);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		System.out.println("Read from console:");
		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence+"\n");
		modifiedSentence = inFromServer.readLine();
		System.out.println(" From Server: "+modifiedSentence);
		clientSocket.close();
	 }
  }