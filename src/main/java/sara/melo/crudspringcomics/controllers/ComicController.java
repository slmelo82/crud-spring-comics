package sara.melo.crudspringcomics.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

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

@RestController
@RequestMapping(value = "/comics")
public class ComicController {
	
	private final String privateKey = "dcb478f14d37940e00ef6201be6ef56f11bfaa1b";
	private final String publicKey = "be8487544ad1f694b79fb77d414dd157";
	
	private ComicRepository comicRepository;	
	private MarvelClient cepService;
	
	public ComicController(ComicRepository comicRepository, MarvelClient cepService) {
		super();
		this.comicRepository = comicRepository;
		this.cepService = cepService;
	}
	
	
	@GetMapping
	//Retorna uma lista de todos os quadrinhos
	public ResponseEntity<List<Comic>> getAll() {
		List<Comic> comics = new ArrayList<>(); // Cria uma lista
		comics = comicRepository.findAll(); // Busca todos os quadrinhos no banco de dados
		return new ResponseEntity<>(comics, HttpStatus.OK); // Retorna status 200 com a lista de todos quadrinhos no banco
	}
	
	@GetMapping(path="/{id}")
	//Retorna um unico quadrinho, de acordo com o id
	public ResponseEntity<Optional<Comic>> getById(@PathVariable Integer id) {
		Optional<Comic> comic;
		
		try {
			
			comic = comicRepository.findById(id); // Busca o usuário no banco de dados
			
			if(comic.isEmpty()) {
				return new ResponseEntity<Optional<Comic>>(comic, HttpStatus.OK); // Retorna status 200 com os dados do quadrinho em questão
			} else {
				return new ResponseEntity<Optional<Comic>>(HttpStatus.NOT_FOUND); // // Não encontrou o quadrinho, retorna status 404;
			}						
			
			
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Comic>>(HttpStatus.NOT_FOUND); // Não encontrou o quadrinho, retorna status 404;
		}			
	}	
	
	@PostMapping
	public ResponseEntity save(@RequestBody Comic comic) {

		try {						
			MarvelComicResponse comicResponse = this.getComicById(comic.getComicId());
			
			Results comicEncontrado = comicResponse.getData().getResults().get(0);
			
			if(comicEncontrado.getIsbn().isEmpty() || comicEncontrado.getIsbn().isBlank()) {
				return ResponseEntity
			            .status(HttpStatus.CONFLICT) 		
			            .body("O Quadrinho com o comicId" + comic.getComicId() + " não tem ISBN.");
			} else {
				Comic comicNew = new Comic();			
				comicNew.setComicId(comicEncontrado.getId());
				comicNew.setAutores(comicEncontrado.getCreators().toString());
				comicNew.setISBN(comicEncontrado.getIsbn());
				comicNew.setTitulo(comicEncontrado.getTitle());
				comicNew.setPreco(comicEncontrado.getPrices().get(0).getPrice());
				
				System.out.println(comicNew.toString());
				
				comicRepository.save(comicNew); // Salva quadrinho no banco de dados			
				
				return new ResponseEntity(comicNew, HttpStatus.OK);
			}
			

		} catch (Exception e) {
			System.out.println(e);:
            .body(e.getMessage()); // Não encontrou o quadrinho, retorna status 404;
		}		

	}
	
	@DeleteMapping(path="/{id}")
	//Deleta um unico quadrinho, de acordo com o id
	public ResponseEntity<Optional<Comic>> deleteById(@PathVariable Integer id) {			
		try {
			comicRepository.deleteById(id); // Deleta o quadrinho no banco de dados
			return new ResponseEntity<>(HttpStatus.OK); // Retorna status 200 , quadrinho deletado com sucesso
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Comic>>(HttpStatus.NOT_FOUND); // Não encontrou o quadrinho, retorna status 404;
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
	
	private MarvelComicResponse getComicById(Integer id) {
		Long currentTime = System.currentTimeMillis();

		MarvelComicResponse comic = this.cepService.getComic(id, this.publicKey, this.MD5(currentTime + this.privateKey + this.publicKey), currentTime);
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
