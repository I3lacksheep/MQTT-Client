package chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import util.Constants;

public class ChatClientState {

    public static void start(Mqtt5AsyncClient client) {
        var message = new Message("Der LeckerSchmecker-Client ist da", Constants.ID, "default");
        sendClientMessage(client, message);
    }

    public static void stop(Mqtt5AsyncClient client) {
        var message = new Message("Der LeckerSchmecker-Client muss los", Constants.ID, "clientstate");
        sendClientMessage(client, message);
    }

    private static void sendClientMessage(Mqtt5AsyncClient client, Message message) {
        var objectMapper = new ObjectMapper();

        Mqtt5Publish mqttMessage;
        try {
            mqttMessage = Mqtt5Publish.builder()
                    .topic("/aichat/default")
                    .payload(objectMapper.writeValueAsBytes(message))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        client.publish(mqttMessage);
    }
}
