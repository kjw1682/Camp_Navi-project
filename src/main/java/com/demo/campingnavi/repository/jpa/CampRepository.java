package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Camp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CampRepository extends JpaRepository<Camp, Integer> {
    Camp findFirstByOrderByCseqDesc();

    Optional<Camp> findCampByContentId(String contentId);

    @Query("SELECT camp FROM Camp camp " +
            "WHERE camp.useyn LIKE %:useyn% " +
            "AND camp.name LIKE %:name% " +
            "AND camp.locationB LIKE %:locationB% " +
            "AND camp.locationS LIKE %:locationS% " +
            "INTERSECT " +
            "SELECT camp FROM Camp camp " +
            "WHERE camp.campType LIKE %:campType1% " +
            "OR camp.campType LIKE %:campType2% " +
            "OR camp.campType LIKE %:campType3% " +
            "OR camp.campType LIKE %:campType4% ")
    List<Camp> getCampList(String useyn, String name, String locationB, String locationS,
                           String campType1, String campType2, String campType3, String campType4);

    List<Camp> findByNameContaining(String name);

    @Query("SELECT c FROM Camp c WHERE c.name LIKE %:keyword%")
    List<Camp> searchCamps(String keyword);

    @Query("SELECT c FROM Camp c WHERE c.name=?1")
    Camp findCampByName(String name);

    Camp findByName(String name);

    List<Camp> findByUseynContaining(String useyn);

    @Modifying
    @Query(value="UPDATE Camp SET useyn = 'n' ", nativeQuery = true)
    void campAllDisabled();
}