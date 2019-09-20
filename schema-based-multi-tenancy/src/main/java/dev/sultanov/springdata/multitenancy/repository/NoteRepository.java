package dev.sultanov.springdata.multitenancy.repository;

import dev.sultanov.springdata.multitenancy.entity.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {
}
