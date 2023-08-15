package Dcoding.Celebrem.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllAdvice {


    @ExceptionHandler({
            BadRequestException.class,
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(final Exception e) {
        log.debug("HandleBadRequest : {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({UnauthorizedException.class, JwtException.class})
    public ResponseEntity<Void> handleUnauthorized(final Exception e) {
        log.debug("UnauthorizedException : {}", e.getMessage());
        log.debug("Exception Stack Trace : \n{}", e.getStackTrace());
        return ResponseEntity.status(UNAUTHORIZED).build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final Exception e) {
        log.debug("NotFoundException : {}", e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(RuntimeException e) {
        log.debug("RuntimeException : {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage() + " 요청을 처리할 수 없습니다."));
    }
}
