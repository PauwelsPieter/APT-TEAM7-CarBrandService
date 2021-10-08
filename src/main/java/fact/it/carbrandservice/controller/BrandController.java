package fact.it.carbrandservice.controller;

import fact.it.carbrandservice.model.Brand;
import fact.it.carbrandservice.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class BrandController {
    @Autowired
    private BrandRepository brandRepository;

    @PostConstruct
    public void fillDB() {
        brandRepository.save(new Brand("Honda", "Japan", "1948"));
        brandRepository.save(new Brand("Tesla", "USA", "2003"));
        brandRepository.save(new Brand("Dacia", "Romania", "1966"));
    }

    @GetMapping("/")
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    @GetMapping("/brands/{country}")
    public List<Brand> getBrandsByCountry(@PathVariable String country) {
        return brandRepository.findBrandsByCountry(country);
    }
}
