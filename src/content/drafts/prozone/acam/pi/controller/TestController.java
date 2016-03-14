package rs.prozone.acam.pi.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/test", method = GET)
	@ResponseStatus(value = OK)
	public String test() {
		return "yes";
	}
	
	@RequestMapping(value = "/test2", method = GET)
	@ResponseStatus(value = OK)
	public String test2() {
		return "second time yes !";
	}
}
