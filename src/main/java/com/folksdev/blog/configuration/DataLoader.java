package com.folksdev.blog.configuration;

import com.folksdev.blog.model.*;
import com.folksdev.blog.repository.CommentRepository;
import com.folksdev.blog.repository.EntryRepository;
import com.folksdev.blog.repository.TagRepository;
import com.folksdev.blog.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * !!!! IMPORTANT INFORMATION !!!!
 * THIS CLASS CAN BE ELIGIBLE JUST FOR H2 in-memory DB
 * If you already change your configuration to switch DB system with
 * persistence DB (such as PostgreSQL or MySQL) you must either
 * comment out this `run` method or remove the whole class.
 */
@Component
@ConditionalOnProperty(name = "command.line.runner.enable", havingValue = "true")
public class DataLoader implements CommandLineRunner {

    private final EntryRepository entryRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final UsersRepository usersRepository;

    public DataLoader(EntryRepository entryRepository,
                      CommentRepository commentRepository,
                      TagRepository tagRepository,
                      UsersRepository usersRepository) {
        this.entryRepository = entryRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        /* This line will provide more reliable Data loading process on booting of application */
        if (!entryRepository.findAll().isEmpty()) {
            return;
        }

        Users user1 = new Users("userId1", "Serdar", Gender.MALE);
        Users user2 = new Users("userId2", "Zeynep", Gender.FEMALE);

        user1 = usersRepository.save(user1);
        user2 = usersRepository.save(user2);

        Tag tag1 = new Tag("tagId1", "personnel");
        Tag tag2 = new Tag("tagId2", "tech");

        List<Tag> tags1 = tagRepository.saveAll(Arrays.asList(tag1, tag2));

        Entry entry1 = new Entry(
                "entryId1",
                "Entry Title",
                "Entry Content",
                null,
                tags1,
                user1);
        Entry entry2 = new Entry(
                "entryId2",
                "Entry Title",
                "Entry Content",
                null,
                tags1,
                user2);

        entry1 = entryRepository.save(entry1);
        entry2 = entryRepository.save(entry2);

        Comment comment1 = new Comment(
                "commentId1",
                "Comment Content",
                null,
                entry1,
                user1);
        Comment comment2 = new Comment(
                "commentId2",
                "Comment Content",
                null,
                entry2,
                user2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }
}
