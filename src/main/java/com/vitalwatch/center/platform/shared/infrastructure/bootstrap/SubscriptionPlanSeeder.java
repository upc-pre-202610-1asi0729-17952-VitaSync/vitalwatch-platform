package com.vitalwatch.center.platform.shared.infrastructure.bootstrap;

import com.vitalwatch.center.platform.subscriptions.domain.model.aggregates.SubscriptionPlan;
import com.vitalwatch.center.platform.subscriptions.domain.model.commands.CreateSubscriptionPlanCommand;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.BillingPeriod;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SupportLevel;
import com.vitalwatch.center.platform.subscriptions.domain.repositories.SubscriptionPlanRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Seeds default subscription plans required by the frontend and checkout flow.
 */
@Component
public class SubscriptionPlanSeeder {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanSeeder(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedDefaultPlans() {
        seedPlan(
                SubscriptionPlanCode.BASIC,
                "Basic",
                "Plan esencial para clínicas pequeñas que desean iniciar con el monitoreo de fatiga del personal médico.",
                BigDecimal.valueOf(100),
                30,
                6,
                6,
                6,
                80,
                45,
                SupportLevel.BASIC
        );

        seedPlan(
                SubscriptionPlanCode.PROFESSIONAL,
                "Professional",
                "Plan avanzado para hospitales y clínicas medianas que necesitan más usuarios, equipos y seguimiento histórico de fatiga.",
                BigDecimal.valueOf(200),
                120,
                25,
                25,
                15,
                350,
                180,
                SupportLevel.PRIORITY
        );

        seedPlan(
                SubscriptionPlanCode.ENTERPRISE,
                "Enterprise",
                "Plan completo para redes hospitalarias y clínicas grandes que requieren mayor capacidad, soporte empresarial y acceso amplio al historial de datos.",
                BigDecimal.valueOf(400),
                1000,
                250,
                250,
                100,
                5000,
                730,
                SupportLevel.ENTERPRISE
        );
    }

    private void seedPlan(
            SubscriptionPlanCode code,
            String name,
            String description,
            BigDecimal priceAmount,
            Integer maxDoctors,
            Integer maxSupervisors,
            Integer maxTeams,
            Integer maxWorkAreas,
            Integer monthlyInvitations,
            Integer dataHistoryDays,
            SupportLevel supportLevel
    ) {
        if (subscriptionPlanRepository.existsByCode(code)) {
            return;
        }

        var plan = new SubscriptionPlan(
                new CreateSubscriptionPlanCommand(
                        code,
                        name,
                        description,
                        priceAmount,
                        "USD",
                        BillingPeriod.MONTHLY,
                        maxDoctors,
                        maxSupervisors,
                        maxTeams,
                        maxWorkAreas,
                        monthlyInvitations,
                        dataHistoryDays,
                        supportLevel
                )
        );

        subscriptionPlanRepository.save(plan);
    }
}