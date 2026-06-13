package com.vitalwatch.center.platform.subscriptions.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.BillingPeriod;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanCode;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SubscriptionPlanStatus;
import com.vitalwatch.center.platform.subscriptions.domain.model.enums.SupportLevel;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * JPA persistence entity for VitalWatch subscription plans.
 */
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlanPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private SubscriptionPlanCode code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 800)
    private String description;

    @Column(name = "price_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false)
    private BillingPeriod billingPeriod;

    @Column(name = "max_doctors", nullable = false)
    private Integer maxDoctors;

    @Column(name = "max_supervisors", nullable = false)
    private Integer maxSupervisors;

    @Column(name = "max_teams", nullable = false)
    private Integer maxTeams;

    @Column(name = "max_work_areas", nullable = false)
    private Integer maxWorkAreas;

    @Column(name = "monthly_invitations", nullable = false)
    private Integer monthlyInvitations;

    @Column(name = "data_history_days", nullable = false)
    private Integer dataHistoryDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "support_level", nullable = false)
    private SupportLevel supportLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlanStatus status;

    public SubscriptionPlanPersistenceEntity() {
    }

    public SubscriptionPlanCode getCode() {
        return code;
    }

    public void setCode(SubscriptionPlanCode code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public Integer getMaxDoctors() {
        return maxDoctors;
    }

    public void setMaxDoctors(Integer maxDoctors) {
        this.maxDoctors = maxDoctors;
    }

    public Integer getMaxSupervisors() {
        return maxSupervisors;
    }

    public void setMaxSupervisors(Integer maxSupervisors) {
        this.maxSupervisors = maxSupervisors;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public Integer getMaxWorkAreas() {
        return maxWorkAreas;
    }

    public void setMaxWorkAreas(Integer maxWorkAreas) {
        this.maxWorkAreas = maxWorkAreas;
    }

    public Integer getMonthlyInvitations() {
        return monthlyInvitations;
    }

    public void setMonthlyInvitations(Integer monthlyInvitations) {
        this.monthlyInvitations = monthlyInvitations;
    }

    public Integer getDataHistoryDays() {
        return dataHistoryDays;
    }

    public void setDataHistoryDays(Integer dataHistoryDays) {
        this.dataHistoryDays = dataHistoryDays;
    }

    public SupportLevel getSupportLevel() {
        return supportLevel;
    }

    public void setSupportLevel(SupportLevel supportLevel) {
        this.supportLevel = supportLevel;
    }

    public SubscriptionPlanStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionPlanStatus status) {
        this.status = status;
    }
}