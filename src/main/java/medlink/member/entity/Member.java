package medlink.member.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.auth.dto.request.SignUpRequest;
import medlink.common.base.BaseTimeEntity;
import medlink.member.enums.Gender;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 50)
    private String uuid;

    @Column(length = 50, nullable = false, unique = true)
    private String loginId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "CHAR(1)")
    private Gender gender;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 14, nullable = false)
    private String rrn;

    private LocalDateTime deleteAt;

    public static Member of(SignUpRequest req, String uuid, String encodedPassword) {
        return Member.builder()
                .uuid(uuid)
                .loginId(req.getLoginId())
                .password(encodedPassword)
                .name(req.getName())
                .phone(req.getPhone())
                .gender(Gender.valueOf(req.getGender()))
                .address(req.getAddress())
                .rrn(req.getRrn())
                .build();
    }
}
