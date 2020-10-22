package dev.sultanov.springsecurity.opaauthz.document;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DocumentRepository extends CrudRepository<Document, Long> {
}
