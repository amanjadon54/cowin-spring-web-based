package cowin;

import cowin.model.CenterModel;
import cowin.model.SessionModel;
import cowin.response.CowinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class CowinController {

    @Autowired
    private RestTemplate restTemplate;

    private Logger log = LoggerFactory.getLogger(CowinController.class);

    @GetMapping("/available")
    public List<CenterModel> getAllAvailableSlots(@RequestParam("age") int ageLimit, @RequestParam("date") String startDate) {
        Set<Integer> allDistricts = getAllDistricts();
        List<CenterModel> allSlots = new LinkedList<>();
        for (Integer district : allDistricts) {
            allSlots.addAll(getCentersByDistrictAndStartDate(district, startDate, ageLimit));
        }
        return allSlots;
    }

    private List<CenterModel> getCentersByDistrictAndStartDate(Integer district, String startDate, int ageLimit) {
        String cowinUrl = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/calendarByDistrict";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cowinUrl).queryParam("district_id", district).queryParam("date", startDate);

        HttpEntity<String> entity = new HttpEntity("", createHttpHeaders());
        ResponseEntity<CowinResponse> response = null;

        try {
            response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, CowinResponse.class);
        } catch (Exception e) {
            log.error("exception occured in retrival from cowin" + e);
        }

        if (response == null)
            log.warn(String.format("Error in retrieving data for the given district: %s and ageLimit: %s", district, ageLimit));

        List<CenterModel> availableSlots = new LinkedList<>();
        if (response != null) availableSlots = filterByAgeAndAvailability(response.getBody(), ageLimit);
        return availableSlots;
    }

    private List<CenterModel> filterByAgeAndAvailability(CowinResponse response, int ageLimit) {
        List<CenterModel> availableSlots = new LinkedList<>();

        for (CenterModel centerModel : response.getCenters()) {
            List<SessionModel> availableSessions = centerModel.getSessions().stream().filter(center -> center.getMin_age_limit() == ageLimit && center.getAvailable_capacity() > 0).collect(Collectors.toList());
            if (!availableSessions.isEmpty()) {
                centerModel.setSessions(availableSessions);
                availableSlots.add(centerModel);
            }
        }
        return availableSlots;

    }

    private Set<Integer> getAllDistricts() {
        Set<Integer> districts = new HashSet<>();
        districts.add(141);  //central delhi
        districts.add(145); //East
        districts.add(140); //New delhi
        districts.add(146); //North delhi
        districts.add(147); //NorthEast
        districts.add(143); //NorthWest
        districts.add(148); //shahdara
        districts.add(149); //South
        districts.add(144); //SouthEast
        districts.add(150); //SouthWest
        districts.add(142); //West
        return districts;
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        return headers;
    }

}
