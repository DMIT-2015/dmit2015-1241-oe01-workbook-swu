package dmit2015.persistence;

import dmit2015.config.ApplicationConfig;
import dmit2015.entity.Student;
import dmit2015.entity.StudentInitializer;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import net.datafaker.Faker;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ArquillianExtension.class)
public class StudentArquillianIT { // The class must be declared as public

    static Faker faker = new Faker();

    static String mavenArtifactIdId;

    @Deployment
    public static WebArchive createDeployment() throws IOException, XmlPullParserException {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        mavenArtifactIdId = model.getArtifactId();
        final String archiveName = model.getArtifactId() + ".war";
        return ShrinkWrap.create(WebArchive.class, archiveName)
                .addAsLibraries(pomFile.resolve("org.codehaus.plexus:plexus-utils:3.4.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest:2.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.assertj:assertj-core:3.26.3").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("net.datafaker:datafaker:2.3.1").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.h2database:h2:2.3.232").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc:12.8.1.jre11").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc11:23.5.0.24.07").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.postgresql.jdbc:postgresql:42.7.4").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.mysql:mysql-connector-j:9.0.0").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.mariadb.jdbc:mariadb-java-client:3.4.1").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hibernate.orm:hibernate-spatial:6.6.1.Final").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.eclipse:yasson:3.0.4").withTransitivity().asFile())
                .addClass(ApplicationConfig.class)
                .addClasses(Student.class, StudentRepository.class, StudentInitializer.class)
//                .addAsLibraries(pomFile.resolve("jakarta.platform:jakarta.jakartaee-api:10.0.0").withTransitivity().asFile())
                // .addPackage("dmit2015.entity")
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    private StudentRepository _studentRepository;

    @Resource
    private UserTransaction _beanManagedTransaction;

    @BeforeAll
    static void beforeAllTests() {
        // code to execute before all tests in the current test class
    }

    @AfterAll
    static void afterAllTests() {
        // code to execute after all tests in the current test class
    }

    @BeforeEach
    void beforeEachTestMethod() {
        // Code to execute before each test such as creating the test data
    }

    @AfterEach
    void afterEachTestMethod() {
        // code to execute after each test such as deleting the test data
    }

    @Order(1)
    @Test
    void givenNewEntity_whenAddEntity_thenEntityIsAdded() throws SystemException, NotSupportedException {
        // Arrange
        Student newStudent = new Student();
        newStudent.setFirstName(faker.name().firstName());
        newStudent.setLastName(faker.name().lastName());
        newStudent.setEmail(faker.internet().emailAddress());

        _beanManagedTransaction.begin();

        try {
            // Act
            _studentRepository.add(newStudent);

            // Assert
            assertThat(newStudent.getId())
                    .isNotNull();
        } finally {
            _beanManagedTransaction.rollback();
        }

    }

