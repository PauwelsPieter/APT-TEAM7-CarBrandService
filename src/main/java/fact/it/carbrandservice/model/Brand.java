package fact.it.carbrandservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
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
}
