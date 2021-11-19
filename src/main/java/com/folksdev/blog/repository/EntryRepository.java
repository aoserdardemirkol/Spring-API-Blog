package com.folksdev.blog.repository;

import com.folksdev.blog.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, String> {
}
