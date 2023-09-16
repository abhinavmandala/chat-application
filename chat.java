import java.io.*;
import java.net.*;

public class chat {
    static final String MESSAGE_TO_TRANSFER = "transfer";
    static final int SIZE_OF_BUFFER = 1024;

    static void send_the_file(String newFilename, DataOutputStream outStream) throws IOException {
        File file = new File(newFilename);
        if (!file.exists()) {
            System.out.println("File not found!!");
            return;
        }

        // Send transfer message with filename
        outStream.writeUTF(MESSAGE_TO_TRANSFER + " " + newFilename);

        // Open file and send contents
        try (FileInputStream fileInStream = new FileInputStream(file)) {
            long length = file.length();
            outStream.writeLong(length);

            byte[] buffer = new byte[SIZE_OF_BUFFER];
            int bytesRead;

            while ((bytesRead = fileInStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
        System.out.println("File sent: " + newFilename);
    }

    static void receive_the_file(String newFilename, DataInputStream inStream) throws IOException {
        long fileSize = inStream.readLong();
        if (fileSize == 0) {
            System.out.println("File not found!!");
            return;
        }

        try (FileOutputStream fileOutStream = new FileOutputStream("new" + newFilename)) {
            byte[] buffer = new byte[SIZE_OF_BUFFER];
            int bytesReceived;
            long totalBytesReceived = 0;

            while (totalBytesReceived < fileSize && (bytesReceived = inStream.read(buffer, 0, SIZE_OF_BUFFER)) > 0) {
                fileOutStream.write(buffer, 0, bytesReceived);
                totalBytesReceived += bytesReceived;
            }

            System.out.println("File received: " + newFilename);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader inputConsole = new BufferedReader(new InputStreamReader(System.in));

        // Create ServerSocket for listening
        ServerSocket serverSocket = new ServerSocket(0); // 0 means an available port will be used
        int portNumber = serverSocket.getLocalPort();
        System.out.println("Listening on port number " + portNumber);

        // Create writing thread
        Thread writingThread = new Thread(() -> {
            try {
                System.out.println("Enter the port number of user to connect to:");
                int port = Integer.parseInt(inputConsole.readLine());

                Socket newSocket = new Socket("localhost", port);
                System.out.println("Connected to port number " + port);

                DataOutputStream outStream = new DataOutputStream(newSocket.getOutputStream());
                String userName = "message";
                if (args.length > 0) {
                    userName = args[0];
                }
                outStream.writeUTF(userName);
                outStream.flush();

                while (true) {
                    String message = inputConsole.readLine();

                    // Check for transfer command and send file
                    if (message.startsWith(MESSAGE_TO_TRANSFER)) {
                        String newFilename = message.substring(MESSAGE_TO_TRANSFER.length() + 1);
                        send_the_file(newFilename, outStream);
                    } else {
                        outStream.writeUTF(message);
                        outStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writingThread.start();

        // Listen for incoming connections
        Socket newClientSocket = serverSocket.accept();

        try {
            DataInputStream inStream = new DataInputStream(newClientSocket.getInputStream());
            String peerUserName = inStream.readUTF();

            while (true) {
                String message = inStream.readUTF();

                // Check for transfer command and receive file
                if (message.startsWith(MESSAGE_TO_TRANSFER)) {
                    String newFilename = message.substring(MESSAGE_TO_TRANSFER.length() + 1);
                    receive_the_file(newFilename, inStream);
                } else {
                    System.out.println(peerUserName + ": " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}