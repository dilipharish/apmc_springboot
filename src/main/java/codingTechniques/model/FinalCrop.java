package codingTechniques.model;

import jakarta.persistence.*;

@Entity
@Table(name = "finalcrop", uniqueConstraints = @UniqueConstraint(columnNames = {"draft_crop_id"}))
public class FinalCrop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "draft_crop_id", unique = true) // Ensure uniqueness
    private DraftCrop draftCrop;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

  

    @Column(name = "max_price")
    private int maxPrice;

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
}
