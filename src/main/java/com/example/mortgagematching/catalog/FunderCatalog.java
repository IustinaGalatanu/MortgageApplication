package com.example.mortgagematching.catalog;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class FunderCatalog {

    private final List<Funder> funderList;

    public FunderCatalog() {

        Funder funderA = new Funder();
        funderA.setFunderId("FUNDER_A");
        funderA.setSupportedProductIds(Set.of("STANDARD_2Y_75", "STANDARD_5Y_75"));
        funderA.setMaxLtv(new BigDecimal("0.65"));
        funderA.setAllowedApplicantTypes(Set.of(ApplicantType.INDIVIDUAL, ApplicantType.LIMITED_COMPANY));
        funderA.setAllowedPropertyLocations(Set.of(PropertyLocation.ENGLAND, PropertyLocation.WALES, PropertyLocation.SCOTLAND));

        Funder funderB = new Funder();
        funderB.setFunderId("FUNDER_B");
        funderB.setSupportedProductIds(Set.of("STANDARD_2Y_75", "STANDARD_5Y_75", "HMO_5Y_75"));
        funderB.setMaxLtv(new BigDecimal("0.75"));
        funderB.setAllowedApplicantTypes(Set.of(ApplicantType.INDIVIDUAL, ApplicantType.LIMITED_COMPANY));
        funderB.setAllowedPropertyLocations(Set.of(PropertyLocation.ENGLAND, PropertyLocation.WALES));

        Funder funderC = new Funder();
        funderC.setFunderId("FUNDER_C");
        funderC.setSupportedProductIds(Set.of("MUFB_5Y_70", "HMO_5Y_75"));
        funderC.setMaxLtv(new BigDecimal("0.70"));
        funderC.setAllowedApplicantTypes(Set.of(ApplicantType.LIMITED_COMPANY));
        funderC.setAllowedPropertyLocations(Set.of(PropertyLocation.ENGLAND, PropertyLocation.WALES));

        funderList = List.of(funderA, funderB, funderC);
    }

    public List<Funder> getFunderList() {
        return funderList;
    }

}
