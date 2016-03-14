package rs.prozone.acam.pi.exception;

import org.springframework.validation.Errors;

public class AcasException extends RuntimeException {

    private static final long serialVersionUID = 8697242425468535939L;
    
    protected String errorCode;
    
    // ako je setovan ovaj objekat - radi se o validacionom exceptionu
    protected Errors errors;
    
    public AcasException(String errorCode){
        super("Exception thrown with code: " + errorCode);
        this.errorCode = errorCode;
    }
    
    public AcasException(Exception e, String errorCode){
        super(e);
        this.errorCode = errorCode;
    }
    
    public AcasException(Errors errors){
        super("Validation error on object: " + errors.getObjectName() + ". Errors: " + errors);
        // TODO ako treba setovati neki default error code
        this.errors = errors;
    }
    
    public String getErrorCode(){
        return this.errorCode;
    }
    
    public Errors getErrors(){
        return this.errors;
    }
}
