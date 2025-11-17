-- ============================================
-- 🏥 HOSPITAL DATABASE - FULL INIT DATA (Part 1/5)
-- ============================================

USE hospital;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1) 회원 (30명)
-- ============================================
INSERT INTO member (uuid, login_id, password, name, phone, gender, address, rrn, created_at, updated_at) VALUES
('UUID-001', 'minji88', 'pw1234', '이민지', '010-2345-7811', 'F', '서울시 송파구 잠실동', '880512-2234567', NOW(), NOW()),
('UUID-002', 'kangjh', 'pw4567', '강지훈', '010-3421-9900', 'M', '서울시 강남구 역삼동', '900318-1234567', NOW(), NOW()),
('UUID-003', 'hanjy', 'pw8910', '한지영', '010-8822-7733', 'F', '경기도 성남시 분당구', '950120-2345678', NOW(), NOW()),
('UUID-004', 'parkdh', 'pw1111', '박동현', '010-6677-5599', 'M', '서울시 영등포구 여의도동', '870915-1239876', NOW(), NOW()),
('UUID-005', 'choiyr', 'pw1212', '최유라', '010-8787-2344', 'F', '서울시 마포구 서교동', '970621-2348765', NOW(), NOW()),
('UUID-006', 'songhm', 'pw1313', '송현민', '010-5522-3311', 'M', '서울시 노원구 공릉동', '920225-1233211', NOW(), NOW()),
('UUID-007', 'juhk', 'pw1414', '주하경', '010-7711-6622', 'F', '경기도 고양시 일산동구', '980904-2234455', NOW(), NOW()),
('UUID-008', 'baekh', 'pw1515', '백현우', '010-9955-8855', 'M', '서울시 강서구 화곡동', '940701-1345790', NOW(), NOW()),
('UUID-009', 'yoonse', 'pw1616', '윤서은', '010-4444-1111', 'F', '인천시 부평구 부개동', '010101-4234567', NOW(), NOW()),
('UUID-010', 'jangms', 'pw1717', '장민석', '010-6666-3434', 'M', '서울시 성북구 길음동', '890210-1334567', NOW(), NOW()),
('UUID-011', 'kimhj', 'pw1818', '김현지', '010-2222-9999', 'F', '서울시 도봉구 창동', '970215-2234567', NOW(), NOW()),
('UUID-012', 'leehj', 'pw1919', '이하준', '010-8888-2222', 'M', '서울시 강북구 수유동', '960215-1234567', NOW(), NOW()),
('UUID-013', 'moonhj', 'pw2020', '문하정', '010-6543-7654', 'F', '서울시 관악구 봉천동', '990615-2234567', NOW(), NOW()),
('UUID-014', 'jooyj', 'pw2121', '주유진', '010-5432-9999', 'F', '경기도 용인시 죽전동', '980915-2234567', NOW(), NOW()),
('UUID-015', 'parkjr', 'pw2222', '박진우', '010-9988-2233', 'M', '서울시 서초구 방배동', '940112-1334567', NOW(), NOW()),
('UUID-016', 'choyh', 'pw2323', '최윤호', '010-3333-1212', 'M', '서울시 은평구 응암동', '910320-1334567', NOW(), NOW()),
('UUID-017', 'songy', 'pw2424', '송유진', '010-9999-5555', 'F', '서울시 강동구 천호동', '990522-2234567', NOW(), NOW()),
('UUID-018', 'anjs', 'pw2525', '안지수', '010-8787-2222', 'F', '서울시 강서구 화곡동', '010310-4234567', NOW(), NOW()),
('UUID-019', 'ryuhj', 'pw2626', '류하진', '010-8888-9999', 'M', '서울시 구로구 고척동', '950122-1334567', NOW(), NOW()),
('UUID-020', 'kimms', 'pw2727', '김민성', '010-2222-3333', 'M', '서울시 성동구 행당동', '980111-1334567', NOW(), NOW()),
('UUID-021', 'hanmj', 'pw2828', '한민정', '010-2233-8899', 'F', '서울시 구로구 신도림동', '960821-2434567', NOW(), NOW()),
('UUID-022', 'leejy', 'pw2929', '이재연', '010-6655-4433', 'F', '경기도 성남시 수정구', '000430-4234567', NOW(), NOW()),
('UUID-023', 'parkys', 'pw3030', '박예슬', '010-8888-9999', 'F', '경기도 김포시 운양동', '970319-2234567', NOW(), NOW()),
('UUID-024', 'choigs', 'pw3131', '최건수', '010-5554-3321', 'M', '서울시 중랑구 면목동', '910310-1334567', NOW(), NOW()),
('UUID-025', 'jeonjw', 'pw3232', '전지원', '010-8889-1111', 'F', '서울시 서초구 반포동', '020411-4234567', NOW(), NOW()),
('UUID-026', 'baeksm', 'pw3333', '백수민', '010-9998-1234', 'F', '서울시 용산구 한남동', '970912-2234567', NOW(), NOW()),
('UUID-027', 'kanghr', 'pw3434', '강하람', '010-2222-6666', 'M', '서울시 강서구 등촌동', '900312-1334567', NOW(), NOW()),
('UUID-028', 'ohsj', 'pw3535', '오서준', '010-3333-2222', 'M', '서울시 마포구 망원동', '950201-1334567', NOW(), NOW()),
('UUID-029', 'jungy', 'pw3636', '정예림', '010-9876-1234', 'F', '서울시 은평구 불광동', '000101-4234567', NOW(), NOW()),
('UUID-030', 'jangsw', 'pw3737', '장서우', '010-6655-7766', 'M', '서울시 동작구 사당동', '980512-1334567', NOW(), NOW());

