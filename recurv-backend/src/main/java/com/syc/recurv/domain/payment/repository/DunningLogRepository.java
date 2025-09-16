package com.syc.recurv.domain.payment.repository;

import com.syc.recurv.domain.payment.entity.DunningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DunningLogRepository extends JpaRepository<DunningLog,Long> {
}
