package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionStatus;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for subscription persistence.
 */
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"organization", "plan"})
    List<SubscriptionJpaEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"organization", "plan"})
    Optional<SubscriptionJpaEntity> findById(Long id);

    @EntityGraph(attributePaths = {"organization", "plan"})
    List<SubscriptionJpaEntity> findByOrganization_Id(Long organizationId);

    boolean existsByOrganization_IdAndStatus(Long organizationId, SubscriptionStatus status);
}