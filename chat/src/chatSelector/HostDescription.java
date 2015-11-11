package chatSelector;

import java.net.Socket;
import java.nio.channels.SocketChannel;

public class HostDescription {
	
	public static int counter=1;
	private int hostID;
	private String ipAddress;
	private int portNo;
	private SocketChannel socket;
	
	public HostDescription(String ipAddress, int portNo, SocketChannel socket) {
		super();
		hostID = counter++;
		this.ipAddress = ipAddress;
		this.portNo = portNo;
		this.socket = socket;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPortNo() {
		return portNo;
	}
	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	public SocketChannel getSocket() {
		return socket;
	}

	public void setSocket(SocketChannel socket) {
		this.socket = socket;
	}
	
	
	public int getHostID() {
		return hostID;
	}

	public void setHostID(int hostID) {
		this.hostID = hostID;
	}

	@Override
	public boolean equals(Object obj) {
		HostDescription paramObj = (HostDescription) obj;
		
		if(paramObj.getHostID() == this.getHostID()){
			return true;
		}else{
			return false;
		}
	}
	
	
}
