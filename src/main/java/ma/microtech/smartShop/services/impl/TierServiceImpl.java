package ma.microtech.smartShop.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.microtech.smartShop.entities.Client;
import ma.microtech.smartShop.entities.enums.CustomerTier;
import ma.microtech.smartShop.services.TierService;

@Service
@Transactional
@RequiredArgsConstructor
public class TierServiceImpl implements TierService {

    @Override
    public Double getTierDiscount(CustomerTier tier, Double subtotal) {
        if (tier == CustomerTier.PLATINUM) {
            if (subtotal >= 1200) {
                return 0.15;
            }
            if (subtotal >= 800) {
                return 0.10;
            }
            if (subtotal >= 500) {
                return 0.05;
            }
        }

        if (tier == CustomerTier.GOLD) {
            if (subtotal >= 800) {
                return 0.10;
            }
            if (subtotal >= 500) {
                return 0.05;
            }
        }

        if (tier == CustomerTier.SILVER) {
            if (subtotal >= 500) {
                return 0.05;
            }
        }

        return 0.0;
    }

    @Override
    public void checkAndUpgradeTier(Client client) {
        CustomerTier newTier = client.getTier();

        if (client.getTotalOrders() >= 20 || client.getTotalSpent() >= 15000) {
            newTier = CustomerTier.PLATINUM;
        } else if (client.getTotalOrders() >= 10 || client.getTotalSpent() >= 5000) {
            newTier = CustomerTier.GOLD;
        } else if (client.getTotalOrders() >= 3 || client.getTotalSpent() >= 1000) {
            newTier = CustomerTier.SILVER;
        } else {
            newTier = CustomerTier.BASIC;
        }

        client.setTier(newTier);
    }
}
