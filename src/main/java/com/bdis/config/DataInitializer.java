package com.bdis.config;

import com.bdis.entity.*;
import com.bdis.enums.*;
import com.bdis.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final LocationStateRepository stateRepository;
    private final LocationDistrictRepository districtRepository;
    private final UserRepository userRepository;
    private final HospitalProfileRepository hospitalRepository;
    private final BloodInventoryRepository inventoryRepository;
    private final BloodRequestRepository requestRepository;
    private final BloodDeliveryRepository deliveryRepository;
    private final DonorPledgeRepository donorPledgeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(LocationStateRepository stateRepository,
                           LocationDistrictRepository districtRepository,
                           UserRepository userRepository,
                           HospitalProfileRepository hospitalRepository,
                           BloodInventoryRepository inventoryRepository,
                           BloodRequestRepository requestRepository,
                           BloodDeliveryRepository deliveryRepository,
                           DonorPledgeRepository donorPledgeRepository,
                           PasswordEncoder passwordEncoder) {
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.userRepository = userRepository;
        this.hospitalRepository = hospitalRepository;
        this.inventoryRepository = inventoryRepository;
        this.requestRepository = requestRepository;
        this.deliveryRepository = deliveryRepository;
        this.donorPledgeRepository = donorPledgeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (stateRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial data population.");
            return;
        }

        log.info("Starting initial seed data population for BDIS...");

        // 1. Seed States and Districts
        Map<String, List<String>> stateDistrictMap = new LinkedHashMap<>();
        stateDistrictMap.put("Odisha:OD", Arrays.asList("Khordha", "Cuttack", "Ganjam", "Sambalpur", "Puri", "Balasore", "Rourkela (Sundargarh)"));
        stateDistrictMap.put("Maharashtra:MH", Arrays.asList("Mumbai City", "Pune", "Nagpur", "Thane", "Nashik", "Aurangabad"));
        stateDistrictMap.put("Karnataka:KA", Arrays.asList("Bengaluru Urban", "Mysuru", "Mangaluru (Dakshina Kannada)", "Belagavi", "Hubballi-Dharwad"));
        stateDistrictMap.put("Delhi:DL", Arrays.asList("Central Delhi", "South Delhi", "North Delhi", "New Delhi", "East Delhi"));
        stateDistrictMap.put("West Bengal:WB", Arrays.asList("Kolkata", "Howrah", "North 24 Parganas", "Darjeeling"));

        Map<String, LocationState> savedStates = new HashMap<>();
        Map<String, LocationDistrict> savedDistricts = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : stateDistrictMap.entrySet()) {
            String[] stateParts = entry.getKey().split(":");
            String stateName = stateParts[0];
            String stateCode = stateParts[1];

            LocationState state = stateRepository.save(new LocationState(stateName, stateCode));
            savedStates.put(stateCode, state);

            for (String distName : entry.getValue()) {
                LocationDistrict district = districtRepository.save(new LocationDistrict(distName, state));
                savedDistricts.put(stateCode + "_" + distName, district);
            }
        }

        LocationState odisha = savedStates.get("OD");
        LocationDistrict khordha = savedDistricts.get("OD_Khordha");
        LocationDistrict cuttack = savedDistricts.get("OD_Cuttack");

        LocationState maharashtra = savedStates.get("MH");
        LocationDistrict mumbai = savedDistricts.get("MH_Mumbai City");

        // 2. Seed Admin User
        User admin = User.builder()
                .name("Master Administrator")
                .email("admin@bloodbank.org")
                .password(passwordEncoder.encode("Admin@123"))
                .phone("+91-9876543210")
                .role(Role.ROLE_ADMIN)
                .bloodGroup(BloodGroup.O_NEG)
                .state(odisha)
                .district(khordha)
                .address("State Blood Transfusion Council HQ, Bhubaneswar, Odisha")
                .isEnabled(true)
                .build();
        userRepository.save(admin);
        log.info("Admin created: admin@bloodbank.org / Admin@123");

        // 3. Seed Hospital 1 (Apollo - Verified)
        User apolloUser = User.builder()
                .name("Apollo Blood Center")
                .email("apollo@hospital.org")
                .password(passwordEncoder.encode("Hospital@123"))
                .phone("+91-674-6661000")
                .role(Role.ROLE_HOSPITAL)
                .state(odisha)
                .district(khordha)
                .address("Plot No. 251, Old Sainik School Road, Bhubaneswar")
                .isEnabled(true)
                .build();
        userRepository.save(apolloUser);

        HospitalProfile apolloHospital = HospitalProfile.builder()
                .user(apolloUser)
                .hospitalName("Apollo Super Specialty Blood Center")
                .licenseNumber("BB-OD-2024-001")
                .isVerified(true)
                .contactPerson("Dr. Siddharth Mohanty (Director of Transfusion)")
                .emergencyHelpline("+91-674-6661066")
                .state(odisha)
                .district(khordha)
                .address("Plot No. 251, Old Sainik School Road, Unit 15, Bhubaneswar, Odisha - 751005")
                .totalBeds(350)
                .build();
        hospitalRepository.save(apolloHospital);

        // 4. Seed Hospital 2 (AIIMS - Verified)
        User aiimsUser = User.builder()
                .name("AIIMS Blood Bank")
                .email("aiims@hospital.org")
                .password(passwordEncoder.encode("Hospital@123"))
                .phone("+91-674-2476789")
                .role(Role.ROLE_HOSPITAL)
                .state(odisha)
                .district(khordha)
                .address("Sijua, Patrapada, Bhubaneswar")
                .isEnabled(true)
                .build();
        userRepository.save(aiimsUser);

        HospitalProfile aiimsHospital = HospitalProfile.builder()
                .user(aiimsUser)
                .hospitalName("AIIMS Central Blood Bank & Trauma Center")
                .licenseNumber("BB-OD-2024-002")
                .isVerified(true)
                .contactPerson("Dr. Ananya Mishra (Blood Bank In-Charge)")
                .emergencyHelpline("+91-674-2476000")
                .state(odisha)
                .district(khordha)
                .address("AIIMS Campus, Sijua, Patrapada, Bhubaneswar, Odisha - 751019")
                .totalBeds(750)
                .build();
        hospitalRepository.save(aiimsHospital);

        // 5. Seed Hospital 3 (Mumbai Lifeline - Verified)
        User mumbaiUser = User.builder()
                .name("Mumbai Lifeline Blood Bank")
                .email("mumbai.lifeline@hospital.org")
                .password(passwordEncoder.encode("Hospital@123"))
                .phone("+91-22-24101000")
                .role(Role.ROLE_HOSPITAL)
                .state(maharashtra)
                .district(mumbai)
                .address("Dr. E Borges Road, Parel, Mumbai")
                .isEnabled(true)
                .build();
        userRepository.save(mumbaiUser);

        HospitalProfile mumbaiHospital = HospitalProfile.builder()
                .user(mumbaiUser)
                .hospitalName("Mumbai Lifeline Charitable Blood Bank")
                .licenseNumber("BB-MH-2024-108")
                .isVerified(true)
                .contactPerson("Dr. Rajesh Kothari")
                .emergencyHelpline("+91-22-24101100")
                .state(maharashtra)
                .district(mumbai)
                .address("Dr. E Borges Road, Parel, Mumbai, Maharashtra - 400012")
                .totalBeds(200)
                .build();
        hospitalRepository.save(mumbaiHospital);

        // 6. Seed Hospital 4 (Pending verification)
        User citycareUser = User.builder()
                .name("City Care Blood Center")
                .email("citycare@hospital.org")
                .password(passwordEncoder.encode("Hospital@123"))
                .phone("+91-671-2300456")
                .role(Role.ROLE_HOSPITAL)
                .state(odisha)
                .district(cuttack)
                .address("Bajrakabati Road, Cuttack")
                .isEnabled(true)
                .build();
        userRepository.save(citycareUser);

        HospitalProfile citycareHospital = HospitalProfile.builder()
                .user(citycareUser)
                .hospitalName("City Care Lifeline Blood Bank")
                .licenseNumber("BB-OD-2026-PENDING-99")
                .isVerified(false)
                .contactPerson("Dr. Soumya Ranjan Das")
                .emergencyHelpline("+91-671-2300777")
                .state(odisha)
                .district(cuttack)
                .address("Bajrakabati Road, Ranihat, Cuttack, Odisha - 753001")
                .totalBeds(80)
                .build();
        hospitalRepository.save(citycareHospital);

        // 7. Pre-populate stock for Apollo, AIIMS and Mumbai
        seedHospitalStock(apolloHospital, 100);
        seedHospitalStock(aiimsHospital, 200);
        seedHospitalStock(mumbaiHospital, 300);

        // 8. Seed Sample Regular Users / Donors
        User donor1 = User.builder()
                .name("Rahul Sharma")
                .email("donor@gmail.com")
                .password(passwordEncoder.encode("User@123"))
                .phone("+91-9123456780")
                .role(Role.ROLE_USER)
                .bloodGroup(BloodGroup.O_POS)
                .state(odisha)
                .district(khordha)
                .address("Flat 402, Royal Palms, Nayapalli, Bhubaneswar")
                .isEnabled(true)
                .build();
        userRepository.save(donor1);
        log.info("User created: donor@gmail.com / User@123");

        User donor2 = User.builder()
                .name("Priya Patel")
                .email("priya@gmail.com")
                .password(passwordEncoder.encode("User@123"))
                .phone("+91-9876501234")
                .role(Role.ROLE_USER)
                .bloodGroup(BloodGroup.B_POS)
                .state(odisha)
                .district(khordha)
                .address("B-12, Saheed Nagar, Bhubaneswar")
                .isEnabled(true)
                .build();
        userRepository.save(donor2);

        // 9. Seed Sample Blood Requests
        BloodRequest req1 = BloodRequest.builder()
                .requester(donor1)
                .hospital(apolloHospital)
                .patientName("Sunita Sharma")
                .admittedHospitalName("Kalinga Hospital, Chandrasekharpur")
                .bloodGroup(BloodGroup.O_POS)
                .componentType(BloodComponentType.WHOLE_BLOOD)
                .unitsRequested(2)
                .urgencyLevel(UrgencyLevel.EMERGENCY)
                .status(RequestStatus.PENDING)
                .deliveryAddress("ICU Ward 3, Kalinga Hospital, Chandrasekharpur, Bhubaneswar")
                .prescriptionNotes("Emergency CABG surgery scheduled. Urgent O+ blood required immediately.")
                .build();
        requestRepository.save(req1);

        BloodRequest req2 = BloodRequest.builder()
                .requester(donor2)
                .hospital(aiimsHospital)
                .patientName("Deepak Patel")
                .admittedHospitalName("Capital Hospital, Unit 6")
                .bloodGroup(BloodGroup.B_POS)
                .componentType(BloodComponentType.PLATELETS)
                .unitsRequested(2)
                .urgencyLevel(UrgencyLevel.CRITICAL)
                .status(RequestStatus.DISPATCHED)
                .deliveryAddress("Pediatric ICU, Capital Hospital, Unit 6, Bhubaneswar")
                .prescriptionNotes("Severe dengue thrombocytopenia. Platelet count dropped below 20,000.")
                .build();
        BloodRequest savedReq2 = requestRepository.save(req2);

        BloodDelivery del2 = BloodDelivery.builder()
                .request(savedReq2)
                .deliveryPartnerName("Express MediTransit - Ashok Kumar")
                .deliveryContact("+91-9876543210")
                .vehicleNumber("OD-02-AX-4512 (Temperature Controlled Van)")
                .trackingNotes("Blood box temperature monitored at 4°C. Estimated arrival in 15 minutes.")
                .dispatchedAt(LocalDateTime.now().minusMinutes(25))
                .currentStatus(DeliveryStatus.IN_TRANSIT)
                .build();
        deliveryRepository.save(del2);

        BloodRequest req3 = BloodRequest.builder()
                .requester(donor1)
                .hospital(apolloHospital)
                .patientName("Manas Rout")
                .admittedHospitalName("Apollo Hospital, Bhubaneswar")
                .bloodGroup(BloodGroup.A_POS)
                .componentType(BloodComponentType.RBC)
                .unitsRequested(1)
                .urgencyLevel(UrgencyLevel.NORMAL)
                .status(RequestStatus.DELIVERED)
                .deliveryAddress("Blood Bank Reception, Apollo Hospital")
                .prescriptionNotes("Elective orthopedic surgery preparation.")
                .build();
        BloodRequest savedReq3 = requestRepository.save(req3);

        BloodDelivery del3 = BloodDelivery.builder()
                .request(savedReq3)
                .deliveryPartnerName("Internal Hospital Ward Courier")
                .deliveryContact("+91-674-6661066")
                .vehicleNumber("Internal Transport Trolley 04")
                .trackingNotes("Handed over to Duty Staff Nurse Sunanda Panda at OT-3.")
                .dispatchedAt(LocalDateTime.now().minusHours(4))
                .deliveredAt(LocalDateTime.now().minusHours(3))
                .currentStatus(DeliveryStatus.DELIVERED)
                .build();
        deliveryRepository.save(del3);

        // 10. Seed Sample Donor Pledge
        DonorPledge pledge = DonorPledge.builder()
                .donor(donor1)
                .hospital(apolloHospital)
                .pledgedDate(LocalDate.now().plusDays(5))
                .status("PLEDGED")
                .unitsDonated(1)
                .notes("Volunteering on World Blood Donor Day celebration camp.")
                .build();
        donorPledgeRepository.save(pledge);

        log.info("BDIS initial seed data populated successfully!");
    }

    private void seedHospitalStock(HospitalProfile hospital, int batchSeedOffset) {
        int batchCount = batchSeedOffset;
        for (BloodGroup bg : BloodGroup.values()) {
            for (BloodComponentType comp : BloodComponentType.values()) {
                int units;
                if (bg == BloodGroup.O_NEG || bg == BloodGroup.AB_NEG) {
                    units = 4 + (batchCount % 6);
                } else {
                    units = 15 + (batchCount % 20);
                }

                LocalDate expiry = LocalDate.now().plusDays(comp.getShelfLifeDays() - (batchCount % 3));

                BloodInventory inv = BloodInventory.builder()
                        .hospital(hospital)
                        .bloodGroup(bg)
                        .componentType(comp)
                        .unitsAvailable(units)
                        .expiryDate(expiry)
                        .batchNumber("BAT-" + hospital.getLicenseNumber().replace("-", "") + "-" + bg.name() + "-" + (batchCount++))
                        .build();

                inventoryRepository.save(inv);
            }
        }
    }
}
