package dmit2015.model;

import lombok.Data;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields, and @RequiredArgsConstructor!
public class Student {

    /** The unique name of the JSON object in the Firebase RT DB.*/
    private String name;
    /** First Name of student */
    private String firstName;
    /** Last Name of student */
    private String lastName;
    /** Student email address */
    private String email;

}
