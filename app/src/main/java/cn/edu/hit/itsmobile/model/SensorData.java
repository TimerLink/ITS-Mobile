package cn.edu.hit.itsmobile.model;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

public class SensorData {
    public int sensorId;
    public String lpNumber;
    public ArrayList<Packet> data;
    
    public List<LatLng> getRoute() {
        List<LatLng> list = new ArrayList<LatLng>();
        for (Packet packet : data) {
            CoordinateConverter converter = new CoordinateConverter()
                                                .from(CoordType.GPS)
                                                .coord(new LatLng(packet.latitude, packet.longitude));
            list.add(converter.convert());
        }
        return list;
    }

    public class Packet {
        public int timestamp;
        public float latitude;
        public char ns;
        public float longitude;
        public char ew;
        public float speed;
        public int passengerUp;
        public int passengerDown;
        public int passengerTotal;
    }
}
