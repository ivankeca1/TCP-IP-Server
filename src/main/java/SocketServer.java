import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SocketServer {

    private ServerSocket server;
    public SocketServer(String ipAddress) throws Exception {
        if (ipAddress != null && !ipAddress.isEmpty())
            this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
        else
            this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
    }
    private void listen() throws Exception {
        String data = null;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);

        BufferedReader in = new BufferedReader(
          new InputStreamReader(client.getInputStream()));
        while ( (data = in.readLine()) != null ) {
            System.out.println("\r\nRequest for encryption/decryption key from: " + clientAddress);

            KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(128); /* 128-bit AES */
            SecretKey secret = gen.generateKey();
            byte[] binary = secret.getEncoded();
            String key = String.format("%032X", new BigInteger(+1, binary));
            System.out.println(key);
        }
    }
    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }
    public static void main(String[] args) throws Exception {
        SocketServer app = new SocketServer(args[0]);
        System.out.println("\r\nRunning Server: " +
                             "Host=" + app.getSocketAddress().getHostAddress() +
                             " Port=" + app.getPort());

        app.listen();
    }

}
