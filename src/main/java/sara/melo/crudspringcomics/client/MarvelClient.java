package sara.melo.crudspringcomics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sara.melo.crudspringcomics.models.MarvelComicResponse;

@FeignClient(name = "marvelClient", url = "https://gateway.marvel.com")
public interface MarvelClient {

	@RequestMapping(method = RequestMethod.GET, value = "/v1/public/comics/{comicId}?apikey={apikey}&hash={hash}&ts={timestamp}")
	MarvelComicResponse getComic(@PathVariable("comicId") Integer comicId, @PathVariable("apikey") String apikey, @PathVariable("hash") String hash, @PathVariable("timestamp") Long timestamp);
}
