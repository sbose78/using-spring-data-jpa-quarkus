package org.acme.spring.data.jpa;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/api/status")
public class Status {

   @GET
   @Path("/dbNameCM")
   @Produces("text/plain")
   public String getName() {
      String v = System.getenv("JDBC_URL");
      return v.substring(v.lastIndexOf("/") + 1);
   }
}
