package com.example.camelvsdwarf.competitor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
    boolean existsByNicknameIgnoreCase(String nickname);
        boolean existsByNicknameIgnoreCaseAndIdNot(String nickname, Long id);

        @Query("""
                        select c from Competitor c
                        where (:query is null or lower(c.name) like lower(concat('%', :query, '%'))
                                or lower(c.nickname) like lower(concat('%', :query, '%')))
                            and (:status is null or c.status = :status)
                        """)
        Page<Competitor> findByFilters(@Param("query") String query, @Param("status") CompetitorStatus status,
                                                                     Pageable pageable);
}