package medlink.route.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoRouteRequest {
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;

    // car / foot
    private String type;
}
