package ru.streetteam.app.model;

import ru.streetteam.app.Chat.ChatActivity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private final String text;
    private final ChatActivity.MemberData memberData;
    private final boolean belongsToCurrentUser;
}
