package pawkar.backend.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseStandard<T> {
    private boolean success;
    private String message;
    private T data;
    private String timestamp;
    private String path;
    private String error;
    private Integer status;

    public static <T> ApiResponseStandard<T> success(T data, String message) {
        return new ApiResponseStandard<>(
                true,
                message,
                data,
                LocalDateTime.now().toString(),
                null,
                null,
                null
        );
    }

    public static <T> ApiResponseStandard<T> success(String message) {
        return success(null, message);
    }

    public static <T> ApiResponseStandard<T> error(String message, String path, String error, Integer status) {
        return new ApiResponseStandard<>(
                false,
                message,
                null,
                LocalDateTime.now().toString(),
                path,
                error,
                status
        );
    }
}
