import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpClient {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        InetAddress address = InetAddress.getByName(host);

        try (DatagramSocket socket = new DatagramSocket()) {
            for (int i = 1; i <= 1000; i++) {
                byte[] data = ("Mensagem " + i).getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, 5000);
                socket.send(packet);
            }
            System.out.println("1000 datagramas UDP enviados");
        }
    }
}
