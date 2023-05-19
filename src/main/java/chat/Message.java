package chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import util.Constants;

@lombok.Data
@NoArgsConstructor(force = true)
public class Message {
    public Message(String text, String clientId, String topic) {
        this.text = text;
        this.clientId = clientId;
        this.topic = topic;
    }

    public Message() {}

    @JsonProperty("sender")
    private String sender = Constants.SENDER_NAME;

    @JsonProperty("text")
    private String text;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("topic")
    private String topic;

    @Override
    public String toString(){
        return topic + " " +
                sender + ": " +
                text;
    }
}
