package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.seeders;

import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities.PlanJpaEntity;
import com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.repositories.PlanJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds default subscription plans when the database is empty.
 */
@Component
public class PlanDataSeeder implements CommandLineRunner {

    private final PlanJpaRepository planRepository;

    public PlanDataSeeder(PlanJpaRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public void run(String... args) {
        if (planRepository.count() > 0) {
            return;
        }

        planRepository.saveAll(List.of(
                basicPlan(),
                professionalPlan(),
                enterprisePlan()
        ));
    }

    private PlanJpaEntity basicPlan() {
        return new PlanJpaEntity(
                "basic",
                "Basic",
                "Essential fatigue monitoring tools for small hospital teams.",
                new BigDecimal("79.00"),
                "USD",
                "MONTHLY",
                15,
                3,
                3,
                3,
                25,
                30,
                "Email support",
                false,
                true,
                List.of(
                        "fatigue.monitoring",
                        "basic.risk.alerts",
                        "staff.management",
                        "basic.reports"
                ),
                List.of(
                        "advanced.analytics",
                        "audit.compliance",
                        "priority.support"
                )
        );
    }

    private PlanJpaEntity professionalPlan() {
        return new PlanJpaEntity(
                "professional",
                "Professional",
                "Advanced fatigue monitoring and operational coordination for growing hospitals.",
                new BigDecimal("129.00"),
                "USD",
                "MONTHLY",
                50,
                10,
                12,
                8,
                100,
                180,
                "Priority email support",
                true,
                true,
                List.of(
                        "fatigue.monitoring",
                        "advanced.risk.alerts",
                        "staff.management",
                        "shift.coordination",
                        "advanced.reports",
                        "audit.compliance"
                ),
                List.of(
                        "dedicated.account.manager"
                )
        );
    }

    private PlanJpaEntity enterprisePlan() {
        return new PlanJpaEntity(
                "enterprise",
                "Enterprise",
                "Full platform capabilities for large hospital networks and critical operations.",
                new BigDecimal("199.00"),
                "USD",
                "MONTHLY",
                150,
                30,
                40,
                20,
                500,
                365,
                "Dedicated support",
                false,
                true,
                List.of(
                        "fatigue.monitoring",
                        "advanced.risk.alerts",
                        "staff.management",
                        "shift.coordination",
                        "advanced.reports",
                        "audit.compliance",
                        "priority.support",
                        "dedicated.account.manager"
                ),
                List.of()
        );
    }
}