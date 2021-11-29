package sara.melo.crudspringcomics.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@GetMapping
	public String getHello() {
		return "Opa, estou no ar!!!";
	}
}
