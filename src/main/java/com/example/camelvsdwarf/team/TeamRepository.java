package com.example.camelvsdwarf.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByNameIgnoreCase(String name);
        boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    @Query("""
                        select t from Team t
                        where (:query is null or lower(t.name) like lower(concat('%', :query, '%')))
                            and (:status is null or t.status = :status)
                        """)
        Page<Team> findByFilters(@Param("query") String query, @Param("status") teamStatus status,
                                                         Pageable pageable);
}
