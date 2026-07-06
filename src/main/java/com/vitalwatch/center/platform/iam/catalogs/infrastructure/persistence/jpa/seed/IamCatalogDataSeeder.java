package com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.seed;

import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities.SpecialtyJpaEntity;
import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.entities.WorkAreaJpaEntity;
import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.repositories.SpecialtyJpaRepository;
import com.vitalwatch.center.platform.iam.catalogs.infrastructure.persistence.jpa.repositories.WorkAreaJpaRepository;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationJpaRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Seeds IAM catalog data required by the frontend.
 */
@Component
public class IamCatalogDataSeeder {

    private final SpecialtyJpaRepository specialtyRepository;
    private final WorkAreaJpaRepository workAreaRepository;
    private final OrganizationJpaRepository organizationRepository;

    private final List<String> defaultSpecialties = List.of(
            "Medicina General",
            "Medicina Interna",
            "Cardiología",
            "Neurología",
            "Pediatría",
            "Ginecología y Obstetricia",
            "Traumatología",
            "Cirugía General",
            "Anestesiología",
            "Emergenciología",
            "Cuidados Intensivos",
            "Neumología",
            "Gastroenterología",
            "Endocrinología",
            "Nefrología",
            "Oncología",
            "Hematología",
            "Infectología",
            "Dermatología",
            "Psiquiatría",
            "Psicología Clínica",
            "Radiología",
            "Medicina Física y Rehabilitación",
            "Otorrinolaringología",
            "Oftalmología",
            "Urología",
            "Reumatología",
            "Nutrición Clínica",
            "Enfermería General",
            "Enfermería UCI",
            "Enfermería Emergencias",
            "Tecnología Médica",
            "Laboratorio Clínico",
            "Farmacia Hospitalaria"
    );

    private final List<String> defaultWorkAreas = List.of(
            "Emergencias",
            "Triaje",
            "UCI Adultos",
            "UCI Pediátrica",
            "UCI Neonatal",
            "Hospitalización",
            "Hospitalización Pediátrica",
            "Centro Quirúrgico",
            "Sala de Operaciones",
            "Recuperación Postoperatoria",
            "Consultorios Externos",
            "Cardiología",
            "Neurología",
            "Pediatría",
            "Ginecología y Obstetricia",
            "Traumatología",
            "Cirugía",
            "Medicina Interna",
            "Oncología",
            "Nefrología",
            "Hemodiálisis",
            "Neumología",
            "Gastroenterología",
            "Laboratorio Clínico",
            "Imagenología",
            "Radiología",
            "Farmacia",
            "Banco de Sangre",
            "Rehabilitación",
            "Nutrición",
            "Psicología",
            "Psiquiatría",
            "Admisión",
            "Archivo Clínico",
            "Esterilización",
            "Unidad de Quemados",
            "Unidad de Dolor",
            "Unidad de Cuidados Intermedios",
            "Sala de Partos",
            "Neonatología"
    );

    public IamCatalogDataSeeder(
            SpecialtyJpaRepository specialtyRepository,
            WorkAreaJpaRepository workAreaRepository,
            OrganizationJpaRepository organizationRepository
    ) {
        this.specialtyRepository = specialtyRepository;
        this.workAreaRepository = workAreaRepository;
        this.organizationRepository = organizationRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        seedSpecialties();

        organizationRepository.findAll()
                .forEach(organization -> seedDefaultWorkAreasForOrganization(organization.getId()));
    }

    @Transactional
    public void ensureWorkAreasForOrganization(Long organizationId) {
        if (organizationId == null || !organizationRepository.existsById(organizationId)) {
            return;
        }

        seedDefaultWorkAreasForOrganization(organizationId);
    }

    private void seedSpecialties() {
        defaultSpecialties.forEach(name -> {
            if (!specialtyRepository.existsByNameIgnoreCase(name)) {
                specialtyRepository.save(new SpecialtyJpaEntity(name));
            }
        });
    }

    private void seedDefaultWorkAreasForOrganization(Long organizationId) {
        defaultWorkAreas.forEach(name -> {
            if (!workAreaRepository.existsByOrganizationIdAndNameIgnoreCase(organizationId, name)) {
                workAreaRepository.save(new WorkAreaJpaEntity(organizationId, name));
            }
        });
    }
}