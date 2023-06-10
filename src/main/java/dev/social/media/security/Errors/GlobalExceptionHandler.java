package dev.social.media.security.Errors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final CustomErrorResponse problemDetail;

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // Handle HTTP request method not supported exception
        // Create and return a CustomErrorResponse object with appropriate error details


        // Handle constraint violation exception
        // Create and return a CustomErrorResponse object with constraint violation error details
        //The 400 Bad Request status code indicates that the server cannot process the request due to client error.
        //In the case of ConstraintViolationException, it typically occurs when the request violates the defined constraints or validations of the input parameters.

        //HttpRequestMethodNotSupportedException is typically 405 Method Not Allowed.
        //This exception occurs when the client sends a request with an HTTP method that is not supported by the server for the requested resource.
        problemDetail.getProblemDetail().setTitle("Method not allowed");
        problemDetail.getProblemDetail().setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
       // problemDetail.getProblemDetail().setType(request.getLocale());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Bad Request",
                ex.getMessage(),
                problemDetail.getProblemDetail()


        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Unsupported Media Type");
        problemDetail.getProblemDetail().setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
        //problemDetail.getProblemDetail().setType(URI.create(request.getRequestURI()));

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "Unsupported Media Type",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server Error");
        problemDetail.getProblemDetail().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
       // problemDetail.getProblemDetail().setType(URI.create(request.getRequestURI()));


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                problemDetail.getProblemDetail()


        );


        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Unsupported Media Type");
        problemDetail.getProblemDetail().setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
     //   problemDetail.getProblemDetail().setType(URI.create(request.getRequestURI()));

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "Unsupported Media Type",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server error");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
       // problemDetail.getProblemDetail().setType(URI.create(request.getRequestURI()));

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Internal Server error",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Bad Request");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
        // problemDetail.getProblemDetail().setType(URI.create(request.getRequestURI()));

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Bad Request",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server Error");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
        // problemDetail.getProblemDetail().setType(URI.create(request.getRequestURI()));

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Internal Server Error",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server Error");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
        // problemDetail.getProblemDetail().setType(URI.create(request.getRequestURI()));

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Internal Server Error",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Bad Request");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Bad Request",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Resource Not Found");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Resource Not Found",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Service Unavailable");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Service Unavailable",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(ErrorResponseException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server error");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());
        problemDetail.getProblemDetail().setDetail("this error can be between you accessing unauthorized resource or a bad request. This will be needed to be manually checked. ");


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Internal Server Error",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server error");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Internal Server error",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);

    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server error");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Internal Server error",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Bad Request");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Bad Request",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        problemDetail.getProblemDetail().setTitle("Internal Server error");
        problemDetail.getProblemDetail().setStatus(status.value());
        problemDetail.getProblemDetail().setDetail(ex.getMessage());


        CustomErrorResponse errorResponse = new CustomErrorResponse(
                status.value(),
                "Internal Server error",
                ex.getMessage(),
                problemDetail.getProblemDetail()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

}