-- ============================================
-- 2) 진료과 (20개)
-- ============================================
INSERT INTO department (name, created_at, updated_at) VALUES
('내과', NOW(), NOW()), ('정형외과', NOW(), NOW()), ('피부과', NOW(), NOW()),
('이비인후과', NOW(), NOW()), ('안과', NOW(), NOW()), ('치과', NOW(), NOW()),
('소아청소년과', NOW(), NOW()), ('신경외과', NOW(), NOW()), ('산부인과', NOW(), NOW()),
('비뇨기과', NOW(), NOW()), ('신경과', NOW(), NOW()), ('정신건강의학과', NOW(), NOW()),
('재활의학과', NOW(), NOW()), ('흉부외과', NOW(), NOW()), ('응급의학과', NOW(), NOW()),
('마취통증의학과', NOW(), NOW()), ('영상의학과', NOW(), NOW()), ('진단검사의학과', NOW(), NOW()),
('가정의학과', NOW(), NOW()), ('소화기내과', NOW(), NOW());

-- ============================================
-- 3) 의사 (84명)
-- ============================================
INSERT INTO doctor (department_id, name, created_at, updated_at) VALUES
-- 내과 (5명)
(1, '김성훈', NOW(), NOW()), (1, '박지은', NOW(), NOW()), (1, '이승현', NOW(), NOW()),
(1, '정민호', NOW(), NOW()), (1, '윤하늘', NOW(), NOW()),
-- 정형외과 (4명)
(2, '이재혁', NOW(), NOW()), (2, '최우석', NOW(), NOW()), (2, '박지환', NOW(), NOW()), (2, '김도현', NOW(), NOW()),
-- 피부과 (4명)
(3, '박수현', NOW(), NOW()), (3, '정유진', NOW(), NOW()), (3, '이하늘', NOW(), NOW()), (3, '문예린', NOW(), NOW()),
-- 이비인후과 (4명)
(4, '최민아', NOW(), NOW()), (4, '김정환', NOW(), NOW()), (4, '류민재', NOW(), NOW()), (4, '서지훈', NOW(), NOW()),
-- 안과 (3명)
(5, '오지훈', NOW(), NOW()), (5, '강소연', NOW(), NOW()), (5, '배민아', NOW(), NOW()),
-- 치과 (4명)
(6, '정다혜', NOW(), NOW()), (6, '이도윤', NOW(), NOW()), (6, '김준영', NOW(), NOW()), (6, '박서진', NOW(), NOW()),
-- 소아청소년과 (4명)
(7, '유지호', NOW(), NOW()), (7, '홍은주', NOW(), NOW()), (7, '남수현', NOW(), NOW()), (7, '임도현', NOW(), NOW()),
-- 신경외과 (4명)
(8, '강서연', NOW(), NOW()), (8, '이한결', NOW(), NOW()), (8, '박재훈', NOW(), NOW()), (8, '최정윤', NOW(), NOW()),
-- 산부인과 (4명)
(9, '홍지은', NOW(), NOW()), (9, '서유림', NOW(), NOW()), (9, '윤소라', NOW(), NOW()), (9, '김선우', NOW(), NOW()),
-- 비뇨기과 (4명)
(10, '문정우', NOW(), NOW()), (10, '박도현', NOW(), NOW()), (10, '이태경', NOW(), NOW()), (10, '한채원', NOW(), NOW()),
-- 신경과 (4명)
(11, '정민수', NOW(), NOW()), (11, '오예진', NOW(), NOW()), (11, '김세현', NOW(), NOW()), (11, '박도하', NOW(), NOW()),
-- 정신건강의학과 (4명)
(12, '김유정', NOW(), NOW()), (12, '이수빈', NOW(), NOW()), (12, '정서윤', NOW(), NOW()), (12, '한지후', NOW(), NOW()),
-- 재활의학과 (3명)
(13, '박한솔', NOW(), NOW()), (13, '이도하', NOW(), NOW()), (13, '최재민', NOW(), NOW()),
-- 흉부외과 (3명)
(14, '정도현', NOW(), NOW()), (14, '이민성', NOW(), NOW()), (14, '박다빈', NOW(), NOW()),
-- 응급의학과 (4명)
(15, '이준혁', NOW(), NOW()), (15, '김지윤', NOW(), NOW()), (15, '박현우', NOW(), NOW()), (15, '정유림', NOW(), NOW()),
-- 마취통증의학과 (3명)
(16, '윤태호', NOW(), NOW()), (16, '김아현', NOW(), NOW()), (16, '박성우', NOW(), NOW()),
-- 영상의학과 (3명)
(17, '최하늘', NOW(), NOW()), (17, '김다연', NOW(), NOW()), (17, '박하준', NOW(), NOW()),
-- 진단검사의학과 (3명)
(18, '이소정', NOW(), NOW()), (18, '정예린', NOW(), NOW()), (18, '홍지원', NOW(), NOW()),
-- 가정의학과 (3명)
(19, '김민수', NOW(), NOW()), (19, '이은지', NOW(), NOW()), (19, '박진우', NOW(), NOW()),
-- 소화기내과 (3명)
(20, '최유진', NOW(), NOW()), (20, '윤지훈', NOW(), NOW()), (20, '이세린', NOW(), NOW());


