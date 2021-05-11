package cowin;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.Map;

public final class CowinApp {

    public static String handleRequest(Map<String, String> input, Context context) {
        context.getLogger().log("age passed:" + input.get("age"));
        context.getLogger().log("user-agent passed:" + input.get("user-agent"));

        if (!input.containsKey("age")) return "";

        int age = Integer.parseInt(input.get("age"));
        String userAgent = input.get("user-agent");

        CowinController cowin = new CowinController();
        return cowin.getAllAvailableSlots(age, userAgent).toString();
    }

    public static void main(String... s) {
        CowinController cowin = new CowinController();
        cowin.getAllAvailableSlots(45, "custom").toString();
    }


}
