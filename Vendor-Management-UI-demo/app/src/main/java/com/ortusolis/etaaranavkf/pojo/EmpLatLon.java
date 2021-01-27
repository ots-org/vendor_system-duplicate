package com.ortusolis.etaaranavkf.pojo;

import java.util.List;

public class EmpLatLon {

    List<EmpLatL> empLatLong;

    public List<EmpLatL> getEmpLatLong() {
        return empLatLong;
    }

    public void setEmpLatLong(List<EmpLatL> empLatLong) {
        this.empLatLong = empLatLong;
    }

    public class EmpLatL{

        String latitude;
        String longitude;
        String userId;
        String latLongId;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLatLongId() {
            return latLongId;
        }

        public void setLatLongId(String latLongId) {
            this.latLongId = latLongId;
        }
    }

}
