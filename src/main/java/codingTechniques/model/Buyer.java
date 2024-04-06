package codingTechniques.model;

import jakarta.persistence.*;

@Entity
@Table(name = "buyer")
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Add other properties specific to a buyer
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    // Constructors, getters, and setters
}
