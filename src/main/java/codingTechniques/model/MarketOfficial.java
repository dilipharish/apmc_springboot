package codingTechniques.model;

import jakarta.persistence.*;

@Entity
@Table(name = "market_official")
public class MarketOfficial {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Add other properties specific to a market official
    
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

	@OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, getters, and setters
}
