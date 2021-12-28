package fact.it.carbrandservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.carbrandservice.model.Brand;
import fact.it.carbrandservice.repository.BrandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class BrandControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BrandRepository brandRepository;

    private Brand brand1 = new Brand("1", "Brand1", "Country1", "0001");
    private Brand brand2 = new Brand("2", "Brand2", "Country2", "0002");
    private Brand brand3 = new Brand("3", "Brand3", "Country3", "0003");

    // Runs before each @Test annotated method
    @BeforeEach
    public void beforeAllTests() {
        brandRepository.deleteAll();
        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.save(brand3);
    }

    // Runs after each @Test annotated method
    @AfterEach
    public void afterAllTests() {
        brandRepository.deleteAll();
    }

    // ObjectMapper to transform objects to Json strings
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenBrand_whenGetAllBrands_thenReturnJsonBrands() throws Exception {
        mockMvc.perform(get("/brands"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name",is("Brand1")))
                .andExpect(jsonPath("$[0].country",is("Country1")))
                .andExpect(jsonPath("$[0].foundingYear",is("0001")))
                .andExpect(jsonPath("$[1].name",is("Brand2")))
                .andExpect(jsonPath("$[1].country",is("Country2")))
                .andExpect(jsonPath("$[1].foundingYear",is("0002")))
                .andExpect(jsonPath("$[2].name",is("Brand3")))
                .andExpect(jsonPath("$[2].country",is("Country3")))
                .andExpect(jsonPath("$[2].foundingYear",is("0003")));
    }

    @Test
    public void givenBrand_whenGetBrandByCountry_thenReturnJsonBrands() throws Exception {
        mockMvc.perform(get("/brands/country/{country}", "Country2"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("Brand2")))
                .andExpect(jsonPath("$[0].country",is("Country2")))
                .andExpect(jsonPath("$[0].foundingYear",is("0002")));
    }

    @Test
    public void givenBrand_whenPostBrand_thenReturnJsonBrand() throws Exception {
        Brand newBrand = new Brand("New Brand", "New Country", "0000");

        mockMvc.perform(post("/brands").content(mapper.writeValueAsString(newBrand)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newBrand.getName())))
                .andExpect(jsonPath("$.country", is(newBrand.getCountry())))
                .andExpect(jsonPath("$.foundingYear", is(newBrand.getFoundingYear())));
    }

    @Test
    public void givenBrand_whenPutBrand_thenReturnJsonBrand() throws Exception {
        Brand updateBrand = new Brand("1","Updated Brand", "Updated Country", "1111");

        mockMvc.perform(put("/brands").content(mapper.writeValueAsString(updateBrand)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updateBrand.getName())))
                .andExpect(jsonPath("$.country", is(updateBrand.getCountry())))
                .andExpect(jsonPath("$.foundingYear", is(updateBrand.getFoundingYear())));
    }

    @Test
    public void givenBrand_whenPutBrand_thenStatusNotFound() throws Exception {
        Brand updateBrand = new Brand("nonExisting","Updated Brand", "Updated Country", "1111");

        mockMvc.perform(put("/brands").content(mapper.writeValueAsString(updateBrand)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenBrand_whenDeleteBrand_thenStatusOk() throws Exception {
        mockMvc.perform(delete("/brands/{id}", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBrand_whenDeleteBrand_thenStatusNotFound() throws Exception {
        mockMvc.perform(delete("/brands/{id}", "nonExisting").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
