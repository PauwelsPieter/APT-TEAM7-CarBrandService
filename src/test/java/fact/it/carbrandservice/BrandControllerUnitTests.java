package fact.it.carbrandservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.carbrandservice.model.Brand;
import fact.it.carbrandservice.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class BrandControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandRepository brandRepository;

    // ObjectMapper to transform objects to Json strings
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenGetRoot_thenReturnSwagger() throws Exception {
        // Test if we get redirected to /swagger-ui.html (status 302)
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void givenBrand_whenGetAllBrands_thenReturnJsonBrands() throws Exception {
        Brand brand1 = new Brand("Brand1", "Country1", "0001");
        Brand brand2 = new Brand("Brand2", "Country2", "0002");
        Brand brand3 = new Brand("Brand3", "Country3", "0003");

        List<Brand> brands = new ArrayList<>();
        brands.add(brand1);
        brands.add(brand2);
        brands.add(brand3);

        // Given method allows you to assign behaviour to the repository mock
        given(brandRepository.findAll()).willReturn(brands);

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
        Brand brand2 = new Brand("Brand2", "Country2", "0002");

        List<Brand> brands = new ArrayList<>();
        brands.add(brand2);

        // Given method allows you to assign behaviour to the repository mock
        given(brandRepository.findBrandsByCountry("Country2")).willReturn(brands);

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
        Brand startBrand = new Brand("1","Start Brand", "Start Country", "0000");

        given(brandRepository.findBrandsById(startBrand.getId())).willReturn(startBrand);

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

    @Test void givenBrand_whenDeleteBrand_thenStatusOk() throws Exception {
        Brand brandToBeDeleted = new Brand("1", "Brand", "Country", "0000");

        given(brandRepository.findBrandsById(brandToBeDeleted.getId())).willReturn(brandToBeDeleted);

        mockMvc.perform(delete("/brands/{id}", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test void givenBrand_whenDeleteBrand_thenStatusNotFound() throws Exception {
        given(brandRepository.findBrandsById("nonExisting")).willReturn(null);

        mockMvc.perform(delete("/brands/{id}", "nonExisting").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
