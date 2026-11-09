import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpClient {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        boolean large = args.length > 1 && args[1].equals("grande");
        int count = large ? 1 : 1000;
        InetAddress address = InetAddress.getByName(host);

        try (DatagramSocket socket = new DatagramSocket()) {
            for (int i = 1; i <= count; i++) {
                String message = large ? "A".repeat(1500) : "Mensagem " + i;
                byte[] data = message.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, 5000);
                socket.send(packet);
            }
            System.out.println("Datagramas UDP enviados: " + count);
        }
    }
}