-- ============================================
-- 🏥 HOSPITAL DATABASE - FULL INIT DATA (Part 2/5)
-- ============================================

-- ============================================
-- 4) 예약 (50건, 최근 1개월 내 무작위 생성)
-- ============================================
INSERT INTO reservation (member_id, doctor_id, reservation_no, 
  appointment_at, status, created_at, updated_at
)
SELECT 
  FLOOR(1 + RAND() * 30), -- 회원
  FLOOR(1 + RAND() * 84), -- 의사
  CONCAT('RSV', LPAD(FLOOR(1000 + RAND() * 9000), 4, '0')), -- 예약번호
  DATE_ADD(CURDATE(), INTERVAL FLOOR(RAND() * 30) DAY), -- 예약일
  ELT(FLOOR(1 + RAND() * 4), 'RESERVED', 'DONE', 'CANCELLED', 'NO_SHOW'), -- 상태
  NOW(), -- 생성시각
  NOW()  -- 수정시각
FROM (
  SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
  UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
  UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
  UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
  UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25
  UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30
  UNION SELECT 31 UNION SELECT 32 UNION SELECT 33 UNION SELECT 34 UNION SELECT 35
  UNION SELECT 36 UNION SELECT 37 UNION SELECT 38 UNION SELECT 39 UNION SELECT 40
  UNION SELECT 41 UNION SELECT 42 UNION SELECT 43 UNION SELECT 44 UNION SELECT 45
  UNION SELECT 46 UNION SELECT 47 UNION SELECT 48 UNION SELECT 49 UNION SELECT 50
) t;

-- ============================================
-- 5) 접수 (40건)
-- ============================================
INSERT INTO reception (reservation_id, member_id, doctor_id, reception_no, type, status, consent_notice, consent_at, note_to_doctor, created_at, updated_at)
SELECT 
  r.reservation_id,
  r.member_id,
  r.doctor_id,
  2000 + r.reservation_id,
  ELT(FLOOR(1 + RAND()*2), 'RESERVATION','DIRECT'),
  ELT(FLOOR(1 + RAND()*4), 'WAITING','IN_SERVICE','DONE','CANCELLED'),
  1, NOW(),
  CONCAT('증상 메모_', r.reservation_no),
  NOW(), NOW()
