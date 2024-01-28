package lk.ijse.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ArrayList<ClientHandler> clientHandlersList;

    public ClientHandler(Socket socket,ArrayList<ClientHandler> clientHandlersList) throws IOException {
        this.socket = socket;
        this.clientHandlersList = clientHandlersList;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = dataInputStream.readUTF();
                for (ClientHandler clientHandler : clientHandlersList) {
                    clientHandler.dataOutputStream.writeUTF(msg);
                    dataOutputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                dataInputStream.close();
                dataOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
