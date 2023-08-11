package Dcoding.Celebrem.domain.base;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_REFRESH_TOKEN(false, 2004, "토큰이 일치하지 않습니다"),
    UNAUTHORIZED(false, 2005, "로그인이 필요합니다."),
    BAD_REQUEST(false, 2006, "잘못된 접근입니다."),

    // member
    INVALID_MEMBER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    INVALID_CODE(false, 2011, "인증 코드가 올바르지 않습니다."),

    // [POST] /member
    POST_MEMBER_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_MEMBER_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_MEMBER_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_MEMBER_EMPTY_PASSWORD(false, 2018, "비밀번호는 영문과 숫자를 조합하여 8~16자리만 입력해주세요."),
    POST_MEMBER_EMPTY_NICKNAME(false, 2019, "닉네임을 입력해주세요."),

    // [PATCH]
    PATCH_USERS_EMPTY_CURRENT_PASSWORD(false, 2020, "현재 비밀번호를 입력해주세요."),
    PATCH_USERS_EMPTY_CHANGED_PASSWORD(false, 2021, "변경할 비밀번호를 입력해주세요."),

    // mail
    INVALID_EMAIL(false, 2030, "존재하지 않는 이메일입니다."),
    INVALID_CHAR_SET(false, 2031, "character set 형식이 잘못되었습니다."),
    FAIL_SEND(false, 2032, "이메일 전송에 실패했습니다."),
    EXPIRED_CODE(false, 2033, "만료된 인증 코드입니다."),

    // [POST] /project
    POST_PROJECT_EMPTY_LEADER(false, 2200, "해당 ID의 리더가 없습니다."),
    POST_PROJECT_EMPTY_REGIONTAG(false, 2210, "해당 ID의 지역태그가 없습니다."),
    POST_PROJECT_EMPTY_TECHTAG(false, 2220, "해당 ID의 기술태그가 없습니다."),
    PROJECT_APPLY_DENIED(false, 2230, "이미 참여중인 프로젝트입니다."),

    // tags
    INVALID_TAG(false, 2040, "올바르지 않은 태그명입니다."),


    // message
    NOT_FOUND_MESSAGE_ROOM(false, 2060, "대화방을 찾을 수 없습니다."),
    NOT_FOUND_MESSAGE(false, 2061, "메시지를 찾을 수 없습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /member
    DUPLICATED_EMAIL(false, 3011, "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(false, 3012, "이미 존재하는 닉네임입니다."),
    NOT_FOUND_USERS_ID(false,3014,"없는 아이디입니다."),
    INVALID_PASSWORD(false, 3015, "비밀번호가 다릅니다."),
    EXPIRED_USERS(false, 3016, "탈퇴한 회원입니다."),

  
    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
