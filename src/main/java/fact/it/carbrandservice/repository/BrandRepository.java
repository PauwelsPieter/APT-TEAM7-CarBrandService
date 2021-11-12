package fact.it.carbrandservice.repository;

import fact.it.carbrandservice.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
    List<Brand> findBrandsByCountry(String country);
    Brand findBrandsById(String id);
}
