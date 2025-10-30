package medlink.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.session.cookie.name:JSESSIONID}")
    private String sessionCookieName;

    // 로그인/로그아웃 등 보안 제외(화이트리스트) 경로. 콤마로 구분해서 주입 가능.
    @Value("${swagger.auth.whitelist}")
    private String authWhitelistCsv;

    @Bean
    public OpenAPI openAPI() {
        Components components = new Components()
                .addSecuritySchemes("cookieAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)    // cookie 기반은 OAS에서 apiKey 타입을 사용
                        .in(SecurityScheme.In.COOKIE)        // 쿠키로 전달
                        .name(sessionCookieName));           // 쿠키명 (JSESSIONID 등)

        return new OpenAPI()
                .info(new Info().title("MediLink API").version("v2"))
                .components(components)
                // 기본적으로 모든 엔드포인트에 cookieAuth 요구
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/v2/**")
                // 화이트리스트 경로는 전역 보안 요구 제거
                .addOpenApiCustomizer(whitelistSecurityCustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer whitelistSecurityCustomizer() {
        final Set<String> whitelist = Arrays.stream(authWhitelistCsv.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());

        return openApi -> {
            Paths paths = openApi.getPaths();
            if (paths == null) return;

            whitelist.forEach(p -> {
                PathItem item = paths.get(p);
                if (item != null) {
                    item.readOperations().forEach(op -> op.setSecurity(new ArrayList<>())); // 보안 요구 제거
                }
            });
        };
    }
}