//    @Order(2)
//    @Test
//    void givenExistingId_whenFindById_thenReturnEntity() throws SystemException, NotSupportedException {
//        // Arrange
//        Student newStudent = new Student();
//        // TODO Set each property of the new entity
//        //newStudent.setProperty1(faker.providerName().methodName());
//        //newStudent.setProperty2(faker.providerName().methodName());
//        //newStudent.setProperty3(faker.providerName().methodName());
//
//        _beanManagedTransaction.begin();
//
//        try {
//            // Act
//            _studentRepository.add(newStudent);
//
//            // Assert
//            // TODO Uncomment code below and change get method for primary key
//            Optional<Student> optionalStudent = _studentRepository.findById(newStudent.getId());
//            assertThat(optionalStudent.isPresent())
//                    .isTrue();
//            // Assert
//            var existingStudent = optionalStudent.orElseThrow();
//            assertThat(existingStudent)
//                    .usingRecursiveComparison()
//                    // .ignoringFields("field1", "field2")
//                    .isEqualTo(newStudent);
//
//        } finally {
//            _beanManagedTransaction.rollback();
//        }
//
//    }
//
//    @Order(3)
//    @Test
//    void givenExistingEntity_whenUpdatedEntity_thenEntityIsUpdated() throws SystemException, NotSupportedException {
//        // Arrange
//        Student newStudent = new Student();
//        // TODO Set each property of the new entity
//        //newStudent.setProperty1(faker.providerName().methodName());
//        //newStudent.setProperty2(faker.providerName().methodName());
//        //newStudent.setProperty3(faker.providerName().methodName());
//
//        _beanManagedTransaction.begin();
//
//        try {
//            // Act
//            _studentRepository.add(newStudent);
//            //newStudent.setProperty1(faker.providerName().methodName());
//            //newStudent.setProperty2(faker.providerName().methodName());
//            //newStudent.setProperty3(faker.providerName().methodName());
//            Student updatedStudent = _studentRepository.update(newStudent.getId(), newStudent);
//
//            // Assert
//            // TODO Uncomment code below and change get method for primary key
//            Optional<Student> optionalStudent = _studentRepository.findById(updatedStudent.getId());
//            assertThat(optionalStudent.isPresent())
//                    .isTrue();
//            // Assert
//            var existingStudent = optionalStudent.orElseThrow();
//            assertThat(existingStudent)
//                    .usingRecursiveComparison()
//                    // .ignoringFields("field1", "field2")
//                    .isEqualTo(newStudent);
//
//        } finally {
//            _beanManagedTransaction.rollback();
//        }
//
//    }
//
//    @Order(4)
//    @Test
//    void givenExistingId_whenDeleteEntity_thenEntityIsDeleted() throws SystemException, NotSupportedException {
//        _beanManagedTransaction.begin();
//
//        try {
//            // Arrange
//            Student newStudent = new Student();
//            // TODO Set each property of the new entity
//            //newStudent.setProperty1(faker.providerName().methodName());
//            //newStudent.setProperty2(faker.providerName().methodName());
//            //newStudent.setProperty3(faker.providerName().methodName());
//            _studentRepository.add(newStudent);
//            // Act
//            _studentRepository.deleteById(newStudent.getId());
//            // Assert
//            Optional<Student> optionalStudent = _studentRepository.findById(newStudent.getId());
//            assertThat(optionalStudent.isPresent())
//                    .isFalse();
//
//        } catch (Exception ex) {
//            fail("Failed to delete entity with exception message %s", ex.getMessage());
//        } finally {
//            _beanManagedTransaction.rollback();
//        }
//
//    }

    @Order(5)
    @ParameterizedTest
    @CsvSource({
            "10"}
    )
    void givenMultipleEntity_whenFindAll_thenReturnEntityList(int recordCount) throws SystemException, NotSupportedException {
        // Arrange: Set up the initial state
//        _beanManagedTransaction.begin();

        try {

            // Act: Perform the action to be tested
            List<Student> studentList = _studentRepository.findAll();

            // Assert: Verify the expected outcome
            assertThat(studentList.size())
                    .isEqualTo(recordCount);
        } catch (Exception ex) {
            fail("Failed to delete entity with exception message %s", ex.getMessage());
        } finally {
//            _beanManagedTransaction.rollback();
        }
    }

//    @Order(6)
//    @ParameterizedTest
//    // TODO Change the value below
//    @CsvSource(value = {
//            "Invalid Property1Value, Property2Value, Property3Value, ExpectedExceptionMessage",
//            "Property1Value, Invalid Property2Value, Property3Value, ExpectedExceptionMessage",
//    }, nullValues = {"null"})
//    void givenEntityWithValidationErrors_whenAddEntity_thenThrowException(String property1, String property2, String property3, String expectedExceptionMessage) throws SystemException, NotSupportedException {
//        // Arrange
//        Student newStudent = new Student();
//        // TODO Change the code below to set each property
//        // newStudent.setProperty1(property1);
//        // newStudent.setProperty2(property2);
//        // newStudent.setProperty3(property3);
//
//        _beanManagedTransaction.begin();
//        try {
//            // Act
//            _studentRepository.add(newStudent);
//            fail("An bean validation constraint should have been thrown");
//        } catch (Exception ex) {
//            // Assert
//            assertThat(ex)
//                    .hasMessageContaining(expectedExceptionMessage);
//        } finally {
//            _beanManagedTransaction.rollback();
//        }
//
//    }

}