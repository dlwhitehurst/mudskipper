package com.ciwise.mudskipper.repository;

import com.ciwise.mudskipper.domain.Genacct;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Genacct entity.
 */
public interface GenacctRepository extends JpaRepository<Genacct,Long> {

}
