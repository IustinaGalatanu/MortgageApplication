package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.funder.FunderLocationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FunderLocationRuleTest {

    private final FunderLocationRule rule = new FunderLocationRule();

    private MortgageApplication application;
    private MortgageProduct product;
    private Funder funder;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();
        product = new MortgageProduct();

        funder = new Funder();
        funder.setAllowedPropertyLocations(Set.of(
                PropertyLocation.ENGLAND,
                PropertyLocation.WALES
        ));
    }

    @Test
    void shouldAcceptEnglandWhenSupported() {
        application.setPropertyLocation(PropertyLocation.ENGLAND);

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldAcceptWalesWhenSupported() {
        application.setPropertyLocation(PropertyLocation.WALES);

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldAcceptScotlandWhenSupported() {
        application.setPropertyLocation(PropertyLocation.SCOTLAND);
        funder.setAllowedPropertyLocations(Set.of(
                PropertyLocation.ENGLAND,
                PropertyLocation.WALES,
                PropertyLocation.SCOTLAND
        ));

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldRejectScotlandWhenNotSupported() {
        application.setPropertyLocation(PropertyLocation.SCOTLAND);

        RejectionReason reason =
                rule.evaluate(application, product, funder).orElseThrow();

        assertEquals(
                RejectionType.LOCATION_NOT_SUPPORTED,
                reason.getRejectionType()
        );
        assertTrue(reason.getMessage().contains("SCOTLAND"));
        assertTrue(reason.getMessage().contains("ENGLAND"));
        assertTrue(reason.getMessage().contains("WALES"));
    }

    @Test
    void shouldRejectNorthernIrelandWhenNotSupported() {
        application.setPropertyLocation(PropertyLocation.NORTHERN_IRELAND);

        RejectionReason reason =
                rule.evaluate(application, product, funder).orElseThrow();

        assertEquals(
                RejectionType.LOCATION_NOT_SUPPORTED,
                reason.getRejectionType()
        );
        assertTrue(reason.getMessage().contains("NORTHERN_IRELAND"));
        assertTrue(reason.getMessage().contains("ENGLAND"));
        assertTrue(reason.getMessage().contains("WALES"));
    }
}