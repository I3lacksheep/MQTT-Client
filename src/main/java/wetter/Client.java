package wetter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import util.Constants;
import wetter.Data;

import java.io.IOException;

public class Client {

    private final Mqtt5Client client;

    public Client() {
        client = getMQTTClient();
    }

    private Mqtt5Client getMQTTClient() {
        // 1. create the client
        final Mqtt5AsyncClient client = Mqtt5Client.builder()
                .identifier(Constants.ID) // use a unique identifier
                .serverHost(Constants.HOST)
                .serverPort(Constants.PORT) // this is the port of your cluster, for mqtt it is the default port 8883
                .buildAsync();

        client.connectWith()
                .cleanStart(false)
                .noSessionExpiry().send().join();

        // 3. subscribe and consume messages
        client.toAsync().subscribeWith()
                .addSubscription()
                .topicFilter("/weather/#")
                .noLocal(true)
                .applySubscription()
                .callback(this::messageReceived)
                .send();

        return client;
    }

    private void messageReceived(Mqtt5Publish publish)  {
        var objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        try {
            Data data = objectMapper.readValue(publish.getPayloadAsBytes(), Data.class);
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
