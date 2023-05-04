package chat;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

public class ChatClientState {

    public static void start(Mqtt5AsyncClient client) {
        var message = Mqtt5Publish.builder()
                .topic("/aichat/default")
                .payload("Der LeckerSchmecker-Client ist da".getBytes())
                .build();

        client.publish(message).whenComplete((e, f) -> System.out.println("Erfolg")).join();
    }

    public static void stop(Mqtt5AsyncClient client) {
        var message = Mqtt5Publish.builder()
                .topic("/aichat/clientstate")
                .payload("Der LeckerSchmecker-Client muss los".getBytes())
                .build();

        client.publish(message);
    }
}
