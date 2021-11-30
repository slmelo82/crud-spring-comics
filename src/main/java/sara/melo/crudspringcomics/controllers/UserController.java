package sara.melo.crudspringcomics.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sara.melo.crudspringcomics.models.User;
import sara.melo.crudspringcomics.repository.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	private UserRepository userRepository;
	
	public UserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@GetMapping
	//Retorna uma lista de todos os usuários
	public ResponseEntity<List<User>> getAll() {
		List<User> users = new ArrayList<>(); // Cria uma lista
		users = userRepository.findAll(); // Busca todos os usuários no banco de dados
		return new ResponseEntity<>(users, HttpStatus.OK); // Retorna status 200 com a lista de todos usuários no banco
	}
	
	@GetMapping(path="/{id}")
	//Retorna um unico usuário, de acordo com o id
	public ResponseEntity<Optional<User>> getById(@PathVariable Integer id) {
		Optional<User> user;
		
		try {
			user = userRepository.findById(id); // Busca o usuário no banco de dados
			return new ResponseEntity<Optional<User>>(user, HttpStatus.OK); // Retorna status 200 com os dados do usuario em questão
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_FOUND); // Não encontrou o usuário, retorna status 404;
		}			
	}	
	
	@PostMapping
	public ResponseEntity<User> save(@RequestBody @Valid User user) {
		userRepository.save(user); // Salva usuário no banco de dados
		return new ResponseEntity<>(user, HttpStatus.OK); // Retorna status 200 com os dados do usuário criado;
	}
	
	@DeleteMapping(path="/{id}")
	//Deleta um unico usuário, de acordo com o id
	public ResponseEntity<Optional<User>> deleteById(@PathVariable Integer id) {			
		try {
			userRepository.deleteById(id); // Deleta o usuário no banco de dados
			return new ResponseEntity<>(HttpStatus.OK); // Retorna status 200 , usuário deletado com sucesso
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_FOUND); // Não encontrou o usuário, retorna status 404;
		}			
	}
	
	@PutMapping(path="/{id}")
	//Atualiza os dados de um usuário; Se não encontrar, retorna erro 404
	public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody @Valid User newUser) {			
		return userRepository.findById(id).map(user -> {
			user.setNome(newUser.getNome());
			user.setEmail(newUser.getEmail());
			user.setNascimento(newUser.getNascimento());
			user.setCpf(newUser.getCpf());
			User userUpdated = userRepository.save(user);
			return new ResponseEntity<>(userUpdated, HttpStatus.OK);
		}).orElse(ResponseEntity.notFound().build());
	}

}
