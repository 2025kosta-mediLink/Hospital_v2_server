package medlink.route.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.route.dto.request.KakaoRouteRequest;
import medlink.route.dto.response.KakaoRouteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoRouteService {

    @Value("${kakao.mobility.rest-api-key}")
    private String kakaoMobilityKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoRouteResponse getRoute(KakaoRouteRequest request) {
        log.info("경로 요청: {} -> {}, type: {}", 
            request.getStartLatitude() + "," + request.getStartLongitude(),
            request.getEndLatitude() + "," + request.getEndLongitude(),
            request.getType());

        String url = "https://apis-navi.kakaomobility.com/v1/directions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoMobilityKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("origin", request.getStartLongitude() + "," + request.getStartLatitude())
                .queryParam("destination", request.getEndLongitude() + "," + request.getEndLatitude());

        // 도보 경로의 경우 priority 파라미터를 사용하지 않거나 올바른 값 사용
        // 카카오 모빌리티 API의 priority 값: RECOMMEND (기본값), SHORTEST (최단거리), FASTEST (최단시간)
        // 도보 경로는 기본적으로 최단거리 경로를 반환하므로 priority를 생략하거나 SHORTEST 사용
        if (!"foot".equalsIgnoreCase(request.getType())) {
            // 자동차 경로인 경우에만 priority 설정
            builder.queryParam("priority", "RECOMMEND");
        }
        // 도보 경로는 priority 파라미터를 사용하지 않음

        String requestUrl = builder.toUriString();
        log.info("카카오 모빌리티 API 요청 URL: {}", requestUrl);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // HTTP 상태 코드 확인
            if (!rawResponse.getStatusCode().is2xxSuccessful()) {
                log.error("카카오 모빌리티 API HTTP 에러: {} - {}", rawResponse.getStatusCode(), rawResponse.getBody());
                throw new RuntimeException("카카오 모빌리티 API 호출 실패: " + rawResponse.getStatusCode() + " - " + rawResponse.getBody());
            }

            String responseBody = rawResponse.getBody();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                log.error("카카오 모빌리티 API 응답이 비어있습니다.");
                throw new RuntimeException("카카오 모빌리티 API 응답이 비어있습니다.");
            }

            log.info("카카오 모빌리티 API 원본 응답: {}", responseBody);

            // JSON을 직접 파싱하여 확인
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            KakaoRouteResponse response;
            try {
                response = mapper.readValue(responseBody, KakaoRouteResponse.class);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                log.error("JSON 파싱 실패. 원본 응답: {}", responseBody, e);
                throw new RuntimeException("카카오 모빌리티 API 응답 파싱 실패: " + e.getMessage(), e);
            }

            log.info("파싱된 응답 - routes 수: {}", response.getRoutes() != null ? response.getRoutes().size() : 0);
            if (response.getRoutes() != null && !response.getRoutes().isEmpty()) {
                var firstRoute = response.getRoutes().get(0);
                log.info("첫 번째 경로 - sections 수: {}", firstRoute.getSections() != null ? firstRoute.getSections().size() : 0);
                if (firstRoute.getSections() != null && !firstRoute.getSections().isEmpty()) {
                    var firstSection = firstRoute.getSections().get(0);
                    log.info("첫 번째 섹션 - roads 수: {}", firstSection.getRoads() != null ? firstSection.getRoads().size() : 0);
                    if (firstSection.getRoads() != null && !firstSection.getRoads().isEmpty()) {
                        var firstRoad = firstSection.getRoads().get(0);
                        log.info("첫 번째 도로 - vertexes 수: {}", firstRoad.getVertexes() != null ? firstRoad.getVertexes().size() : 0);
                    }
                }
            }

            return response;
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("카카오 모빌리티 API HTTP 클라이언트 에러: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("카카오 모빌리티 API 호출 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            log.error("카카오 모빌리티 API HTTP 서버 에러: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("카카오 모빌리티 API 서버 에러: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (org.springframework.web.client.RestClientException e) {
            log.error("카카오 모빌리티 API RestClient 에러", e);
            throw new RuntimeException("카카오 모빌리티 API 네트워크 에러: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("카카오 모빌리티 API 호출 실패", e);
            throw new RuntimeException("경로 조회 실패: " + e.getMessage(), e);
        }
    }
}
