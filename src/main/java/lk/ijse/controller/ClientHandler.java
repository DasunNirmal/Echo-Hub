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
    private static ArrayList<DataOutputStream> clientOutputStreams;

    public ClientHandler(Socket socket, ArrayList<DataOutputStream> clientOutputStreams) throws IOException {
        this.socket = socket;
        this.clientOutputStreams = clientOutputStreams;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        clientOutputStreams.add(dataOutputStream);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = dataInputStream.readUTF();
                broadcastMessage(msg); // Broadcast the message to all clients
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

    public static void broadcastMessage(String message) throws IOException {
        for (DataOutputStream outputStream : clientOutputStreams) {
            outputStream.writeUTF(message);
            outputStream.flush();
        }
    }
}
