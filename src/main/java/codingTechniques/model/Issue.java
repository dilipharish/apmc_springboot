package codingTechniques.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @Column(name = "final_crops_id")
    private Long finalCropsId;

    @Column(name = "market_officer_id")
    private Long marketOfficerId = 102L;

    @Column(name = "message")
    private String message;

    @Column(name = "sender")
    private String sender; // Possible values: "farmer", "buyer", "market_officer"

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public Long getFinalCropsId() {
        return finalCropsId;
    }

    public void setFinalCropsId(Long finalCropsId) {
        this.finalCropsId = finalCropsId;
    }

    public Long getMarketOfficerId() {
        return marketOfficerId;
    }

    public void setMarketOfficerId(Long marketOfficerId) {
        this.marketOfficerId = marketOfficerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}