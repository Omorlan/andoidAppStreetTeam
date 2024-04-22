package ru.streetteam.app.model;

import com.google.android.gms.maps.model.Marker;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChannelInfoMarker {

    private final Marker defaultMarker;
    private final String channelId;
    private final String roomName;
}
