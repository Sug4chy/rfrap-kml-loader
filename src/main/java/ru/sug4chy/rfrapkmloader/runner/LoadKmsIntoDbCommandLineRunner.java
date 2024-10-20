package ru.sug4chy.rfrapkmloader.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.sug4chy.rfrapkmloader.entity.Road;
import ru.sug4chy.rfrapkmloader.model.xml.PlacemarkXmlModel;
import ru.sug4chy.rfrapkmloader.repository.RegionRepository;
import ru.sug4chy.rfrapkmloader.repository.RoadRepository;
import ru.sug4chy.rfrapkmloader.repository.VerifiedPointRepository;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoadKmsIntoDbCommandLineRunner implements CommandLineRunner {

    private static final Map<String, Road> ROAD_MAP = new HashMap<>();
    private final RoadRepository roadRepository;
    private final RegionRepository regionRepository;
    private final VerifiedPointRepository verifiedPointRepository;

    private final StaxEventItemReader<PlacemarkXmlModel> reader;

    @Override
    public void run(String... args) throws Exception {

        PlacemarkXmlModel placemarkXmlModel;
        reader.open(new ExecutionContext());
        while ((placemarkXmlModel = reader.read()) != null) {
            placemarkXmlModel.getPoint().setCoordinates();
            System.out.println(placemarkXmlModel);
        }

        reader.close();
    }

    private void initRoadMap() {
        var roads = roadRepository.findAll();
        for (Road road : roads) {
            switch (road.getRoadName()) {
                case "М-5 \"Урал\": Уфа - Челябинск":
                    ROAD_MAP.put("М-5 \"Урал\"", road);
                    break;
                case "М-5 \"Урал\" ПкЕ: Челябинск - Екатеринбург":
                    ROAD_MAP.put("М-5 \"Урал \"Подъезд к г. Екатеринбургу\"", road);
                    break;
                case "А-310: Челябинск - Троицк":
                    ROAD_MAP.put("А-310", road);
                    break;
                case "Р-254 \"Иртыш\": Челябинск - Курган":
                    ROAD_MAP.put("Р-254", road);
                    break;
                case "Р-254 \"Иртыш\" ПкТ: Курган - Тюмень":
                    ROAD_MAP.put("Р-254, подъезд к г. Тюмень", road);
                    break;
                case "Р-354: Екатеринбург - Курган":
                    ROAD_MAP.put("Р-354", road);
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестное имя дороги в файле");
            }
        }
    }
}
