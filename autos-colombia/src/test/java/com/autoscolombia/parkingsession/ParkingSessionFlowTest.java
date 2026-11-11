package com.autoscolombia.parkingsession;

import com.autoscolombia.common.exception.BusinessRuleException;
import com.autoscolombia.common.exception.ResourceNotFoundException;
import com.autoscolombia.customer.persistence.entity.CustomerEntity;
import com.autoscolombia.operator.persistence.entity.OperatorEntity;
import com.autoscolombia.operator.service.domain.Operator;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterEntryHandler;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterExitHandler;
import com.autoscolombia.parkingsession.service.ParkingSessionDomainService;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import com.autoscolombia.parkingspot.persistence.entity.ParkingSpotEntity;
import com.autoscolombia.parkingspot.persistence.repository.ParkingSpotRepository;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.entity.VehicleTypeEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParkingSessionFlowTest {

    @Autowired ParkingSessionRegisterEntryHandler entryHandler;
    @Autowired ParkingSessionRegisterExitHandler exitHandler;
    @Autowired ParkingSessionDomainService sessionService;
    @Autowired ParkingSpotRepository spotRepository;
    @Autowired EntityManager em;
    @Autowired TransactionTemplate tx;

    Long operatorId;
    Long carSpotId;
    Long carSpot2Id;
    Long carId;
    Long car2Id;
    Long motoId;

    @BeforeEach
    void seed() {
        tx.executeWithoutResult(status -> {
            em.createNativeQuery("delete from parking_session").executeUpdate();
            em.createNativeQuery("delete from vehicle").executeUpdate();
            em.createNativeQuery("delete from parking_spot").executeUpdate();
            em.createNativeQuery("delete from vehicle_type").executeUpdate();
            em.createNativeQuery("delete from customer").executeUpdate();
            em.createNativeQuery("delete from operator").executeUpdate();

            VehicleTypeEntity car = em.merge(VehicleTypeEntity.builder().name("CARRO").monthlyRate(BigDecimal.TEN).build());
            VehicleTypeEntity moto = em.merge(VehicleTypeEntity.builder().name("MOTO").monthlyRate(BigDecimal.ONE).build());
            CustomerEntity customer = em.merge(CustomerEntity.builder().identificationNumber("1").identificationType("CC").name("Cliente").build());
            operatorId = em.merge(OperatorEntity.builder().name("Op").identificationNumber("2").identificationType("CC").username("op").build()).getId();
            carSpotId = em.merge(ParkingSpotEntity.builder().spotNumber("A-01").available(true).vehicleType(car).build()).getId();
            carSpot2Id = em.merge(ParkingSpotEntity.builder().spotNumber("A-02").available(true).vehicleType(car).build()).getId();
            carId = em.merge(VehicleEntity.builder().licensePlate("ABC-123").type(car).customer(customer).build()).getId();
            car2Id = em.merge(VehicleEntity.builder().licensePlate("DEF-456").type(car).customer(customer).build()).getId();
            motoId = em.merge(VehicleEntity.builder().licensePlate("MOT-01A").type(moto).customer(customer).build()).getId();
        });
    }

    private ParkingSession entryRequest(Long vehicleId, Long spotId) {
        return ParkingSession.builder()
                .vehicle(Vehicle.builder().id(vehicleId).build())
                .spot(ParkingSpot.builder().id(spotId).build())
                .entryOperator(Operator.builder().id(operatorId).build())
                .build();
    }

    private ParkingSession exitRequest(Long sessionId) {
        return ParkingSession.builder()
                .id(sessionId)
                .exitOperator(Operator.builder().id(operatorId).build())
                .build();
    }

    private long countSuccesses(List<Callable<Boolean>> tasks) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(tasks.size());
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();
        for (Callable<Boolean> task : tasks) {
            futures.add(pool.submit(() -> {
                start.await();
                return task.call();
            }));
        }
        start.countDown();
        long successes = 0;
        for (Future<Boolean> future : futures) {
            if (future.get()) successes++;
        }
        pool.shutdown();
        return successes;
    }

    @Test
    void entryAndExitFlowFreesTheRealSpotAndKeepsHistory() {
        ParkingSession entered = entryHandler.registerEntry(entryRequest(carId, carSpotId));
        assertNull(entered.getExitTime());
        assertFalse(spotRepository.findById(carSpotId).orElseThrow().isAvailable());
        assertEquals(1, sessionService.findActive().size());

        ParkingSession exited = exitHandler.registerExit(exitRequest(entered.getId()));
        assertNotNull(exited.getExitTime());
        assertTrue(spotRepository.findById(carSpotId).orElseThrow().isAvailable());
        assertTrue(sessionService.findActive().isEmpty());
        assertEquals(1, sessionService.findHistory(PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    void rejectsDuplicateEntryOfSameVehicle() {
        entryHandler.registerEntry(entryRequest(carId, carSpotId));
        assertThrows(BusinessRuleException.class, () -> entryHandler.registerEntry(entryRequest(carId, carSpot2Id)));
    }

    @Test
    void rejectsOccupiedSpot() {
        entryHandler.registerEntry(entryRequest(carId, carSpotId));
        assertThrows(BusinessRuleException.class, () -> entryHandler.registerEntry(entryRequest(car2Id, carSpotId)));
    }

    @Test
    void rejectsSpotOfDifferentVehicleType() {
        assertThrows(BusinessRuleException.class, () -> entryHandler.registerEntry(entryRequest(motoId, carSpotId)));
    }

    @Test
    void rejectsExitWithoutOpenEntry() {
        assertThrows(ResourceNotFoundException.class, () -> exitHandler.registerExit(exitRequest(9999L)));

        ParkingSession entered = entryHandler.registerEntry(entryRequest(carId, carSpotId));
        exitHandler.registerExit(exitRequest(entered.getId()));
        assertThrows(BusinessRuleException.class, () -> exitHandler.registerExit(exitRequest(entered.getId())));
    }

    @Test
    void concurrentEntriesOnSameSpotOnlyOneSucceeds() throws Exception {
        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (Long vehicleId : List.of(carId, car2Id)) {
            tasks.add(() -> {
                try {
                    entryHandler.registerEntry(entryRequest(vehicleId, carSpotId));
                    return true;
                } catch (RuntimeException ex) {
                    return false;
                }
            });
        }
        assertEquals(1, countSuccesses(tasks));
        assertEquals(1, sessionService.findActive().size());
    }

    @Test
    void concurrentExitsOfSameSessionOnlyOneSucceeds() throws Exception {
        ParkingSession entered = entryHandler.registerEntry(entryRequest(carId, carSpotId));
        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            tasks.add(() -> {
                try {
                    exitHandler.registerExit(exitRequest(entered.getId()));
                    return true;
                } catch (RuntimeException ex) {
                    return false;
                }
            });
        }
        assertEquals(1, countSuccesses(tasks));
    }
}
