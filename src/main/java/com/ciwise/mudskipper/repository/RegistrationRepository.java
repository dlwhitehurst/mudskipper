package com.ciwise.mudskipper.repository;

import com.ciwise.mudskipper.domain.Registration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Registration entity.
 */
public interface RegistrationRepository extends JpaRepository<Registration,Long> {

}
