package ru.streetteam.app.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.streetteam.app.Map.MapPage;
import ru.streetteam.app.R;
import ru.streetteam.app.model.Message;
@Slf4j
public class ChatActivity extends AppCompatActivity implements RoomListener {

    private String roomName;
    private String roomTitle;
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private TextView chatName;
    private String channelId;
    private MemberData memberData;
    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        chatName = (TextView) findViewById(R.id.chat_name);
        messagesView.setAdapter(messageAdapter);

        memberData = new MemberData(getRandomName(), getRandomColor());

        Bundle chatInfo = getIntent().getExtras();
        channelId = (String) chatInfo.get("channelId");
        roomName = (String) chatInfo.get("roomName");
        roomTitle = (String) chatInfo.get("roomTitle");
        chatName.setText(roomTitle);
        scaledrone = new Scaledrone(channelId, memberData);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                log.info("Соединение с Scaledrone открыто");
                scaledrone.subscribe(roomName, ChatActivity.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                log.warn(ex.getMessage());
            }

            @Override
            public void onFailure(Exception ex) {
                log.warn(ex.getMessage());
            }

            @Override
            public void onClosed(String reason) {
                log.warn(reason);
            }
        });
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            scaledrone.publish(roomName, addDateToMsg(message));
            editText.getText().clear();
        }
    }

    private String addDateToMsg(String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("d.MM HH:mm  ");
        Date date = new Date(System.currentTimeMillis());
        date.setHours(date.getHours() + 3);
        return String.format("%s  %s  %s  %s",
                memberData.name,
                memberData.color,
                formatter.format(date),
                message);
    }

    @Override
    public void onOpen(Room room) {
        log.info("Подключение к комнате");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        log.warn(ex.getMessage());
    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
            boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
            final Message message = new Message(receivedMessage.getData().asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
           log.error(e.getMessage());
        }
    }

    private String getRandomName() {
        String[] adjs = {"Могучий", "Быстрый", "Ловкий", "Прыткий", "Сильный", "Опытный", "Непобедимый", "Неостановимый", "Спортивный", "Неудержимый", "Двужильный", "Знаменитый", "Талантливый", "Тренированный", "Настоящий", "Выдающийся"};
        String[] nouns = {"Спортсмен", "Бегун", "Прыгун", "Юниор", "Тяжелоатлет", "Шахматист", "Бык", "Новичок", "Отлыниватель", "Арбитр", "Судья", "Фанат", "Пловец", "Чемпион", "Фаворит", "Незнакомец", "Проходимец", "Спринтер"};
        return (
                adjs[(int) Math.floor(Math.random() * adjs.length)] +
                        " " + nouns[(int) Math.floor(Math.random() * nouns.length)]
        );
    }

    private String getRandomColor() {
        StringBuilder sb = new StringBuilder("#");
        while (sb.length() < 7) {
            sb.append(Integer.toHexString(random.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

    // Обработчик нажатия кнопки "назад"
    public void buttonClickBack(View view) {
        Intent intent = new Intent(ChatActivity.this, MapPage.class);
        startActivity(intent);

    }

    // Обработчик нажатия кнопки "История"
    public void buttonClickHistory(View view) {
        Intent intent = new Intent(ChatActivity.this, ChatHistory.class);
        Bundle chatInfo = new Bundle();
        chatInfo.putString("channelId", channelId);
        chatInfo.putString("roomName", roomName);
        chatInfo.putString("roomTitle", roomTitle);
        intent.putExtras(chatInfo);
        startActivity(intent);
    }


    @Data
    public static class MemberData {
        private String name;
        private String color;

        public MemberData(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public MemberData() {
        }
    }
}
