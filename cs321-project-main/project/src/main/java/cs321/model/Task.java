package cs321.model;
import javax.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String taskName;
    private String dueDate;
    @Column(name = "user_name")
    private String username;

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for taskName
    public String getTaskName() {
        return taskName;
    }

    // Setter for taskName
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Getter for dueDate
    public String getDueDate() {
        return dueDate;
    }

    // Setter for dueDate
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
