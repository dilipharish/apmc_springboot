package codingTechniques.model;

import jakarta.persistence.*;

@Entity
@Table(name = "draftcrops")
public class DraftCrop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "scientific_name")
    private String scientificName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "base_price_per_quintal")
    private double basePricePerQuintal;

    // Define the status field with default value "Pending"
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CropStatus status = CropStatus.PENDING;

    // Define the relationship with Farmer
    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getBasePricePerQuintal() {
        return basePricePerQuintal;
    }

    public void setBasePricePerQuintal(double basePricePerQuintal) {
        this.basePricePerQuintal = basePricePerQuintal;
    }

    public CropStatus getStatus() {
        return status;
    }

    public void setStatus(CropStatus status) {
        this.status = status;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }
}
