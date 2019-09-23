package org.acme.spring.data.jpa;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.logmanager.Level;
import org.jboss.logmanager.Logger;

import java.util.List;
import java.util.Optional;

@Path("/api/fruits")
public class FruitResource {

    private final static Logger log = Logger.getLogger(FruitResource.class.getCanonicalName());

    private final FruitRepository fruitRepository;

    public FruitResource(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @GET
    @Produces("application/json")
    public Iterable<Fruit> findAll() {
        return fruitRepository.findAll();
    }


    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        fruitRepository.deleteById(id);
    }

    @POST
    @Path("/name/{name}/color/{color}")
    @Produces("application/json")
    public Fruit create(@PathParam("name") String name, @PathParam("color") String color) {
        return fruitRepository.save(new Fruit(name, color));
    }

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    public Fruit create(JsonObject fruit) {
        System.out.println(fruit.toString());
        return fruitRepository.save(new Fruit(fruit.getString("name"), fruit.getString("color")));
    }

    @PUT
    @Path("/id/{id}/color/{color}")
    @Produces("application/json")
    public Fruit changeColor(@PathParam("id") Long id, @PathParam("color") String newColor) {
        Optional<Fruit> optional = fruitRepository.findById(id);
        if (optional.isPresent()) {
            Fruit fruit = optional.get();
            fruit.setColor(newColor);
            return fruitRepository.save(fruit);
        }

        throw new IllegalArgumentException("No Fruit with id " + id + " exists");
    }

    @PUT
    @Path("/id/{id}/name/{name}")
    @Produces("application/json")
    public Fruit u(@PathParam("id") Long id, @PathParam("name") String newName) {
        Optional<Fruit> optional = fruitRepository.findById(id);
        if (optional.isPresent()) {
            Fruit fruit = optional.get();
            fruit.setName(newName);
            return fruitRepository.save(fruit);
        }

        throw new IllegalArgumentException("No Fruit with id " + id + " exists");
    }

    @PUT
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Fruit update(@PathParam("id") Long id, JsonObject fruit) {
        Optional<Fruit> optional = fruitRepository.findById(id);
        if (optional.isPresent()) {
            Fruit currentFruit = optional.get();
            log.log(Level.DEBUG, String.format("Current Fruit: %s\n", currentFruit.toString()));
            currentFruit.setName(fruit.getString("name"));
            currentFruit.setColor(fruit.getString("color"));
            log.log(Level.DEBUG, String.format("Updated Fruit: %s\n", currentFruit.toString()));
            return fruitRepository.save(currentFruit);
        }

        throw new IllegalArgumentException("No Fruit with id " + id + " exists");
    }

    @GET
    @Path("/color/{color}")
    @Produces("application/json")
    public List<Fruit> findByColor(@PathParam("color") String color) {
        return fruitRepository.findByColor(color);
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Fruit findById(@PathParam("id") Long id) {
        return fruitRepository.findById(id).get();
    }
}
