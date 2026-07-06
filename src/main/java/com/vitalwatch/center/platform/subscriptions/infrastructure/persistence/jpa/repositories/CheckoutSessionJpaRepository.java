package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.CheckoutSessionJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for checkout session persistence.
 */
public interface CheckoutSessionJpaRepository extends JpaRepository<CheckoutSessionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"organization", "plan"})
    Optional<CheckoutSessionJpaEntity> findBySessionId(String sessionId);

    @EntityGraph(attributePaths = {"organization", "plan"})
    List<CheckoutSessionJpaEntity> findByOrganization_IdOrderByCreatedAtDesc(Long organizationId);
}