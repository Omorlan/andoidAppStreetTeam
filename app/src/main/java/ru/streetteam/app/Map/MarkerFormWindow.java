package ru.streetteam.app.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.Marker;

import lombok.AllArgsConstructor;
import ru.streetteam.app.Chat.ChatActivity;
import ru.streetteam.app.R;
import ru.streetteam.app.model.ChannelInfoMarker;

@AllArgsConstructor
public class MarkerFormWindow extends Fragment {

    private final ChannelInfoMarker channelInfoMarker;
    public final MapPage mapPage;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.info_window_form_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Marker marker = channelInfoMarker.getDefaultMarker();
        TextView locationTitle = getView().findViewById(R.id.markerTitle);
        locationTitle.setText(marker.getTitle());
        TextView locationType = getView().findViewById(R.id.markerType);
        locationType.setText(marker.getSnippet());

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //сделать переход на чат
                Intent intent = new Intent(mapPage, ChatActivity.class);
                Bundle chatInfo = new Bundle();
                chatInfo.putString("channelId", channelInfoMarker.getChannelId());
                chatInfo.putString("roomName", channelInfoMarker.getRoomName());
                chatInfo.putString("roomTitle", marker.getTitle());
                intent.putExtras(chatInfo);
                startActivity(intent);
            }
        };
        view.findViewById(R.id.goToChat).setOnClickListener(onClickListener);
    }
}