FROM reservation r
ORDER BY r.reservation_id
LIMIT 40;

-- ============================================
-- 6) 처방전 (DONE 상태의 접수만)
-- ============================================
INSERT INTO prescription (reception_id, doctor_id, issued_at, content, created_at, updated_at)
SELECT 
  r.reception_id,
  r.doctor_id,
  NOW(),
  CONCAT('처방 내용 - ', r.reception_id, '번 접수'),
  NOW(), NOW()
FROM reception r
WHERE r.status = 'DONE';


-- ============================================
-- 🏥 HOSPITAL DATABASE - FULL INIT DATA (Part 3/5)
-- ============================================


-- ============================================
-- 7) 약국별 처방전 (pharmacy_id NULL 허용, pharmacy_name 추가)
-- ============================================
INSERT INTO pharmacy_prescription (pharmacy_id, prescription_id, pharmacy_name, expected_finish_time, status, assigned_pharmacist, created_at, updated_at)
SELECT
NULL,  -- pharmacy_id는 NULL로 설정 (API에서 가져올 예정)
p.prescription_id,
CONCAT('약국_', FLOOR(1 + RAND()*10)),  -- API에서 선택한 약국이름
DATE_ADD(NOW(), INTERVAL FLOOR(RAND()*60) MINUTE),
ELT(FLOOR(1 + RAND()*4), 'START','IN_PROGRESS','READY','DONE'),  -- COMPLETED → DONE
CONCAT('약사_', FLOOR(1 + RAND()*30)),
NOW(), NOW()
FROM prescription p;


-- ============================================
-- 8) 수령 이력 (member_id 추가)
-- ============================================
INSERT INTO pickup_history (pharmacy_prescription_id, member_id, pickup_at, status, verified_by, created_at) 
SELECT 
  pp.pharmacy_prescription_id,
  r.member_id,  -- member_id 추가
  NOW(),
  ELT(FLOOR(1 + RAND()*2), 'PICKED_UP','COMPLETED'),  -- status 추가
  CONCAT('약사_', FLOOR(1 + RAND()*30)),
  NOW()
FROM pharmacy_prescription pp 
JOIN prescription p ON pp.prescription_id = p.prescription_id
JOIN reception r ON p.reception_id = r.reception_id
WHERE pp.status = 'DONE'  -- COMPLETED → DONE
LIMIT 5;


-- ============================================
-- 🏥 HOSPITAL DATABASE - FULL INIT DATA (Part 4/5)
-- ============================================

-- ============================================
-- 10) 증상 (10개)
-- ============================================
INSERT INTO symptom (name, created_at, updated_at) VALUES
('기침', NOW(), NOW()),
('두통', NOW(), NOW()),
('피부 트러블', NOW(), NOW()),
('눈 충혈', NOW(), NOW()),
('콧물', NOW(), NOW()),
('목통증', NOW(), NOW()),
('소화불량', NOW(), NOW()),
('생리불순', NOW(), NOW()),
('치통', NOW(), NOW()),
('허리통증', NOW(), NOW());

-- ============================================
-- 11) 접수-증상 관계 (15건)
-- ============================================
INSERT INTO reception_symptom (reception_id, symptom_id, created_at) VALUES
(1, 3, NOW()),
(2, 1, NOW()),
(3, 10, NOW()),
(4, 2, NOW()),
(5, 4, NOW()),
(6, 6, NOW()),
(7, 7, NOW()),
(8, 5, NOW()),
(9, 9, NOW()),
(10, 8, NOW()),
(11, 2, NOW()),
(12, 1, NOW()),
(13, 3, NOW()),
(14, 4, NOW()),
(15, 6, NOW());

-- ============================================
-- 12) 의사 예외일 (6건)
-- ============================================
INSERT INTO doctor_exception_day (doctor_id, exception_date, type, created_at, updated_at) VALUES
(3, '2025-10-15', '학회 참석', NOW(), NOW()),
(5, '2025-10-17', '휴가', NOW(), NOW()),
(8, '2025-10-20', '개인사정', NOW(), NOW()),
(15, '2025-10-25', '병원 행사', NOW(), NOW()),
(22, '2025-10-28', '학회 발표', NOW(), NOW()),
(35, '2025-11-01', '정기 휴무', NOW(), NOW());

