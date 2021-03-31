package Service.Result;

/**
 * Load Result class with a boolean request and a message response
 */
public class LoadResult {
    boolean success;
    String message;
    /**
     * Load Result class with a boolean request and a message response
     * @param success boolean identifier: whether request was successful or not
     * @param message Success message or Description of the error
     */
    public LoadResult(boolean success, String message) {
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
}
