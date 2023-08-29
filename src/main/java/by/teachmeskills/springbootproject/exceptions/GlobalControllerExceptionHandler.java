package by.teachmeskills.springbootproject.exceptions;

import by.teachmeskills.springbootproject.utils.ErrorPopulatorUtils;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private final Logger logger;

    @Autowired
    GlobalControllerExceptionHandler() {
        logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleConstraintsExceptions(Exception e) {
        return new ResponseEntity<>(ErrorPopulatorUtils.populateErrors(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, AuthorizationException.class, InsufficientFundsException.class, NoProductsInOrderException.class})
    public ResponseEntity<String> handleCustomExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}

