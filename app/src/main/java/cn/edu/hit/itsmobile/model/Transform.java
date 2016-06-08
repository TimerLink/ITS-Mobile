package cn.edu.hit.itsmobile.model;

/**
 * Created by Newsoul on 2016/6/5.
 */
public class Transform {
    public final static int COORDINATE_MULTIPLE = 3600000;
    /**
     * @link http://stackoverflow.com/questions/837872/calculate-geoDistance-in-meters-when-you-know-longitude-and-latitude-in-java
     */

    public double timeCalculate(Location a,Location b){
        double distance = geoDistance(a,b);
        double speedAverage = (a.speed+b.speed)/2;
        return distance/speedAverage;
    }
    public static double geoDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;               // 地球平均半径，单位：米
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
    /**
     * 计算经纬度距离
     */
    public static double geoDistance(Location a, Location b) {
        return geoDistance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
    }
//    /**
//     * 计算欧氏距离
//     */
//    public static double euclideanDistance (Coordinate a, Coordinate b) {
//        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
//    }
//    /**
//     * 粗略地判断坐标点是否在中国国境以内
//     */
//    public static boolean isInsideChina (Location Location) {
//        return Location.getLatitude() > 4 && Location.getLatitude() < 54 &&
//                Location.getLongitude() > 72 && Location.getLongitude() < 136;
//    }
//    /**
//     * 将经纬度转换为XY坐标
//     * @link http://411431586.blog.51cto.com/4043583/743305
//     */
//    public static Coordinate transformToXY (Location Location) {
//        double longitude = Location.getLongitude();
//        double latitude = Location.getLatitude();
//        int zoneWidth = 6;                                              // 6度带带宽
//        int projNo = (int)(longitude / zoneWidth);                      // 6度带带号
//        double iPI = Math.PI / 180;                                     // 弧度单位
//        double a = 6378137.0;                                           // WGS-84椭球体长半轴长度
//        double b = 6356752.3142;                                        // WGS-84椭球体短半轴长度
//        double L = longitude * iPI ;                                    // 经度转换为弧度
//        double B = latitude * iPI ;                                     // 纬度转换为弧度
//        double e1 = Math.sqrt(a * a - b * b) / a;                       // 椭球第一偏心率
//        double e2 = Math.sqrt(a * a - b * b) / b;                       // 椭球第二偏心率
//        double L0 = (projNo * zoneWidth + zoneWidth / 2) * iPI;         // 对应带的中央子午线到本初子午线的弧度
//        double T = Math.pow(Math.tan(B), 2);
//        double C = Math.pow(Math.cos(B) * e2, 2);
//        double A = (L - L0) * Math.cos(B);
//        double N = a / Math.sqrt(1.0 - Math.pow(e1 * Math.sin(B), 2));  // 卯酉圈曲率半径
//        double M = a * (                                                // 子午线弧长
//                (1 - Math.pow(e1, 2) / 4 - 3 * Math.pow(e1, 4) / 64 - 5 * Math.pow(e1, 6) / 256) * B
//                        - (3 * Math.pow(e1, 2) / 8 + 3 * Math.pow(e1, 4) / 32 + 45 * Math.pow(e1, 6) / 1024) * Math.sin(2 * B)
//                        + (15 * Math.pow(e1, 4) / 256 + 45 * Math.pow(e1, 6) / 1024) * Math.sin(4 * B)
//                        - (35 * Math.pow(e1, 6) / 3072) * Math.sin(6 * B)
//        );
//        double Y0 = 1000000L * (projNo + 1) + 500000L;                  // Y坐标转换到对应的带内
//        double X = M + N * Math.tan(B) * (Math.pow(A, 2) / 2 + (5 - T + 9 * C + 4 * Math.pow(C, 2)) * Math.pow(A, 4) / 24 + (61 - 58 * T + Math.pow(T, 2) + 270 * C - 330 * T * C) * Math.pow(A, 6) / 720);
//        double Y = Y0 + N * (A + (1 - T + C) * Math.pow(A, 3) / 6 + (5 - 18 * T + Math.pow(T, 2) + 14 * C - 58 * T * C) * Math.pow(A, 5) / 120);
//        return new Coordinate(X, Y);
//    }
}

