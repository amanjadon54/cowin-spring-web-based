package cowin.response;

import cowin.model.CenterModel;

import java.util.List;

public class CowinResponse {

    public List<CenterModel> centers;

    public List<CenterModel> getCenters() {
        return centers;
    }

    public void setCenters(List<CenterModel> centers) {
        this.centers = centers;
    }
}
