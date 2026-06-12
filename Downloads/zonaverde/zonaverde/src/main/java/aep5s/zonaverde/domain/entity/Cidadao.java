package aep5s.zonaverde.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Cidadao")
@Getter @Setter @NoArgsConstructor
public class Cidadao extends Usuario{
}
