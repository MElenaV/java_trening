package ru.stqa.pft.soap;


import com.lavasoft.GeoIPService;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class GeoIpServiceTest {

  @Test
  public void testMyIp() {
   String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("192.162.88.2");
    assertEquals(ipLocation, "<GeoIP><Country>RU</Country><State>CA</State></GeoIP>");
  }
}
