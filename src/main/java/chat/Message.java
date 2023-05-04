package chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@lombok.Data
public class Message {
    @JsonProperty("sender")
    private String sender;

    @JsonProperty("text")
    private String text;

    @JsonProperty("clientID")
    private String clientID;

    @JsonProperty("topic")
    private String topic;

    @Override
    public String toString(){
        return "sender:"+sender+"\n"+
                "text:"+text+"\n"+
                "clientID:"+clientID+"\n"+
                "topic:"+topic+"\n";
    }
}
