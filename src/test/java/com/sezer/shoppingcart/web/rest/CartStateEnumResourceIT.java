package com.sezer.shoppingcart.web.rest;

import com.sezer.shoppingcart.ShoppingCartApp;
import com.sezer.shoppingcart.domain.CartState;
import com.sezer.shoppingcart.repository.CartStateRepository;
import com.sezer.shoppingcart.service.CartStateService;
import com.sezer.shoppingcart.service.dto.CartStateDTO;
import com.sezer.shoppingcart.service.mapper.CartStateMapper;
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
 * Integration tests for the {@link CartStateResource} REST controller.
 */
@SpringBootTest(classes = ShoppingCartApp.class)
public class CartStateEnumResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private CartStateRepository cartStateRepository;

    @Autowired
    private CartStateMapper cartStateMapper;

    @Autowired
    private CartStateService cartStateService;

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

    private MockMvc restCartStateMockMvc;

    private CartState cartState;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CartStateResource cartStateResource = new CartStateResource(cartStateService);
        this.restCartStateMockMvc = MockMvcBuilders.standaloneSetup(cartStateResource)
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
    public static CartState createEntity(EntityManager em) {
        CartState cartState = new CartState()
            .title(DEFAULT_TITLE);
        return cartState;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartState createUpdatedEntity(EntityManager em) {
        CartState cartState = new CartState()
            .title(UPDATED_TITLE);
        return cartState;
    }

    @BeforeEach
    public void initTest() {
        cartState = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartState() throws Exception {
        int databaseSizeBeforeCreate = cartStateRepository.findAll().size();

        // Create the CartState
        CartStateDTO cartStateDTO = cartStateMapper.toDto(cartState);
        restCartStateMockMvc.perform(post("/api/cart-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartStateDTO)))
            .andExpect(status().isCreated());

        // Validate the CartState in the database
        List<CartState> cartStateList = cartStateRepository.findAll();
        assertThat(cartStateList).hasSize(databaseSizeBeforeCreate + 1);
        CartState testCartState = cartStateList.get(cartStateList.size() - 1);
        assertThat(testCartState.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createCartStateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartStateRepository.findAll().size();

        // Create the CartState with an existing ID
        cartState.setId(1L);
        CartStateDTO cartStateDTO = cartStateMapper.toDto(cartState);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartStateMockMvc.perform(post("/api/cart-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartState in the database
        List<CartState> cartStateList = cartStateRepository.findAll();
        assertThat(cartStateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCartStates() throws Exception {
        // Initialize the database
        cartStateRepository.saveAndFlush(cartState);

        // Get all the cartStateList
        restCartStateMockMvc.perform(get("/api/cart-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartState.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getCartState() throws Exception {
        // Initialize the database
        cartStateRepository.saveAndFlush(cartState);

        // Get the cartState
        restCartStateMockMvc.perform(get("/api/cart-states/{id}", cartState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartState.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCartState() throws Exception {
        // Get the cartState
        restCartStateMockMvc.perform(get("/api/cart-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartState() throws Exception {
        // Initialize the database
        cartStateRepository.saveAndFlush(cartState);

        int databaseSizeBeforeUpdate = cartStateRepository.findAll().size();

        // Update the cartState
        CartState updatedCartState = cartStateRepository.findById(cartState.getId()).get();
        // Disconnect from session so that the updates on updatedCartState are not directly saved in db
        em.detach(updatedCartState);
        updatedCartState
            .title(UPDATED_TITLE);
        CartStateDTO cartStateDTO = cartStateMapper.toDto(updatedCartState);

        restCartStateMockMvc.perform(put("/api/cart-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartStateDTO)))
            .andExpect(status().isOk());

        // Validate the CartState in the database
        List<CartState> cartStateList = cartStateRepository.findAll();
        assertThat(cartStateList).hasSize(databaseSizeBeforeUpdate);
        CartState testCartState = cartStateList.get(cartStateList.size() - 1);
        assertThat(testCartState.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCartState() throws Exception {
        int databaseSizeBeforeUpdate = cartStateRepository.findAll().size();

        // Create the CartState
        CartStateDTO cartStateDTO = cartStateMapper.toDto(cartState);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartStateMockMvc.perform(put("/api/cart-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartState in the database
        List<CartState> cartStateList = cartStateRepository.findAll();
        assertThat(cartStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCartState() throws Exception {
        // Initialize the database
        cartStateRepository.saveAndFlush(cartState);

        int databaseSizeBeforeDelete = cartStateRepository.findAll().size();

        // Delete the cartState
        restCartStateMockMvc.perform(delete("/api/cart-states/{id}", cartState.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CartState> cartStateList = cartStateRepository.findAll();
        assertThat(cartStateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartState.class);
        CartState cartState1 = new CartState();
        cartState1.setId(1L);
        CartState cartState2 = new CartState();
        cartState2.setId(cartState1.getId());
        assertThat(cartState1).isEqualTo(cartState2);
        cartState2.setId(2L);
        assertThat(cartState1).isNotEqualTo(cartState2);
        cartState1.setId(null);
        assertThat(cartState1).isNotEqualTo(cartState2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartStateDTO.class);
        CartStateDTO cartStateDTO1 = new CartStateDTO();
        cartStateDTO1.setId(1L);
        CartStateDTO cartStateDTO2 = new CartStateDTO();
        assertThat(cartStateDTO1).isNotEqualTo(cartStateDTO2);
        cartStateDTO2.setId(cartStateDTO1.getId());
        assertThat(cartStateDTO1).isEqualTo(cartStateDTO2);
        cartStateDTO2.setId(2L);
        assertThat(cartStateDTO1).isNotEqualTo(cartStateDTO2);
        cartStateDTO1.setId(null);
        assertThat(cartStateDTO1).isNotEqualTo(cartStateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cartStateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cartStateMapper.fromId(null)).isNull();
    }
}
