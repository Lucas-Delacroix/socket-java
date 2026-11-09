import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";

        try (Socket socket = new Socket(host, 5000);
             BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            for (int i = 1; i <= 1000; i++) {
                output.write("Mensagem " + i);
                output.newLine();
            }
            output.flush();
            System.out.println("1000 linhas enviadas pela mesma conexão TCP");
        }
    }
}
