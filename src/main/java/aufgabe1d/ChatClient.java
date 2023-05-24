package aufgabe1d;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5WillPublish;
import util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class ChatClient {

    private final Mqtt5AsyncClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ChatClient() {

        var willMessage = new Message("Der LeckerSchmecker-Client unerwartet weg", Constants.ID, "default");
        Mqtt5WillPublish will;
        try {
            will = Mqtt5WillPublish.builder()
                    .topic("/aichat/default")
                    .payload(objectMapper.writeValueAsBytes(willMessage))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        client = Mqtt5Client.builder()
                .identifier(Constants.ID) // use a unique identifier
                .serverHost(Constants.HOST)
                .serverPort(Constants.PORT) // this is the port of your cluster, for mqtt it is the default port 8883
                .willPublish(will)
                .buildAsync();

        client.connect().join();

        ChatClientState.start(client);

        client.toAsync().subscribeWith()
                .addSubscription()
                .topicFilter("/aichat/default")
                .noLocal(true)
                .applySubscription()
                .callback(this::messageReceived)
                .send();
    }

    public void start(){

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (true) {
            try {
                input = in.readLine();
                if(Objects.equals(input, "!quit")) {
                    ChatClientState.stop(client);
                    client.disconnect().join();
                    System.exit(0);
                }

                var message = new Message(input, Constants.ID, "default");

                var mqttMessage = Mqtt5Publish.builder()
                        .topic("/aichat/default")
                        .payload(objectMapper.writeValueAsBytes(message))
                        .build();

                client.publish(mqttMessage).join();
            } catch(IOException e){
                System.out.println("Fehler beim Einlesen der Kommandozeile");
            }
        }
    }

     private void messageReceived(Mqtt5Publish publish)  {
        var objectMapper = new ObjectMapper();

        try {
            Message data = objectMapper.readValue(publish.getPayloadAsBytes(), Message.class);
            System.out.println(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
