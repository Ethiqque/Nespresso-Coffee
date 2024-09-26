package com.nespresso.product.repository;

import com.nespresso.product.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, UUID> {

    @Query("SELECT product FROM ProductInfo product " +
            "WHERE (" +
            "(:minPrice IS NULL AND :maxPrice IS NULL) OR " +
            "(:minPrice IS NULL AND :maxPrice >= product.price) OR " +
            "(:maxPrice IS NULL AND :minPrice <= product.price) OR " +
            "(product.price BETWEEN :minPrice AND :maxPrice)" +
            ") " +
            "AND (:minimumAverageRating IS NULL OR " + ":minimumAverageRating <= product.averageRating) " +
            "AND (:brandNames IS NULL OR " + "product.brandName IN :brandNames) " +
            "AND (:sellerNames IS NULL OR " + "product.sellerName IN :sellerNames) ")
    Page<ProductInfo> findAllProducts(@Param(value = "minPrice") BigDecimal minPrice,
                                      @Param(value = "maxPrice") BigDecimal maxPrice,
                                      @Param(value = "minimumAverageRating") BigDecimal minimumAverageRating,
                                      @Param(value = "brandNames") List<String> brandNames,
                                      @Param(value = "sellerNames") List<String> sellerNames,
                                      Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE product p " +
                    "SET average_rating = COALESCE((SELECT AVG(pr.rating) " +
                    "FROM product_reviews pr " +
                    "WHERE pr.product_id = p.id), 0) " +
                    "WHERE p.id = :productId")
    void updateAverageRating(@Param("productId") UUID productId);


    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE product p " +
                    "SET reviews_count = (" +
                    "SELECT COUNT(pr.id) " +
                    "FROM product_reviews pr " +
                    "WHERE pr.product_id = p.id" +
                    ") " +
                    "WHERE p.id = :productId")
    void updateReviewsCount(final UUID productId);
}