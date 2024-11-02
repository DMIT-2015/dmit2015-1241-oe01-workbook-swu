package dmitt2015.resource;

import common.validator.BeanValidator;
import dmit2015.entity.Job;
import dmit2015.persistence.JobRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

/**
 * This Jakarta RESTful Web Services root resource class provides common REST API endpoints to
 * perform CRUD operations on Jakarta Persistence entity.
 */
@ApplicationScoped
@Path("Jobs")                    // All methods of this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)    // All methods this class accept only JSON format data
@Produces(MediaType.APPLICATION_JSON)    // All methods returns data that has been converted to JSON format
public class JobResource {

    @Inject
    private JobRepository _jobRepository;

    @GET    // This method only accepts HTTP GET requests.
    public Response findAllJobsJobs() {
        return Response.ok(_jobRepository.findAll()).build();
    }

    @Path("{id}")
    @GET    // This method only accepts HTTP GET requests.
    public Response findJobById(@PathParam("id") String jobId) {
        Job existingJob = _jobRepository.findById(jobId).orElseThrow(NotFoundException::new);

        return Response.ok(existingJob).build();
    }

    @POST    // This method only accepts HTTP POST requests.
    public Response createJobJob(Job newJob, @Context UriInfo uriInfo) {

        String errorMessage = BeanValidator.validateBean(newJob);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        try {
            // Persist the new Job into the database
            _jobRepository.add(newJob);
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // userInfo is injected via @Context parameter to this method
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newJob.getJobId()))
                .build();

        // Set the location path of the new entity with its identifier
        // Returns an HTTP status of "201 Created" if the Job was successfully persisted
        return Response
                .created(location)
                .build();
    }

    @PUT            // This method only accepts HTTP PUT requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response updateJobJob(@PathParam("id") String id, Job updatedJob) {
        if (!id.equals(updatedJob.getJobId())) {
            throw new BadRequestException();
        }

        String errorMessage = BeanValidator.validateBean(updatedJob);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        Job existingJob = _jobRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        existingJob.setJobTitle(updatedJob.getJobTitle());
        existingJob.setMinSalary(updatedJob.getMinSalary());
        existingJob.setMaxSalary(updatedJob.getMaxSalary());

        try {
            _jobRepository.update(id, existingJob);
        } catch (OptimisticLockException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("The data you are trying to update has changed since your last read request.")
                    .build();
        } catch (Exception ex) {
            // Return an HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // Returns an HTTP status "200 OK" and include in the body of the response the object that was updated
        return Response.ok(existingJob).build();
    }

    @DELETE            // This method only accepts HTTP DELETE requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response deleteJob(@PathParam("id") String id) {

        Job existingJob = _jobRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);

        try {
            _jobRepository.delete(existingJob);    // Removes the Job from being persisted
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response
                    .serverError()
                    .encoding(ex.getMessage())
                    .build();
        }

        // Returns an HTTP status "204 No Content" to indicated that the resource was deleted
        return Response.noContent().build();
    }

}