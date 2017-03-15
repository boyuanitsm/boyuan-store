package com.boyuanitsm.store.web.rest;

import com.boyuanitsm.store.BoyuanStoreApp;

import com.boyuanitsm.store.domain.WishList;
import com.boyuanitsm.store.repository.WishListRepository;
import com.boyuanitsm.store.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WishListResource REST controller.
 *
 * @see WishListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BoyuanStoreApp.class)
public class WishListResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Integer DEFAULT_NEW_FIELD = 0;
    private static final Integer UPDATED_NEW_FIELD = 1;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWishListMockMvc;

    private WishList wishList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            WishListResource wishListResource = new WishListResource(wishListRepository);
        this.restWishListMockMvc = MockMvcBuilders.standaloneSetup(wishListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishList createEntity(EntityManager em) {
        WishList wishList = new WishList()
                .name(DEFAULT_NAME)
                .createDate(DEFAULT_CREATE_DATE)
                .body(DEFAULT_BODY)
                .newField(DEFAULT_NEW_FIELD);
        return wishList;
    }

    @Before
    public void initTest() {
        wishList = createEntity(em);
    }

    @Test
    @Transactional
    public void createWishList() throws Exception {
        int databaseSizeBeforeCreate = wishListRepository.findAll().size();

        // Create the WishList

        restWishListMockMvc.perform(post("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isCreated());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate + 1);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWishList.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testWishList.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testWishList.getNewField()).isEqualTo(DEFAULT_NEW_FIELD);
    }

    @Test
    @Transactional
    public void createWishListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wishListRepository.findAll().size();

        // Create the WishList with an existing ID
        WishList existingWishList = new WishList();
        existingWishList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishListMockMvc.perform(post("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWishList)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wishListRepository.findAll().size();
        // set the field null
        wishList.setName(null);

        // Create the WishList, which fails.

        restWishListMockMvc.perform(post("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isBadRequest());

        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWishLists() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get all the wishListList
        restWishListMockMvc.perform(get("/api/wish-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].newField").value(hasItem(DEFAULT_NEW_FIELD)));
    }

    @Test
    @Transactional
    public void getWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get the wishList
        restWishListMockMvc.perform(get("/api/wish-lists/{id}", wishList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wishList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.newField").value(DEFAULT_NEW_FIELD));
    }

    @Test
    @Transactional
    public void getNonExistingWishList() throws Exception {
        // Get the wishList
        restWishListMockMvc.perform(get("/api/wish-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Update the wishList
        WishList updatedWishList = wishListRepository.findOne(wishList.getId());
        updatedWishList
                .name(UPDATED_NAME)
                .createDate(UPDATED_CREATE_DATE)
                .body(UPDATED_BODY)
                .newField(UPDATED_NEW_FIELD);

        restWishListMockMvc.perform(put("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWishList)))
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWishList.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWishList.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWishList.getNewField()).isEqualTo(UPDATED_NEW_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Create the WishList

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWishListMockMvc.perform(put("/api/wish-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isCreated());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);
        int databaseSizeBeforeDelete = wishListRepository.findAll().size();

        // Get the wishList
        restWishListMockMvc.perform(delete("/api/wish-lists/{id}", wishList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WishList.class);
    }
}
