package Service.Result;

import java.util.Objects;

/**
 * Fill Result class with a boolean request and a message response
 */
public class FillResult {
    boolean success;
    String message;
    /**
     * Fill Result class with a boolean request and a message response
     * @param success boolean identifier: whether request was successful or not
     * @param message Success message or Description of the error
     */
    public FillResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillResult that = (FillResult) o;
        return isSuccess() == that.isSuccess() && Objects.equals(getMessage(), that.getMessage());
    }

}
