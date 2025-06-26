package back.vybz.paymentservice.subscription.batch.policy;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class BillingPaymentPolicy {

    // 최대 실패 허용 횟수
    public static final int MAX_FAIL_COUNT = 3;

    // 재시도 간격 (일 단위)
    public static final int RETRY_INTERVAL_DAYS = 1;

    // 마지막 결제일(lastPaymentAt)을 기준으로 다음 결제일을 계산
    public static LocalDateTime calculateNextPaymentAt(LocalDateTime lastPaymentAt) {

        // 마지막 결제일에서 1개월 뒤의 YearMonth 객체를 생성함
        YearMonth nextMonth = YearMonth.from(lastPaymentAt.plusMonths(1));

        // 다음 달의 마지막 일자 (윤년 자동 반영됨)
        int lastDayOfMonth = nextMonth.lengthOfMonth();

        // 현재 날짜의 일(day)이 다음 달에도 존재하는지 체크
        int originalDay = lastPaymentAt.getDayOfMonth();
        int safeDay = Math.min(originalDay, lastDayOfMonth);

        return LocalDateTime.of(
                nextMonth.getYear(),
                nextMonth.getMonth(),
                safeDay,
                lastPaymentAt.getHour(),
                lastPaymentAt.getMinute()
        );
    }

    public static boolean isRetryExceeded (int failCount) {
        return failCount >= MAX_FAIL_COUNT;
    }

    public static LocalDateTime calculateNextRetryAt (LocalDateTime now) {
        return now.plusDays(RETRY_INTERVAL_DAYS);
    }
}
