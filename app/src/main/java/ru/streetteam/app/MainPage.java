package ru.streetteam.app;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import lombok.extern.slf4j.Slf4j;
import ru.streetteam.app.Chat.ChatActivity;
import ru.streetteam.app.Map.MapPage;
import ru.streetteam.app.Map.MarkersPage;

@Slf4j
public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_page);
        Intent intent = new Intent(MainPage.this, MapPage.class);
        startActivity(intent);
    }

    // Обработчик нажатия для перехода к активити профиля
    public void buttonClickProfile(View view) {
        log.info("The Profile button is pressed");
        Intent intent = new Intent(MainPage.this, MarkersPage.class);
        startActivity(intent);
    }

    // Обработчик нажатия для перехода к активити карты
    public void buttonClickMap(View view) {
        log.info("The Map button is pressed");
        Intent intent = new Intent(MainPage.this, MapPage.class);
        startActivity(intent);
    }

    // Обработчик нажатия для перехода к активити чата
    public void buttonClickChat(View view) {
        log.info("The Chat button is pressed");
        Intent intent = new Intent(MainPage.this, ChatActivity.class);
        startActivity(intent);

    }
}
