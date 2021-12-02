package sara.melo.crudspringcomics.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sara.melo.crudspringcomics.client.MarvelClient;
import sara.melo.crudspringcomics.models.Comic;
import sara.melo.crudspringcomics.models.MarvelComicResponse;
import sara.melo.crudspringcomics.models.MarvelComicResponse.Results;
import sara.melo.crudspringcomics.models.User;
import sara.melo.crudspringcomics.repository.ComicRepository;
import sara.melo.crudspringcomics.repository.UserRepository;

@RestController
@RequestMapping(value = "/comics")
public class ComicController {
	
	private final String privateKey = "dcb478f14d37940e00ef6201be6ef56f11bfaa1b";
	private final String publicKey = "be8487544ad1f694b79fb77d414dd157";

	@Autowired
	private ComicRepository comicRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MarvelClient marvelClient;

	
	@GetMapping
	//Retorna uma lista de todos os quadrinhos
	public ResponseEntity<List<Comic>> getAll() {
		List<Comic> comics = new ArrayList<>(); // Cria uma lista
		comics = comicRepository.findAll(); // Busca todos os quadrinhos no banco de dados
		return new ResponseEntity<>(comics, HttpStatus.OK); // Retorna status 200 com a lista de todos quadrinhos no banco
	}
	
	@GetMapping(path="/{id}")
	//Retorna um unico quadrinho, de acordo com o id
	public ResponseEntity<Comic> getById(@PathVariable Integer id) {
		Optional<Comic> comic;
		
		try {
			
			comic = comicRepository.findById(id); // Busca o usuário no banco de dados
			
			if(!comic.isEmpty()) {
				return new ResponseEntity<Comic>(comic.get(), HttpStatus.OK); // Retorna status 200 com os dados do quadrinho em questão
			} else {
				return new ResponseEntity<Comic>(HttpStatus.NOT_FOUND); // // Não encontrou o quadrinho, retorna status 404;
			}						
			
			
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Comic>(HttpStatus.NOT_FOUND); // Não encontrou o quadrinho, retorna status 404;
		}			
	}

	@GetMapping(path="/byUser/{id}")
	//Retorna um unico quadrinho, de acordo com o id
	public ResponseEntity<Set<Comic>> getByUserId(@PathVariable Integer id) {
		Set<Comic> comics;

		try {

			comics = comicRepository.findAllByUserId(id);

			if(!comics.isEmpty()) {
				return new ResponseEntity(comics, HttpStatus.OK); // Retorna status 200 com os dados do quadrinho em questão
			} else {
				return new ResponseEntity(HttpStatus.NOT_FOUND); // // Não encontrou o quadrinho, retorna status 404;
			}


		} catch (NoSuchElementException nsee) {
			return new ResponseEntity(HttpStatus.NOT_FOUND); // Não encontrou o quadrinho, retorna status 404;
		}
	}

	@PostMapping
	public ResponseEntity save(@RequestBody Comic comic) {
		try {
			// REALIZA A CONSULTA NA API DA MARVEL, PASSANDO O comicId
			MarvelComicResponse comicResponse = this.getComicById(comic.getComicId());

			//Busca o primeiro resultado da busca
			Results comicEncontrado = comicResponse.getData().getResults().get(0);

			// No catalogo de quadrinhos, existem poucos quadrinhos com ISBN. Então verificamos se esse banco e vazio
			// Se for vazio, retorna uma exceção de conflito
			if(comicEncontrado.getIsbn().isEmpty() || comicEncontrado.getIsbn().isBlank()) {
				return ResponseEntity
			            .status(HttpStatus.CONFLICT)
			            .body("O Quadrinho com o comicId" + comic.getComicId() + " não tem ISBN.");
			} else {
				 // SE TIVER O ISBN, PEGAMOS AS INFORMAÇÕES DO QUADRINHO E SALVAMOS EM NOSSO BANCO DE DADOS

				Comic comicNew = new Comic();
				comicNew.setComicId(comicEncontrado.getId());
				comicNew.setAutores(comicEncontrado.getCreators().toString());
				comicNew.setISBN(comicEncontrado.getIsbn());
				comicNew.setTitulo(comicEncontrado.getTitle());
				comicNew.setPreco(comicEncontrado.getPrices().get(0).getPrice());

				comicRepository.save(comicNew); // Salva quadrinho no banco de dados
				return new ResponseEntity(comicNew, HttpStatus.CREATED);
			}

		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
	
	@DeleteMapping(path="/{id}")
	//Deleta um unico quadrinho, de acordo com o id
	public ResponseEntity<Comic> deleteById(@PathVariable Integer id) {
		try {
			comicRepository.deleteById(id); // Deleta o quadrinho no banco de dados
			return new ResponseEntity<>(HttpStatus.OK); // Retorna status 200 , quadrinho deletado com sucesso
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Comic>(HttpStatus.NOT_FOUND); // Não encontrou o quadrinho, retorna status 404;
		}			
	}
	
	@PutMapping(path="/{id}")
	//Atualiza os dados de um quadrinho; Se não encontrar, retorna erro 404
	public ResponseEntity<Comic> update(@PathVariable Integer id, @RequestBody @Valid Comic newComic) {			
		return comicRepository.findById(id).map(comic -> {
			comic.setComicId(newComic.getComicId());
			comic.setTitulo(newComic.getTitulo());
			comic.setPreco(newComic.getPreco());
			comic.setAutores(newComic.getAutores());
			comic.setISBN(newComic.getISBN());
			
			if(newComic.getDescricao() != null) {
				comic.setDescricao(newComic.getDescricao());
			}
			
			Comic comicUpdated = comicRepository.save(comic);
			return new ResponseEntity<>(comicUpdated, HttpStatus.OK);
		}).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping(path="/{comicId}/user/{userId}")
	//Vincula um quadrinho a um determinado usuário.
	public ResponseEntity linkComicToUser(@PathVariable Integer comicId, @PathVariable Integer userId) {

		if(comicRepository.findById(comicId).isEmpty()) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("O Quadrinho com o comicId "+ comicId + " não foi encontrado!");
		}

		if(userRepository.findById(userId).isEmpty()) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("O Usuário com o userId"+ userId + " não foi encontrado!");
		}

		try {
			Comic comic = comicRepository.findById(comicId).get();
			User user = userRepository.findById(userId).get();

			comic.getUsers().add(user);

			Comic comicUpdated = comicRepository.save(comic);

			return new ResponseEntity<>(comicUpdated, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(e.getMessage());
		}
	}
	
	private MarvelComicResponse getComicById(Integer comicId) {
		Long timestamp = System.currentTimeMillis();

		MarvelComicResponse comic = this.marvelClient.getComic(comicId, this.publicKey, this.MD5(timestamp + this.privateKey + this.publicKey), timestamp);
		return comic;
	}
	
	// GERA O MD5 HASH
	private String MD5(String md5) {
		   try {
		        MessageDigest md = MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (NoSuchAlgorithmException e) {
		    }
		    return null;
	}
}
