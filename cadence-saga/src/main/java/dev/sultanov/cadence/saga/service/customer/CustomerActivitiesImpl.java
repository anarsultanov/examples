package dev.sultanov.cadence.saga.service.customer;

import dev.sultanov.cadence.saga.service.common.CustomerActivities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class CustomerActivitiesImpl implements CustomerActivities {

    private static final Logger logger = LoggerFactory.getLogger(CustomerActivitiesImpl.class);

    @Override
    public boolean reserveCredit(String customerId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(100)) > 0) {
            logger.info("Credit limit is exceeded for customer {}", customerId);
            return false;
        }
        logger.info("Credit reserved for customer {}", customerId);
        return true;
    }
}
