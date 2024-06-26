package ru.streetteam.app.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ru.streetteam.app.Database.DatabaseAdapter;
import ru.streetteam.app.Map.locations.PlacesManagement;
import ru.streetteam.app.R;
import ru.streetteam.app.model.Place;
@Slf4j
public class MarkersPage extends AppCompatActivity {

    private ListView userList;
    ArrayAdapter<Place> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dbpage);

        userList = (ListView) findViewById(R.id.list);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = arrayAdapter.getItem(position);
                if (place != null) {
                    Intent intent = new Intent(getApplicationContext(), PlacesManagement.class);
                    intent.putExtra("id", place.getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseAdapter adapter = new DatabaseAdapter(this);
        adapter.open();

        List<Place> places = adapter.getPlaces();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        userList.setAdapter(arrayAdapter);
        adapter.close();
    }

    // Обработчик нажатия кнопки "назад"
    public void buttonClickBack(View view) {
        log.info("Кнопка *Назад* нажата");
        Intent intent = new Intent(MarkersPage.this, MapPage.class);
        startActivity(intent);

    }

    // по нажатию на кнопку запускаем UserActivity для добавления данных
    public void add(View view) {
        log.info("Кнопка *Добавить маркер* нажата");
        Intent intent = new Intent(this, PlacesManagement.class);
        startActivity(intent);
    }
}