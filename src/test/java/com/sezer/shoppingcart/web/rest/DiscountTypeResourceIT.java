package com.sezer.shoppingcart.web.rest;

import com.sezer.shoppingcart.ShoppingCartApp;
import com.sezer.shoppingcart.domain.DiscountType;
import com.sezer.shoppingcart.repository.DiscountTypeRepository;
import com.sezer.shoppingcart.service.DiscountTypeService;
import com.sezer.shoppingcart.service.dto.DiscountTypeDTO;
import com.sezer.shoppingcart.service.mapper.DiscountTypeMapper;
import com.sezer.shoppingcart.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sezer.shoppingcart.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DiscountTypeResource} REST controller.
 */
@SpringBootTest(classes = ShoppingCartApp.class)
public class DiscountTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private DiscountTypeRepository discountTypeRepository;

    @Autowired
    private DiscountTypeMapper discountTypeMapper;

    @Autowired
    private DiscountTypeService discountTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDiscountTypeMockMvc;

    private DiscountType discountType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscountTypeResource discountTypeResource = new DiscountTypeResource(discountTypeService);
        this.restDiscountTypeMockMvc = MockMvcBuilders.standaloneSetup(discountTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscountType createEntity(EntityManager em) {
        DiscountType discountType = new DiscountType()
            .title(DEFAULT_TITLE);
        return discountType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscountType createUpdatedEntity(EntityManager em) {
        DiscountType discountType = new DiscountType()
            .title(UPDATED_TITLE);
        return discountType;
    }

    @BeforeEach
    public void initTest() {
        discountType = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscountType() throws Exception {
        int databaseSizeBeforeCreate = discountTypeRepository.findAll().size();

        // Create the DiscountType
        DiscountTypeDTO discountTypeDTO = discountTypeMapper.toDto(discountType);
        restDiscountTypeMockMvc.perform(post("/api/discount-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the DiscountType in the database
        List<DiscountType> discountTypeList = discountTypeRepository.findAll();
        assertThat(discountTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DiscountType testDiscountType = discountTypeList.get(discountTypeList.size() - 1);
        assertThat(testDiscountType.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createDiscountTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discountTypeRepository.findAll().size();

        // Create the DiscountType with an existing ID
        discountType.setId(1L);
        DiscountTypeDTO discountTypeDTO = discountTypeMapper.toDto(discountType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountTypeMockMvc.perform(post("/api/discount-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DiscountType in the database
        List<DiscountType> discountTypeList = discountTypeRepository.findAll();
        assertThat(discountTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDiscountTypes() throws Exception {
        // Initialize the database
        discountTypeRepository.saveAndFlush(discountType);

        // Get all the discountTypeList
        restDiscountTypeMockMvc.perform(get("/api/discount-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountType.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    
    @Test
    @Transactional
    public void getDiscountType() throws Exception {
        // Initialize the database
        discountTypeRepository.saveAndFlush(discountType);

        // Get the discountType
        restDiscountTypeMockMvc.perform(get("/api/discount-types/{id}", discountType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discountType.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscountType() throws Exception {
        // Get the discountType
        restDiscountTypeMockMvc.perform(get("/api/discount-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscountType() throws Exception {
        // Initialize the database
        discountTypeRepository.saveAndFlush(discountType);

        int databaseSizeBeforeUpdate = discountTypeRepository.findAll().size();

        // Update the discountType
        DiscountType updatedDiscountType = discountTypeRepository.findById(discountType.getId()).get();
        // Disconnect from session so that the updates on updatedDiscountType are not directly saved in db
        em.detach(updatedDiscountType);
        updatedDiscountType
            .title(UPDATED_TITLE);
        DiscountTypeDTO discountTypeDTO = discountTypeMapper.toDto(updatedDiscountType);

        restDiscountTypeMockMvc.perform(put("/api/discount-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountTypeDTO)))
            .andExpect(status().isOk());

        // Validate the DiscountType in the database
        List<DiscountType> discountTypeList = discountTypeRepository.findAll();
        assertThat(discountTypeList).hasSize(databaseSizeBeforeUpdate);
        DiscountType testDiscountType = discountTypeList.get(discountTypeList.size() - 1);
        assertThat(testDiscountType.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscountType() throws Exception {
        int databaseSizeBeforeUpdate = discountTypeRepository.findAll().size();

        // Create the DiscountType
        DiscountTypeDTO discountTypeDTO = discountTypeMapper.toDto(discountType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountTypeMockMvc.perform(put("/api/discount-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DiscountType in the database
        List<DiscountType> discountTypeList = discountTypeRepository.findAll();
        assertThat(discountTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiscountType() throws Exception {
        // Initialize the database
        discountTypeRepository.saveAndFlush(discountType);

        int databaseSizeBeforeDelete = discountTypeRepository.findAll().size();

        // Delete the discountType
        restDiscountTypeMockMvc.perform(delete("/api/discount-types/{id}", discountType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DiscountType> discountTypeList = discountTypeRepository.findAll();
        assertThat(discountTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountType.class);
        DiscountType discountType1 = new DiscountType();
        discountType1.setId(1L);
        DiscountType discountType2 = new DiscountType();
        discountType2.setId(discountType1.getId());
        assertThat(discountType1).isEqualTo(discountType2);
        discountType2.setId(2L);
        assertThat(discountType1).isNotEqualTo(discountType2);
        discountType1.setId(null);
        assertThat(discountType1).isNotEqualTo(discountType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountTypeDTO.class);
        DiscountTypeDTO discountTypeDTO1 = new DiscountTypeDTO();
        discountTypeDTO1.setId(1L);
        DiscountTypeDTO discountTypeDTO2 = new DiscountTypeDTO();
        assertThat(discountTypeDTO1).isNotEqualTo(discountTypeDTO2);
        discountTypeDTO2.setId(discountTypeDTO1.getId());
        assertThat(discountTypeDTO1).isEqualTo(discountTypeDTO2);
        discountTypeDTO2.setId(2L);
        assertThat(discountTypeDTO1).isNotEqualTo(discountTypeDTO2);
        discountTypeDTO1.setId(null);
        assertThat(discountTypeDTO1).isNotEqualTo(discountTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(discountTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(discountTypeMapper.fromId(null)).isNull();
    }
}
