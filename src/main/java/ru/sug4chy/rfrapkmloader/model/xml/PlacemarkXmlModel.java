package ru.sug4chy.rfrapkmloader.model.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XStreamAlias("Placemark")
public class PlacemarkXmlModel {

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("description")
    private String description;

    @XStreamAlias("Point")
    private PointXmlModel point;
}
