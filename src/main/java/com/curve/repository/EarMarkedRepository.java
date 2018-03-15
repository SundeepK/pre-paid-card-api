package com.curve.repository;

import com.curve.domain.EarMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EarMarkedRepository extends JpaRepository<EarMarked, Integer> {

    public EarMarked findByCardIdAndMerchantId(int cardId, int merchantId);

    @Query("select sum(e.amount) from EarMarked as e where e.merchantId=:merchantId")
    public double sumBydMerchantId(@Param("merchantId") int merchantId);


}
