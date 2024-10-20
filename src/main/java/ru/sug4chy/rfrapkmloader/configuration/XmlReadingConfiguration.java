package ru.sug4chy.rfrapkmloader.configuration;

import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import ru.sug4chy.rfrapkmloader.model.xml.PlacemarkXmlModel;
import ru.sug4chy.rfrapkmloader.model.xml.PointXmlModel;

import java.util.Map;

@Configuration
public class XmlReadingConfiguration {

    @Value("classpath:${rfrap-kml-loader.kml-file-name}")
    private Resource kmlFile;

    @Bean
    public XStreamMarshaller marshaller() {
        XStreamMarshaller marshaller = new XStreamMarshaller();

        marshaller.setAliases(Map.of(
                "Placemark", PlacemarkXmlModel.class,
                "Point", PointXmlModel.class

        ));

        marshaller.getXStream().allowTypes(new Class[] {
                PlacemarkXmlModel.class,
                PointXmlModel.class
        });

        marshaller.getXStream().ignoreUnknownElements("Style");

        marshaller.getXStream().processAnnotations(PlacemarkXmlModel.class);
        marshaller.getXStream().processAnnotations(PointXmlModel.class);

        return marshaller;
    }

    @Bean
    public StaxEventItemReader<PlacemarkXmlModel> reader(XStreamMarshaller marshaller) {
        return new StaxEventItemReaderBuilder<PlacemarkXmlModel>()
                .name("placemarksReader")
                .resource(kmlFile)
                .addFragmentRootElements("Placemark")
                .unmarshaller(marshaller)
                .build();
    }
}