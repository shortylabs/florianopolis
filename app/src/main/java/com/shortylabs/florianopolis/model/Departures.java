package com.shortylabs.florianopolis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeri on 12/6/14.
 */
public class Departures {

    private static final String WEEKDAY = "WEEKDAY";
    private static final String SATURDAY = "SATURDAY";
    private static final String SUNDAY = "SUNDAY";

    public List<Departure> rows = new ArrayList<Departure>();
    public Integer rowsAffected;

    public List<Departure> weekday() {
        List<Departure> list = new ArrayList<Departure>();
        for (Departure dep: rows) {
            if (WEEKDAY.equals(dep.calendar)) {
                list.add(dep);
            }
        }
        return list;
    }

    public List<Departure> saturday() {
        List<Departure> list = new ArrayList<Departure>();
        for (Departure dep: rows) {
            if (SATURDAY.equals(dep.calendar)) {
                list.add(dep);
            }
        }
        return list;
    }

    public List<Departure> sunday() {
        List<Departure> list = new ArrayList<Departure>();
        for (Departure dep: rows) {
            if (SUNDAY.equals(dep.calendar)) {
                list.add(dep);
            }
        }
        return list;
    }

}
