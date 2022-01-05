package fact.it.carbrandservice.controller;

import fact.it.carbrandservice.model.Brand;
import fact.it.carbrandservice.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class BrandController {
    @Autowired
    private BrandRepository brandRepository;

    @PostConstruct
    public void fillDB() {
        brandRepository.deleteAll();
        brandRepository.save(new Brand("1", "Honda", "Japan", "1948"));
        brandRepository.save(new Brand("2","Tesla", "USA", "2003"));
        brandRepository.save(new Brand("3","Dacia", "Romania", "1966"));
    }

    @GetMapping("/brands")
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    @GetMapping("/brands/country/{country}")
    public List<Brand> getBrandsByCountry(@PathVariable String country) {
        return brandRepository.findBrandsByCountry(country);
    }

    @PostMapping("/brands")
    public Brand addBrand(@RequestBody Brand brand) {
        brandRepository.save(brand);
        return brand;
    }

    @PutMapping("/brands")
    public Brand updateBrand(@RequestBody Brand updatedBrand) {
        Brand retrievedBrand = brandRepository.findBrandsById(updatedBrand.getId());

        if (retrievedBrand == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        retrievedBrand.setName(updatedBrand.getName());
        retrievedBrand.setCountry(updatedBrand.getCountry());
        retrievedBrand.setFoundingYear(updatedBrand.getFoundingYear());

        brandRepository.save(retrievedBrand);

        return retrievedBrand;
    }

    @DeleteMapping("/brands/{id}")
    public ResponseEntity deleteBrand(@PathVariable String id) {
        Brand brand = brandRepository.findBrandsById(id);
        if (brand != null) {
            brandRepository.delete(brand);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
