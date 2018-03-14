package com.capitalone.challenge.Models;

import java.util.List;

public class Data {
    public DataSet dataset;

    public class DataSet {
        public long id;
        public String dataset_code;
        public String database_code;
        public String name;
        public String description;
        public String refreshed_at;
        public String newest_available_date;
        public String oldest_available_date;
        public String frequency;
        public String type;
        public boolean premium;
        public String limit;
        public String transform;
        public String column_index;
        public String start_date;
        public String end_date;
        public String collapse;
        public String order;
        public int database_id;
        public List<List<Object>> data;
        public List<String> column_names;
    }
}
