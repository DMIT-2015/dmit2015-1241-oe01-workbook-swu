package dmit2015.mapper;

import dmit2015.dto.JobDto;
import dmit2015.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * This MapStruct interface contains methods on how to map a Jakarta Persistence entity to a DTO
 * (Data Transfer Object) and a method on how to map a DTO to a JPA entity.
 * <p>
 * The following code snippets shows how to call that class-level methods.
 * {@snippet :
 * //Job newJobEntity = JobMapper.INSTANCE.toEntity(newJobDto);
 * //JobDto newJobDto = JobMapper.INSTANCE.toDto(newJobEntity);
 * }
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapper {

    JobMapper INSTANCE = Mappers.getMapper( JobMapper.class );

    // You only need to specify property names that are not the same in the source and target.
    // @Mappings({
    //     @Mapping(target = "dtoProperty1Name", source = "entityProperty1Name"),
    //     @Mapping(target = "dtoProperty2Name", source = "entityProperty2Name"),
    //     @Mapping(target = "dtoProperty3Name", source = "entityProperty3Name"),
    // })
    JobDto toDto(Job entity);

    // You only need to specify property names that are not the same in the source and target.
    // @Mappings({
    //     @Mapping(target = "entityProperty1Name", source = "dtoProperty1Name"),
    //     @Mapping(target = "entityProperty1Name", source = "dtoProperty2Name"),
    //     @Mapping(target = "entityProperty1Name", source = "dtoProperty3Name"),
    // })
    Job toEntity(JobDto dto);

}