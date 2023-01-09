package travel2.service;

import edu.fudan.common.entity.Route;
import edu.fudan.common.entity.Seat;
import edu.fudan.common.entity.TrainType;
import edu.fudan.common.entity.TravelResult;
import edu.fudan.common.entity.TripId;
import edu.fudan.common.entity.TripAllDetailInfo;
import edu.fudan.common.entity.TripInfo;
import edu.fudan.common.entity.Type;
import edu.fudan.common.util.Response;
import edu.fudan.common.util.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import travel2.entity.Travel;
import travel2.entity.Trip;
import travel2.repository.TripRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class TravelServiceImplTest {

    @InjectMocks
    private TravelServiceImpl travel2ServiceImpl;

    @Mock
    private TripRepository repository;

    @Mock
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();
    String success = "Success";
    String noCnontent = "No Content";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRouteByTripId1() {
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(null);
        Response result = travel2ServiceImpl.getRouteByTripId("K1255", headers);
        Assert.assertEquals(new Response<>(0, "\"[Get Route By Trip ID] Trip Not Found:\" + tripId", null), result);
    }

    @Test
    public void testGetRouteByTripId2() {
        Trip trip = new Trip();
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);
        //mock getRouteByRouteId()
        edu.fudan.common.entity.Route route = new edu.fudan.common.entity.Route();
        Response response = new Response(1, null, route);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)))
                .thenReturn(re);
        Response result = travel2ServiceImpl.getRouteByTripId("K1255", headers);
        Assert.assertEquals("[Get Route By Trip ID] Success", result.getMsg());
    }

    @Test
    public void testGetTrainTypeByTripId() {
        Trip trip = new Trip();
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);
        //mock getTrainType()
        edu.fudan.common.entity.TrainType trainType = new edu.fudan.common.entity.TrainType();
        Response<edu.fudan.common.entity.TrainType> response = new Response<>(null, null, trainType);
        ResponseEntity<Response<edu.fudan.common.entity.TrainType>> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re);
        Response result = travel2ServiceImpl.getTrainTypeByTripId("K1255", headers);
        Assert.assertEquals(new Response<>(1, "Success query Train by trip id", trainType), result);
    }

    @Test
    public void testGetTripByRoute1() {
        ArrayList<String> routeIds = new ArrayList<>();
        Response result = travel2ServiceImpl.getTripByRoute(routeIds, headers);
        Assert.assertEquals(new Response<>(0, noCnontent, null), result);
    }

    @Test
    public void testGetTripByRoute2() {
        ArrayList<String> routeIds = new ArrayList<>();
        routeIds.add("route_id_1");
        Mockito.when(repository.findByRouteId(Mockito.anyString())).thenReturn(null);
        Response result = travel2ServiceImpl.getTripByRoute(routeIds, headers);
        Assert.assertEquals(success, result.getMsg());
    }

    @Test
    public void testCreate1() {
        edu.fudan.common.entity.TravelInfo info = new edu.fudan.common.entity.TravelInfo();
        info.setTripId("Z");
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(null);
        Mockito.when(repository.save(Mockito.any(Trip.class))).thenReturn(null);
        Response result = travel2ServiceImpl.create(info, headers);
        Assert.assertEquals(new Response<>(1, "Create trip info:Z.", null), result);
    }

    @Test
    public void testCreate2() {
        edu.fudan.common.entity.TravelInfo info = new edu.fudan.common.entity.TravelInfo();
        info.setTripId("Z");
        Trip trip = new Trip();
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);
        Response result = travel2ServiceImpl.create(info, headers);
        Assert.assertEquals(new Response<>(1, "Trip Z already exists", null), result);
    }

    @Test
    public void testRetrieve1() {
        Trip trip = new Trip();
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);
        Response result = travel2ServiceImpl.retrieve("trip_id_1", headers);
        Assert.assertEquals(new Response<>(1, "Search Trip Success by Trip Id trip_id_1", trip), result);
    }

    @Test
    public void testRetrieve2() {
        Trip trip = new Trip();
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);
        Response result = travel2ServiceImpl.retrieve("trip_id_1", headers);
        Assert.assertEquals(new Response<>(1, "Search Trip Success by Trip Id trip_id_1", trip), result);
    }

    @Test
    public void testUpdate1() {
        edu.fudan.common.entity.TravelInfo info = new edu.fudan.common.entity.TravelInfo();
        info.setTripId("Z");
        Trip trip = new Trip();
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);
        Mockito.when(repository.save(Mockito.any(Trip.class))).thenReturn(null);
        Response result = travel2ServiceImpl.update(info, headers);
        Assert.assertEquals("Update trip info:Z", result.getMsg());
    }

    @Test
    public void testUpdate2() {
        edu.fudan.common.entity.TravelInfo info = new edu.fudan.common.entity.TravelInfo();
        info.setTripId("Z");
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(null);
        Response result = travel2ServiceImpl.update(info, headers);
        Assert.assertEquals(new Response<>(1, "TripZdoesn 't exists", null), result);
    }

    @Test
    public void testDelete1() {
        Trip trip = new Trip();
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);
        Mockito.doNothing().doThrow(new RuntimeException()).when(repository).deleteByTripId(Mockito.any(TripId.class));
        Response result = travel2ServiceImpl.delete("trip_id_1", headers);
        Assert.assertEquals(new Response<>(1, "Delete trip:trip_id_1.", "trip_id_1"), result);
    }

    @Test
    public void testDelete2() {
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(null);
        Response result = travel2ServiceImpl.delete("trip_id_1", headers);
        Assert.assertEquals(new Response<>(0, "Trip trip_id_1 doesn't exist.", null), result);
    }

    @Test
    public void testQuery() {
        TripInfo info = new TripInfo();
        //mock queryForStationId()
        Response<String> response1 = new Response<>(null, null, "");
        ResponseEntity<Response<String>> re1 = new ResponseEntity<>(response1, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re1);

        ArrayList<Trip> tripList = new ArrayList<>();
        Trip trip = new Trip();
        trip.setRouteId("route_id");
        tripList.add(trip);
        Mockito.when(repository.findAll()).thenReturn(tripList);

        //mock getRouteByRouteId()
        edu.fudan.common.entity.Route route = new edu.fudan.common.entity.Route();
        route.setStations(new ArrayList<>());
        Response response2 = new Response(1, null, route);
        ResponseEntity<Response> re2 = new ResponseEntity<>(response2, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)))
                .thenReturn(re2);
        Response result = travel2ServiceImpl.query(info, headers);
        Assert.assertEquals(new Response<>(1, "Success Query", new ArrayList<>()), result);
    }

    @Test
    public void testGetTripAllDetailInfo() {
        TripAllDetailInfo gtdi = new TripAllDetailInfo();
        gtdi.setTripId("Z1255");
        gtdi.setFrom("from_station");
        gtdi.setTo("to_station");
        gtdi.setTravelDate(StringUtils.Date2String(new Date(System.currentTimeMillis() + 86400000)));

        Trip trip = new Trip();
        trip.setRouteId("route_id");
        TripId tripId = new TripId();
        tripId.setNumber("1");
        tripId.setType(Type.G);
        trip.setTripId(tripId);
        Mockito.when(repository.findByTripId(Mockito.any(TripId.class))).thenReturn(trip);

        //mock queryForStationId()
        Response<String> response1 = new Response<>(null, null, "");
        ResponseEntity<Response<String>> re1 = new ResponseEntity<>(response1, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                        Mockito.anyString(),
                        Mockito.any(HttpMethod.class),
                        Mockito.any(HttpEntity.class),
                        Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re1);

        //mock getRouteByRouteId()
        Route route = new Route();
        route.setStations(Arrays.asList("from_station", "to_station"));
        route.setDistances(Arrays.asList(10,20));
        Response response2 = new Response(1, null, route);
        ResponseEntity<Response> re2 = new ResponseEntity<>(response2, HttpStatus.OK);

        TravelResult travelResult = new TravelResult();
        travelResult.setRoute(route);
        TrainType trainType = new TrainType();
        travelResult.setTrainType(trainType);
        Map<String, String> map = new HashMap<>();
        map.put("confortClass", "100");
        map.put("economyClass", "50");
        travelResult.setPrices(map);
        trainType.setAverageSpeed(10);
        Response response3 = new Response(1, null, travelResult);
        ResponseEntity<Response<edu.fudan.common.entity.TravelResult>> re3 = new ResponseEntity<>(response3, HttpStatus.OK);
        Travel query = new Travel();
        query.setTrip(trip);
        query.setStartPlace("from_station");
        query.setEndPlace("to_station");
        query.setDepartureTime(gtdi.getTravelDate());
        HttpEntity requestEntity = new HttpEntity(query, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-basic-service/api/v1/basicservice/basic/travel",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<Response<edu.fudan.common.entity.TravelResult>>() {
                        }))
                .thenReturn(re3);

        Response response4 = new Response(1, null, 1);
        ResponseEntity<Response<Integer>> re4 = new ResponseEntity<>(response4, HttpStatus.OK);

        Seat seatRequest = new Seat();
        seatRequest.setDestStation("to_station");
        seatRequest.setStartStation("from_station");
        seatRequest.setTrainNumber("G1");
        seatRequest.setTravelDate(gtdi.getTravelDate());
        seatRequest.setSeatType(2);
        seatRequest.setTotalNum(0);
        seatRequest.setStations(route.getStations());
        HttpEntity requestEntity3 = new HttpEntity(seatRequest, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity3,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        Seat seatRequest2 = new Seat();
        seatRequest2.setDestStation("to_station");
        seatRequest2.setStartStation("from_station");
        seatRequest2.setTrainNumber("G1");
        seatRequest2.setTravelDate(gtdi.getTravelDate());
        seatRequest2.setSeatType(3);
        seatRequest2.setTotalNum(0);
        seatRequest2.setStations(route.getStations());
        HttpEntity requestEntity4 = new HttpEntity(seatRequest2, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity4,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);
        Response result = travel2ServiceImpl.getTripAllDetailInfo(gtdi, headers);
        Assert.assertEquals("Success", result.getMsg());
    }

    @Test
    public void testQueryAll1() {
        ArrayList<Trip> tripList = new ArrayList<>();
        tripList.add(new Trip());
        Mockito.when(repository.findAll()).thenReturn(tripList);
        Response result = travel2ServiceImpl.queryAll(headers);
        Assert.assertEquals(new Response<>(1, success, tripList), result);
    }

    @Test
    public void testQueryAll2() {
        Mockito.when(repository.findAll()).thenReturn(null);
        Response result = travel2ServiceImpl.queryAll(headers);
        Assert.assertEquals(new Response<>(0, noCnontent, null), result);
    }

    @Test
    public void testAdminQueryAll1() {
        ArrayList<Trip> tripList = new ArrayList<>();
        Trip trip = new Trip();
        trip.setRouteId("route_id");
        tripList.add(trip);
        Mockito.when(repository.findAll()).thenReturn(tripList);

        //mock getRouteByRouteId()
        edu.fudan.common.entity.Route route = new edu.fudan.common.entity.Route();
        Response response2 = new Response(1, null, route);
        ResponseEntity<Response> re2 = new ResponseEntity<>(response2, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)))
                .thenReturn(re2);

        //mock getTrainType()
        edu.fudan.common.entity.TrainType trainType = new edu.fudan.common.entity.TrainType();
        Response<edu.fudan.common.entity.TrainType> response = new Response<>(null, null, trainType);
        ResponseEntity<Response<edu.fudan.common.entity.TrainType>> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re);
        Response result = travel2ServiceImpl.adminQueryAll(headers);
        Assert.assertEquals("Travel Service Admin Query All Travel Success", result.getMsg());
    }
    @Test
    public void testAdminQueryAll2() {
        ArrayList<Trip> tripList = new ArrayList<>();
        Mockito.when(repository.findAll()).thenReturn(tripList);
        Response result = travel2ServiceImpl.adminQueryAll(headers);
        Assert.assertEquals(new Response<>(0, noCnontent, null), result);
    }

}
