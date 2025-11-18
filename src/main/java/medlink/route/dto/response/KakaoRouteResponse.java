package medlink.route.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoRouteResponse {

    private List<Route> routes;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private Summary summary;
        private List<Section> sections;
        private List<Guide> guides;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        private int distance;
        private int duration;
        private Point start;
        private Point end;
        private Point origin; // 카카오 모빌리티 API 응답에 포함될 수 있음
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Point {
        private double x;
        private double y;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Section {
        private int distance;
        private int duration;
        private List<Road> roads;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Road {
        private List<Double> vertexes; // [lng1, lat1, lng2, lat2, ...]
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Guide {
        private int distance;
        private int duration;
        private String instruction;
    }
}
