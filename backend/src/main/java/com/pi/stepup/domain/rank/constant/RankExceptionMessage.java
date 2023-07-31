package com.pi.stepup.domain.rank.constant;

import javax.persistence.Table;

public enum RankExceptionMessage {
    UNAUTHORIZED_USER_ACCESS("접근 권한 없음"),
    POINT_POLICY_NOT_FOUND("포인트 정책 없음")
    ;

    String message;

    RankExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
