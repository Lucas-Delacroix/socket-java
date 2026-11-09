import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        boolean large = args.length > 1 && args[1].equals("grande");
        int count = large ? 1 : 1000;

        try (Socket socket = new Socket(host, 5000);
             BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            for (int i = 1; i <= count; i++) {
                String message = large ? "A".repeat(1500) : "Mensagem " + i;
                output.write(message);
                output.newLine();
            }
            output.flush();
            System.out.println("Linhas enviadas pela mesma conexão TCP: " + count);
        }
    }
}
