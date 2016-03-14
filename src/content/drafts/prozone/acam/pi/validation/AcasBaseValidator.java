package rs.prozone.acam.pi.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import rs.prozone.acam.pi.exception.AcasException;


public class AcasBaseValidator extends LocalValidatorFactoryBean implements Validator {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public void validate(Object baseBean) {
        validate(baseBean, getNewErrors(baseBean));
    }
    
    public void validate(Object baseBean, Errors errors) {
        super.validate(baseBean, errors);
        if (errors.hasErrors()){
            throw new AcasException(errors);
        }
    }
    
    public Errors getNewErrors(Object baseBean) {
        return new BeanPropertyBindingResult(baseBean, baseBean.getClass().getSimpleName());
    }
}
