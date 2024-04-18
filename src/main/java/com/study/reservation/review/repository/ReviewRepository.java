package com.study.reservation.review.repository;

import com.study.reservation.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select m from Review m where m.member.id = :memberId order by m.time desc")
    List<Review> findByMemberId(@Param("memberId") Long memberId);

    @Query("select m from Review m where m.product.id = :productId order by m.time desc")
    List<Review> findByProductId(@Param("productId") Long productId);

    @Query("select m from Review m where m.member.id = :memberId and m.product.id = :productId order by m.time desc")
    Review findByMemberIdAndProductId(@Param("memberId") Long memberId, @Param("productId") Long productId);

}
