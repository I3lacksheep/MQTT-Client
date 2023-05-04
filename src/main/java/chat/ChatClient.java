package chat;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    public ChatClient() {

        Mqtt5WillPublish will = Mqtt5WillPublish.builder()
                .topic("/aichat/clientstate")
                .payload("LeckerSchmecker unerwartet fort".getBytes())
                .build();

        // 1. create the client
        client = Mqtt5Client.builder()
                .identifier(Constants.ID) // use a unique identifier
                //.identifier( )
                .serverHost(Constants.CHAT_HOST)
                .serverPort(Constants.CHAT_PORT) // this is the port of your cluster, for mqtt it is the default port 8883
                .willPublish(will)
                .buildAsync();

        client.connect().join();

        ChatClientState.start(client);

        // 3. subscribe and consume messages
        client.toAsync().subscribeWith()
                .addSubscription()
                .topicFilter("/aichat/clientstate")
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
                    client.disconnect();
                    System.exit(0);
                }

                var message = Mqtt5Publish.builder()
                        .topic("/aichat/default")
                        .payload(input.getBytes())
                        .build();

                client.publish(message);
                /*
                client.toAsync().publishWith()
                        .topic("aichat/default")
                        // .qos(MqttQos.AT_LEAST_ONCE)
                        .payload(input.getBytes())
                        .send()
                        .whenComplete((publishResult, throwable) -> {
                            if (throwable == null) {
                                // Nachricht erfolgreich gepublisht
                            } else {
                                // Fehler beim Publishen
                            }
                        });

                 */
            }catch(IOException e){
                System.out.println("Fehler beim einlesen der Kommandozeile");
            }
        }
    }

     private void messageReceived(Mqtt5Publish publish)  {
        //System.out.println(publish);
        var objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        try {
            Message data = objectMapper.readValue(publish.getPayloadAsBytes(), Message.class);
            System.out.println(data.toString());
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
