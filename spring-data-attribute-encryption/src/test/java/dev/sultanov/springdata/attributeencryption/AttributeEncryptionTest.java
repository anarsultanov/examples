package dev.sultanov.springdata.attributeencryption;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttributeEncryptionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeEncryptionTest.class);

    private static final String NAME = "John";
    private static final String EMAIL = "john@example.com";

    private long id;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user = new User();
        user.setName(NAME);
        user.setEmail(EMAIL);
        id = userRepository.save(user).getId();
    }

    @Test
    public void readDecrypted() {
        User user = userRepository.findById(id).orElseThrow();
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    public void readEncrypted() {
        User user = jdbcTemplate.queryForObject(
                "select * from user where id = ?",
                (resultSet, i) -> {
                    User result = new User();
                    result.setId(resultSet.getLong("id"));
                    result.setName(resultSet.getString("name"));
                    result.setEmail(resultSet.getString("email"));
                    return result;
                },
                id
        );

        assertThat(user.getName()).isNotEqualTo(NAME);
        LOGGER.info("Encrypted name value in DB is {}", user.getName());
        assertThat(user.getEmail()).isNotEqualTo(EMAIL);
        LOGGER.info("Encrypted email value in DB is {}", user.getEmail());
    }
}
