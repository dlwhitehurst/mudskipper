package com.ciwise.mudskipper.repository;

import com.ciwise.mudskipper.domain.Genacctentry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Genacctentry entity.
 */
public interface GenacctentryRepository extends JpaRepository<Genacctentry,Long> {

}
