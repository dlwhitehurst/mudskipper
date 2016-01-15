package com.ciwise.mudskipper.repository;

import com.ciwise.mudskipper.domain.Genaccttype;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Genaccttype entity.
 */
public interface GenaccttypeRepository extends JpaRepository<Genaccttype,Long> {

}
