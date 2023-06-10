package dev.social.media.security.Errors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;


@NoArgsConstructor

public class CustomErrorResponse implements ErrorResponse {
    private int statusCode;
    private  String statusMessage;
    private String errorMessage;


    private ProblemDetail problemDetail;

    public ProblemDetail getProblemDetail() {
        return problemDetail;
    }

    public CustomErrorResponse(int statusCode, String statusMessage, String errorMessage, ProblemDetail problemDetail) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.errorMessage = errorMessage;
        this.problemDetail = problemDetail;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(statusCode);
    }

    @Override
    public ProblemDetail getBody() {
        return problemDetail;
    }
}
