package ru.sug4chy.rfrapkmloader.spring.batch.processor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;
import ru.sug4chy.rfrapkmloader.entity.Region;
import ru.sug4chy.rfrapkmloader.entity.Road;
import ru.sug4chy.rfrapkmloader.entity.VerifiedPoint;
import ru.sug4chy.rfrapkmloader.model.xml.PlacemarkXmlModel;
import ru.sug4chy.rfrapkmloader.repository.RegionRepository;
import ru.sug4chy.rfrapkmloader.repository.RoadRepository;

import java.util.*;

@Component
@RequiredArgsConstructor
@AutoConfigureAfter(value = { RoadRepository.class, RegionRepository.class })
public class PlacemarkToVerifiedPointItemProcessor implements ItemProcessor<PlacemarkXmlModel, VerifiedPoint> {

    private static Map<String, Road> roads;
    private static List<Region> regions;

    private final RoadRepository roadRepository;
    private final RegionRepository regionRepository;

    private static Map<String, Road> initRoads(RoadRepository roadRepository) {
        var roads = roadRepository.findAll();
        var resultMap = new HashMap<String, Road>();

        for (Road road : roads) {
            switch (road.getRoadName()) {
                case "М-5 \"Урал\": Уфа - Челябинск":
                    resultMap.put("М-5 \"Урал\"", road);
                    resultMap.put("М-5 \" Урал\"", road);
                    resultMap.put("М-5 \"Урал\"<br/>", road);
                    break;
                case "М-5 \"Урал\" ПкЕ: Челябинск - Екатеринбург":
                    resultMap.put("М-5 \"Урал \"Подъезд к г. Екатеринбургу\"", road);
                    resultMap.put("М-5 \"Урал\" Подъезд к г. Екатеринбург", road);
                    resultMap.put("М-5 \"Урал \"Подъезд к . Екатеринбургу\"", road);
                    break;
                case "А-310: Челябинск - Троицк":
                    resultMap.put("А-310", road);
                    break;
                case "Р-254 \"Иртыш\": Челябинск - Курган":
                    resultMap.put("Р-254", road);
                    resultMap.put("Р-254<br/>", road);
                    break;
                case "Р-254 \"Иртыш\" ПкТ: Курган - Тюмень":
                    resultMap.put("Р-254, подъезд к г. Тюмень", road);
                    break;
                case "Р-354: Екатеринбург - Курган":
                    resultMap.put("Р-354", road);
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестное имя дороги в базе данных");
            }
        }

        return resultMap;
    }

    private static List<Region> initRegions(RegionRepository regionRepository) {
        var regions = regionRepository.findAll();
        var resultList = new ArrayList<Region>();
        regions.forEach(resultList::add);

        return resultList;
    }

    private static Region getRegionForVerifiedPoint(VerifiedPoint point) {
        if (point.getRoad().getRoadName().equals("Р-254 \"Иртыш\": Челябинск - Курган")) {
            int kilometerNumber = Integer.parseInt(
                    point.getPointName().split(" ")[0]
            );

            return kilometerNumber >= 56
                    ? getRegionByName("Челябинская область").orElseThrow()
                    : getRegionByName("Курганская область").orElseThrow();
        }

        if (point.getRoad().getRoadName().contains("Челябинск")) {
            return getRegionByName("Челябинская область").orElseThrow();
        } else {
            return getRegionByName("Курганская область").orElseThrow();
        }
    }

    private static Optional<Region> getRegionByName(String name) {
        return regions.stream()
                .filter(region -> region.getRegionName().equals(name))
                .findFirst();
    }

    @Override
    public VerifiedPoint process(@NotNull PlacemarkXmlModel item) {
        item.getPoint().writeCoordinates();
        item.setDescription(item.getDescription().trim());

        VerifiedPoint verifiedPoint = new VerifiedPoint();
        verifiedPoint.setId(UUID.randomUUID());
        verifiedPoint.setRoad(
                roads.get(item.getDescription())
        );
        if (verifiedPoint.getRoad() == null) {
            System.out.println(item.getDescription());
        }

        verifiedPoint.setPointName(item.getName());
        verifiedPoint.setLatitude(item.getPoint().getLatitude());
        verifiedPoint.setLongitude(item.getPoint().getLongitude());
        verifiedPoint.setRegion(
                getRegionForVerifiedPoint(verifiedPoint)
        );
        verifiedPoint.setDescription(null);

        return verifiedPoint;
    }

    @PostConstruct
    private void init() {
        roads = initRoads(this.roadRepository);
        regions = initRegions(this.regionRepository);
    }
}
