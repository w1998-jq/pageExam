package com.wang.indexStruc;

import ch.hsr.geohash.GeoHash;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/15 14:27
 */
public class CompressPoint {

    Map<String, Integer> cache;
    int count;

    public CompressPoint() {
        this.cache = new HashMap<>();
        count = 0;
    }

    public int getGeoHashID(double lon, double lat, int precision) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, lon, precision);
        if (!cache.containsKey(geoHash.toBase32())) {
            cache.put(geoHash.toBase32(), count);
            count++;
        }
        return cache.get(geoHash.toBase32());
    }

    public static String geoHash(double lon, double lat, int precision) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, lon, precision);
        return geoHash.toBase32();
    }

    public static long googleS2(double lon, double lat, int precision) {
        S2LatLng s2LatLng = S2LatLng.fromDegrees(lat, lon);
        S2CellId cellId = S2CellId.fromLatLng(s2LatLng).parent(precision);
        return cellId.id();
    }

}