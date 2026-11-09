import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UdpServer {
    public static void main(String[] args) throws IOException {
        try (DatagramSocket socket = new DatagramSocket(5000)) {
            System.out.println("Servidor UDP na porta 5000");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String message = new String(request.getData(), request.getOffset(), request.getLength(), StandardCharsets.UTF_8);
                System.out.println("Recebido: " + message);

                DatagramPacket response = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
                socket.send(response);
            }
        }
    }
}
