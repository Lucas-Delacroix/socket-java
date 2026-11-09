import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UdpClient {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        try (Scanner keyboard = new Scanner(System.in, StandardCharsets.UTF_8);
             DatagramSocket socket = new DatagramSocket()) {
            System.out.print("Mensagem: ");
            String message = keyboard.nextLine();
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket request = new DatagramPacket(data, data.length, InetAddress.getByName(host), 5000);
            socket.send(request);

            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            System.out.println("Resposta: " + new String(response.getData(), response.getOffset(), response.getLength(), StandardCharsets.UTF_8));
        }
    }
}
