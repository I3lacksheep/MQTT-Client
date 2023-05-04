import chat.ChatClient;
import wetter.Client;

public class Main {
    public static void main(String[] args) {
        var chatClient = new ChatClient();
        chatClient.start();

        var client = new Client();
    }
}
