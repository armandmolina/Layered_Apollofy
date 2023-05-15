package cat.tecnocampus.apollofy.application.exceptions;

public class ElementNotFoundInBBDD extends RuntimeException{
    public ElementNotFoundInBBDD(String message) {
        super(message + " not found in BBDD");
    }
}
