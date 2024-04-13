package codingTechniques.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "finalcrop", uniqueConstraints = @UniqueConstraint(columnNames = {"draft_crop_id"}))
public class FinalCrop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "draft_crop_id")
    private DraftCrop draftCrop;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @Column(name = "max_price")
    private int maxPrice;

    // Add bid start time
    @Column(name = "bid_start_time")
    private LocalDateTime bidStartTime;

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DraftCrop getDraftCrop() {
        return draftCrop;
    }

    public void setDraftCrop(DraftCrop draftCrop) {
        this.draftCrop = draftCrop;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public LocalDateTime getBidStartTime() {
        return bidStartTime;
    }

    public void setBidStartTime(LocalDateTime bidStartTime) {
        this.bidStartTime = bidStartTime;
    }

    public void setBuyer(Long buyerId) {
        Buyer buyer = new Buyer();
        buyer.setId(buyerId);
        this.buyer = buyer;
    }

}
