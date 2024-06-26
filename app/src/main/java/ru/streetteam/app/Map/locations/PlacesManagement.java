package ru.streetteam.app.Map.locations;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import lombok.extern.slf4j.Slf4j;
import ru.streetteam.app.Database.DatabaseAdapter;
import ru.streetteam.app.Map.MarkersPage;
import ru.streetteam.app.R;
import ru.streetteam.app.model.Place;

@Slf4j
public class PlacesManagement extends AppCompatActivity {

    private EditText latBox;
    private EditText infoBox;
    private EditText labelBox;
    private EditText lonBox;
    private EditText chanId;
    private EditText roomName;
    private static final int DIALOG_EXIT = 1;
    private DatabaseAdapter adapter;
    private long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_adder);
        labelBox = (EditText) findViewById(R.id.label);
        infoBox = (EditText) findViewById(R.id.info);
        latBox = (EditText) findViewById(R.id.latitude);
        lonBox = (EditText) findViewById(R.id.longitude);
        chanId = (EditText) findViewById(R.id.channelId);
        roomName = (EditText) findViewById(R.id.roomName);
        Button delButton = (Button) findViewById(R.id.deleteButton);
        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        if (userId > 0) {
            // получаем элемент по id из бд
            adapter.open();
            Place place = adapter.getPlace(userId);
            labelBox.setText(place.getLabel());
            latBox.setText(String.valueOf(place.getLatitude()));
            lonBox.setText(String.valueOf(place.getLongitude()));
            chanId.setText(String.valueOf(place.getChannelId()));
            roomName.setText(String.valueOf(place.getRoomName()));
            infoBox.setText(String.valueOf(place.getInfo()));
            adapter.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view) {
        log.info("The *Save* button is pressed");
        String label = labelBox.getText().toString();
        if (checkFieldLenght(label, "Введите Название маркера!")) {
            return;
        }
        String info = infoBox.getText().toString();
        if (checkFieldLenght(info, "Введите Описание канала!")) {
            return;
        }
        String latitude = latBox.getText().toString();
        if (checkFieldLenght(latitude, "Введите Широту!")) {
            return;
        }
        String longitude = lonBox.getText().toString();
        if (checkFieldLenght(longitude, "Введите Долготу!")) {
            return;
        }
        String channelId = chanId.getText().toString();
        if (checkFieldLenght(channelId, "Введите ID комнаты!")) {
            return;
        }
        String roomNam = roomName.getText().toString();
        if (checkFieldLenght(roomNam, "Введите Имя комнаты!")) {
            return;
        }

        Place place = new Place(
                userId,
                label,
                info,
                Float.parseFloat(latitude),
                Float.parseFloat(longitude),
                channelId,
                roomNam);

        adapter.open();
        if (userId > 0) {
            adapter.update(place);
        } else {
            adapter.insert(place);
        }
        adapter.close();
        goHome();
    }

    private boolean checkFieldLenght(String field, String s) {
        if (field.equals("")) {
            Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast.getView();
            ImageView warnImageView = new ImageView(getApplicationContext());
            toastContainer.addView(warnImageView, 0);
            toast.show();
            return true;
        }
        return false;
    }


    public void delete(View view) {
        log.info("The *Delete* button is pressed");
        showDialog(DIALOG_EXIT);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Удаление маркера");
            // сообщение
            adb.setMessage("Вы уверены, что хотите удалить маркер?");
            // кнопка положительного ответа
            adb.setPositiveButton("Да", myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton("Нет", myClickListener);

            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case DialogInterface.BUTTON_POSITIVE:
                    adapter.open();
                    adapter.delete(userId);
                    adapter.close();
                    finish();
                    break;
                // негативная кнопка
                case DialogInterface.BUTTON_NEGATIVE:
                    finish();
                    break;
            }
        }
    };

    public void backToList(View view) {
        log.info("Кнопка *Назад* нажата");
        Intent intent = new Intent(PlacesManagement.this, MarkersPage.class);
        startActivity(intent);
    }

    private void goHome() {
        Intent intent = new Intent(this, MarkersPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}