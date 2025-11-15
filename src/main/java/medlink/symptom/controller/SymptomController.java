package medlink.symptom.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;
import medlink.symptom.dto.SymptomResponse;
import medlink.symptom.service.SymptomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/symptoms")
public class SymptomController {

  private final SymptomService symptomService;

  @GetMapping
  public ApiResponse<List<SymptomResponse>> getSymptoms(HttpServletRequest req) {

    String uuid = AuthSessionUtil.getUuid(req);
    if (uuid == null) throw new GlobalException(ErrorStatus.UNAUTHORIZED);

    return ApiResponse.onSuccess(symptomService.getSymptoms());
  }

}
