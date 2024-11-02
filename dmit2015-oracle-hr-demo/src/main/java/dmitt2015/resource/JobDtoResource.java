package dmitt2015.resource;

import common.validator.BeanValidator;
import dmit2015.entity.Job;
import dmit2015.dto.JobDto;
import dmit2015.mapper.JobMapper;

import dmit2015.persistence.JobRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * This Jakarta RESTful Web Services root resource class provides common REST API endpoints to
 * perform CRUD operations on the DTO (Data Transfer Object) for a Jakarta Persistence entity.
 */
@ApplicationScoped
@Path("JobDtos")                // All methods in this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)
// All methods in this class expects method parameters to contain data in JSON format
@Produces(MediaType.APPLICATION_JSON)    // All methods in this class returns data in JSON format
public class JobDtoResource {

    @Inject
    private JobRepository _jobRepository;

    @GET    // This method only accepts HTTP GET requests.
    public Response findAllJobsJobs() {
        return Response.ok(
                _jobRepository
                        .findAll()
                        .stream()
                        .map(JobMapper.INSTANCE::toDto)
                        .collect(Collectors.toList())
        ).build();
    }

    @Path("{id}")
    @GET    // This method only accepts HTTP GET requests.
    public Response findJobByIdJobById(@PathParam("id") String jobId) {
        Job existingJob = _jobRepository.findById(jobId).orElseThrow(NotFoundException::new);

        JobDto dto = JobMapper.INSTANCE.toDto(existingJob);

        return Response.ok(dto).build();
    }

    @POST    // This method only accepts HTTP POST requests.
    public Response createJobJob(JobDto dto, @Context UriInfo uriInfo) {
        Job newJob = JobMapper.INSTANCE.toEntity(dto);

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

        // uriInfo is injected via @Context parameter to this method
        URI location = UriBuilder
                .fromPath(uriInfo.getPath())
                .path("{id}")
                .build(newJob.getJobId());

        // Set the location path of the new entity with its identifier
        // Returns an HTTP status of "201 Created" if the Job was created.
        return Response
                .created(location)
                .build();
    }

    @PUT            // This method only accepts HTTP PUT requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response updateJobJob(@PathParam("id") String id, JobDto dto) {
        if (!id.equals(dto.getJobId())) {
            throw new BadRequestException();
        }

        Job existingJob = _jobRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);

        Job updatedJob = JobMapper.INSTANCE.toEntity(dto);

        String errorMessage = BeanValidator.validateBean(updatedJob);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

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
        dto = JobMapper.INSTANCE.toDto(existingJob);
        return Response.ok(dto).build();
    }

    @DELETE            // This method only accepts HTTP DELETE requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response deleteJobJob(@PathParam("id") String jobId) {

        Job existingJob = _jobRepository
                .findById(jobId)
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

        // Returns an HTTP status "204 No Content" to indicate the resource was deleted
        return Response.noContent().build();

    }

}