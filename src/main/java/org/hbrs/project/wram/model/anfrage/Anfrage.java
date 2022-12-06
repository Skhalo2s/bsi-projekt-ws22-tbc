/**
 * @outhor Salah & Tom
 * @vision 1.0
 * @Zuletzt bearbeiret: 17.11.22 by Salah
 *
 */
package org.hbrs.project.wram.model.anfrage;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hbrs.project.wram.model.common.BaseEntity;
import org.hbrs.project.wram.model.entwickler.Entwickler;
import org.hbrs.project.wram.model.kundenprojekt.Kundenprojekt;
import org.hbrs.project.wram.model.manager.Manager;
import org.hbrs.project.wram.model.reviewer.Reviewer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Diese Klasse stellt die Entity Anfrage mit ihren Attributen dar.*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "anfrage", schema = "public")
public class Anfrage extends BaseEntity {
    
    @Column(name = "reason", nullable = true)
    private String reason;

    @Column(name = "entwicklerpublic", nullable = true)
    private boolean entwicklerpublic;

    @Column(name = "accepted", nullable = true)
    private boolean accepted;


    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "reviewer_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_reviewer_id"))
    private Reviewer reviewer;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "entwicklerprofil_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_entwicklerprofil_id"))
    private Entwickler entwicklerProfil;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "kundenprojekt_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_kundenprojekt_id"))
    private Kundenprojekt kundenprojekt;

    
}
