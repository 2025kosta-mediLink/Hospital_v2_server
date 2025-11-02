package medlink.auth.service;

import lombok.RequiredArgsConstructor;
import medlink.auth.dto.request.LoginRequest;
import medlink.auth.dto.request.SignUpRequest;
import medlink.auth.dto.response.MemberSessionResponse;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.member.repository.MemberRepository;
import medlink.member.entity.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean isLoginIdAvailable(String loginId) {
        return !memberRepository.existsByLoginIdAndDeleteAtIsNull(loginId);
    }

    @Transactional
    public void signUp(SignUpRequest req) {
        // 1) 아이디 중복 확인
        if (!isLoginIdAvailable(req.getLoginId())) {
            throw new GlobalException(ErrorStatus.DUPLICATE_LOGIN_ID);
        }

        // 2) 비번 인코드
        String encoded = passwordEncoder.encode(req.getPassword());

        // 3) 엔티티 생성
        Member member = Member.of(req, UUID.randomUUID().toString(), encoded);

        // 4) 저장
        memberRepository.save(member);
    }

    public MemberSessionResponse login(LoginRequest req) {
        Member member = memberRepository.findByLoginIdAndDeleteAtIsNull(req.getLoginId())
                .orElseThrow(() -> new GlobalException(ErrorStatus.INVALID_LOGIN_CREDENTIALS));

        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new GlobalException(ErrorStatus.INVALID_LOGIN_CREDENTIALS);
        }

        return MemberSessionResponse.of(member.getUuid());
    }
}