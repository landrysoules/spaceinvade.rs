package rs.prozone.acam.pi.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * The Class BaseController.
 *
 * @author Landry Soules
 */

@RestController
public abstract class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Inits the binder.
	 *
	 * @param binder
	 *            the binder
	 * @param request
	 *            the request
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder, final WebRequest request) {

		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");

		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, true));

	}
}
