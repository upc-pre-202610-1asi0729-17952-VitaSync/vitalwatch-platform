workspace "VitalWatch Platform - C4 Model" "System-wide software architecture for VitalWatch." {
    model {
        admin = person "Hospital Administrator" "Manages hospital configuration and staff."
        doctor = person "Doctor" "Monitors patients and health indicators."
        supervisor = person "Clinical Supervisor" "Coordinates teams and reviews incidents."

        vitalwatch = softwareSystem "VitalWatch Platform" "Health monitoring and clinical risk platform." {
            api = container "VitalWatch REST API" "Backend REST API." "Java and Spring Boot"
            database = container "VitalWatch Database" "Stores platform data." "MySQL"
        }

        admin -> api "Uses"
        doctor -> api "Uses"
        supervisor -> api "Uses"
        api -> database "Reads and writes"
    }

    views {
        systemContext vitalwatch "SystemContext" {
            include *
            autoLayout lr
        }
        theme default
    }
}
