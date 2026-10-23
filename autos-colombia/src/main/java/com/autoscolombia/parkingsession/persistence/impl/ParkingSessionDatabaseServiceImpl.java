package com.autoscolombia.parkingsession.persistence.impl;

import com.autoscolombia.parkingsession.persistence.ParkingSessionDatabaseService;
import com.autoscolombia.parkingsession.persistence.entity.ParkingSessionEntity;
import com.autoscolombia.parkingsession.persistence.mapper.ParkingSessionMapper;
import com.autoscolombia.parkingsession.persistence.repository.ParkingSessionRepository;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingSessionDatabaseServiceImpl implements ParkingSessionDatabaseService {

    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingSessionMapper mapper;

    @Override
    public ParkingSession save(ParkingSession session) {

        ParkingSessionEntity sessionToSave = computeDomainToSave(session);
        ParkingSessionEntity savedSession = parkingSessionRepository.save(sessionToSave);
        return mapper.mapToDomain(savedSession);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByVehicleIdAndNullExitTime(Long vehicleId) {
        return parkingSessionRepository.existsByVehicleIdAndExitTimeIsNull(vehicleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParkingSession> findById(Long id) {
        return parkingSessionRepository.findById(id)
                .map(mapper::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParkingSession> findAll(Pageable pageable) {
        return parkingSessionRepository.findAll(pageable)
                .map(mapper::mapToDomain);
    }

    private ParkingSessionEntity computeDomainToSave(ParkingSession session) {

        return Optional.of(session)
                .filter(sessionToSave -> Objects.isNull(sessionToSave.getId()))
                .map(mapper::mapToEntity)
                .orElseGet(() -> Optional.of(session)
                        .map(ParkingSession::getId)
                        .map(parkingSessionRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(existingSpot -> mapper.mapToExistingEntity(session, existingSpot))
                        .orElseThrow());
    }
}
