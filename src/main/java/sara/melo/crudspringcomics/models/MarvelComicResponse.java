package sara.melo.crudspringcomics.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MarvelComicResponse {
    public int code;
    public String status;
    public Data data;

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        public int total;

        ArrayList<Results> results = new ArrayList<Results>();
        public ArrayList<Results> getResults() {
            return results;
        }
        public void setResults(ArrayList<Results> results) {
            this.results = results;
        }
        public int getTotal() {
            return total;
        }
        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class Results {
        public int id;
        public String title;
        public String description;
        public String isbn;
        public String issn;
        public List<Price> prices;
        public Creators creators;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getIsbn() {
            return isbn;
        }
        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }
        public String getIssn() {
            return issn;
        }
        public void setIssn(String issn) {
            this.issn = issn;
        }
        public List<Price> getPrices() {
            return prices;
        }
        public void setPrices(List<Price> prices) {
            this.prices = prices;
        }
        public Creators getCreators() {
            return creators;
        }
        public void setCreators(Creators creators) {
            this.creators = creators;
        }

    }

    public static class Price {
        public String type;
        public double price;

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public double getPrice() {
            return price;
        }
        public void setPrice(double price) {
            this.price = price;
        }
    }

    public static class Item {
        public String name;
        public String role;
        public String type;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Creators {
        public int available;
        public String collectionURI;
        public List<Item> items;
        public int returned;

        public int getAvailable() {
            return available;
        }
        public void setAvailable(int available) {
            this.available = available;
        }
        public String getCollectionURI() {
            return collectionURI;
        }
        public void setCollectionURI(String collectionURI) {
            this.collectionURI = collectionURI;
        }
        public List<Item> getItems() {
            return items;
        }
        public void setItems(List<Item> items) {
            this.items = items;
        }
        public int getReturned() {
            return returned;
        }
        public void setReturned(int returned) {
            this.returned = returned;
        }

        @Override
        public String toString() {
            String creators = "";

            for (Item item : items) {
                creators += item.name + ", ";
            }

            return creators;
        }
    }
}
