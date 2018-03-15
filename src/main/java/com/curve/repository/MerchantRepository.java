package com.curve.repository;

import com.curve.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {

    public Merchant findByMerchantId(int merchantId);

}
