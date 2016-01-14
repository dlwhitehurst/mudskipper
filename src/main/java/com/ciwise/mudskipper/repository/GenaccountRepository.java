package com.ciwise.mudskipper.repository;

import com.ciwise.mudskipper.domain.Genaccount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Genaccount entity.
 */
public interface GenaccountRepository extends JpaRepository<Genaccount,Long> {

}