-- ============================================
-- 13) 의사 공지사항 (6건)
-- ============================================
INSERT INTO doctor_notice (doctor_id, content, starts_at, ends_at, priority, created_at, updated_at) VALUES
(3, '10월 15일 학회 참석으로 오후 진료 휴진', '2025-10-13 00:00:00', '2025-10-15 23:59:59', 1, NOW(), NOW()),
(5, '10월 17일(금) 휴진 예정', '2025-10-10 00:00:00', '2025-10-17 23:59:59', 1, NOW(), NOW()),
(8, '10월 20일 개인 사정으로 오전 진료만 운영', '2025-10-18 00:00:00', '2025-10-20 23:59:59', 2, NOW(), NOW()),
(15, '10월 25일 병원 행사로 휴진', '2025-10-23 00:00:00', '2025-10-25 23:59:59', 1, NOW(), NOW()),
(22, '10월 28일 학회 발표 참석', '2025-10-27 00:00:00', '2025-10-28 23:59:59', 2, NOW(), NOW()),
(35, '11월 1일 정기 휴무 안내', '2025-10-30 00:00:00', '2025-11-01 23:59:59', 3, NOW(), NOW());


-- ============================================
-- 🏥 HOSPITAL DATABASE - FULL INIT DATA (Part 5/5)
-- ============================================

-- ============================================
-- 14) 대기 티켓 (40건)
-- ============================================
INSERT INTO waiting_ticket (reception_id, queue_no, status, estimated_wait_minutes, estimated_call_time, called_at, created_at, updated_at)
SELECT 
  r.reception_id,
  3000 + r.reception_id,
  ELT(FLOOR(1 + RAND()*5), 'WAITING','IN_SERVICE','DONE','CANCELLED','SKIPPED'),
  FLOOR(RAND()*20),
  DATE_ADD(NOW(), INTERVAL FLOOR(RAND()*15) MINUTE),
  CASE WHEN RAND() > 0.5 THEN NOW() ELSE NULL END,
  NOW(), NOW()
FROM reception r;

-- ============================================
-- 15) 의사 스케줄
-- ============================================
-- 1) 시드(원하면 바꿔서 다른 무늬 생성)
SET @seed := 20251016;

-- 2) CTE 없이 시퀀스 서브쿼리로 랜덤 생성 (월~토만)
INSERT INTO doctor_weekly_schedule (doctor_id, day_of_week, am_flag, pm_flag, created_at, updated_at)
SELECT
  doctor_id,
  day_of_week,
  am_flag,
  CASE WHEN am_flag=0 AND pm_base=0 THEN 1 ELSE pm_base END AS pm_flag,  -- 둘 다 0이면 오후를 1로 보정
  NOW(), NOW()
FROM (
  SELECT
    d.doctor_id,
    w.day_of_week,
    /* 오전 확률: 기본 70%, 목(4) 40% */
    CASE
      WHEN RAND(d.doctor_id*97 + w.day_of_week*13 + @seed) <
           (CASE WHEN w.day_of_week=4 THEN 0.40 ELSE 0.70 END)
      THEN 1 ELSE 0
    END AS am_flag,

    /* 오후 확률: 기본 60%, 수/토(3,6)=35%, 목(4)=25% */
    CASE
      WHEN w.day_of_week IN (3,6)
        THEN CASE WHEN RAND(d.doctor_id*131 + w.day_of_week*29 + @seed) < 0.35 THEN 1 ELSE 0 END
      WHEN w.day_of_week = 4
        THEN CASE WHEN RAND(d.doctor_id*131 + w.day_of_week*29 + @seed) < 0.25 THEN 1 ELSE 0 END
      ELSE CASE WHEN RAND(d.doctor_id*131 + w.day_of_week*29 + @seed) < 0.60 THEN 1 ELSE 0 END
    END AS pm_base
  FROM
    /* doctor_id 시퀀스 1~73 생성 (0..9 세트 3개로 조합) */
    (
      SELECT (a.n + b.n*10 + c.n*100) AS doctor_id
      FROM
        (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
         UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
      CROSS JOIN
        (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
         UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
      CROSS JOIN
        (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
         UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
    ) d
  JOIN
    (SELECT 1 AS day_of_week UNION ALL SELECT 2 UNION ALL SELECT 3
     UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) w
  WHERE d.doctor_id BETWEEN 1 AND 73
) x
ORDER BY doctor_id, day_of_week;
SET FOREIGN_KEY_CHECKS = 1;