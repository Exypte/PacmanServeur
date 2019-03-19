

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable{

	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;

	public ClientHandler(Socket clientSocket) {
		// TODO Auto-generated constructor stub
		try {
			this.clientSocket = clientSocket;
			/* Return l'input stream du socket */
			this.in = new DataInputStream(this.clientSocket.getInputStream());
			/* Return l'output stream du socket */
			this.out = new DataOutputStream(this.clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			/* Read la ligne de l'input stream */
			String action;
			try {
				action = this.in.readUTF();
				
				if(action.equals("START")) {
					this.out.writeUTF("START");
				}else if(action.equals("STEP")) {
					this.out.writeUTF("STEP");
				}else if(action.equals("RESTART")) {
					this.out.writeUTF("RESTART");
				}else if(action.equals("PAUSE")) {
					this.out.writeUTF("PAUSE");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
