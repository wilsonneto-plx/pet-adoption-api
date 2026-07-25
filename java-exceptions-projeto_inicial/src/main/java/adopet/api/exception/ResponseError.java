package adopet.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseError(String message,
                            HttpStatus httpStatus,

                            @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                            LocalDateTime time) {
}
