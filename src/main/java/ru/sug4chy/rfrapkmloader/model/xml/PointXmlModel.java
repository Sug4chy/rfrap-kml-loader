package ru.sug4chy.rfrapkmloader.model.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;

@XStreamAlias("Point")
public class PointXmlModel {

    @Getter
    private double latitude;

    @Getter
    private double longitude;

    @XStreamAlias("coordinates")
    private String coordinates;

    public void writeCoordinates() {
        String[] coordinatesSplit = this.coordinates.split(",");

        this.longitude = Double.parseDouble(coordinatesSplit[0]);
        this.latitude = Double.parseDouble(coordinatesSplit[1]);
    }
}