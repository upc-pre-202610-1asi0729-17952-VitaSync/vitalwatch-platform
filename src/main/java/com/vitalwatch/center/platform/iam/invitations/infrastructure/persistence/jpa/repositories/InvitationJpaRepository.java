package com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.iam.invitations.domain.model.enums.InvitationStatus;
import com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.entities.InvitationJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for invitation persistence.
 */
public interface InvitationJpaRepository extends JpaRepository<InvitationJpaEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "organization")
    List<InvitationJpaEntity> findAll();

    @Override
    @EntityGraph(attributePaths = "organization")
    Optional<InvitationJpaEntity> findById(Long id);

    @EntityGraph(attributePaths = "organization")
    Optional<InvitationJpaEntity> findByToken(String token);

    @EntityGraph(attributePaths = "organization")
    List<InvitationJpaEntity> findByOrganization_Id(Long organizationId);

    boolean existsByEmailAndOrganization_IdAndStatus(
            String email,
            Long organizationId,
            InvitationStatus status
    );
}