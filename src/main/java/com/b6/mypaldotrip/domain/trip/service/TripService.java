package com.b6.mypaldotrip.domain.trip.service;

import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripListReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.*;
import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.trip.store.repository.TripRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final CityService cityService;

    public TripCreateRes createTrip(TripCreateReq req, UserDetailsImpl userDetails) {
        String name = req.name();
        checkAuthorization(userDetails);
        if (tripRepository.findByName(name).isPresent()) {
            throw new GlobalException(TripErrorCode.ALREADY_EXIST_TRIP);
        }
        TripEntity trip =
                TripEntity.builder()
                        .city(cityService.findCity(req.cityId()))
                        .category(req.category())
                        .name(req.name())
                        .description(req.description())
                        .build();
        tripRepository.save(trip);
        return TripCreateRes.builder()
                .city(trip.getCity().getCityName())
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .build();
    }

    public List<TripListRes> getTripList(TripListReq req) {
        Pageable pageable = PageRequest.of(req.page(), 20);
        return tripRepository.searchTripsAndSort(req.cityName(), req.category(), pageable).stream()
                .map(
                        trip ->
                                TripListRes.builder()
                                        .city(trip.getCity().getCityName())
                                        .category(trip.getCategory())
                                        .name(trip.getName())
                                        .build())
                .toList();
    }

    public TripGetRes getTrip(Long tripId) {
        TripEntity trip = findTrip(tripId);
        return TripGetRes.builder()
                .city(trip.getCity().getCityName())
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .build();
    }

    @Transactional
    public TripUpdateRes updateTrip(Long tripId, TripUpdateReq req, UserDetailsImpl userDetails) {
        checkAuthorization(userDetails);
        TripEntity trip = findTrip(tripId);
        trip.updateTrip(
                cityService.findCity(req.cityId()), req.category(), req.name(), req.description());
        return TripUpdateRes.builder()
                .city(trip.getCity().getCityName())
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .build();
    }

    public TripDeleteRes deleteTrip(Long tripId, UserDetailsImpl userDetails) {
        checkAuthorization(userDetails);
        TripEntity trip = findTrip(tripId);
        tripRepository.delete(trip);
        return TripDeleteRes.builder().message("여행 정보가 삭제되었습니다.").build();
    }

    // 권한 체크 메서드
    private static void checkAuthorization(UserDetailsImpl userDetails) {
        if (userDetails.getUserEntity().getUserRole() == UserRole.ROLE_USER) {
            throw new GlobalException(TripErrorCode.UNAUTHORIZED_ROLE_ERROR);
        }
    }

    // 여행정보 전체 조회 메서드
    public List<TripEntity> findAllTrips() {
        return tripRepository.findAll();
    }

    // 여행정보 조회 메서드
    public TripEntity findTrip(Long tripId) {
        return tripRepository
                .findById(tripId)
                .orElseThrow(() -> new GlobalException(TripErrorCode.NON_EXIST_TRIP));
    }
}
