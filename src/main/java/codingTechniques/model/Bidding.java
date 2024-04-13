package codingTechniques.model;



import jakarta.persistence.*;

@Entity
@Table(name = "bidding")
public class Bidding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "final_crop_id")
    private FinalCrop finalCrop;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @Column(name = "bid_amount")
    private double bidAmount;

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinalCrop getFinalCrop() {
        return finalCrop;
    }

    public void setFinalCrop(FinalCrop finalCrop) {
        this.finalCrop = finalCrop;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }
}
