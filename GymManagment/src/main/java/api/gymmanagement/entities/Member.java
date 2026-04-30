package api.gymmanagement.entities;

import api.gymmanagement.enums.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private MembershipPlan plan;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String surname;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, updatable = false)
    private LocalDate membershipStartDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    public Member(MembershipPlan plan, String name, String surname, String email, String address) {
        this.plan = plan;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.membershipStartDate = LocalDate.now();
        this.status = MemberStatus.ACTIVE;
    }

    public void cancel() {
        this.status = MemberStatus.CANCELLED;
    }
}
