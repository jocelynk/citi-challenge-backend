package exception;

import service.DataService;

/**
 * Created by sasinda on 10/16/15.
 */
public class RESTException extends RuntimeException  {

    public String message;

    public RESTException(String message) {
        this.message=message;
    }

    @Override
    public String toString() {
        return DataService.asDocument(this).toJson();
    }
}
