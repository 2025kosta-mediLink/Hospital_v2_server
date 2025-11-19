package medlink.route.controller;

import lombok.RequiredArgsConstructor;
import medlink.route.dto.request.KakaoRouteRequest;
import medlink.route.dto.response.KakaoRouteResponse;
import medlink.route.service.KakaoRouteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoRouteController {

    private final KakaoRouteService kakaoRouteService;

    @PostMapping("/route")
    public KakaoRouteResponse getRoute(@RequestBody KakaoRouteRequest request) {
        return kakaoRouteService.getRoute(request);
    }
}
