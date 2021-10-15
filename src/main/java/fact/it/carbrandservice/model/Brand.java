package fact.it.carbrandservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brands")
public class Brand {
    @Id
    private String id;
    private String name;
    private String country;
    private String foundingYear;

    public Brand(String name, String country, String foundingYear) {
        this.name = name;
        this.country = country;
        this.foundingYear = foundingYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFoundingYear() {
        return foundingYear;
    }

    public void setFoundingYear(String foundingYear) {
        this.foundingYear = foundingYear;
    }
}
