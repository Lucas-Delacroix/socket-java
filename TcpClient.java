import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TcpClient {
    public static void main(String[] args) throws IOException {
        try (Scanner keyboard = new Scanner(System.in, StandardCharsets.UTF_8);
             Socket socket = new Socket("localhost", 5000);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {
            System.out.print("Mensagem: ");
            String message = keyboard.nextLine();
            output.println(message);
            System.out.println("Resposta: " + input.readLine());
        }
    }
}
