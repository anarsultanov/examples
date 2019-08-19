package dev.sultanov.springdata.attributeencryption;

import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = AttributeEncryptor.class)
    private String name;

    @ColumnTransformer(
            read = "TRIM(CHAR(0) FROM UTF8TOSTRING(DECRYPT('AES', HASH('SHA256', STRINGTOUTF8('secret-key-12345'), 1), email)))",
            write = "ENCRYPT('AES', HASH('SHA256', STRINGTOUTF8('secret-key-12345'), 1), STRINGTOUTF8(?))"
    )
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
