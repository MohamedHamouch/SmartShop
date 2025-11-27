package ma.microtech.smartShop.services;

import ma.microtech.smartShop.entities.Client;
import ma.microtech.smartShop.entities.enums.CustomerTier;

public interface TierService {

    Double getTierDiscount(CustomerTier tier, Double subtotal);

    void checkAndUpgradeTier(Client client);
}
