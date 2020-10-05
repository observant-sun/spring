package kriuchkov.maksim.lesson4.rest;

import org.springframework.http.HttpStatus;

public class ResponseException extends RuntimeException {

    private HttpStatus status;

    private String reason;

    public ResponseException(HttpStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
