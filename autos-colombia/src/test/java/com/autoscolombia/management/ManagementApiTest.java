package com.autoscolombia.management;

import com.autoscolombia.operator.persistence.entity.OperatorEntity;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterEntryHandler;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterExitHandler;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import com.autoscolombia.operator.service.domain.Operator;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.entity.VehicleTypeEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.support.TransactionTemplate;

import com.jayway.jsonpath.JsonPath;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ManagementApiTest {

    @Autowired MockMvc mvc;
    @Autowired EntityManager em;
    @Autowired TransactionTemplate tx;
    @Autowired ParkingSessionRegisterEntryHandler entryHandler;
    @Autowired ParkingSessionRegisterExitHandler exitHandler;

    Long carTypeId;
    Long operatorId;

    @BeforeEach
    void seed() {
        tx.executeWithoutResult(status -> {
            em.createNativeQuery("delete from parking_session").executeUpdate();
            em.createNativeQuery("delete from vehicle").executeUpdate();
            em.createNativeQuery("delete from parking_spot").executeUpdate();
            em.createNativeQuery("delete from vehicle_type").executeUpdate();
            em.createNativeQuery("delete from customer").executeUpdate();
            em.createNativeQuery("delete from operator").executeUpdate();
            carTypeId = em.merge(VehicleTypeEntity.builder().name("CARRO").monthlyRate(BigDecimal.TEN).build()).getId();
            operatorId = em.merge(OperatorEntity.builder().name("Op").identificationNumber("2")
                    .identificationType("CC").username("op").build()).getId();
        });
    }

    // ---------- helpers ----------

    private static String customerJson(String type, String number, String name) {
        return "{\"identificationType\":\"" + type + "\",\"identificationNumber\":\"" + number
                + "\",\"name\":\"" + name + "\",\"email\":null,\"phone\":\"3001234567\"}";
    }

    private long postJson(String url, String json, int expectedStatus) throws Exception {
        return send(post(url).contentType(MediaType.APPLICATION_JSON).content(json), expectedStatus);
    }

    private long send(MockHttpServletRequestBuilder request, int expectedStatus) throws Exception {
        MvcResult result = mvc.perform(request).andExpect(status().is(expectedStatus)).andReturn();
        String body = result.getResponse().getContentAsString();
        if (expectedStatus == 200 && body.contains("\"id\"")) {
            return ((Number) JsonPath.read(body, "$.id")).longValue();
        }
        return -1;
    }

    private long createCustomer(String number) throws Exception {
        return postJson("/customer", customerJson("CC", number, "Cliente " + number), 200);
    }

    private String vehicleJson(String plate, long customerId) {
        return "{\"licensePlate\":\"" + plate + "\",\"brand\":\"Mazda\",\"model\":2020,\"color\":\"Rojo\","
                + "\"customerId\":" + customerId + ",\"vehicleTypeId\":" + carTypeId + "}";
    }

    private String spotJson(String number, Long typeId) {
        String type = typeId == null ? "null" : "{\"id\":" + typeId + "}";
        return "{\"spotNumber\":\"" + number + "\",\"vehicleType\":" + type + "}";
    }

    private void occupy(long vehicleId, long spotId) {
        entryHandler.registerEntry(ParkingSession.builder()
                .vehicle(Vehicle.builder().id(vehicleId).build())
                .spot(ParkingSpot.builder().id(spotId).build())
                .entryOperator(Operator.builder().id(operatorId).build())
                .build());
    }

    // ---------- customers ----------

    @Test
    void createsCustomerAndReadsItBack() throws Exception {
        long id = createCustomer("100");

        mvc.perform(get("/customer/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cliente 100"))
                .andExpect(jsonPath("$.identificationType").value("CC"));
        mvc.perform(get("/customer").param("search", "cliente 100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
        mvc.perform(get("/customer").param("search", "zzz"))
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void rejectsInvalidCustomerWith400() throws Exception {
        postJson("/customer", "{\"identificationType\":\"CC\",\"identificationNumber\":\"\",\"name\":\"\"}", 400);
        postJson("/customer", "{\"identificationType\":\"CC\",\"identificationNumber\":\"1\",\"name\":\"X\","
                + "\"email\":\"no-es-correo\"}", 400);
    }

    @Test
    void rejectsDuplicateCustomerIdentificationWith409ButAllowsOtherType() throws Exception {
        createCustomer("200");
        postJson("/customer", customerJson("CC", "200", "Otro"), 409);
        postJson("/customer", customerJson("CE", "200", "Extranjero"), 200);
    }

    @Test
    void updatesCustomerAndRejectsCollisionAndMissing() throws Exception {
        long first = createCustomer("300");
        createCustomer("301");

        send(put("/customer/" + first).contentType(MediaType.APPLICATION_JSON)
                .content(customerJson("CC", "300", "Nombre nuevo")), 200);
        mvc.perform(get("/customer/" + first)).andExpect(jsonPath("$.name").value("Nombre nuevo"));

        send(put("/customer/" + first).contentType(MediaType.APPLICATION_JSON)
                .content(customerJson("CC", "301", "Choca")), 409);
        send(put("/customer/99999").contentType(MediaType.APPLICATION_JSON)
                .content(customerJson("CC", "999", "Nadie")), 404);
    }

    @Test
    void deletesCustomerWith204AndThen404() throws Exception {
        long id = createCustomer("400");
        send(delete("/customer/" + id), 204);
        send(get("/customer/" + id), 404);
        send(delete("/customer/" + id), 404);
    }

    @Test
    void blocksCustomerDeletionWhenAVehicleIsInside() throws Exception {
        long customerId = createCustomer("500");
        long vehicleId = postJson("/vehicle", vehicleJson("ABC123", customerId), 200);
        long spotId = postJson("/parking-spot", spotJson("A-01", carTypeId), 200);
        occupy(vehicleId, spotId);

        send(delete("/customer/" + customerId), 409);
        mvc.perform(get("/customer/" + customerId)).andExpect(status().isOk());
    }

    // ---------- vehicles ----------

    @Test
    void createsVehicleNormalizesPlateAndListsByCustomer() throws Exception {
        long customerId = createCustomer("600");
        postJson("/vehicle", vehicleJson("abc123", customerId), 200);

        mvc.perform(get("/vehicle").param("customerId", String.valueOf(customerId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].licensePlate").value("ABC123"));
        mvc.perform(get("/vehicle").param("licensePlate", "abc-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(customerId));
    }

    @Test
    void rejectsDuplicatePlateWith409() throws Exception {
        long customerId = createCustomer("700");
        postJson("/vehicle", vehicleJson("DUP111", customerId), 200);
        postJson("/vehicle", vehicleJson("dup111", customerId), 409);
    }

    @Test
    void rejectsVehicleWithMissingCustomerOrTypeOrInvalidBody() throws Exception {
        long customerId = createCustomer("800");
        postJson("/vehicle", vehicleJson("NOC111", 99999), 404);
        postJson("/vehicle", vehicleJson("NOC111", customerId).replace("\"vehicleTypeId\":" + carTypeId,
                "\"vehicleTypeId\":99999"), 404);
        postJson("/vehicle", "{\"licensePlate\":\"\",\"customerId\":null,\"vehicleTypeId\":null}", 400);
    }

    // ---------- parking spots ----------

    @Test
    void createsEditsAndDeletesSpot() throws Exception {
        long id = postJson("/parking-spot", spotJson("B-01", carTypeId), 200);
        mvc.perform(get("/parking-spot")).andExpect(jsonPath("$.content[0].available").value(true));

        send(put("/parking-spot/" + id).contentType(MediaType.APPLICATION_JSON)
                .content(spotJson("B-02", carTypeId)), 200);
        mvc.perform(get("/parking-spot")).andExpect(jsonPath("$.content[0].spotNumber").value("B-02"));

        send(delete("/parking-spot/" + id), 204);
        send(delete("/parking-spot/" + id), 404);
    }

    @Test
    void rejectsInvalidSpotWith400AndDuplicateWith409() throws Exception {
        postJson("/parking-spot", spotJson("", carTypeId), 400);
        postJson("/parking-spot", spotJson("C-01", null), 400);

        postJson("/parking-spot", spotJson("C-01", carTypeId), 200);
        postJson("/parking-spot", spotJson("c-01", carTypeId), 409);
    }

    @Test
    void rejectsSpotEditCollisionAndMissing() throws Exception {
        postJson("/parking-spot", spotJson("D-01", carTypeId), 200);
        long second = postJson("/parking-spot", spotJson("D-02", carTypeId), 200);

        send(put("/parking-spot/" + second).contentType(MediaType.APPLICATION_JSON)
                .content(spotJson("D-01", carTypeId)), 409);
        send(put("/parking-spot/99999").contentType(MediaType.APPLICATION_JSON)
                .content(spotJson("D-09", carTypeId)), 404);
    }

    @Test
    void rejectsEditAndDeletionOfOccupiedSpotAndFlowStillWorks() throws Exception {
        long customerId = createCustomer("900");
        long vehicleId = postJson("/vehicle", vehicleJson("OCC111", customerId), 200);
        long spotId = postJson("/parking-spot", spotJson("E-01", carTypeId), 200);
        occupy(vehicleId, spotId);

        send(put("/parking-spot/" + spotId).contentType(MediaType.APPLICATION_JSON)
                .content(spotJson("E-99", carTypeId)), 409);
        send(delete("/parking-spot/" + spotId), 409);
    }
}
