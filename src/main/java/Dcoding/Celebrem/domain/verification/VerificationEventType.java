package Dcoding.Celebrem.domain.verification;

import Dcoding.Celebrem.common.exception.UnauthorizedException;

public enum VerificationEventType {
    SUCCESS {
        @Override
        public void execute() {
        }
    },
    FAILURE {
        @Override
        public void execute() {
            throw new UnauthorizedException("인증번호가 올바르지 않습니다.");
        }
    },
    EXPIRED {
        @Override
        public void execute() {
            throw new UnauthorizedException("만료된 인증번호 입니다.");
        }
    };

    public abstract void execute();
}
