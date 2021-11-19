package com.folksdev.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.folksdev.blog.dto.converter.UsersDtoConverter;
import com.folksdev.blog.model.Comment;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Users;
import com.folksdev.blog.repository.CommentRepository;
import com.folksdev.blog.repository.EntryRepository;
import com.folksdev.blog.repository.TagRepository;
import com.folksdev.blog.repository.UsersRepository;
import com.folksdev.blog.service.UsersService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Context ayaga kaldirir
@TestPropertySource(locations = "classpath:application.properties") // Test context icin kullanilacak propertyleri ayarlar
@DirtiesContext
@AutoConfigureMockMvc
public class IntegrationTestSupport extends TestSupport{
    @Autowired
    public MockMvc mockMvc;

    public final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public UsersService usersService;

    @Autowired
    public UsersRepository usersRepository;

    @Autowired
    public UsersDtoConverter usersDtoConverter;

    @Autowired
    public TagRepository tagRepository;

    @Autowired
    public EntryRepository entryRepository;

    @Autowired
    public CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    }

    @AfterEach
    public void tearDown(){
        usersRepository.deleteAll();
        entryRepository.deleteAll();
        commentRepository.deleteAll();
        tagRepository.deleteAll();
    }

    public Entry generateEntryToIT(){
        Users users = usersRepository.save(generateUsers());
        return entryRepository.save(
                new Entry(
                        "entryId",
                        "title",
                        "content",
                        LocalDateTime.of(2021, 11, 14, 20, 30),
                        null,
                        Collections.emptyList(),
                        users,
                        Collections.emptyList()
                ));
    }

    public Comment generateCommentToIT(){
        Users users = usersRepository.save(generateUsers());
        Entry entry = entryRepository.save(new Entry(
                "entryId",
                "title",
                "content",
                LocalDateTime.of(2021, 11, 14, 20, 30),
                null,
                Collections.emptyList(),
                users,
                Collections.emptyList())
        );

        return commentRepository.save(
                new Comment(
                        "commentId",
                        "content",
                        LocalDateTime.of(2021, 11, 14, 20, 30),
                        null,
                        entry,
                        users
                )
        );
    }
}
