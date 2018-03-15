package com.curve.repository;

import com.curve.domain.MerchantBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantBalanceRepository extends JpaRepository<MerchantBalance, String> {

    public MerchantBalance findByMerchantId(int merchantId);


}
