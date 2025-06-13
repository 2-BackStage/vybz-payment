package back.vybz.paymentservice.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    /**
     * 200: 요청 성공
     **/
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공하였습니다."),
    EMAIL_CODE_SUCCESS(HttpStatus.OK, true, 201, "이메일 인증코드 발송에 성공하였습니다."),
    EMAIL_CODE_VERIFICATION_SUCCESS(HttpStatus.OK, true, 202, "이메일 인증에 성공하였습니다."),
    SIGN_UP_SUCCESS(HttpStatus.OK, true, 203, "회원가입에 성공하였습니다."),
    SIGN_IN_SUCCESS(HttpStatus.OK, true, 204, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, true, 205, "로그아웃 되었습니다."),
    SUCCESS_MATCH_PASSWORD(HttpStatus.OK, true, 206, "비밀번호가 확인되었습니다."),
    SUCCESS_UPDATE_PASSWORD(HttpStatus.OK, true, 207, "비밀번호가 변경되었습니다."),
    SUCCESS_UPDATE_NICKNAME(HttpStatus.OK, true, 208, "닉네임이 변경되었습니다."),
    SUCCESS_RECENT_VIEW(HttpStatus.OK, true, 209, "최근 본 상품이 등록되었습니다."),
    SUCCESS_WITHDRAWAL_USER(HttpStatus.OK, true, 210, "회원 탈퇴가 완료되었습니다. 2주안에 재 로그인 시 계정 복구가 가능합니다."),
    SUCCESS_ACCOUNT_RECOVERY(HttpStatus.OK, true, 211, "계정 복구가 완료되었습니다. 로그인 해주세요."),
    NO_OAUTH_USER(HttpStatus.OK, true, 2200, "소셜 계정이 존재하지 않습니다. 추가 정보를 입력해 회원가입 해주세요."),
    SUCCESS_DOWNLOAD_COUPON(HttpStatus.OK, true, 2201, "쿠폰 다운로드에 성공하였습니다."),
    SUCCESS_USE_COUPON(HttpStatus.OK, true, 2202, "쿠폰 사용에 성공하였습니다."),

    /**
     * 400 : security 에러
     */
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, false, 401, "토큰이 유효하지 않습니다"),
    NO_SIGN_IN(HttpStatus.UNAUTHORIZED, false, 402, "로그인을 먼저 진행해주세요"),
    NO_ACCESS_AUTHORITY(HttpStatus.FORBIDDEN, false, 403, "접근 권한이 없습니다"),
    DISABLED_USER(HttpStatus.FORBIDDEN, false, 404, "비활성화된 계정입니다. 계정을 복구하시겠습니까?"),
    FAILED_TO_RESTORE(HttpStatus.INTERNAL_SERVER_ERROR, false, 405, "계정 복구에 실패했습니다. 관리자에게 문의해주세요."),
    NO_EXIST_OAUTH(HttpStatus.NOT_FOUND, false, 406, "소셜 로그인 정보가 존재하지 않습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, false, 407, "이메일 또는 패스워드를 다시 확인해주세요."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, false, 408, "Refresh Token이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, false, 409, "Refresh Token이 만료되었습니다. 다시 로그인해주세요."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, false, 410, "유효하지 않은 Access Token입니다."),

    // payment
    PAYMENT_DUPLICATE_PAYMENT_UUID(HttpStatus.BAD_REQUEST, false, 7101, "이미 존재하는 주문번호입니다"),
    PAYMENT_NO_EXIST(HttpStatus.NOT_FOUND, false, 7102, "존재하지 않는 결제입니다"),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, false, 7104, "결제 금액이 일치하지 않습니다"),
    PAYMENT_ALREADY_DONE(HttpStatus.BAD_REQUEST, false, 7105, "이미 처리가 완료된 주문입니다"),
    TOSS_EMPTY_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, false, 7106, "TOSS 결제 승인 응답이 비어있습니다"),
    VIRTUAL_PAYMENT_FAIL(HttpStatus.BAD_REQUEST, false, 7107, "가상계좌 결제에 실패했습니다."),

    TOSS_PAYMENT_REJECTED(HttpStatus.BAD_REQUEST, false, 7108, "결제가 거절되었습니다. 사유를 확인해주세요."),
    TOSS_API_CALL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 7109, "결제 처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요."),

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, false, 7110, "결제를 찾을 수 없습니다."),
    PAYMENT_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, false, 7111, "이미 환불된 결제입니다."),
    PAYMENT_NOT_DONE(HttpStatus.BAD_REQUEST, false, 7112, "결제가 완료되지 않아 환불할 수 없습니다."),
    PAYMENT_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 7113, "결제 환불 처리에 실패했습니다."),
    PAYMENT_CANCEL_ALREADY_REQUESTED(HttpStatus.BAD_REQUEST, false, 7114, "이미 환불이 요청된 결제입니다."),


    /**
     * 900: 기타 에러
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 900, "Internal server error"),
    SSE_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 901, "알림 전송에 실패하였습니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 902, "로그인에 실패하였습니다."),


    /**
     * Request 유효성 에러
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, false, 1000, "잘못된 요청입니다.");

    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}