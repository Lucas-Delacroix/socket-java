import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Servidor TCP na porta 5000");

            while (true) {
                try (Socket client = server.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8))) {
                    System.out.println("Conectado: " + client.getRemoteSocketAddress());
                    String message;
                    while ((message = input.readLine()) != null) {
                        System.out.println("Recebido: " + message);
                    }
                    System.out.println("Conexão encerrada: " + client.getRemoteSocketAddress());
                }
            }
        }
    }
}
