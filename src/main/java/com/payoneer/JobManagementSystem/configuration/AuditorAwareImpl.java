package com.payoneer.JobManagementSystem.configuration;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author Ashish
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("XXX");
    }

}